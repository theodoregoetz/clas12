/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.group;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jlab.evio.stream.EvioOutputStream;
import org.root.base.EvioWritableTree;

/**
 *
 * @author gavalian
 */
public class TDirectory {
    
    private final TreeMap<String,TDirectory>     directory = new TreeMap<String,TDirectory>();
    private final TreeMap<String,Object>           content = new TreeMap<String,Object>();
    private String                        currentDirectory = "/";
    private String                        directoryName    = "/";
    
    public TDirectory(){
        
    }
    
    public TDirectory(String name){
        this.directoryName = name;
    }
    
    public String getName(){
        return this.directoryName;
    }
    
    public void addDirectory(TDirectory dir){
        this.directory.put(dir.getName(), dir);
    }
    
    
    
    public void cd(String dir){
        if(directory.containsKey(dir)==true){
            this.currentDirectory = dir;
        } else {
            this.directory.put(dir, new TDirectory(dir));
            this.currentDirectory = dir;
        }
    }
    
    public void scan(){
        
        Set<String> dirs = this.getDirectoryLevelSet(0,"/");
        int count = dirs.size();
        int loop  = 0;
        
        while(count>0){
            count = 0;
            //for(int loop)
            System.out.println("LEVEL = " + loop + "  COUNT = " + count);
            System.out.println(dirs);
            loop++;
            dirs  = this.getDirectoryLevelSet(loop,"/");
            count = dirs.size();
        }
    }
    
    private Set<String>  getDirectoryLevelSet(int level, String start){
        Set<String>  entrySet = new HashSet<String>();
        for(Map.Entry<String,TDirectory> dirs : this.directory.entrySet()){
            String path = dirs.getKey();
            if(path.startsWith(start)==true||(start.compareTo("/")==0)){
                String reducedPath = path.replace(start, "");
                String[] tokens = path.split("/");
                if(tokens.length>level){
                    entrySet.add(tokens[level]);
                }
            }
        }
        return entrySet;
    }
    
    public void add(String name, EvioWritableTree obj){
        this.directory.get(this.currentDirectory).addObject(name, obj);
    }
    
    public void addObject(String name, EvioWritableTree obj){
        this.content.put(name, obj);
        //this.directory.get(this.currentDirectory).content.put(name, obj);
    }
    
    public void mkdir(String dirname){
        this.directory.put(dirname, new TDirectory(dirname));
    }
    
    public TDirectory  getDirectory(String name){
        if(this.directory.containsKey(name)==true){
            return this.directory.get(name);
        }
        return null;
    }
    
    public void ls(){
        if(currentDirectory.compareTo("/")==0){
            System.out.println(this.toString());
        } else {
            directory.get(this.currentDirectory).toString();
        }
    }
    
    public DefaultMutableTreeNode  getLeafNode(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.directoryName);
        for(Map.Entry<String,Object> items : this.content.entrySet()){
            root.add(new DefaultMutableTreeNode(items.getKey()));
        }
        return root;
    }
    
    public DefaultMutableTreeNode getTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.directoryName);
        for(Map.Entry<String,TDirectory> entry : this.directory.entrySet()){
            //    root.add(new DefaultMutableTreeNode(entry.getKey()));
            //DefaultMutableTreeNode groupRoot = new DefaultMutableTreeNode(entry.getKey());
            System.out.println("Get Leaf from : " + entry.getKey() + "  :  "
            + entry.getValue().getName());
            root.add(entry.getValue().getLeafNode());
            //root.add(groupRoot);
        }
        return root;
    }
    
    public Map<String,Object>  getObjects(){
        return this.content;
    }
    
    public Object  getObject(String objname){
        return this.content.get(objname);
    }
    
    public boolean hasObject(String objname){
        if(this.content.containsKey(objname)==true) return true;
        return false;
    }
    
    public boolean hasDirectory(String dir){
        if(this.directory.containsKey(dir)==true){
            return true;
        }
        return false;
    }
    
    public void write(String filename){
        EvioOutputStream outStream = new EvioOutputStream(filename);
        System.out.println("[Directory] -----> open file : " + filename);
        for(Map.Entry<String,TDirectory> group : this.directory.entrySet()){

            String dirname = group.getKey();//this.getName() + "/" + group.getValue().getName();
            Map<String,Object>  groupObjects = group.getValue().getObjects();
            int counter = 0;
            for(Map.Entry<String,Object> objects : groupObjects.entrySet()){
                Object dataObject = objects.getValue();
                if(dataObject instanceof EvioWritableTree){
                    TreeMap<Integer,Object> tree = ((EvioWritableTree) dataObject).toTreeMap();
                    String absolutePath = dirname + "/" + objects.getKey();
                    byte[] nameBytes = absolutePath.getBytes();
                    tree.put(6, nameBytes);
                    outStream.writeTree(tree);
                    counter++;
                    //System.out.println(" Saving : [" + dirname + "]  [" 
                    //        +  objects.getKey() + "]");
                }
            }
            System.out.println("[Directory] -----> process group : " + group.getKey() + 
                    "  Objects saved = " + counter);
        }
        outStream.close();
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("DIRECTORY : %s\n", this.getName()));
        
        for(Map.Entry<String,TDirectory> dirs : this.directory.entrySet()){
            str.append(String.format("\t%s\n", dirs.getKey()));
        }
        
        for(Map.Entry<String,Object> dirs : this.content.entrySet()){
            str.append(String.format("\t ---> %s\n", dirs.getKey()));
        }
        
        return str.toString();
    }
}
