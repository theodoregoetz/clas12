/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.raw;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class DataUtilities {
    public static TreeMap<Integer,Integer> bitMap = DataUtilities.createBitMap();
            
    public static TreeMap<Integer,Integer>  createBitMap(){
        TreeMap<Integer,Integer> map = new TreeMap<Integer,Integer>();
        for(int loop = 0; loop < 25; loop++){
            int  integer_value = 0;
            for(int hb = 0 ; hb < loop; hb++){
                integer_value = integer_value | (1<<hb);
            }
            map.put(loop, integer_value);
        }
        return map;
    }
    
    public static void printBitMap(){
        for(Map.Entry<Integer,Integer> entry : bitMap.entrySet()){
            System.out.println(String.format("%4d : ", entry.getKey()) 
                    + String.format("%32s", Integer.toBinaryString(entry.getValue())).replace(' ', '0'));
        }
    }
    
    
    public static int getInteger(int data, int bitstart, int bitend){
        int length = bitend - bitstart + 1;
        if(DataUtilities.bitMap.containsKey(length)==true){
            int value = (data&DataUtilities.bitMap.get(length))>>bitstart;
            return value;
        } else {
            System.out.println("[DataUtilities] : ERROR length = " + length);
        }
        return 0;
    }
    
    public static void main(String[] args){
        DataUtilities.printBitMap();
    }
}
