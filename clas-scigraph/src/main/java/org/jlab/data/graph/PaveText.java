/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.data.graph;

import java.util.ArrayList;
import org.jlab.scichart.attr.AttributesText;
import org.jlab.scichart.group.ScRange;

/**
 *
 * @author gavalian
 */
public class PaveText {
    
    private ScRange  paveTextRange  = new ScRange();
    private ArrayList<String>  paveTexts = new ArrayList<String>();
    private Integer            paveTextSize = 14;
    private AttributesText     paveTextAttributes = new AttributesText();
    
    public PaveText(double cornerX, double cornerY, String options){
        paveTextRange.set(cornerX, cornerY, 1.0, 1.0);
    }
    
    public void setTextFont(int textfont){
        this.paveTextAttributes.setTextFont(textfont);
    }
    
    public void setTextSize(int size){
        this.paveTextAttributes.setTextSize(size);
    }
    
    public void setTextColor( int color ){
        this.paveTextAttributes.setTextColor(color);
    }
    
    public void addText(String text){
        this.paveTexts.add(text);
    }
    
    public String[] getText(){
        String[] textbuffer = new String[this.paveTexts.size()];
        for(int loop = 0; loop < this.paveTexts.size(); loop++){
            textbuffer[loop] = new String(this.paveTexts.get(loop));
        }
        return textbuffer;
    }
    
    public ScRange getTextRange(){ return this.paveTextRange; }
    
    public AttributesText getTextAttributes(){ return paveTextAttributes;}
    
}
