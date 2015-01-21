/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.styles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class ColorManager {
    private static final TreeMap<Integer, ArrayList<Color> > colorFamily =
            ColorManager.initColors();
    
    public ColorManager(){
        
    }
    
    public static Color getColor(int family, int index){
        return colorFamily.get(family).get(index);
    }
    
    public static TreeMap<Integer, ArrayList<Color> > initColors(){
        TreeMap<Integer, ArrayList<Color> > colMap = new 
        TreeMap<Integer, ArrayList<Color> >();        
        /**
         * Pastel Colors
         */
        ArrayList<Color>  pastel_1 = new ArrayList<Color>();
        /*
        pastel_1.add(new Color( 50, 50, 50));
        pastel_1.add(new Color(137,186,190));
        pastel_1.add(new Color(255,168,145));
        pastel_1.add(new Color(255,196,114));
        pastel_1.add(new Color(245,230,101));
        pastel_1.add(new Color(237,243,147));
        */
        
        pastel_1.add(new Color( 255, 255, 255));
        pastel_1.add(new Color(   0,   0,   0));
        pastel_1.add(new Color( 221,  75,  57)); // Red
        pastel_1.add(new Color(  83, 169,  63)); // Green
        pastel_1.add(new Color(  57,  86, 145)); // Blue
        pastel_1.add(new Color( 254, 209,  75)); // Yellow
        pastel_1.add(new Color( 102,  17, 204)); // Magenta
        pastel_1.add(new Color(  17, 193, 255)); // Cyan
        pastel_1.add(new Color(   0, 119, 181)); // Navy Blue
        //pastel_1.add(new Color( ));
                
        colMap.put(  1, pastel_1);
        colMap.put(100, ColorManager.cloneTransparent(pastel_1, 220));
        colMap.put(200, ColorManager.cloneTransparent(pastel_1, 180));
        colMap.put(300, ColorManager.cloneTransparent(pastel_1, 128));
        colMap.put(400, ColorManager.cloneTransparent(pastel_1,  32));
        
        return colMap;
    }
    
    public static ArrayList<Color>  cloneTransparent(ArrayList<Color> cols, int alpha){
        ArrayList<Color> transCol = new ArrayList<Color>();
        for(Color item : cols){
            transCol.add(new Color(item.getRed(),item.getGreen(),item.getBlue(),alpha));
        }
        return transCol;
    }
}
