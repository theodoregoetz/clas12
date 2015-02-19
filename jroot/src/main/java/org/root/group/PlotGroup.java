/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.group;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class PlotGroup {
    
    private String groupName = "undefined";
    private TreeMap<String,Object>  groupObjects = new  TreeMap<String,Object>();
    private TreeMap<Integer,PlotDescriptor> groupPlots = new TreeMap<Integer,PlotDescriptor>();
    private Integer  numColumns = 1;
    private Integer  numRows    = 1;
    
    public PlotGroup(String name, int col, int row){
        this.groupName  = name;
        this.numColumns = col;
        this.numRows    = row;
    }
    
    public PlotGroup(String name){
        this.groupName  = name;
        this.numColumns = 1;
        this.numRows    = 1;
    }
    
    public void add(String name,Object hobj){
        this.groupObjects.put(name,hobj);
    }
    
    public void addDescriptor(int pad, String... plots){
        this.groupPlots.put(pad, new PlotDescriptor(pad,plots));
    }
    
    public TreeMap<Integer,PlotDescriptor> getDescriptors(){
        return this.groupPlots;
    }
    
    public TreeMap<String,Object> getObjects(){
        return this.groupObjects;
    }
    
    public int getColumns() { return this.numColumns;}
    public int getRows()    { return this.numRows;   }
    public String getName() { return this.groupName; }
    
}
