/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.calib;

import org.root.func.F1D;
import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class ComponentH1D {
    private Integer SECTOR = 0;
    private Integer LAYER  = 0;
    private Integer COMPONENT  = 0;
    private H1D  histogram = new H1D();
    private F1D  function  = new F1D("p1");
    
    public ComponentH1D(int sec, int lay, int comp, int bins, double min, double max){
        this.SECTOR = sec;
        this.LAYER = lay;
        histogram.set(bins, min, max);
        
    }
    
    public void setFitParameter(int par){
        
    }
    
}
