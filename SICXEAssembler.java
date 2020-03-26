/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sic.xe.assembler;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.io.PrintWriter;
import java.lang.Character;
/**
 *
 * @author YasMIna
 */
public class SICXEAssembler {
   // static Optable optable = new Optable();
    static HashMap<String, String> symtable = new HashMap<String, String>();

    public static String opcode (String word){
        String[] words = word.split("\\s+");
        int counter = words.length;
        if (counter == 3)
            return words[1];
        else if (counter == 2){
            return words[1];
        }
        else {
            System.out.println("Opcode not Found.\n Error\n");
            return null;
        }
    }
    public static String operand (String word){
        String[] words = word.split("\\s+");
        int counter = words.length;
        if (counter == 3)
            return words[2];
        else {
            System.out.println("operand not found.");
            return null;
        }
    }
    public static String label (String word){
        String[]words = word.split("\\s+");
        int count = words.length;
        if (count==3 && (words[0].length()>1))
            return words[0];
        else
            return null;
    }
    public static String opcode2 (String word){
        String[] words = word.split("\\s+");
        int counter = words.length;
        if (counter == 4)
            return words[2];
        else if (counter == 3 || counter == 2){
            return words[1];
        }
        else {
            System.out.println("Opcode2 not Found.");
            return null;
        }
    }
    public static String operand2 (String word){
        String[] words = word.split("\\s+");
        int counter = words.length;
        if (counter == 4)
            return words[3];
        else if (counter == 3)
            return words[2];
        else if (counter == 2)
            return null;
        else {
            System.out.println("operand2 not found.");
            return null;
        }
    }
    public static String label2 (String word){
        String[]words = word.split("\\s+");
        int count = words.length;
        if (count==4)
            return words[1];
        else
            return null;
    }
    public static String LOCCTR (String line){
        String[] loc = line.split("\\s+");
        return loc[0];
    }
    //to put address of format of 4 digits
    public static String form4(String word){
        if (word.length() == 4)
            return word;
        if(word.length()<4){
            while(word.length()<4)
                word = '0' + word;
            return word;        
        }
        else{ 
            System.out.println("Error in Form4");
            return "Form4 invalid";
        }
        
    }
    //to put address of format of 6 digits
    public static String form6(String word){
        if (word.length() == 6)
            return word;
        else if(word.length()<6){
            while(word.length()<6)
                word = '0' + word;
            return word;
        }
        else if (word.length()> 6){
            return word.substring(0, 5);
        }
        else{
            System.out.println("Error in Form6");
            return "Form6 invalid";
        }
    }
    public static String form2(String word){
        if (word.length() == 2)
            return word;
        if (word.length()<2)
            return "0"+word;
        else {
            System.out.println("Error in Form2");
            return "Form2 invalid";
        }
    }
    //************PASS1****************
    static String locctr,startAdd,programLength;
    public static void PASS1(File input , File output) throws FileNotFoundException,IOException,NumberFormatException{
       Scanner scanInput = new Scanner(input);
        PrintWriter printInLocctr = new PrintWriter(output);
        String inputLine = scanInput.nextLine();
        if(opcode(inputLine).equals("START")){
            startAdd = operand(inputLine);
            locctr = operand(inputLine);
            printInLocctr.println(operand(inputLine)+"\t"+inputLine);
        } 
        symtable.put(label(inputLine), locctr);
        inputLine = scanInput.nextLine();
        printInLocctr.println(locctr+"\t"+inputLine);
        symtable.put(label(inputLine), locctr);
        while (!opcode(inputLine).equals("END")){
            //check opcode not null and found        
                if (opcode(inputLine).equals("WORD")){
                    int loctr4 = Integer.parseInt(locctr, 16) + 3;
                    locctr = (Integer.toHexString(loctr4)).toUpperCase();
                }
                else if (opcode(inputLine).equals("RESW")){        
                    int loctr2 = (Integer.parseInt(operand(inputLine)) * 3) + Integer.parseInt(locctr,16);
                    locctr = Integer.toHexString(loctr2).toUpperCase();
                }
                else if (opcode(inputLine).equals("RESB")){
                    int loctr3 = Integer.parseInt(locctr,16) + Integer.parseInt(operand(inputLine));
                    locctr = Integer.toHexString(loctr3).toUpperCase();
                }
                else if (opcode(inputLine).equals("BYTE")){
                    if(operand(inputLine).charAt(0)=='C'){
                        int countChar = operand(inputLine).length() - 3;
                        int loctr4 = Integer.parseInt(locctr, 16) + countChar;
                        locctr = Integer.toHexString(loctr4).toUpperCase();
                    }
                    else if (operand(inputLine).charAt(0)=='X'){
                        int countHex = operand(inputLine).length() - 3;
                        int loctr5 = Integer.parseInt(locctr, 16) + (countHex/2);
                        locctr = Integer.toHexString(loctr5).toUpperCase();
                    }
                }
                else if (Optable.getOptab().containsKey(opcode(inputLine))){
                    int loctr1 = Integer.parseInt(locctr, 16) + 3;
                    locctr = Integer.toHexString(loctr1).toUpperCase();
                }    
                else {
                    System.out.println("invalid operation code");
                }
                locctr = form4(locctr);
            inputLine = scanInput.nextLine();     
            printInLocctr.println(locctr +"\t"+inputLine);
                if (symtable.containsValue(label(inputLine)))
                    System.out.println("Duplicate symbol");
                else {
                        symtable.put(label(inputLine), locctr);
                }
        }
        programLength = Integer.toHexString(Integer.parseInt(locctr, 16)-Integer.parseInt(startAdd,16));
        programLength = form4(programLength);
        printInLocctr.close();
        scanInput.close();
        for (HashMap.Entry<String,String> entry : symtable.entrySet()){
            if (entry.getValue() != locctr)
                System.out.println("Symbol = " + entry.getKey() + "\t"+"Location = " + entry.getValue());
        }
        System.out.println("PASS1 IS DONEEEE!!!!!!!!!!!!!!");
    }
    //************PASS2****************
    public static void PASS2(File input , File output) throws FileNotFoundException,IOException{
        String operandAddress,objectCode="",startAddLine="",lastAddLine="",lengthRecordLine="",listingLine="",tLine="",lastObject;
        int counter=1;
        Scanner scanInput =  new Scanner(input);
        PrintWriter printInRecord = new PrintWriter(output);
        String inputLine = scanInput.nextLine();
        if (opcode2(inputLine).equals("START")){
            printInRecord.println("H"+"\t"+label2(inputLine)+form6(startAdd)+form6(programLength));
        }
        inputLine = scanInput.nextLine();
        printInRecord.print("T"+"\t");
        startAddLine = form6(startAdd);
        while (!opcode2(inputLine).equals("END")){
            if(Optable.getOptab().containsKey(opcode2(inputLine))){
                objectCode = Optable.getOptab().get(opcode2(inputLine));
                if (operand2(inputLine) != null){
                    if (symtable.containsKey(operand2(inputLine))){
                        operandAddress = symtable.get(operand2(inputLine));
                        objectCode = form6(objectCode + operandAddress);
                    }
                    else if (operand2(inputLine).endsWith(",X")){
                        int length = operand2(inputLine).length();
                        String newOperand = operand2(inputLine).substring(0, operand2(inputLine).length()-2);
                        operandAddress = symtable.get(newOperand);
                        //update operanAddress
                        if ((!operandAddress.startsWith("8")) && (!operandAddress.startsWith("9"))){
                            operandAddress = Integer.toHexString(Integer.parseInt(operandAddress,16)+Integer.parseInt("8000", 16));
                        }
                        objectCode = form6(objectCode + operandAddress);
                    }
                    else {
                        operandAddress = form4("0");
                        System.out.println("undefined symbol");
                    }
                }
                else {
                    operandAddress = form4("0");
                    objectCode = form6(objectCode + operandAddress);
                }
            }
            else if (opcode2(inputLine).equals("WORD")){
             objectCode = Integer.toHexString(Integer.parseInt(operand2(inputLine), 10));
             objectCode = form6(objectCode);
            }
            else if (opcode2(inputLine).equals("BYTE")){
             if(operand2(inputLine).startsWith("C'")){
                 String newOperand = operand2(inputLine).substring(2,operand2(inputLine).length()-1) ;
                 char[] chars = newOperand.toCharArray();
                 int i,ascii;
                 objectCode = "";
                 for (i=0;i<chars.length;i++){
                     ascii = (int)chars[i];
                     objectCode = objectCode + Integer.toHexString(ascii);
                 }
                 objectCode = form6(objectCode);
             }
             else if (operand2(inputLine).startsWith("X'")){
                 String newOperand = operand2(inputLine).substring(2,operand2(inputLine).length()-1) ;
                 objectCode = newOperand;
             }
            }
            else if (opcode2(inputLine).equals("RESW") || opcode2(inputLine).equals("RESB")){
                objectCode = "";
            }
                listingLine = listingLine + objectCode;
                System.out.println(counter +"--->"+listingLine);
            if (counter % 10 == 0) {
                inputLine = scanInput.nextLine();
                counter++;
                lastAddLine = form6(LOCCTR(inputLine));
                lengthRecordLine = Integer.toHexString(Integer.parseInt(lastAddLine, 16)-Integer.parseInt(startAddLine, 16));
                lengthRecordLine = form2(lengthRecordLine);
                tLine = startAddLine+lengthRecordLine+listingLine;
                printInRecord.println(tLine);
                printInRecord.print("T"+"\t");
                startAddLine = lastAddLine;
                listingLine = "";}
            else if((opcode2(inputLine).equals("RESW") || opcode2(inputLine).equals("RESB"))){
                lastAddLine = form6(LOCCTR(inputLine));
                lengthRecordLine = Integer.toHexString(Integer.parseInt(lastAddLine, 16)-Integer.parseInt(startAddLine, 16));
                lengthRecordLine = form2(lengthRecordLine);
                tLine = startAddLine+lengthRecordLine+listingLine;
                System.out.println(tLine);
                printInRecord.println(tLine);
                inputLine = scanInput.nextLine();
                counter = 1;
                while(opcode2(inputLine).equals("RESB") || opcode2(inputLine).equals("RESW")){
                    inputLine = scanInput.nextLine();
                }
                lastAddLine = form6(LOCCTR(inputLine));
                printInRecord.print("T"+"\t");
                startAddLine = lastAddLine;
                listingLine = "";
            } 
            else if (scanInput.hasNextLine()){
                inputLine = scanInput.nextLine();
                counter++;
                if (opcode2(inputLine).equals("END") && listingLine.length() < 60){
                    lastAddLine = form6(LOCCTR(inputLine));
                    lengthRecordLine = Integer.toHexString(Integer.parseInt(lastAddLine, 16)-Integer.parseInt(startAddLine, 16));
                    lengthRecordLine = form2(lengthRecordLine);
                    tLine = startAddLine+lengthRecordLine+listingLine;
                }
            }
        }
        printInRecord.println(tLine);
        printInRecord.println("E"+"\t"+form6(startAdd));
        scanInput.close();
        printInRecord.close();
        System.out.println("PASS2 IS DONEEEE!!!!!!!!!!!!!!!!!!!");
    }
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException,NumberFormatException{
        // TODO code application logic here
        File inputFile = new File("D:\\COLLEGE\\7 - systems programming\\Project\\SIC-XE Assembler\\src\\sic\\xe\\assembler\\input.txt");
        File locctrFile = new File("D:\\COLLEGE\\7 - systems programming\\Project\\SIC-XE Assembler\\src\\sic\\xe\\assembler\\pass1.txt");
        File recordFile = new File("D:\\COLLEGE\\7 - systems programming\\Project\\SIC-XE Assembler\\src\\sic\\xe\\assembler\\HTE record.txt");
        PASS1(inputFile,locctrFile);
        PASS2(locctrFile,recordFile);
    }    
}
