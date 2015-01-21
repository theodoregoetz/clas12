/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.histogram;

import java.util.Map;
import java.util.TreeMap;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jlab.data.base.EvioWritableTree;
import org.jlab.data.base.TreeBrowser;
import org.jlab.data.graph.DataSet;
import org.jlab.data.graph.DataSetXY;
import org.jlab.evio.stream.EvioInputStream;
import org.jlab.evio.stream.EvioOutputStream;

/**
 *
 * @author gavalian
 */
public class HGroup implements TreeBrowser {
    
    private final TreeMap<String,EvioWritableTree>  groupHistograms = new 
                TreeMap<String,EvioWritableTree>();
    
    private String groupName = "generic";
    
    public HGroup(){
        
    }
    
    public HGroup(String name){
        groupName = name;
    }
    
    /*
    public void add(H1D... hists){
        for(int loop = 0; loop < hists.length; loop++){
            this.add(hists[loop]);
        }
    }*/
    /*
    public void add(H1D hist){
        if(groupHistograms.containsKey(hist.name())){
            System.err.println("[HGroup : " + groupName + "]---> ERROR " +
            " the histogram with name " + hist.name() + " already exists..");
            return;
        }
        groupHistograms.put(hist.name(), hist);
    }*/
    public void add(DataSetXY dataset){
        if(groupHistograms.containsKey(dataset.getName())==true){
            System.err.println("[HGroup : " + groupName + "]---> ERROR " +
                        " a DataSet with name " + dataset.getName() + " already exists..");
        } else {
            groupHistograms.put(dataset.getName(), dataset);
        }
    }
    
    public String getName(){
        return this.groupName;
    }
    
    public EvioWritableTree get(String name){
        return groupHistograms.get(name);
    }
    
    public void add(H1D... collection){
        for(H1D hist : collection){
            //this.add(hist);
            if(groupHistograms.containsKey(hist.name())){
                System.err.println("[HGroup : " + groupName + "]---> ERROR " +
                        " the histogram with name " + hist.name() + " already exists..");
            } else {
                groupHistograms.put(hist.name(), hist);
            }
        }
    }
    
    public void add(H3D hist){
        if(groupHistograms.containsKey(hist.getName())){
            System.err.println("[HGroup : " + groupName + "]---> ERROR " +
                    " the histogram with name " + hist.getName() + " already exists..");
            return;
        }
        groupHistograms.put(hist.getName(), hist);
    }
    
    public void add(H2D hist){
        if(groupHistograms.containsKey(hist.getName())){
            System.err.println("[HGroup : " + groupName + "]---> ERROR " +
                    " the histogram with name " + hist.getName() + " already exists..");
            return;
        }
        groupHistograms.put(hist.getName(), hist);
    } 
    
    
    public void add(String name, int nbins, double min, double max){
        this.add(new H1D(name,nbins,min,max));
    }
    
    public void add(String name, int nbinsx, double minx, double maxx
            , int nbinsy, double miny, double maxy){
        this.add(new H2D(name,nbinsx,minx,maxx,nbinsy,miny,maxy));
    }
    
    public H1D getH1D(String name){
        if(groupHistograms.containsKey(name)==false){
            System.err.println("[HGroup : " + groupName + "]---> ERROR " +
            " group does not contain histogram with name " + name);
            return null;
        }
        if(groupHistograms.get(name) instanceof H1D){
            return (H1D) groupHistograms.get(name);
        }
        System.err.println("[HGroup : " + groupName + "]---> ERROR " +
                 " histogram " + name + " is not instance of H1D");
        return null;
    }
    
    public DataSetXY getDataSet(String name){
        if(groupHistograms.containsKey(name)==true){
            if(groupHistograms.get(name) instanceof DataSetXY){
                return (DataSetXY) groupHistograms.get(name);
            }
        }
        return null;
    }
    
    public H2D getH2D(String name){
        if(groupHistograms.containsKey(name)==false){
            System.err.println("[HGroup : " + groupName + "]---> ERROR " +
            " group does not contain histogram with name " + name);
            return null;
        }
        if(groupHistograms.get(name) instanceof H2D){
            return (H2D) groupHistograms.get(name);
        }
        System.err.println("[HGroup : " + groupName + "]---> ERROR " +
                 " histogram " + name + " is not instance of H2D");
        return null;
    }
    
