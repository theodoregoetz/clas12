/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.attr;

import java.util.Properties;

/**
 *
 * @author gavalian
 */
public class Attributes {
    
    private Properties attr = new Properties();
    
    public Attributes(){
        
    }
    
    public void addLineProperties(){
        attr.setProperty("line-color", "1");
        attr.setProperty("line-width", "1");
        attr.setProperty("line-style", "1");
    }
    
    public void addFillProperties(){
        attr.setProperty("fill-color", "-1");
    }
    
    public void addMarkerAttributes(){
        attr.setProperty("marker-color"  , "2");
        attr.setProperty("marker-size"   , "4");
        attr.setProperty("marker-style"  , "1");
    }
    
    public Properties  getProperties(){
        return this.attr;
    }
}
