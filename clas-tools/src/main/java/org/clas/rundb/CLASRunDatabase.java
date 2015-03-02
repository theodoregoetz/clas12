/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clas.rundb;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class CLASRunDatabase {
    private static TreeMap<String,CLASRunPeriod>  runPeriods = CLASRunDatabase.initRunPeriods();
            
    public static TreeMap<String,CLASRunPeriod> initRunPeriods(){
        TreeMap<String,CLASRunPeriod> runs = new TreeMap<String,CLASRunPeriod>();
        
        CLASRunPeriod  period_g11a = new CLASRunPeriod("g11a",43490,44133);
        CLASRunPeriod  period_g12a = new CLASRunPeriod("g12a",1000,1005);
        CLASRunPeriod  period_e2a  = new CLASRunPeriod("e2a" ,1000,1005);
        
        runs.put(period_g11a.getName(), period_g11a);
        runs.put(period_g12a.getName(), period_g12a);
        runs.put(period_e2a.getName() , period_e2a);
        return runs;
    }
    
    public static String getRunPeriod(int runno){
        for(Map.Entry<String,CLASRunPeriod> entry : CLASRunDatabase.getCLASRunPeriods().entrySet()){
            if(entry.getValue().hasRun(runno)==true) return entry.getKey();
        }
        return null;
    }
    
    public static TreeMap<String,CLASRunPeriod> getCLASRunPeriods(){
        return runPeriods;
    }
    
    public static ArrayList<String> getMssDirs(int runno){
        String runperiod = CLASRunDatabase.getRunPeriod(runno);
        if(runperiod==null) return new ArrayList<String>();
        return CLASRunDatabase.getCLASRunPeriods().get(runperiod).getMssPaths();
    }
    
    public static ArrayList<String> getMssFiles(int run){
        return new ArrayList<String>();
    }
    
    public static ArrayList<String> getMssFiles(int run, int ext){
        return new ArrayList<String>();
    }
    
}