    public H3D getH3D(String name){
        if(groupHistograms.containsKey(name)==false){
            System.err.println("[HGroup : " + groupName + "]---> ERROR " +
            " group does not contain histogram with name " + name);
            return null;
        }
        if(groupHistograms.get(name) instanceof H3D){
            return (H3D) groupHistograms.get(name);
        }
        System.err.println("[HGroup : " + groupName + "]---> ERROR " +
                 " histogram " + name + " is not instance of H3D");
        return null;
    }
    
    public void list(){
        System.err.println(this.getList());
    }
    
    public String getList(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<String,EvioWritableTree> entry : groupHistograms.entrySet()){
            if(entry.getValue() instanceof H1D){
                H1D h1d = (H1D) entry.getValue();
                str.append(String.format("%-32s : ( MEAN = %12.5f , RMS = %12.5f ) Axis (%5d,%12.5f,%12.5f)\n", 
                        entry.getKey(),h1d.getMean(),h1d.getRMS(),
                        h1d.getxAxis().getNBins(),h1d.getxAxis().min(),
                        h1d.getxAxis().max()));
            }
            if(entry.getValue() instanceof DataSetXY){
                DataSetXY data = (DataSetXY) entry.getValue();
                str.append(String.format("%-32s : ( %s , SIZE = %d )\n",data.getName(),
                        "DataSetXY",data.getDataX().getSize()));
            }
        }
        return str.toString();
    }
    
    public void save(String filename){
        try {
            EvioOutputStream outStream = new EvioOutputStream(filename);
            for(Map.Entry<String,EvioWritableTree> entry : groupHistograms.entrySet()){
                outStream.writeTree(entry.getValue().toTreeMap());
            }
            outStream.close();
        } catch (Exception e){
            System.err.println("[HGroup]---> Error writing output file....");
        }
    }

    @Override
    public void open(String filename) {
        EvioInputStream  stream = new EvioInputStream();
        stream.open(filename);
        int nevents = stream.getEntries();
        for(int loop = 0; loop < nevents; loop++){
            stream.readEvent(loop+1);
            int[] type = stream.getInt(200, 1);
            //System.err.println("LOOP = " + loop + " TYPE = " + type[0]);
            if(type[0]==1){
                TreeMap<Integer,Object>  objMap = new TreeMap<Integer,Object>();
                objMap.put(1, type);
                objMap.put(2, stream.getInt(200, 2));
                objMap.put(3, stream.getDouble(200, 3));
                objMap.put(4, stream.getDouble(200, 4));
                objMap.put(5, stream.getDouble(200, 5));
                objMap.put(6, stream.getByte(200, 6));
                H1D h = new H1D();
                h.fromTreeMap(objMap);
                this.add(h);                
            }
            
            if(type[0]==6){
                TreeMap<Integer,Object>  objMap = new TreeMap<Integer,Object>();
                objMap.put(1, type);
                //objMap.put(2, stream.getInt(200, 2));
                //objMap.put(3, stream.getDouble(200, 3));
                objMap.put(4, stream.getDouble(200, 4));
                objMap.put(5, stream.getDouble(200, 5));
                objMap.put(6, stream.getByte(200, 6));
                DataSetXY data = new DataSetXY();
                data.fromTreeMap(objMap);
                this.add(data);
            }
        }
    }

    @Override
    public DefaultMutableTreeNode getTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.groupName);
        for(Map.Entry<String,EvioWritableTree> entry : groupHistograms.entrySet()){
            root.add(new DefaultMutableTreeNode(entry.getKey()));
        }
        return root;
    }

    @Override
    public DataSet getData(String selection) {
        DataSet  data = new DataSet();
        try {
            H1D hist = this.getH1D(selection);
            data.set(hist.getxAxis().getBinCenters(),hist.getData());
        } catch (Exception e){
            System.err.println("[H1D::getData()] ---> error getting data for " +
                    selection);
        }
        return data;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataSet getData(String selection, String condition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDataType(String selection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
