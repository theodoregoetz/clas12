/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.base;

/**
 *
 * @author gavalian
 */
public class ScAxisPair {
    
    private final ScAxis axisX = new ScAxis();
    private final ScAxis axisY = new ScAxis();

    public ScAxisPair(){   }
    
    public ScAxisPair(double xmin, double xmax, double ymin, double ymax){
        this.set(xmin, xmax, ymin, ymax);
    }
    
    public final void set(double xmin, double xmax, double ymin, double ymax){
        //System.err.println(" SET XY " + xmin + " " + xmax +
        //        " " + ymin + " " + ymax);
        axisX.setMinMax(xmin, xmax);
        axisY.setMinMax(ymin, ymax);
    }
    
    public ScAxis getAxisX() { return axisX; }
    public ScAxis getAxisY() { return axisY; }
    
}
