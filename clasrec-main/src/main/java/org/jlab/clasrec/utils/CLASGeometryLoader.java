/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jlab.data.detector.DetectorType;
import org.jlab.geom.DetectorHit;
import org.jlab.geom.DetectorId;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.base.Detector;
import org.jlab.geom.base.DetectorTransformation;
import org.jlab.geom.component.ScintillatorPaddle;
import org.jlab.geom.component.SiStrip;
import org.jlab.geom.detector.bst.BSTFactory;
import org.jlab.geom.detector.bst.BSTLayer;
import org.jlab.geom.detector.ec.ECDetector;
import org.jlab.geom.detector.ec.ECFactory;
import org.jlab.geom.detector.ftof.FTOFDetector;
import org.jlab.geom.detector.ftof.FTOFFactory;
import org.jlab.geom.gui.DetectorComponentUI;
import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Path3D;
import org.jlab.geom.prim.Point3D;
import org.jlab.geom.prim.Shape3D;
import org.jlab.geom.prim.Transformation3D;
import org.jlab.geom.prim.Triangle3D;

/**
 *
 * @author gavalian
 */
public class CLASGeometryLoader {
    
    private final TreeMap<String,Detector>  clasDetectors = new TreeMap<String,Detector>();
    private final ArrayList<BSTLayer>       bstLayers     = new ArrayList<BSTLayer>();
    private DetectorTransformation  bstTransform = null;
    
    public CLASGeometryLoader(){
        
    }
    
    public void loadGeometry(String name){
        
        if(name.compareTo("EC")==0){
            System.out.println("[GEOMETRY-LOADER] --> loading geometry EC");
            ConstantProvider    cp = DataBaseLoader.getConstantsEC();
            ECFactory      factory = new ECFactory();
            ECDetector     ecdet   = factory.createDetectorCLAS(cp);
            this.clasDetectors.put("EC", ecdet);
        }
        
        if(name.compareTo("FTOF")==0){
            System.out.println("[GEOMETRY-LOADER] --> loading geometry FTOF");
            ConstantProvider      cp = DataBaseLoader.getConstantsFTOF();
            FTOFFactory      factory = new FTOFFactory();
            FTOFDetector     ftofdet = factory.createDetectorCLAS(cp);
            this.clasDetectors.put("FTOF", ftofdet);
        }
        
        if(name.compareTo("BST")==0){
            System.out.println("[GEOMETRY-LOADER] --> loading geometry BST");
            bstLayers.clear();
            ConstantProvider      cp = DataBaseLoader.getConstantsBST();
            BSTFactory   factory    = new BSTFactory();     
            bstTransform = factory.getDetectorTransform(cp);
            BSTLayer layerDOWN    = factory.createRingLayer(cp,0,0,0);
            BSTLayer layerUP      = factory.createRingLayer(cp,0,0,1);
            System.out.println("BOUNDARY \n " + layerUP.getBoundary());
            bstLayers.add(layerDOWN);
            bstLayers.add(layerUP);
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
    
    public List<DetectorHit>  getDetectorHits(String detector, Path3D path){
        if(detector.compareTo("BST")==0){
            ArrayList<DetectorHit>  hits = new ArrayList<DetectorHit>();
            int[] sectors = new int[]{10,14,18,24};
            for(int region = 0; region < 4; region++){
                for(int sec = 0; sec < sectors[region]; sec++){
                    for(int layer = 0; layer < 2; layer++){
                        DetectorHit hit = this.getDetectorHit(path, sec, region, layer);
                        if(hit!=null) hits.add(hit);
                    }
                }
            }
            return hits;
        }
        
        if(detector.compareTo("FTOF")==0){
            if(this.clasDetectors.containsKey("FTOF")==true){
                return this.clasDetectors.get("FTOF").getHits(path);
            }
        }
        
        if(detector.compareTo("EC")==0){
            if(this.clasDetectors.containsKey("EC")==true){
                return this.clasDetectors.get("EC").getHits(path);
            }
        }
        return null;
    }
    
    public List<DetectorHit>  getDetectorHits(Path3D path){
        ArrayList<DetectorHit>  dhits = new ArrayList<DetectorHit>();
        if(this.bstLayers.size()>0&&this.bstTransform!=null){
            List<DetectorHit> hits = this.getDetectorHits("BST", path);
            if(hits!=null) dhits.addAll(hits);
        }
        
        for(Map.Entry<String,Detector>  entry : this.clasDetectors.entrySet()){
            List<DetectorHit> hits = this.getDetectorHits(entry.getKey(), path);
            if(hits!=null) dhits.addAll(hits);
        }
        return dhits;
    }
    
    public DetectorHit  getDetectorHit(Path3D path, int sector, int region, int layer){
        Transformation3D trans   = bstTransform.get(sector,region,layer);
        Shape3D  boundary = new Shape3D();
        boundary.addFace(new Triangle3D( -2.00160,  0.00000,  22.40850,
                2.00160,      0.00000,     22.40850,
                -2.00160,      0.00000,     33.40400
        ));
        
        boundary.addFace(new Triangle3D(
                2.00160,      0.00000,     33.40400,
                -2.00160,      0.00000,     33.40400,
                2.00160,      0.00000,     22.40850
        ));
        
        if(layer<0||layer>1) return null;
        
        trans.apply(boundary);
        
        
        List<SiStrip>  strips = this.bstLayers.get(layer).getAllComponents();
        int cid = -1;
        double distance = 100.0;
        Point3D  ip = new Point3D();
        Line3D   stripLine = new Line3D();
        
        for(int loop = 0; loop < path.getNumLines(); loop++){
            if(boundary.hasIntersectionSegment(path.getLine(loop))==true){
                for(SiStrip strip : strips){
                    stripLine.copy(strip.getLine());
                    trans.apply(stripLine);  
                    if(stripLine.distance(path.getLine(loop)).length()<distance){
                        ip.copy( stripLine.distance(path.getLine(loop)).midpoint());
                        distance = stripLine.distance(path.getLine(loop)).length();
                        cid      = strip.getComponentId();
                    }                    
                }
            }
        }
        if(cid<=0||cid==255) return null;
        //System.out.println(" S/L/C " + " " + region + " " + sector + "  " + layer + " distance = " + distance);
        DetectorHit hit = new DetectorHit(DetectorId.BST,sector,region,layer,cid,ip);
        return hit;
    }
}
