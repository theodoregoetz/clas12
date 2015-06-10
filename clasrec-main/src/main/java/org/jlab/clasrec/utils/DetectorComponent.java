/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.utils;

import org.jlab.data.detector.DetectorType;
import org.jlab.geom.prim.Line3D;
import org.jlab.geom.prim.Point3D;

/**
 *
 * @author gavalian
 */
public class DetectorComponent {
    
    private DetectorType   type = DetectorType.UNDEFINED;
    private Line3D         line = new Line3D();
    private Integer        sector = 0;
    private Integer        layer = 0;
    private Integer        component = 0;
    private Point3D        hposition = new Point3D();
    private Point3D        herror    = new Point3D();
    
    public DetectorComponent(){
        
    }
    
    public DetectorComponent(DetectorType t){
        this.type = t;
    }
    
    public DetectorComponent(DetectorType t, int s, int l, int c){
        this.type = t;
        this.sector = s;
        this.layer  = l;
        this.component = c;
    }
    
    public Point3D getPosition(){ return this.hposition;}
    public Point3D getError(){ return this.herror;}
    public Line3D  getLine(){return this.line;}
    
    public final void    set(DetectorType t, int s, int l, int c){
        this.type = t;
        this.sector = s;
        this.layer  = l;
        this.component = c;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("[%8s %4d %4d %4d]  ", this.type.getName(),
                this.sector,this.layer,this.component));
        str.append(String.format("POS (%8.3f %8.3f %8.3f) ",
                this.hposition.x(),this.hposition.y(),this.hposition.z()));
        str.append(String.format("LINE [%8.3f %8.3f %8.3f] [%8.3f %8.3f %8.3f]", 
                line.origin().x(),this.line.origin().y(), line.origin().z(),
                line.end().x(),line.end().y(),line.end().z()
        ));
        return str.toString();
    }
}
