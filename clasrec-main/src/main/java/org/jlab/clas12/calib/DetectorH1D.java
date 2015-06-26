/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.calib;

import java.util.TreeMap;
import org.jlab.clas.detector.DetectorDescriptor;
import org.jlab.clas.detector.DetectorType;

/**
 *
 * @author gavalian
 */
public class DetectorH1D {
    private TreeMap<Integer,ComponentH1D>  componentH1D = new TreeMap<Integer,ComponentH1D>();
    private DetectorType   detectorType = DetectorType.UNDEFINED;
    public DetectorH1D(){
        
    }
    
    public DetectorH1D(DetectorType type){
        this.detectorType = type;
    }
    
    public void add(int sector, int layer, int component, int nbins, double xmin, double xmax){
        ComponentH1D hist = new ComponentH1D(sector,layer,component,nbins,xmin,xmax);
        this.add(hist);
    }
    
    public void add(ComponentH1D h1d){
        this.componentH1D.put(h1d.getDescriptor().getHashCode(), h1d);
    }
    
    public ComponentH1D get(int sector, int layer, int comp){
        int hash = DetectorDescriptor.generateHashCode(sector, layer, comp);
        if(this.componentH1D.containsKey(hash)==true){
            return this.componentH1D.get(hash);
        }
        return null;
    }
    
    
}
