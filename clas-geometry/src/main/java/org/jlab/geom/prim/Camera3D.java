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
    private final Point3D  cameraPoint  = new Point3D();
    private final Vector3D cameraNormal = new Vector3D();
    private final Vector3D cameraDirection = new Vector3D();
    
    public Camera3D(){
        cameraPoint.set(0.0, 0.0, 0.0);
        cameraNormal.setXYZ(1.0, 0.0, 0.0);
        cameraDirection.setXYZ(0.0,0.0,1.0);        
    }

    @Override
    public void translateXYZ(double dx, double dy, double dz) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void rotateX(double angle) {
        //cameraNormal.rotateX(angle);
    }

    @Override
    public void rotateY(double angle) {
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void rotateZ(double angle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
