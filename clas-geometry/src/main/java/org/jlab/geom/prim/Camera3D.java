/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.geom.prim;

/**
 *
 * @author gavalian
 */
public class Camera3D implements Transformable {
    
    private double  aspectRatio   =  16.0/9.0;
    private double  operture      =  200.0;
    
    private final Point3D  cameraPoint  = new Point3D();
    private final Vector3D cameraNormal = new Vector3D();
    private final Vector3D cameraDirection = new Vector3D();
    private final Plane3D  cameraProjectionPlane = new Plane3D();
    
    
    public Camera3D(){
        cameraPoint.set(0.0, 0.0, 0.0);
        cameraNormal.setXYZ(1.0, 0.0, 0.0);
        cameraDirection.setXYZ(0.0,0.0,1.0);        
    }

    @Override
    public void translateXYZ(double dx, double dy, double dz) {
        cameraPoint.translateXYZ(dx, dy, dz);
        cameraNormal.translateXYZ(dx, dy, dz);
        cameraDirection.translateXYZ(dx, dy, dz);
        cameraProjectionPlane.translateXYZ(dx, dy, dz);
        this.setDistance(this.operture);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void rotateX(double angle) {
        this.cameraPoint.rotateX(angle);
        this.cameraNormal.rotateX(angle);
        this.cameraDirection.rotateX(angle);
        this.cameraProjectionPlane.rotateX(angle);
        this.setDistance(this.operture);
        //cameraNormal.rotateX(angle);
    }

    @Override
    public void rotateY(double angle) {
         this.cameraPoint.rotateY(angle);
        this.cameraNormal.rotateY(angle);
        this.cameraDirection.rotateY(angle);
        this.cameraProjectionPlane.rotateY(angle);
        this.setDistance(this.operture);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void rotateZ(double angle) {
        this.cameraPoint.rotateZ(angle);
        this.cameraNormal.rotateZ(angle);
        this.cameraDirection.rotateZ(angle);
        this.cameraProjectionPlane.rotateZ(angle);
        this.setDistance(this.operture);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setAspectRatio(double ratio){
        this.aspectRatio = ratio;
    }
    
    public void setDistance(double distance){
        this.operture = distance;
        
        Vector3D  normal = new Vector3D(
                -this.cameraNormal.x(),
                -this.cameraNormal.y(),
                -this.cameraNormal.z()
        );
        
        Point3D point = new Point3D(
                this.operture*this.cameraNormal.x(),
                this.operture*this.cameraNormal.y(),
                this.operture*this.cameraNormal.z()
        );
        
        this.cameraProjectionPlane.set(point, normal);
    }
    
    public Point3D  getPoint(Point3D point){
        Line3D  line = new Line3D(point.x(),point.y(),point.z(),
                cameraPoint.x(),cameraPoint.y(),cameraPoint.z());
        Point3D interPoint = new Point3D();
        this.cameraProjectionPlane.intersection(line, interPoint);
        return interPoint;
    }
    
    public Line3D  getLine(Line3D line){
        Point3D p1 = this.getPoint(line.origin());
        Point3D p2 = this.getPoint(line.end());
        return new Line3D(p1,p2);
    }
}
