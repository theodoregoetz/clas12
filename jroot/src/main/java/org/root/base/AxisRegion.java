/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import org.root.attr.Attributes;

/**
 *
 * @author gavalian
 */
public class AxisRegion {
    
    private final Rectangle  axisFrame       = new Rectangle();
    private final DataRegion frameDataRegion = new DataRegion(); 
    private Attributes       axisAttributes  = new Attributes();
    
    public  AxisRegion(){
        this.axisFrame.setBounds(10, 10, 40, 40);
        this.axisAttributes.getProperties().setProperty("background-color", "0");
        this.axisAttributes.getProperties().setProperty("line-color", "1");
        this.axisAttributes.getProperties().setProperty("line-width", "2");        
    }
    
    public DataRegion getDataRegion(){ return this.frameDataRegion; }
    public Rectangle  getFrame() { return this.axisFrame;}
    public Attributes getAttributes(){ return this.axisAttributes;};
    
    
    public double   getFramePointX(double relX){
        //double length = 
        double xp = this.axisFrame.x+this.axisFrame.width*relX;
        return xp;
    }
    
    public double   getFramePointY(double relY){
        //double length = 
        double yp = this.axisFrame.y+ this.axisFrame.height - this.axisFrame.height*relY;
        return yp;
    }
    
}
