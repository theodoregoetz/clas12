/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.jlab.data.detector.DetectorType;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.base.Detector;
import org.jlab.geom.component.ScintillatorPaddle;
import org.jlab.geom.detector.ec.ECDetector;
import org.jlab.geom.detector.ec.ECFactory;
import org.jlab.geom.detector.ftof.FTOFDetector;
import org.jlab.geom.detector.ftof.FTOFFactory;

/**
 *
 * @author gavalian
 */
public class CLASGeometryLoader {
    
    private final TreeMap<String,Detector>  clasDetectors = new TreeMap<String,Detector>();

    public CLASGeometryLoader(){
        
    }
    
    public void loadGeometry(String name){
        
        if(name.compareTo("EC")==0){
            ConstantProvider    cp = DataBaseLoader.getConstantsEC();
            ECFactory      factory = new ECFactory();
            ECDetector     ecdet   = factory.createDetectorCLAS(cp);
            this.clasDetectors.put("EC", ecdet);
        }
        
        if(name.compareTo("FTOF")==0){
            ConstantProvider      cp = DataBaseLoader.getConstantsFTOF();
            FTOFFactory      factory = new FTOFFactory();
            FTOFDetector     ftofdet = factory.createDetectorCLAS(cp);
            this.clasDetectors.put("FTOF", ftofdet);
        }
        
        /*
        if(name.compareTo("FTOF")==0){
            ConstantProvider      cp = DataBaseLoader.getConstantsFTOF();
            FTOFFactory      factory = new FTOFFactory();
            FTOFDetector     ftofdet = factory.createDetectorCLAS(cp);
            this.clasDetectors.put("EC", ftofdet);
        } */       
    }
    
    public Detector  getDetector(String name){
        if(clasDetectors.containsKey(name)==true){
            return clasDetectors.get(name);
        }
        return null;
    }
    
    public DetectorComponent getComponent(DetectorType t, int sector, int layer,
            int component){
        if(t==DetectorType.EC){
            int superlayer = layer/3;
            int reallayer  = layer%3;
            DetectorComponent  comp = new DetectorComponent(DetectorType.EC,sector,layer,component);
            ScintillatorPaddle  paddle = (ScintillatorPaddle) this.clasDetectors.get("EC").getSector(sector).
                    getSuperlayer(superlayer).getLayer(reallayer).getComponent(component);
            comp.getLine().copy(paddle.getLine());
            return comp;
        }
        if(t==DetectorType.FTOF){
            int superlayer = 0;
            int reallayer  = layer;
            DetectorComponent  comp = new DetectorComponent(DetectorType.FTOF,sector,layer,component);
            ScintillatorPaddle  paddle = (ScintillatorPaddle) this.clasDetectors.get("EC").getSector(sector).
                    getSuperlayer(superlayer).getLayer(reallayer).getComponent(component);
            comp.getLine().copy(paddle.getLine());
            return comp;
        }
        
        return null;
    }
    
    public DetectorComponent getComponent(String ts, int sector, int layer,
            int component){
        if(ts.compareTo("EC")==0){
            return this.getComponent(DetectorType.EC, sector, layer, component);
        }
        return null;
    }
    
    
    public List<DetectorComponent>  getComponents(DetectorType t, int[] sector, int[] layer,
            int[] component){
        ArrayList<DetectorComponent> compList = new ArrayList<DetectorComponent>();
        for(int loop = 0; loop < sector.length; loop++){
            DetectorComponent comp = this.getComponent(t, sector[loop], layer[loop],component[loop]);
            if(comp!=null) compList.add(comp);
        }
        return compList;
    }
    
    public List<DetectorComponent>  getComponents(String t, int[] sector, int[] layer,
            int[] component){
        return this.getComponents(DetectorType.getType(t), sector, layer, component);
    }
    
    
    
}
