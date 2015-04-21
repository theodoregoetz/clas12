/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.group;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.jlab.evio.stream.EvioInputStream;
import org.jlab.evio.stream.EvioOutputStream;
import org.root.base.EvioWritableTree;
import org.root.data.DataSetXY;
import org.root.func.F1D;
import org.root.histogram.GraphErrors;
import org.root.histogram.H1D;
import org.root.histogram.H2D;
import org.root.histogram.PaveText;
import org.root.pad.RootCanvas;

/**
 *
 * @author gavalian
 */
public class TDirectory implements ITreeViewer {
    
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
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        for(Map.Entry<String,TDirectory> items : this.directory.entrySet()){
            this.makeTreeDirectory(root, items.getKey());
        }
        /*
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
        }*/
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
    
    public void add( EvioWritableTree obj){
        this.addObject(obj.getName(), obj);
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
    
    private DefaultMutableTreeNode  getTreeNode(DefaultMutableTreeNode rootNode, String[] dirpath, int length){
        //System.out.println("--------------------->");
        //System.out.print(" LOOKING FOR : ");
        /*
        for(int loop = 0 ; loop < length; loop ++ ) System.out.print( dirpath[loop] + " / ");
            System.out.println();
          */ 
        Enumeration<DefaultMutableTreeNode> en = rootNode.preorderEnumeration();
        while (en.hasMoreElements())
        {
            DefaultMutableTreeNode node = en.nextElement();
            TreeNode[] path = node.getPath();
            
            //System.out.print(" ANALZING : ");
            //for(int loop = 0 ; loop < path.length; loop ++ ) System.out.print( path[loop].toString() + " / ");
            //System.out.println();
            /*System.out.print(" MATCHING : ");
            for(int loop = 0 ; loop < length; loop ++ ) System.out.print( dirpath[loop] + " / ");
            System.out.println();*/
            if((path.length-1) == length){
                boolean hasDir  = true;
                for(int loop = 0; loop < length; loop++){
                    if(path[loop+1].toString().compareTo(dirpath[loop])!=0)
                        hasDir = false;
                }
                if(hasDir == true) return node;
            }
        }
        return null;
    }
    
    private void makeTreeDirectory(DefaultMutableTreeNode rootNode, String dir){
        //System.out.println("--------------------->");

        String[] tokens = dir.split("/");
        //System.out.println("ANALYZING Directory : " + dir + "  size = " + tokens.length);
        //ArrayList<DefaultMutableTreeNode>  nodesArray = new ArrayList<DefaultMutableTreeNode>();
        DefaultMutableTreeNode node = this.getTreeNode(rootNode, tokens, 1);
        if(node==null){
            rootNode.add(new DefaultMutableTreeNode(tokens[0]));
        }
        
        for(int loop = 1; loop < tokens.length; loop++){
            
            //System.out.print("---->  LOOK FOR : " + loop );
            /*
            for(int i = 0 ; i <= loop ; i++) System.out.print(" " + tokens[i]);
            System.out.println();
            */
            DefaultMutableTreeNode dirnode = this.getTreeNode(rootNode, tokens, loop+1);
            //System.out.println("result ----> " + dirnode);
            if(dirnode==null){
                DefaultMutableTreeNode parent = this.getTreeNode(rootNode, tokens,loop);
                //System.out.println(" =====> Creating " + tokens[loop] + 
                 //       "  in parent " + parent.toString());
                parent.add(new DefaultMutableTreeNode(tokens[loop]));
            }  
        }
        /*
        for(int loop = nodesArray.size()-1; loop > 0; loop--){
            nodesArray.get(loop-1).add(nodesArray.get(loop));
        }
        
        rootNode.add(nodesArray.get(0));*/
        /*
        Enumeration<DefaultMutableTreeNode> en = rootNode.preorderEnumeration();
        while (en.hasMoreElements())
        {
            DefaultMutableTreeNode node = en.nextElement();
            TreeNode[] path = node.getPath();
            boolean hasDir  = false;
            int     nlookup = dirpath.length - 1;
            System.out.println((node.isLeaf() ? "  - " : "+ ") + path[path.length - 1]);
        }*/
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
    
    private void addObjectsToTree(DefaultMutableTreeNode tree){
        for(Map.Entry<String,TDirectory> dirs : this.directory.entrySet()){
            String[] path = dirs.getKey().split("/");
            DefaultMutableTreeNode node = this.getTreeNode(tree, path, path.length);
            if(node!=null){
                for(Map.Entry<String,Object> items : dirs.getValue().getObjects().entrySet()){
                    node.add(new DefaultMutableTreeNode(items.getKey()));
                }
            } else {
                System.err.println("===> ERROR");
            }
        }
    }
    
    public DefaultMutableTreeNode getTree() {
        /*
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.directoryName);
        for(Map.Entry<String,TDirectory> entry : this.directory.entrySet()){
            //    root.add(new DefaultMutableTreeNode(entry.getKey()));
            //DefaultMutableTreeNode groupRoot = new DefaultMutableTreeNode(entry.getKey());
            System.out.println("Get Leaf from : " + entry.getKey() + "  :  "
            + entry.getValue().getName());
            root.add(entry.getValue().getLeafNode());
            //root.add(groupRoot);
        }*/
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        for(Map.Entry<String,TDirectory> items : this.directory.entrySet()){
            this.makeTreeDirectory(root, items.getKey());
        }
        this.addObjectsToTree(root);
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
                    tree.put(2, nameBytes);
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
    
    private String[] getPathComponents(String path){
        int first = path.indexOf("/", 0);
        int last  = path.lastIndexOf("/");
        String[] tokens = new String[3];
        tokens[0] = path.substring(0, first);
        //tokens[1] = path.substring(first+1, last);
        tokens[1] = path.substring(0, last);
        tokens[2] = path.substring(last+1, path.length());
        return tokens;
    }
    
    public void readFile(String filename){
        this.directory.clear();
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
                //System.out.println("****** LOADING OBJECT");
                //System.err.println(h.name());
                //System.err.println(tokens[0] + " " + tokens[1] + " " + tokens[2]);
                //this.setName(tokens[0]);
                if(this.directory.containsKey(tokens[1])==false){
                    this.directory.put(tokens[1], new TDirectory(tokens[1]));
                }
                
                h.setName(tokens[2]);
                this.getDirectory(tokens[1]).add(h);
            }
            
            if(type[0]==2){
                H2D h = new H2D();
                h.fromTreeMap(treemap);
                String[] tokens = this.getPathComponents(h.getName());
                //System.err.println(h.name() + "\n" + h.toString());
                //System.err.println(tokens[0] + " " + tokens[1] + " " + tokens[2]);
                //this.setName(tokens[0]);
                if(this.directory.containsKey(tokens[1])==false){
                    this.directory.put(tokens[1], new TDirectory(tokens[1]));
                }
                h.setName(tokens[2]);
                this.getDirectory(tokens[1]).add(h);
            }
            
            if(type[0]==6){
                DataSetXY h = new DataSetXY();
                h.fromTreeMap(treemap);
                String[] tokens = this.getPathComponents(h.getName());
                
                if(this.directory.containsKey(tokens[1])==false){
                    this.directory.put(tokens[1], new TDirectory(tokens[1]));
                }
                h.setName(tokens[2]);
                this.getDirectory(tokens[1]).add(h);
            }
            
            if(type[0]==7){
                F1D h = new F1D("gaus",0.0,1.0);
                h.fromTreeMap(treemap);
                
                String[] tokens = this.getPathComponents(h.getName());
                if(this.directory.containsKey(tokens[1])==false){
                    this.directory.put(tokens[1], new TDirectory(tokens[1]));
                }
                h.setName(tokens[2]);
                this.getDirectory(tokens[1]).add(h);
            }
            if(type[0]==14){
                PaveText h = new PaveText(0.0,0.0);
                h.fromTreeMap(treemap);
                String[] tokens = this.getPathComponents(h.getName());
                
                if(this.directory.containsKey(tokens[1])==false){
                    this.directory.put(tokens[1], new TDirectory(tokens[1]));
                }
                h.setName(tokens[2]);
                this.getDirectory(tokens[1]).add(h);
            }
        }
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("DIRECTORY : %s\n", this.getName()));
        
        for(Map.Entry<String,TDirectory> dirs : this.directory.entrySet()){
            str.append(String.format("\t%s\n", dirs.getKey()));
            for(Map.Entry<String,Object> objects : dirs.getValue().getObjects().entrySet()){
                EvioWritableTree evT = (EvioWritableTree) objects.getValue();
                str.append(String.format("\t\t-> Object : (Name) %-24s : (Type) %s\n", objects.getKey(),
                        objects.getValue().getClass().getName()));
            }
        }
        /*
        for(Map.Entry<String,Object> dirs : this.content.entrySet()){
            str.append(String.format("\t ---> %s\n", dirs.getKey()));
        }*/
        
        return str.toString();
    }

