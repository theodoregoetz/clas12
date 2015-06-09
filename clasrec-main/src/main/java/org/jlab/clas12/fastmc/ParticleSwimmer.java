/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.fastmc;

import cnuphys.magfield.CompositeField;
import cnuphys.magfield.Solenoid;
import cnuphys.magfield.Torus;
import cnuphys.rk4.RungeKuttaException;
import cnuphys.swim.DefaultListener;
import cnuphys.swim.DefaultSwimStopper;
import cnuphys.swim.SwimTrajectory;
import cnuphys.swim.Swimmer;
import java.io.File;
import java.io.FileNotFoundException;
import org.jlab.clas.physics.Particle;
import org.jlab.geom.prim.Path3D;
import org.jlab.utils.CLASResources;

/**
 *
 * @author gavalian
 */
public class ParticleSwimmer {
    private  CompositeField compositeField;
    private static final double rmax = 8.0;
    private static final double maxPathLength = 12.5;
    private static final double hdata[] = new double[3];
    
    private  Swimmer swimmer = null;
    //MagneticFields.getIField(MagneticFields.FieldType.TORUS));
    public ParticleSwimmer(){
         this.initFiled(-1.0, 1.0);
         swimmer = new Swimmer(this.compositeField);
    }
        
    public ParticleSwimmer(double tscale, double sscale){
        this.initFiled(tscale, sscale);
        swimmer = new Swimmer(this.compositeField);        
    }
    
    private void initFiled(Double torusScale, Double solenoidScale){
        Torus torus = null;
         Solenoid solenoid = null;
         //will read mag field assuming 
         String clasDictionaryPath = CLASResources.getResourcePath("etc");		
         String torusFileName = clasDictionaryPath + "/data/magfield/clas12-fieldmap-torus.dat";		
         File torusFile = new File(torusFileName);
         try {
             torus = Torus.fromBinaryFile(torusFile);
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
		
         //OK, see if we can create a Solenoid
         String solenoidFileName = clasDictionaryPath + "/data/magfield/clas12-fieldmap-solenoid.dat";
         //OK, see if we can create a Torus
         if(clasDictionaryPath == "../clasJLib")
             solenoidFileName = clasDictionaryPath + "/data/solenoid/v1.0/solenoid-srr.dat";
         
         File solenoidFile = new File(solenoidFileName);
         try {
             solenoid = Solenoid.fromBinaryFile(solenoidFile);
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
         
		
         compositeField = new CompositeField();
         
         if (torus != null) {
             /*
             if(torusScale<0) {
                 if(torus.isInvertField()==false)
                     torus.setInvertField(true);
             }
             
             if(torusScale>0) {
                 if(torus.isInvertField()==true)
                     torus.setInvertField(false);
             }
             
             torus.setScaleField(true);*/
             torus.setScaleFactor(torusScale);
             
             compositeField.add(torus);
         }
         
         if (solenoid != null) {
             /*
             if(solenoidScale<0) {
                 if(solenoid.isInvertField()==false)
                     solenoid.setInvertField(true);
             }
             
             if(solenoidScale>0) {
                 if(solenoid.isInvertField()==true)
                     solenoid.setInvertField(false);
             }
             solenoid.setScaleField(true);
             */
             solenoid.setScaleFactor(solenoidScale);
             
             compositeField.add(solenoid);
             
         }
    }
    
    public Path3D particlePath(Particle part){
        
        if(part.charge()==0){
            Path3D  ppath = new Path3D();
            ppath.addPoint(part.vertex().x(), 
                    part.vertex().y(),part.vertex().z());
            ppath.addPoint(
                    1500.0 * part.px(),
                    1500.0 * part.py(),
                    1500.0 * part.pz()
                    );
            return ppath;
        }
        
        DefaultListener listener = new DefaultListener();
        DefaultSwimStopper stopper = new DefaultSwimStopper(rmax);
        
        // step size in m
        double stepSize = 5e-4; // m
        int  charge     = part.charge();
        try {
            /*
            int nstep = swimmer.swim(charge, 
                    part.vertex().x(),part.vertex().y(),part.vertex().z(),
                    part.vector().p(),part.vector().theta(),part.vector().phi(),
                    stopper, listener, maxPathLength,
                    stepSize, Swimmer.CLAS_Tolerance, hdata);*/
            SwimTrajectory traj = swimmer.swim(charge, 
                                        part.vertex().x(),part.vertex().y(),part.vertex().z(),
                                        part.vector().p(),
                                        Math.toDegrees(part.vector().theta()),
                                        Math.toDegrees(part.vector().phi()),
                                        stopper, maxPathLength, stepSize,
					Swimmer.CLAS_Tolerance, hdata);
            Path3D  particlePath = new Path3D();
            
            for(int loop = 0; loop < traj.size(); loop++){
                particlePath.addPoint(
                        100.0*traj.get(loop)[0], 
                        100.0*traj.get(loop)[1], 
                        100.0*traj.get(loop)[2] 
                );
            }
            return particlePath;
            //double[] lastY = listener.getLastStateVector();
            //printSummary("\nresult from adaptive stepsize method with errvect",
            //        nstep, momentum, lastY, hdata);
            
        } catch (RungeKuttaException e) {
            e.printStackTrace();
        }
        return null;
    }
}
