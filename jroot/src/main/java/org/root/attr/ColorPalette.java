/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.attr;

import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class ColorPalette {
    
    private final ArrayList<Color>  palette = new ArrayList<Color>();
    private static TreeMap<Integer,Color>  colorPalette = ColorPalette.initColorMap();

    public ColorPalette(){
        this.set(1);
    }
    
    public void ColorPalette(int mode){
        this.set(mode);
    }
    
    public final void set(int mode){
        if(mode==1){
            palette.clear();
            palette.add(new Color(0x00,0x00,0x90));
            palette.add(new Color(0x00,0x0f,0xff));
            palette.add(new Color(0x00,0x90,0xff));
            palette.add(new Color(0x0f,0xff,0xee));
            palette.add(new Color(0x90,0xff,0x70));
            palette.add(new Color(0xff,0xee,0x00));
            palette.add(new Color(0xff,0x70,0x00));
            palette.add(new Color(0xee,0x00,0x00));            
            palette.add(new Color(0x7f,0x00,0x00));
        }
        
        if(mode==2){
            palette.clear();
            palette.add(new Color(255,  0,  0));
            palette.add(new Color(255, 15,  0));
            palette.add(new Color(255, 67,  0));
            palette.add(new Color(255,110,  0));
            palette.add(new Color(255,132,  0));
            palette.add(new Color(255,160,  0));
            
            palette.add(new Color(255,187,  0));
            palette.add(new Color(255,215,  0));
            
            palette.add(new Color(255,240,  0));
            palette.add(new Color(250,255,  0));
            palette.add(new Color(250,255, 85));
            palette.add(new Color(250,255,155));
            palette.add(new Color(250,255,224));
            
            
            
        }
    }
    
    public Color getRange(double fraction){
        if(fraction>1.0) fraction = 1;
        double binC = fraction*palette.size();
        int bin = (int) binC;
        if(bin>=palette.size()) bin = palette.size()-1;
        return palette.get(bin);
    }
    
    
    public static Color  getColor(int color){
        return ColorPalette.colorPalette.get(color);
    }
    
    public static TreeMap<Integer,Color> initColorMap(){
        TreeMap<Integer,Color> colors = new TreeMap<Integer,Color>();
        colors.put(0, Color.white);
        colors.put(1, Color.BLACK);
        colors.put(2,new Color(210,79,68));
        colors.put(3,new Color(137,216,68));
        colors.put(4,new Color(77,176,221));
        colors.put(5,new Color(246,188,47));        
        colors.put(6,new Color(222,82,111));
        colors.put(7,new Color(230,130,58));
        colors.put(8,new Color(90,207,161));
        colors.put(8,new Color(106,120,203));
        
        
        return colors;
    }
}
