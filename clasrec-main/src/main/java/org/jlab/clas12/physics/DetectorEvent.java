/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.physics;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavalian
 */
public class DetectorEvent {
    
    private List<DetectorParticle>  particleList = new ArrayList<DetectorParticle>();
    
    public DetectorEvent(){
        
    }
    
    public void clear(){
        this.particleList.clear();
    }
    
    public void addParticle(DetectorParticle particle){
        
        this.particleList.add(particle);
    }
    
    public List<DetectorParticle> getParticles(){ return this.particleList;}
    
    public void addParticle(double px, double py, double pz,
            double vx, double vy, double vz){
        DetectorParticle particle = new DetectorParticle();
        particle.vector().setXYZ(px, py, pz);
        particle.vertex().setXYZ(vx, vy, vz);
        this.addParticle(particle);
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(" === [ DETECTOR EVENT ] === \n");
        for(DetectorParticle particle : this.particleList){
            str.append(particle.toString());
            str.append("\n");
        }
        return str.toString();
    }
}
