/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.physics;

import org.jlab.clas.detector.DetectorDescriptor;
import org.jlab.clas.physics.Vector3;

/**
 *
 * @author gavalian
 */
public class DetectorResponse {
    
    private DetectorDescriptor  descriptor  = new DetectorDescriptor();
    private Vector3             hitPosition = new Vector3();
    private Vector3             hitPositionMatched = new Vector3();
    private Double             detectorTime = 0.0;
    private Double           detectorEnergy = 0.0;
    private Double           particlePath   = 0.0;
    
    public DetectorResponse(){
        
    }
    
    public void   setTime(double time){ this.detectorTime = time;}
    public void   setPosition(double x, double y, double z){this.hitPosition.setXYZ(x, y, z);}
    public void   setPath(double path){ this.particlePath = path;}
    public void   setEnergy(double energy) { this.detectorEnergy = energy; }
    
    public double getTime(){ return this.detectorTime;}
    public double getEnergy(){ return this.detectorEnergy; }
    public double getPath(){ return this.particlePath;}
    public Vector3 getPosition(){ return this.hitPosition;}
    public Vector3 getMatchedPosition(){ return this.hitPositionMatched;}
    
    public DetectorDescriptor getDescriptor(){ return this.descriptor;}
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("\t [%8s] [%3d %3d %3d] ", 
                this.descriptor.getType().toString(),
                this.descriptor.getSector(),
                this.descriptor.getLayer(),
                this.descriptor.getComponent()
                ));
        str.append(String.format(" T/E %8.4f %8.4f", this.detectorTime,
                this.detectorEnergy));
        str.append(String.format(" POS [ %8.4f %8.4f %8.4f ]", 
                this.hitPosition.x(),this.hitPosition.y(),this.hitPosition.z()));
        str.append(String.format(" ERROR [ %8.4f %8.4f %8.4f ] ",
                this.hitPosition.x()-this.hitPositionMatched.x(),
                this.hitPosition.y()-this.hitPositionMatched.y(),
                this.hitPosition.z()-this.hitPositionMatched.z()
                ));
        return str.toString();
    }
}
