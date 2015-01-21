/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.main;

import org.jlab.clasrec.utils.ServiceConfiguration;
import org.jlab.data.histogram.H1D;
import org.jlab.data.histogram.HDirectory;
import org.jlab.evio.clas12.EvioDataEvent;

/**
 *
 * @author gavalian
 */
public abstract class DetectorMonitoring {
    
    private String moduleName      = "undef";
    private String moduleVersion   = "0.5";
    private String moduleAuthor    = "unknown";
    private final HDirectory  hDirectory = new HDirectory();
    
    public DetectorMonitoring(String name, String version, String author){
        this.moduleName     = name;
        this.moduleVersion  = version;
        this.moduleAuthor   = author;
        hDirectory.setName(name);
    }
    
    public abstract void processEvent(EvioDataEvent event);
    public abstract void init();
    public abstract void configure(ServiceConfiguration c);
    public abstract void analyze();    
    
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
    
    public HDirectory getDirectory(){
        return hDirectory;
    }
    
}
