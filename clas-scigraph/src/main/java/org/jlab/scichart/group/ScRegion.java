/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.group;

/**
 *
 * @author gavalian
 */
public class ScRegion {
    
    private Double positionX  = 0.0;
    private Double positionY  = 0.0;
    private Double rectWidth  = 10.0;
    private Double rectHeight = 10.0;
    
    public ScRegion(){
        
    }
    
    public ScRegion(double x, double y, double w, double h){
        this.setSize(x, y, w, h);
    }
    
    public final void setSize(double x, double y, double w, double h){
        positionX  = x; positionY  = y;
        rectWidth  = w; rectHeight = h;
    }
    
    public boolean isInside(double x, double y){
        return (this.isInsideX(x) != false && this.isInsideY(y) != false);
    }
    
    public boolean isInsideX(double x){
        return (x >= positionX && x <= (positionX+rectWidth));
    }
    
    public boolean isInsideY(double y){
        return (y >= positionY && y <= (positionY+rectHeight));
    }
    
    public double getX()      { return positionX;  }
    public double getY()      { return positionY;  }
    public double getWidth()  { return rectWidth;  } 
    public double getHeight() { return rectHeight; } 
    
    public double getNormalizedX(double xr){
        double xrn = xr;
        if(xr<0.0) xrn = 0.0;
        if(xr>1.0) xrn = 1.0;
        return positionX + xrn*rectWidth;
    }
    
    public double getNormalizedY(double yr){
        double yrn = yr;
        if(yr<0.0) yrn = 0.0;
        if(yr>1.0) yrn = 1.0;
        return positionY + yrn*rectHeight;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("Region : %12.5f %12.5f %12.5f %12.5f", 
                this.getX(),this.getY(), this.getWidth(), this.getHeight()));
        return str.toString();
    }
}
