/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.attr;

/**
 *
 * @author gavalian
 */
public class AttributesText {
    
    private Integer textSize  = 12;
    private Integer textFont  =  1;
    private Integer textColor =  1;
    
    public AttributesText(){
        
    }
    
    public void setTextColor(int color) { this.textColor = color; }
    public void setTextFont (int font ) { this.textFont  = font; }
    public void setTextSize (int size ) { this.textSize = size; }
    
    public int getTextColor() { return this.textColor; }
    public int getTextSize () { return this.textSize; }
    public int getTextFont () { return this.textFont; }
        
}
