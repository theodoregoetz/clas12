/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.group;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jlab.evio.stream.EvioInputStream;
import org.jlab.evio.stream.EvioOutputStream;
import org.root.base.EvioWritableTree;
import org.root.data.DataSetXY;
import org.root.histogram.H1D;
import org.root.histogram.H2D;

/**
 *
 * @author gavalian
 */
public class PlotDirectory {
    
    private TreeMap<String,PlotGroup> directoryGroups = new TreeMap<String,PlotGroup>();
    private String directoryName = "undefined";
    
    public PlotDirectory(){
        
    }
    
    public PlotDirectory(String name){
        this.directoryName = name;
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
    
    public void list(){
        for(Map.Entry<String,PlotGroup> group : directoryGroups.entrySet()){
            String dirname = this.getName() + "/" + group.getValue().getName();
            TreeMap<String,Object>  groupObjects = group.getValue().getObjects();
            for(Map.Entry<String,Object> objects : groupObjects.entrySet()){
                System.out.println(" Object : [" + dirname + "]  [" 
                        +  objects.getKey() + "]");
            }
        }
    }
    
    public void write(String filename){
        EvioOutputStream outStream = new EvioOutputStream(filename);
        for(Map.Entry<String,PlotGroup> group : directoryGroups.entrySet()){
            String dirname = this.getName() + "/" + group.getValue().getName();
            TreeMap<String,Object>  groupObjects = group.getValue().getObjects();
            for(Map.Entry<String,Object> objects : groupObjects.entrySet()){
                Object dataObject = objects.getValue();
                if(dataObject instanceof EvioWritableTree){
                    TreeMap<Integer,Object> tree = ((EvioWritableTree) dataObject).toTreeMap();
                    String absolutePath = dirname + "/" + objects.getKey();
                    byte[] nameBytes = absolutePath.getBytes();
                    tree.put(6, nameBytes);
                    outStream.writeTree(tree);
                    System.out.println(" Saving : [" + dirname + "]  [" 
                            +  objects.getKey() + "]");
                }
            }
        }
        outStream.close();
    }
    
    public void load(String filename){
        this.directoryGroups.clear();
        EvioInputStream stream = new EvioInputStream();
        stream.open(filename);
        ArrayList< TreeMap<Integer,Object> > objects = stream.getObjectTree();
        stream.close();
        //System.out.println(objects.size());
        
        for(TreeMap<Integer,Object> treemap : objects){
            //System.err.println(treemap.size() + "  " + treemap.containsKey(1));            
            int[] type = (int[]) treemap.get(1);
            if(type[0]==1){
                H1D h = new H1D();
                h.fromTreeMap(treemap);
                String[] tokens = this.getPathComponents(h.name());
                //System.err.println(h.name() + "\n" + h.toString());
                //System.err.println(tokens[0] + " " + tokens[1] + " " + tokens[2]);
                this.setName(tokens[0]);
                if(this.directoryGroups.containsKey(tokens[1])==false){
                    this.directoryGroups.put(tokens[1], new PlotGroup(tokens[1]));
                }
                h.setName(tokens[2]);
                this.getGroup(tokens[1]).add(h.name(), h);
            }
            if(type[0]==2){
                H2D h = new H2D();
                h.fromTreeMap(treemap);
                String[] tokens = this.getPathComponents(h.getName());
                //System.err.println(h.name() + "\n" + h.toString());
                //System.err.println(tokens[0] + " " + tokens[1] + " " + tokens[2]);
                this.setName(tokens[0]);
                if(this.directoryGroups.containsKey(tokens[1])==false){
                    this.directoryGroups.put(tokens[1], new PlotGroup(tokens[1]));
                }
                h.setName(tokens[2]);
                this.getGroup(tokens[1]).add(h.getName(), h);
            }
            
            if(type[0]==6){
                DataSetXY h = new DataSetXY();
                h.fromTreeMap(treemap);
                String[] tokens = this.getPathComponents(h.getName());
                this.setName(tokens[0]);
                if(this.directoryGroups.containsKey(tokens[1])==false){
                    this.directoryGroups.put(tokens[1], new PlotGroup(tokens[1]));
                }
                h.setName(tokens[2]);
                this.getGroup(tokens[1]).add(h.getName(), h);
            }
            
        }
    }
    
    private String[] getPathComponents(String path){
        int first = path.indexOf("/", 0);
        int last  = path.lastIndexOf("/");
        String[] tokens = new String[3];
        tokens[0] = path.substring(0, first);
        tokens[1] = path.substring(first+1, last);
        tokens[2] = path.substring(last+1, path.length());
        return tokens;
    }
}
