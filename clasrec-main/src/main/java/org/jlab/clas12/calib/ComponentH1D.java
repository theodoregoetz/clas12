/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.calib;

import org.jlab.clas.detector.DetectorDescriptor;
import org.root.func.F1D;
import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class ComponentH1D {
    
    private DetectorDescriptor  descriptor = new DetectorDescriptor();
    private Integer fitParameter           = 0;
    private H1D  histogram = new H1D();
    private F1D  function  = new F1D("p1");
    
    public ComponentH1D(int sec, int lay, int comp, int bins, double min, double max){
        this.descriptor.setSectorLayerComponent(sec, lay, comp);
        histogram.set(bins, min, max);        
    }
    
    public void setFitParameter(int par){
        this.fitParameter = par;
    }
    
    public double getFittdParameter(){
        return this.function.getParameter(this.fitParameter);
    }
    
    public H1D getHistogram(){return this.histogram;}
    public F1D getFunction(){return function;}
    public DetectorDescriptor getDescriptor(){return this.descriptor;}
}
