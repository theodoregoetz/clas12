/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.histogram;

import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class PaveText {
    public ArrayList<String> paveStrings = new ArrayList<String>();
    private double pavePositionX = 0.0;
    private double pavePositionY = 0.0;
    public  Integer FontSize     = 12;
    
    public PaveText(double rx, double ry){
        this.pavePositionX = rx;
        this.pavePositionY = ry;
    }
    
    public double getPositionX(){
        return this.pavePositionX;
    }
    public double getPositionY(){
        return this.pavePositionY;
    }
    
    public void addText(String text){
        this.paveStrings.add(text);
    }
    
    public void setFontSize(int size){
        this.FontSize = size;
    }
}
