/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.group;

import java.util.Map;
import java.util.TreeMap;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author gavalian
 */
public class PlotDirectory {
    
    private TreeMap<String,PlotGroup> directoryGroups = new TreeMap<String,PlotGroup>();
    private String directoryName = "undefined";
    
    public PlotDirectory(){
        
    }
    
    public void setName(String name){ this.directoryName = name;}
    public String getName() { return this.directoryName;}
    
    public void addGroup(PlotGroup group){
        this.directoryGroups.put(group.getName(), group);
    }
    
    public void addGroup(String name, int xs, int ys){
        this.directoryGroups.put(name, new PlotGroup(name,xs,ys));
    }
    
    public PlotGroup getGroup(String name){
        return this.directoryGroups.get(name);
    }
    
    public DefaultMutableTreeNode getTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.directoryName);
        for(Map.Entry<String,PlotGroup> entry : this.directoryGroups.entrySet()){
            //    root.add(new DefaultMutableTreeNode(entry.getKey()));
            //DefaultMutableTreeNode groupRoot = new DefaultMutableTreeNode(entry.getKey());
            root.add(new DefaultMutableTreeNode(entry.getValue().getName()));
            //root.add(groupRoot);
        }
        return root;
    }     
}
