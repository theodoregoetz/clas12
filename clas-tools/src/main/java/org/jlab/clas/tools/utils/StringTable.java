/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.tools.utils;

import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class StringTable {
    
    private final ArrayList< ArrayList<String> > tableItems = new 
            ArrayList< ArrayList<String> >();
    private final ArrayList<String>  description = new ArrayList<String>();
    
    public  Integer TABLE_COLOR = 0;
    
    public StringTable(){
        
    }
    
    public void setDescription(String... headers){
        description.clear();
        tableItems.clear();
        for(String item : headers){
            description.add(item);
        }
    }
    
    public static String getCharacterString(String character, int len){
        StringBuilder str = new StringBuilder();
        for(int loop = 0; loop < len; loop++) str.append(character);
        return str.toString();
    }
    
    public void add(String... tokens){
        if(tokens.length != description.size()){
            System.err.println("ERROR : Table entry is longer than number of columns..");
            return;
        }
        ArrayList<String> tokenArray = new ArrayList<String>();
        for(int loop = 0; loop < tokens.length; loop++){
            tokenArray.add(tokens[loop]);
        }
        tableItems.add(tokenArray);
    }
    
    public String getLine(ArrayList<String> items, Integer... lengths){
        return null;
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
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        return str.toString();
    }
    
    public static void main(String[] args){
        System.err.println(
                StringUtils.getStringBlue(
                        StringTable.getTitleString("COAT/JAVA PLATFORM for CLAS12  version 1.0", 1)));
    }
}
