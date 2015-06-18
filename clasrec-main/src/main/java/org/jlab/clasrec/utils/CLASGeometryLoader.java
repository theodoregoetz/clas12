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
import org.jlab.geom.gui.DetectorComponentUI;

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
    
    
    public List<DetectorComponentUI>  getLayerUI(String detector, int sector, int layer){
        if(detector.compareTo("BST")==0){
            ArrayList<DetectorComponentUI>  uiList = new ArrayList<DetectorComponentUI>();
            int[] sectors = new int[]{10,14,18,24};
            double[] radius = new double[]{200,300,400,500};
            double   width  = 30;
            double   angleGap = 1.0;
            for(int loop = 0; loop < 8; loop++){
                int region = loop/2;
                int regionLayer = loop%2;
                double angle = 360.0/sectors[region];
                for(int sec = 0; sec < sectors[region]; sec++){
                    DetectorComponentUI comp = new DetectorComponentUI();
                    comp.COMPONENT = -1;
                    comp.SECTOR    = sec;
                    comp.LAYER     = loop;
                    
                    double xc = (radius[region] + regionLayer*width)*Math.cos(Math.toRadians(angle*sec));
                    double yc = (radius[region] + regionLayer*width)*Math.sin(Math.toRadians(angle*sec));
                    double xp = 0;
                    double yp = 0;
                    double offset = width*regionLayer;
                    xp = (radius[region] + offset - width/4.0)*Math.cos(Math.toRadians(angle*sec - angle/2.0 + angleGap));
                    yp = (radius[region] + offset - width/4.0)*Math.sin(Math.toRadians(angle*sec - angle/2.0 + angleGap));
                    comp.shapePolygon.addPoint((int) yp,(int) -xp);

                    xp = (radius[region] + offset + width/4.0)*Math.cos(Math.toRadians(angle*sec - angle/2.0 + angleGap));
                    yp = (radius[region] + +offset+ width/4.0)*Math.sin(Math.toRadians(angle*sec - angle/2.0 + angleGap));
                    comp.shapePolygon.addPoint((int) yp,(int) -xp);

                    xp = (radius[region] + offset + width/4.0)*Math.cos(Math.toRadians(angle*sec + angle/2.0 - angleGap));
                    yp = (radius[region] + offset + width/4.0)*Math.sin(Math.toRadians(angle*sec + angle/2.0 - angleGap));
                    comp.shapePolygon.addPoint((int) yp,(int) -xp);
                    
                    xp = (radius[region] + offset - width/4.0)*Math.cos(Math.toRadians(angle*sec + angle/2.0 - angleGap));
                    yp = (radius[region] + offset - width/4.0)*Math.sin(Math.toRadians(angle*sec + angle/2.0 - angleGap));
                    comp.shapePolygon.addPoint((int) yp,(int) -xp);
                    
                    /*
                    xp = (radius[region] - regionLayer*width/2.0)*Math.cos(Math.toRadians(angle*sec - sec*angle/2.0));
                    yp = (radius[region] - regionLayer*width/2.0)*Math.sin(Math.toRadians(angle*sec - sec*angle/2.0));
                    comp.shapePolygon.addPoint((int) xp,(int) yp);

                    xp = (radius[region] + regionLayer*width/2.0)*Math.cos(Math.toRadians(angle*sec - sec*angle/2.0));
                    yp = (radius[region] + regionLayer*width/2.0)*Math.sin(Math.toRadians(angle*sec - sec*angle/2.0));
                    comp.shapePolygon.addPoint((int) xp,(int) yp);

                    xp = (radius[region] + regionLayer*width/2.0)*Math.cos(Math.toRadians(angle*sec + sec*angle/2.0));
                    yp = (radius[region] + regionLayer*width/2.0)*Math.sin(Math.toRadians(angle*sec + sec*angle/2.0));
                    comp.shapePolygon.addPoint((int) xp,(int) yp);
                    
                    xp = (radius[region] - regionLayer*width/2.0)*Math.cos(Math.toRadians(angle*sec + sec*angle/2.0));
                    yp = (radius[region] - regionLayer*width/2.0)*Math.sin(Math.toRadians(angle*sec + sec*angle/2.0));
                    comp.shapePolygon.addPoint((int) xp,(int) yp);
                    */
                    uiList.add(comp);
                }
            }
            return uiList;
        }
        return null;
    }
}
