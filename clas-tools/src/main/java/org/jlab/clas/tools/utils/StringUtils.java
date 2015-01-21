/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clas.tools.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavalian
 */
public class StringUtils {
    private static final String blackColor   = "\033[0;30m";
    private static final String redColor     = "\033[0;31m";
    private static final String greenColor   = "\033[0;32m";
    private static final String orangeColor  = "\033[0;33m";
    private static final String blueColor    = "\033[0;34m";
    private static final String noColor      = "\033[0m";

    
    
    public static String  numberString(Integer number, int spaces){
        StringBuilder str = new StringBuilder();
        str.append(number.toString());
        int nLeadingZeros = spaces - str.length();
        if(nLeadingZeros>0){
            for(int loop = 0; loop < nLeadingZeros; loop++) str.insert(0, '0');
        }
        return str.toString();
    }
    
    public static String numberString(Integer[] numbers, int spaces, String delim){
        StringBuilder str = new StringBuilder();
        for(int loop = 0; loop < numbers.length; loop++){
            if(loop!=0) str.append(delim);
            str.append(StringUtils.numberString(numbers[loop], spaces));
        }
        return str.toString();
    }
    
    public static List<String> tokenizeWhiteSpace(String stringLine){
        String[] tokens = stringLine.trim().split("\\s+");
        ArrayList<String> buffer = new ArrayList<String>();
        for(String token : tokens){
            buffer.add(token);
        }
        return buffer;
    }
    
    public static String getStringRed(String line){
        StringBuilder str = new StringBuilder();
        str.append(StringUtils.redColor);
        str.append(line);
        str.append(StringUtils.noColor);
        return str.toString();
    }
    
    public static String getStringGreen(String line){
        StringBuilder str = new StringBuilder();
        str.append(StringUtils.greenColor);
        str.append(line);
        str.append(StringUtils.noColor);
        return str.toString();
    }
    
    public static String getStringBlue(String line){
        StringBuilder str = new StringBuilder();
        str.append(StringUtils.blueColor);
        str.append(line);
        str.append(StringUtils.noColor);
        return str.toString();
    }
}
