/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.awt.font.TextAttribute;
import java.text.AttributedString;

/**
 *
 * @author gavalian
 */
public class LatexText {
    
    private String  textFamily = "Helvetica";
    private Double  relativeX  = 0.0;
    private Double  relativeY  = 0.0;
    private AttributedString  latexString = null;
    private Integer           textColor   = 1;
    
    public LatexText(String text, double xc, double yc){
        this.setText(text);
        this.setLocation(xc, yc);
        this.setFont(textFamily);
        this.setFontSize(24);
    }
    
    public final void setText(String text){
        String ltx  = LatexTextTools.convertUnicode(text);
        latexString = LatexTextTools.converSuperScript(ltx);
    }
    
    public final void setLocation(double xr, double yr){
        this.relativeX = xr;
        this.relativeY = yr;
    }
    
    public int    getColor(){return this.textColor;}
    public void    setColor(int color){ this.textColor = color;}
    
    public double getX(){ return this.relativeX;}
    public double getY(){ return this.relativeY;}
    
    public AttributedString getText(){ return this.latexString;}
    
    public void setFont(String fontname){
        latexString.addAttribute(TextAttribute.FAMILY, fontname);
    }
    
    public void setFontSize(int size){
        latexString.addAttribute(TextAttribute.SIZE, (float)size);
    }
}
