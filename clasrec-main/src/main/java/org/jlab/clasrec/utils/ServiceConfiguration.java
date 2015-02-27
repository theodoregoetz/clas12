/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clasrec.utils;

import java.util.Map;
import org.clas.tools.config.Configuration;


/**
 *
 * @author gavalian
 */
public class ServiceConfiguration {
    
    private final Configuration config = new Configuration();
    
    public ServiceConfiguration(){
        
    }
    
    public boolean hasItem(String system, String item){
        return this.config.hasItem(system, item);
    }
    
    public void addItem(String name, String type, Double value){ 
        this.config.addItem(name, type, value);
    }
    public void addItem(String name, String type, Integer value){ 
        this.config.addItem(name, type, value);
    }
    public void addItem(String name, String type, String value){ 
        this.config.addItem(name, type, value);
    }
    
    public void addItem(String name, String type, String defvalue, String values){
        
    }
    
    public double asDouble(String system, String item){
        return config.doubleValue(system, item);
    }
    
    public int    asInteger(String system, String item){
        return config.intValue(system, item);
    }
    
    public String asString(String system, String item){
        return this.config.stringValue(system, item);
    }
    
    public void show(){
        for(Map.Entry<String,Object> system : config.getMap().entrySet()){
            System.out.println(" SYSTEM = " + system.getKey());
            Map<String,Object> items = (Map<String,Object>) system.getValue();

            for(Map.Entry<String,Object> item : items.entrySet()){
                System.out.println(" KEY " + item.getKey() + "   :  VALUE  =  "
                + item.getValue());
            }
        }
    }
}
