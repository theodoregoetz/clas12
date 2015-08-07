/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.detector;

import java.util.ArrayList;
import java.util.TreeMap;
import org.jlab.clas.tools.utils.DataUtils;

/**
 *
 * @author gavalian
 */
public class DetectorRawBank {
    
    private ArrayList<DetectorRawData> detectorBank = new  ArrayList<DetectorRawData>();
    private DetectorType  detectorType = DetectorType.UNDEFINED;
    
    public DetectorRawBank(DetectorType type){
        this.detectorType = type;
    }
    
    public DetectorRawBank(DetectorType type, ArrayList<DetectorRawData> dataEntries){
        this.detectorType = type;
        this.addData(dataEntries);
    }
    
    public void add(DetectorRawData dataEntry){
        if(dataEntry.getDescriptor().getType()==this.detectorType){
            this.detectorBank.add(dataEntry);
        }
    }
    
    public final void addData(ArrayList<DetectorRawData> dataEntries){
        for(DetectorRawData data : dataEntries){
            if(data.getDescriptor().getType()==this.detectorType){
                detectorBank.add(data);
            }
        }
    }
    
    public int getTag(){
        return this.detectorType.getDetectorId();
    }
    
    public TreeMap<Integer,Object>  getTreeMap(){
        TreeMap<Integer,Object> treeMap = new TreeMap<Integer,Object>();
        int nrows = this.detectorBank.size();
        byte[] sector = new byte[nrows];
        byte[] layer  = new byte[nrows];
        byte[] comp   = new byte[nrows];
        for(int loop = 0; loop < nrows; loop++){
            sector[loop] = DataUtils.getByteFromInt(
                    this.detectorBank.get(loop).getDescriptor().getSector());
            layer[loop]  = DataUtils.getByteFromInt(
                    this.detectorBank.get(loop).getDescriptor().getLayer());
            comp[loop]  = DataUtils.getByteFromInt(
                    this.detectorBank.get(loop).getDescriptor().getComponent());
        }
        
        treeMap.put(0, sector);
        treeMap.put(1, layer);
        treeMap.put(2, comp);
        
        return treeMap;
    }
    
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("[DETECTOR BANK] TYPE = %s  [ ENTRIES = %d ]\n",
                this.detectorType.getName(),this.detectorBank.size()));
        for(DetectorRawData data : this.detectorBank){
            str.append(data.toString());
            str.append("\n");
        }
        return str.toString();
    }
}
