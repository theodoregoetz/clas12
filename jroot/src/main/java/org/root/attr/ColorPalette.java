/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.attr;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class ColorPalette {
    private final ArrayList<Color>  palette = new ArrayList<Color>();
    
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
}
