/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.physics;

import org.jlab.clas.physics.EventFilter;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.clasrec.main.DetectorMonitoring;
import org.jlab.clasrec.rec.CLASMonitoring;
import org.jlab.clasrec.utils.ServiceConfiguration;
import org.jlab.evio.clas12.EvioDataEvent;
import org.root.group.PlotGroup;
import org.root.histogram.H1D;
import org.root.pad.DirectoryViewer;

/**
 *
 * @author gavalian
 */
public class KinematicsMonitoring extends DetectorMonitoring {
    private GenericKinematicFitter  fitter = new GenericKinematicFitter(11.0,"11");
    
    public KinematicsMonitoring() {
        super("KINMON","1.0","gavalian");
    }

    @Override
    public void processEvent(EvioDataEvent event) {
        
        
        EventFilter  filterPPip   = new EventFilter("11:2212:211:X-:X+:Xn");
        EventFilter  filterPPim   = new EventFilter("11:2212:-211:X-:X+:Xn");
        EventFilter  filterPipPim = new EventFilter("11:211:-211:X-:X+:Xn");
        
        PhysicsEvent physEvent = fitter.getPhysicsEvent(event);
        //PhysicsEvent physEvent = fitter.getGeneratedEvent(event);
        
        if(filterPPip.isValid(physEvent)==true){
            this.fill("PionProduction", "MissingPionMinus", 
                    physEvent.getParticle("[b]+[t]-[11]-[2212]-[211]").vector().mass());
        }
        
        if(filterPPim.isValid(physEvent)==true){
            this.fill("PionProduction", "MissingPionPlus", 
                    physEvent.getParticle("[b]+[t]-[11]-[2212]-[-211]").vector().mass());
        }
        
        if(filterPipPim.isValid(physEvent)==true){
            this.fill("PionProduction", "MissingProton", 
                    physEvent.getParticle("[b]+[t]-[11]-[211]-[-211]").vector().mass());
            this.fill("PionProduction", "InvariantPions", 
                    physEvent.getParticle("[211]+[-211]").vector().mass());
        }
        
        if(filterPipPim.isValid(physEvent)==true||filterPPim.isValid(physEvent)==true||
                filterPPip.isValid(physEvent)==true){
            this.fill("PionProduction", "Q2",  
                    -physEvent.getParticle("[b]-[11]").vector().mass2());
            this.fill("PionProduction", "W2",  
                    physEvent.getParticle("[b]+[t]-[11]").vector().mass2());
        }
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init() {
        PlotGroup  dblPion = new PlotGroup("PionProduction",2,3);
        dblPion.add("Q2", new H1D("Q2",200,0.0,12.0));
        dblPion.add("W2", new H1D("W2",200,0.0,12.0));
        
        dblPion.add("MissingProton", new H1D("MissingProton",200,0.6,1.4));
        dblPion.add("MissingPionPlus", new H1D("MissingPionPlus",200,0.01,0.3));
        dblPion.add("MissingPionMinus", new H1D("MissingPionMinus",200,0.01,0.3));
        dblPion.add("InvariantPions", new H1D("InvariantPions",200,0.28,1.2));
        
        dblPion.addDescriptor(0, "Q2");
        dblPion.addDescriptor(1, "W2");
        dblPion.addDescriptor(2, "MissingProton");
        dblPion.addDescriptor(3, "MissingPionPlus");
        dblPion.addDescriptor(4, "MissingPionMinus");
        dblPion.addDescriptor(5, "InvariantPions");
        
        this.addGroup(dblPion);
        
        PlotGroup  dvcsPion = new PlotGroup("DvcsProduction",2,3);
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configure(ServiceConfiguration c) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void analyze() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static void main(String[] args){
        String inputFile = args[0];
        System.err.println(" \n[PROCESSING FILE] : " + inputFile);
        KinematicsMonitoring  dcrecMonitor = new KinematicsMonitoring();
        dcrecMonitor.init();
        CLASMonitoring  monitor = new CLASMonitoring(inputFile, dcrecMonitor);
        monitor.process();
        DirectoryViewer browser = new DirectoryViewer(dcrecMonitor.getDirectory());
    }
}