    public void draw(String obj, String selection, String options, RootCanvas canvas) {
        //System.out.println(" Asking to draw following object " + obj);
        int index = obj.lastIndexOf("/");
        if(index>0&&index<obj.length()){
            String  directoryL = obj.substring(0, index);
            String  object    = obj.substring(index+1, obj.length());
            //System.out.println(" DRAW " + directory + "  " + object);
            if(this.hasDirectory(directoryL)==true){
                if(this.getDirectory(directoryL).hasObject(object)==true){
                    //System.out.println("==========> Drawing ");
                    Object dirObject = this.getDirectory(directoryL).getObject(object);
                    if(dirObject instanceof H1D){
                        H1D h1 = (H1D) dirObject;
                        canvas.draw(canvas.getCurrentPad(), h1, options);
                        canvas.incrementPad();
                    }
                    if(dirObject instanceof H2D){
                        H2D h2 = (H2D) dirObject;
                        canvas.draw(canvas.getCurrentPad(), h2, options);
                        canvas.incrementPad();
                    }
                    if(dirObject instanceof GraphErrors){
                        GraphErrors gr = (GraphErrors) dirObject;
                        canvas.draw(canvas.getCurrentPad(), gr);
                        canvas.incrementPad();
                    }
                    if(dirObject instanceof DataSetXY){
                        DataSetXY gr = (DataSetXY) dirObject;
                        canvas.draw(canvas.getCurrentPad(), gr);
                        canvas.incrementPad();
                    }
                    if(dirObject instanceof F1D){
                        F1D func = (F1D) dirObject;
                        canvas.draw(canvas.getCurrentPad(), func);
                        canvas.incrementPad();
                    }
                }
            }
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> getVariables() {
        return new ArrayList<String>();
    }
}
