/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.main;

import java.awt.Color;
import org.jlab.clasrec.ui.IDetectorHistogramDraw;
import org.jlab.clasrec.utils.ServiceConfiguration;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.geom.gui.IDetectorShapeIntensity;
import org.root.group.PlotDirectory;
import org.root.group.PlotGroup;
import org.root.group.TDirectory;
import org.root.histogram.H1D;
import org.root.histogram.H2D;
import org.root.pad.EmbeddedCanvas;

/**
 *
 * @author gavalian
 */
public abstract class DetectorMonitoring implements IDetectorHistogramDraw,IDetectorShapeIntensity {
    
    private String moduleName      = "undef";
    private String moduleVersion   = "0.5";
    private String moduleAuthor    = "unknown";
    private final PlotDirectory  hDirectory = new PlotDirectory();
    private final TDirectory  monDirectory = new TDirectory();
    
    public DetectorMonitoring(String name, String version, String author){
        this.moduleName     = name;
        this.hDirectory.setName(name);
        this.moduleVersion  = version;
        this.moduleAuthor   = author;
        hDirectory.setName(name);
    }
    
    public abstract void processEvent(EvioDataEvent event);
    public abstract void init();
    public abstract void configure(ServiceConfiguration c);
    public abstract void analyze();
    
    public String getName(){ return moduleName;}
    
    public void addDirectory(TDirectory dir){
        this.monDirectory.addDirectory(dir);
    }
    
    public TDirectory getDir(){
        return this.monDirectory;
    }
    
    public void addGroup(PlotGroup group){
        this.hDirectory.addGroup(group);
    }
    /*
    public void add(String group, String histname, int bins, double xmin, 
            double xmax){
        if(hDirectory.getGroups().containsKey(group)==false){
            hDirectory.addGroup(group);
        }
        
        hDirectory.group(group).add(histname, bins, xmin, xmax);
    }
    
    public void fill(String group, String histname, double value){
        if(hDirectory.getGroups().containsKey(group)==true){
            H1D hist = hDirectory.group(group).getH1D(histname);
            if(hist!=null){
                hist.fill(value);
            } else {
                System.err.println("[DetectorMonitoring] ("+ this.moduleName +
                    ")  group  " + group + "  does not have histogram " 
                + histname);
            }
        } else {
            System.err.println("[DetectorMonitoring] ("+ this.moduleName +
                    ") does not have a plotting group \""+group +"\"");
        }
    }
    */
    public void fill(String group, String hist, double value)
    {        
        if(hDirectory.getGroup(group) != null){
            Object h1d = hDirectory.getGroup(group).getObjects().get(hist);
            if(h1d!=null){
                if(h1d instanceof H1D){
                    //System.out.println("Fillinf histogram");
                    ((H1D) h1d).fill(value);
                } else {
                    System.err.println(" --- error --- " + h1d.getClass());
                }
            } else {
                System.err.println(" histogram not found with name " + hist);
            }
        } else {
            System.err.println(" Group not found with name " + group);
        }    
    }
    
    public void fill(String group, String hist, double vx, double vy)
    {
        if(hDirectory.getGroup(group) != null){
            Object h2d = hDirectory.getGroup(group).getObjects().get(hist);
            if(h2d!=null){
                if(h2d instanceof H2D){
                    ((H2D) h2d).fill(vx,vy);
                }
            }
        }        
    }
    
    public PlotDirectory getDirectory(){
        return hDirectory;
    }    

    public void drawComponent(int sector, int layer, int component, EmbeddedCanvas canvas) {
        
    }

    public void drawLayer(int sector, int layer, EmbeddedCanvas canvas) {
        
    }

    public void drawSector(int sector, EmbeddedCanvas canvas) {
        
    }
    
    public Color getColor(int sector, int layer, int component) {
        
        if(component%2==0){
            return  new Color(77,176,221);
        }
        return new Color(137,216,68);
    }
    
}
