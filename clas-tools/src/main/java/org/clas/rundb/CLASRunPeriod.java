/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clas.rundb;

import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class CLASRunPeriod {
    
    private String   runPeriodName = "e1a";
    private Integer  runMinimum    = 10000;
    private Integer  runMaximum    = 10005;
    private ArrayList<String>  mssPaths = new ArrayList<String>();
    
    public CLASRunPeriod(){
        
    }
    
    public CLASRunPeriod(String name, int min, int max){
        this.setName(name);
        this.setRunRange(min, max);
    }
    
    public final void setName(String name){
        this.runPeriodName = name;
    }
    
    public String getName(){ return this.runPeriodName;}
    
    public final void setRunRange(int min, int max){
        this.runMinimum = min;
        this.runMaximum = max;
    }
    
    public boolean hasRun(int runnumber){
        return (runnumber>=this.runMinimum&&runnumber<=this.runMaximum);
    }
    
    public void addPath(String path){
        mssPaths.add(path);
    }
    
    public ArrayList<String> getMssPaths(){
        return this.mssPaths;
    }        
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("%-12s : minRun = %12d maxRun = %12d", this.getName(),
                this.runMinimum, this.runMaximum));
        return str.toString();
    }
    
        
}
