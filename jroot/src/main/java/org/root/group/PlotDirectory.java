/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.group;

import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class PlotDirectory {
    private TreeMap<String,PlotGroup> directoryGroups = new TreeMap<String,PlotGroup>();
    public PlotDirectory(){
        
    }
    
    public void addGroup(String name, int xs, int ys){
        this.directoryGroups.put(name, new PlotGroup(name,xs,ys));
    }
    
    public PlotGroup getGroup(String name){
        return this.directoryGroups.get(name);
    }
}
