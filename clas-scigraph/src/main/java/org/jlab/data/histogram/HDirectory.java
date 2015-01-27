/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.data.histogram;

import java.util.Map;
import java.util.TreeMap;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jlab.data.base.TreeBrowser;
import org.jlab.data.graph.DataSet;

/**
 *
 * @author gavalian
 */
public class HDirectory implements TreeBrowser {
    String directoryName = "FTOF";
    private final TreeMap<String,HGroup> directoryGroups = new TreeMap<String,HGroup>();
    
    public HDirectory(){
        this.directoryName = "Undefined";
    }
    
    public void setName(String name){
        this.directoryName = name;
    }
    
    public HDirectory(String name){
        this.directoryName = name;
    }
    
    public void open(String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DefaultMutableTreeNode getTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.directoryName);
        for(Map.Entry<String,HGroup> entry : this.directoryGroups.entrySet()){
            //    root.add(new DefaultMutableTreeNode(entry.getKey()));
            //DefaultMutableTreeNode groupRoot = new DefaultMutableTreeNode(entry.getKey());
            root.add(entry.getValue().getTree());
            //root.add(groupRoot);
        }
        return root;
    }

    public void addGroup(String groupname){
        directoryGroups.put(groupname, new HGroup(groupname));
    }
    
    public void addGroup(HGroup group){
        directoryGroups.put(group.getName(), group);
    }
    
    public HGroup group(String groupname){
        if(this.directoryGroups.containsKey(groupname)==false) return null;
        return this.directoryGroups.get(groupname);
    }
    
    public TreeMap<String,HGroup> getGroups(){
        return this.directoryGroups;
    }
            
    public DataSet getData(String selection) {
        System.err.println("---> Getting data for : " + selection );
        return null;
    }

    public DataSet getData(String selection, String condition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getDataType(String selection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void show(){
        for(Map.Entry<String,HGroup> entry : this.directoryGroups.entrySet()){
            System.err.println(String.format("%-12s : %d", entry.getKey(), 1));
        }
    }
}
