/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clas.tools.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gavalian
 */
public class Configuration {
    
    private Map<String,Object>  config = new HashMap<String,Object>();
    public Configuration(){
        
    }
    
    public void addSystem(String system){
        config.put(system, new HashMap<String,Object>());
    }
    
    public void addItem(String system,String item, String value){
        if(config.containsKey(system)==false){
            this.addSystem(system);
        }
        ArrayList< Map<String,String> > keyValue = new ArrayList< Map<String,String> >();
        keyValue.add(new HashMap<String,String>() );
        keyValue.get(0).put(item, value);
        HashMap<String,Object> itemMap = (HashMap<String,Object>) config.get(system);
        itemMap.put(item, keyValue);
    }
    
    public void addItem(String system,String item, Integer value){
        if(config.containsKey(system)==false){
            this.addSystem(system);
        }
        HashMap<String,Object> itemMap = (HashMap<String,Object>) config.get(system);
        itemMap.put(item, value);
    }
    
    public void addItem(String system,String item, Double value){
        if(config.containsKey(system)==false){
            this.addSystem(system);
        }
        HashMap<String,Object> itemMap = (HashMap<String,Object>) config.get(system);
        itemMap.put(item, value);
    }
    
    public Object getObject(String system, String item){
        if(config.containsKey(system)==true){
            HashMap<String,Object> itemMap = (HashMap<String,Object>) config.get(system);
            if(itemMap.containsKey(item)==true){
                return itemMap.get(item);
            }
        }
        return null;
    }
    
    public String stringValue(String system, String item){
        Object obj = this.getObject(system, item);
        if(obj!=null){
            if(obj instanceof String){
                return (String) obj;
            }
        }
        return "";
    }
    
    public Double doubleValue(String system, String item){
        Object obj = this.getObject(system, item);
        if(obj!=null){
            if(obj instanceof Double){
                return (Double) obj;
            }
        }
        return 0.0;
    }
    
    public Integer intValue(String system, String item){
        Object obj = this.getObject(system, item);
        if(obj!=null){
            if(obj instanceof Integer){
                return (Integer) obj;
            }
        }
        return 0;
    }
    
    public Map<String,Object> getMap(){
        return this.config;
    }
    
}
