/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.tools.utils;

/**
 *
 * @author gavalian
 */
public class StringTable {
    
    
    public StringTable(){
        
    }
    
    public static String getCharacterString(String character, int len){
        StringBuilder str = new StringBuilder();
        for(int loop = 0; loop < len; loop++) str.append(character);
        return str.toString();
    }
    
    public static String getCharacterString(String character, String endpoints,
            int len){
        StringBuilder str = new StringBuilder();
        str.append(endpoints);
        str.append(StringTable.getCharacterString(character, len-2));
        str.append(endpoints);
        return str.toString();
    }
    
    public static String getTitleString(String title, int padding){
        int length = title.length()+4;
        StringBuilder str = new StringBuilder();
        str.append(StringTable.getCharacterString("-", "+", length));
        str.append("\n");
        str.append(String.format("| %s |\n", title));
        str.append(StringTable.getCharacterString("-", "+", length));        
        str.append("\n");
        return str.toString();
    }
    
    public static void main(String[] args){
        System.err.println(
                StringUtils.getStringBlue(
                        StringTable.getTitleString("COAT/JAVA PLATFORM for CLAS12  version 1.0", 1)));
    }
}
