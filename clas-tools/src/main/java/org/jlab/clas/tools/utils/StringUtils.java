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
    
}
