/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

/**
 *
 * @author gavalian
 */
public class DataRegion {
    
    public double MINIMUM_X = 0.0;
    public double MAXIMUM_X = 0.0;
    public double MINIMUM_Y = 0.0;
    public double MAXIMUM_Y = 0.0;
    
    public DataRegion(){
        
    }
    
    public DataRegion(double xmin, double xmax, double ymin, double ymax){
        this.MINIMUM_X = xmin;
        this.MAXIMUM_X = xmax;
        this.MINIMUM_Y = ymin;
        this.MAXIMUM_Y = ymax;
    }
    
    public DataRegion(DataRegion region){
        this.MAXIMUM_X = region.MAXIMUM_X;
        this.MINIMUM_X = region.MINIMUM_X;
        this.MAXIMUM_Y = region.MAXIMUM_Y;
        this.MINIMUM_Y = region.MINIMUM_Y;
    }
    
    public void copy(DataRegion region){
        this.MINIMUM_X = region.MINIMUM_X;
        this.MAXIMUM_X = region.MAXIMUM_X;
        this.MINIMUM_Y = region.MINIMUM_Y;
        this.MAXIMUM_Y = region.MAXIMUM_Y;
    }
    
    public void combine(DataRegion region){
        if(region.MINIMUM_X<this.MINIMUM_X) this.MINIMUM_X = region.MINIMUM_X;
        if(region.MAXIMUM_X>this.MAXIMUM_X) this.MAXIMUM_X = region.MAXIMUM_X;
        if(region.MINIMUM_Y<this.MINIMUM_Y) this.MINIMUM_Y = region.MINIMUM_Y;
        if(region.MAXIMUM_Y>this.MAXIMUM_Y) this.MAXIMUM_Y = region.MAXIMUM_Y;
    }
    
    public double  fractionX(double x){
        double length = this.MAXIMUM_X - this.MINIMUM_X;
        if(length==0) return 0.0;
        if(length<0)  System.err.println("(DataRegion) error: negative length");
        return (x-this.MINIMUM_X)/length;
    }
    
    public double  fractionY(double y){
        double length = this.MAXIMUM_Y - this.MINIMUM_Y;
        if(length==0) return 0.0;
        if(length<0)  System.err.println("(DataRegion) error: negative length");
        return (y-this.MINIMUM_Y)/length;
    }
    
    public Boolean contains(double x, double y){
        return (this.containsX(x)&&this.containsY(y));
    }
    
    public Boolean containsX(double x){
        return (x>=this.MINIMUM_X&&x<=this.MAXIMUM_X);
    }
    
    public Boolean containsY(double y){
        return (y>=this.MINIMUM_Y&&y<=this.MAXIMUM_Y);
    }
    
    public void growX(double lowBoundPercent, double highBoundPercent){
        this.MINIMUM_X = this.MINIMUM_X - lowBoundPercent*(this.MAXIMUM_X-this.MINIMUM_X);
        this.MAXIMUM_X = this.MAXIMUM_X + highBoundPercent*(this.MAXIMUM_X-this.MINIMUM_X);
    }
    
    public void growY(double lowBoundPercent, double highBoundPercent){
        this.MINIMUM_Y = this.MINIMUM_Y - lowBoundPercent*(this.MAXIMUM_Y-this.MINIMUM_Y);
        this.MAXIMUM_Y = this.MAXIMUM_Y + highBoundPercent*(this.MAXIMUM_Y-this.MINIMUM_Y);
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("DataRegion : ( X ) %9.4f %9.4f   ( Y ) %9.4f %9.4f", 
                this.MINIMUM_X,this.MAXIMUM_X,this.MINIMUM_Y, this.MAXIMUM_Y));
        return str.toString();
    }
}
