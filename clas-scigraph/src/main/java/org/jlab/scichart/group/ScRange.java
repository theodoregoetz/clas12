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
public class ScRange {
    
    private Double       boundaryLowX = 0.1;
    private Double       boundaryLowY = 0.1;
    private Double       boundaryHiX  = 0.9;
    private Double       boundaryHiY  = 0.9;
    
    public ScRange(){
        
    }
    
    public ScRange(double xl, double yl, double xh, double yh){
        this.set(xl, yl, xh, yh);
    }
    
    public final void set(double xl, double yl, double xh, double yh){
          boundaryLowX = xl;
          boundaryLowY = yl;
          boundaryHiX  = xh;
          boundaryHiY  = yh;
    }
    
    public double getLowBoundX() { return boundaryLowX; }
    public double getLowBoundY() { return boundaryLowY; }
    public double getHighBoundX() { return boundaryHiX; }
    public double getHighBoundY() { return boundaryHiY; }
}
