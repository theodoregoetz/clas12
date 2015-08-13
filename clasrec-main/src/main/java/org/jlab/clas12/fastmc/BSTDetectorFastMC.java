/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.fastmc;

import java.util.*;
import org.jlab.clasrec.utils.DataBaseLoader;
import org.jlab.geom.DetectorHit;
import org.jlab.geom.DetectorId;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.base.DetectorTransformation;
import org.jlab.geom.detector.bst.BSTFactory;
import org.jlab.geom.detector.bst.BSTLayer;
import org.jlab.geom.prim.*;

/**
 *
 * @author gavalian
 */
public class BSTDetectorFastMC {
    BSTLayer  layerUP   = null;
    BSTLayer  layerDOWN = null;
    DetectorTransformation  bstTransform = null;
    ConstantProvider  bstDB = null;
    BSTFactory   factory = null;

    public BSTDetectorFastMC(int run, String variation, Date date){
        bstDB = DataBaseLoader.getConstantsBST(run,variation,date);
        factory    = new BSTFactory();
        bstTransform = factory.getDetectorTransform(bstDB);
        layerDOWN    = factory.createRingLayer(bstDB,0,0,0);
        layerUP      = factory.createRingLayer(bstDB,0,0,1);
    }

    public boolean intersects(Line3D line, int sector, int region, int layer){
        Shape3D  surface = new Shape3D();
        surface.addFace(new Triangle3D(-2.1,0.0,0.0,-2.1,0.0,33.509,2.1,0.0,0.0));
        surface.addFace(new Triangle3D(-2.1,0.0,33.509,2.1,0.0,0.0,2.1,0.0,33.509));
        Transformation3D trans = bstTransform.get(sector, region,layer);
        trans.apply(surface);
        if(surface.hasIntersectionSegment(line)==true) return true;
        return false;
    }

    public List<DetectorHit>  getHits(Path3D path){

        ArrayList<DetectorHit>     detHits = new ArrayList<DetectorHit>();
        int[] sectors = new int[]{10,14,18,24};

        for(int region = 0; region < 4 ; region++){
            for(int sec = 0; sec < sectors[region]; sec++){
                List<DetectorHit> comp = this.component(path, sec, region, 0);
                detHits.addAll(comp);
                 List<DetectorHit> compD = this.component(path, sec, region, 1);
                detHits.addAll(compD);
            }
        }
        return detHits;
    }

    public List<DetectorHit> component(Path3D path, int sector, int region, int layer){
        int this_layer = 0;
        if(layer==0) this_layer = 1;

        BSTLayer  layer_bst = factory.createRingLayer(bstDB, 0,0,this_layer);

        Transformation3D trans = bstTransform.get(sector, region ,layer);

        layer_bst.setTransformation(trans);
        ArrayList<DetectorHit>  hits = new ArrayList<DetectorHit>();
        double minDistance = 100.0;
        Point3D intersect = new Point3D();
        int comp = -1;
        for(int loop = 0; loop < path.getNumLines(); loop++){
            for(int c = 0; c < 255; c++){
                Line3D  dist = layer_bst.getComponent(c).getLine().distanceSegments(path.getLine(loop));
                double distance = dist.length();
                if(distance<minDistance){
                    intersect.copy(dist.midpoint());
                    minDistance = distance;
                    comp = c;
                }
            }
        }
        //System.out.println(sector + " " + region +  "  " + comp + " " + minDistance);
        if(minDistance<0.01){
            DetectorHit hit = new DetectorHit(DetectorId.BST,sector,region,layer,comp,intersect);
            hits.add(hit);
        }
        return hits;

    }
}
