/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.tools.utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class CommandLineTools {
    
    private Properties inputLineProperties  = new Properties();
    private TreeMap<String,String>  inputOptions = new TreeMap<String,String>();
    private TreeMap<String,String>  optionDescriptions = new TreeMap<String,String>();
    private String programName = "generic";
    private ArrayList<String>  requiredOptions = new ArrayList<String>();
    private ArrayList<String>  configItems     = new ArrayList<String>();
    
    public CommandLineTools(){
        
    }
    
    public CommandLineTools(String prg){
        this.programName = prg;
    }
    public void setOptions(String options){
        String[] tokens = options.split(":");
        for(String item : tokens){
            this.inputOptions.put(item, "false");
        }
    }
    
    public String usageString(){
        StringBuilder str = new StringBuilder();
        str.append("\n\n");
        str.append(" Usage : ");
        str.append(programName);
        str.append("  [options]\n");
        str.append("\n\n    Options: \n");
        for(Map.Entry<String,String> entry : this.optionDescriptions.entrySet()){
            str.append(String.format("\t%-12s:   %s\n", entry.getKey(),entry.getValue()));
        }
        str.append("\n\n");
        return str.toString();
    }
    
    public ArrayList<String> getConfigItems(){
        return this.configItems;
    }
    
    public void addRequired(String option){
        this.requiredOptions.add(option);
    }
    
    public Boolean isComplete(){
        for(String option : this.requiredOptions){
            if(this.inputLineProperties.keySet().contains(option)==false){
                String errorMsg = "[CommandLineTools] --> option is missing: '" 
                        + option + "' is required by the program.";
                System.err.println(StringUtils.getStringRed(errorMsg));
                System.err.println("\n");
                return false;
            }
        }
        return true;
    }
    
    public void showUsage(){
        System.err.println(this.usageString());
    }
    
    public void addDescription(String option, String description){
        this.optionDescriptions.put(option, description);
    }
    
    public void parse(String[] args){
        this.configItems.clear();
        int icounter = 0;
        int arglength = args.length;
        while(icounter<arglength){
            if(args[icounter].compareTo("-config")==0){
                this.configItems.add(args[icounter+1]);
                icounter += 2;
            } else {
                if(this.inputOptions.containsKey(args[icounter])==true){
                    //this.inputOptions.
                    icounter += 1;
                } else {
                    String key = args[icounter];
                    String val = args[icounter+1];
                    this.inputLineProperties.put(key, val);
                    icounter += 2;
                }
            }
        }
    }
    
    public Boolean hasOption(String option){
        return this.inputLineProperties.keySet().contains(option);
    }
    
    public String asString(String option){
        return (String) this.inputLineProperties.getProperty(option);
    }
    
    public Integer asInteger(String option){
        String value = (String) this.inputLineProperties.getProperty(option);
        return Integer.parseInt(value);
    }
    
    public Double asDouble(String option){
        String value = (String) this.inputLineProperties.getProperty(option);
        return Double.parseDouble(value);
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        
        for(Map.Entry<String,String> entry : this.inputOptions.entrySet()){
            str.append(String.format("%-8s | %-12s\n",entry.getKey(), entry.getValue()));
        }
        
        for(Map.Entry<Object,Object> entry : this.inputLineProperties.entrySet()){
            str.append(String.format("%-8s | %-12s\n",entry.getKey(), entry.getValue()));
        }
        
        return str.toString();
    }
}
