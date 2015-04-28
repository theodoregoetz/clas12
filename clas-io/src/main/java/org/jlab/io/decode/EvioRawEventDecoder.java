/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.io.decode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jlab.clas12.raw.RawDataEntry;

/**
 *
 * @author gavalian
 */
public class EvioRawEventDecoder {
    
    private TreeMap<String,IDetectorTranslationTable> translationTables
            = new TreeMap<String,IDetectorTranslationTable>();
    public EvioRawEventDecoder(){
        
    }
    
    public void decode(List<RawDataEntry> dataEntries){
        for(RawDataEntry data : dataEntries){
            for(Map.Entry<String,IDetectorTranslationTable> tr : this.translationTables.entrySet()){
                if(tr.getValue().getSector(data.getCrate(), data.getSlot(), data.getChannel())>0){
                    int sector = tr.getValue().getSector(data.getCrate(), data.getSlot(), data.getChannel());
                    int layer = tr.getValue().getLayer(data.getCrate(), data.getSlot(), data.getChannel());
                    int component = tr.getValue().getComponent(data.getCrate(), data.getSlot(), data.getChannel());
                    data.setSectorLayerComponent(sector,layer,component);
                }
            }
        }
    }    
    
    public List<RawDataEntry>  getDecodedData(List<RawDataEntry> dataEntries, IDetectorTranslationTable table){
        ArrayList<RawDataEntry>  rawdata = new ArrayList<RawDataEntry>();
        for(RawDataEntry data : dataEntries){
            if(table.getSector(data.getCrate(), data.getSlot(), data.getChannel())>0){
                int sector = table.getSector(data.getCrate(), data.getSlot(), data.getChannel());
                int layer = table.getLayer(data.getCrate(), data.getSlot(), data.getChannel());
                int component = table.getComponent(data.getCrate(), data.getSlot(), data.getChannel());
                data.setSectorLayerComponent(sector,layer,component);
                rawdata.add(data);
            }
        }
        return rawdata;
    }
    
    public void addTranslationTable(IDetectorTranslationTable table){
        this.translationTables.put(table.getName(), table);
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<String,IDetectorTranslationTable> entry : this.translationTables.entrySet()){
            str.append(String.format("TRANSLATION : [%12s]", entry.getKey()));
        }
        return str.toString();
    }
        
}
