/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sic.xe.assembler;
import java.util.HashMap;
/**
 *
 * @author YasMIna
 */
public class Optable {
    public static HashMap<String,String> optab;
    static {
        optab = new HashMap<>();
        optab.put("ADD", "18");
        optab.put("AND", "40");
        optab.put("COMP", "28");
        optab.put("DIV", "24");
        optab.put("J", "3C");
        optab.put("JEQ", "30");
        optab.put("JGT", "34");
        optab.put("JLT", "38");
        optab.put("JSUB", "48");
        optab.put("LDA", "00");
        optab.put("LDCH", "50");
        optab.put("LDL", "08");
        optab.put("LDX", "04");
        optab.put("MUL", "20");
        optab.put("OR", "44");
        optab.put("RD", "D8");
        optab.put("RSUB", "4C");
        optab.put("TD", "E0");
        optab.put("TIX", "2C");
        optab.put("WD", "DC");
        optab.put("STA", "0C");
        optab.put("STCH", "54");
        optab.put("STL", "14");
        optab.put("STSW", "E8");
        optab.put("STX", "10");
        optab.put("SUB", "1C");
    }

    public static HashMap<String, String> getOptab() {
        return optab;
    }
    
}
