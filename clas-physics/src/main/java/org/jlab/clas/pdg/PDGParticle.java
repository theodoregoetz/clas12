/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.pdg;

/**
 *
 * @author gavalian
 */
public class PDGParticle {
    
    String  particleName;
    Integer particleId;
    Integer particleIdGeant;
    Double  particleMass;
    Integer    particleCharge;
    Double     particleWidth;
    
    public PDGParticle(String partname, int partid, double partmass)
    {
        particleName = partname;
        particleId   = partid;
        particleMass = partmass;
    }
    
    public PDGParticle(String partname, int partid, double partmass, int charge)
    {
        particleName = partname;
        particleId   = partid;
        particleMass = partmass;
        particleCharge = charge;
        particleWidth  = 0.0;
    }
    
    public PDGParticle(String partname, int partid, int geantid, double partmass, int charge)
    {
        particleName = partname;
        particleId   = partid;
        particleMass = partmass;
        particleCharge = charge;
        particleWidth  = 0.0;
    }
    
    public PDGParticle(String partname, int partid, double partmass, int charge, double width)
    {
        particleName = partname;
        particleId   = partid;
        particleMass = partmass;
        particleCharge = charge;
        particleWidth  = width;
    }
    
    public int charge()
    { return particleCharge;}
    
    public String name()
    {
        return particleName;
    }
    
    public int gid(){
        return particleIdGeant;
    }
    
    public int    pid()
    {
        return particleId;
    }
    
    public double mass()
    {
        return particleMass;
    }
    
    public double width()
    {
        return particleWidth;
    }
}
