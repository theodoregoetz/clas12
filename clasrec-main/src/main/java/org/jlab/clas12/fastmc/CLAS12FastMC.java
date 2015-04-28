/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.fastmc;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.jlab.clas.physics.Particle;
import org.jlab.clasrec.utils.DataBaseLoader;
import org.jlab.geom.DetectorHit;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.base.Detector;
import org.jlab.geom.detector.dc.DCFactory;
import org.jlab.geom.detector.ec.ECFactory;
import org.jlab.geom.detector.ftof.FTOFFactory;
import org.jlab.geom.prim.Path3D;

/**
 *
 * @author gavalian
 */
public class CLAS12FastMC {
    private ParticleSwimmer  particleSwimmer = null;
    private final TreeMap<String,Detector>  detectors = new TreeMap<String,Detector>();
    private final TreeMap<String,Integer>   detectorMinHits = new TreeMap<String,Integer>();
        
    public CLAS12FastMC(double torus, double solenoid){
        particleSwimmer = new ParticleSwimmer(torus,solenoid);
        this.initDetectors();
        this.initDetectosHits();
    }
    
    private void initDetectors(){
        // Init DC detector
        DCFactory      dcfactory = new DCFactory();
        ConstantProvider  dcdata = DataBaseLoader.getConstantsDC();
        Detector geomDC = dcfactory.createDetectorCLAS(dcdata);
        this.detectors.put("DC", geomDC);
        // Init FTOF detector
        FTOFFactory ftoffactory = new FTOFFactory();
        ConstantProvider  ftofdata = DataBaseLoader.getTimeOfFlightConstants();
        Detector geomFTOF = ftoffactory.createDetectorCLAS(ftofdata);
        this.detectors.put("FTOF", geomFTOF);
        // Init EC detector 
        ECFactory ecfactory = new ECFactory();
        ConstantProvider  ecdata = DataBaseLoader.getCalorimeterConstants();
        Detector geomEC = ecfactory.createDetectorCLAS(ecdata);
        this.detectors.put("EC", geomEC);
    }
    
    private void initDetectosHits(){
        this.detectorMinHits.put("DC"   , 36);
        this.detectorMinHits.put("FTOF" , 1);
        this.detectorMinHits.put("EC"   , 30);
    }
    
    public List<DetectorHit>  getDetectorHits(String det, Path3D path){
        if(this.detectors.containsKey(det)==true){
            return this.detectors.get(det).getHits(path);
        }
        return new ArrayList<DetectorHit>();
    }
    
    public boolean isParticleDetected(Particle  part){
        Path3D  path = this.particleSwimmer.particlePath(part);
        List<DetectorHit>  dcHits = this.getDetectorHits("DC", path);
        if(dcHits==null) return false;
        if(dcHits.size()<this.detectorMinHits.get("DC")) return false;

        List<DetectorHit>  ftofHits = this.getDetectorHits("FTOF", path);
        if(ftofHits.size()<this.detectorMinHits.get("FTOF")) return false;
        
        List<DetectorHit> ecHits    = this.detectors.get("EC").getLayerHits(path);
        
        //System.out.println(" EC LAYER HITS = " + ecHits.size());
        if(ecHits.size()<this.detectorMinHits.get("EC")) return false;
        return true;
    }
    
}
