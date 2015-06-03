/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.rec;

import java.util.ArrayList;
import org.jlab.clas.tools.benchmark.ProgressPrintout;
import org.jlab.clasrec.main.DetectorMonitoring;
import org.jlab.clasrec.ui.IDetectorHistogramDraw;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;
import org.root.pad.EmbeddedCanvas;

/**
 *
 * @author gavalian
 */
public class CLASMonitoring implements IDetectorHistogramDraw {
    
    private final ArrayList<DetectorMonitoring>  detectorFactory =
            new ArrayList<DetectorMonitoring>();
    
    private String inputFileName = "";
    
    public CLASMonitoring(String file, DetectorMonitoring detector){
        this.inputFileName = file;
        detectorFactory.add(detector);
    }
    
    
    public void process(){
        ProgressPrintout printout = new ProgressPrintout("Monitoring ");
        printout.setInterval(1.0);
        
        detectorFactory.get(0).init();
        EvioSource  reader = new EvioSource();
        reader.open(this.inputFileName);
        int icounter = 0;
        while(reader.hasEvent()){
            icounter++;
            EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
            try {
                detectorFactory.get(0).processEvent(event);
            } catch (Exception e){
                System.err.println("[CLASMonitoring] ----> error in event "
                + icounter);
                e.printStackTrace();
            }
            
            printout.setAsInteger("nevents", icounter);
            printout.updateStatus();
         }
        detectorFactory.get(0).analyze();
    }

    public void drawComponent(int sector, int layer, int component, EmbeddedCanvas canvas) {
        
    }

    public void drawLayer(int sector, int layer, EmbeddedCanvas canvas) {
        
    }

    public void drawSector(int sector, EmbeddedCanvas canvas) {
        
    }
}
