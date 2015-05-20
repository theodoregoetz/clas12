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
import org.jlab.data.detector.DetectorDataBank;
import org.jlab.data.detector.DetectorType;

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
    
    public DetectorDataBank  getDetectorBank(String name, List<RawDataEntry> raw){
        
        DetectorType  type = DetectorType.getType(name);
        DetectorDataBank  bank = new DetectorDataBank(type,raw.size());
        
        for(int loop = 0; loop < raw.size(); loop++){
            bank.setSectorLayerComponent(
                    raw.get(loop).getSector(), 
                    raw.get(loop).getLayer(),
                    raw.get(loop).getComponent()
                    ,loop);
            if(type==DetectorType.BST){
                //System.out.println(" FILLING BST = " + raw.get(loop).getADC() +  raw.get(loop).getTDC());
                bank.setADC((short) (raw.get(loop).getADC()&0xFFFF), loop);
                bank.setTDC((short) (raw.get(loop).getTDC()&0xFFFF), loop);
            }
            
            if(type==DetectorType.EC){
                bank.setADC((short) (raw.get(loop).getADC()&0xFFFF), loop);
                bank.setTDC((short) (raw.get(loop).getTDC()&0xFFFF), loop);
            }
            
            if(type==DetectorType.DC){
                bank.setTDC((short) (raw.get(loop).getTDC()&0xFFFF), loop);
            }
            
            if(type==DetectorType.FTOF){
                bank.setADCL((short) (raw.get(loop).getADC()&0xFFFF), loop);
                bank.setADCR((short) (raw.get(loop).getADC()&0xFFFF), loop);
            }
        }
        return bank;
    }
    
    
    
    public DetectorDataBank getDecodedDetectorBank(List<RawDataEntry> dataEntries, IDetectorTranslationTable table){
        DetectorType type = DetectorType.getType(table.getName());
        if(type==DetectorType.UNDEFINED){
            System.err.println("[EvioRawEventDecoder] ERROR: detector with name [" + table.getName()
            +"]  does not exist");
            return null;
        }
        List<RawDataEntry>  entries = this.getDecodedData(dataEntries, table);
        return this.getDetectorBank(table.getName(), entries);
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
    
    public List<DetectorDataBank>  getDecodedDetectorBanks(ArrayList<RawDataEntry> data){
        ArrayList<DetectorDataBank>  banks = new ArrayList<DetectorDataBank>();
        for(Map.Entry<String,IDetectorTranslationTable> tbl : this.translationTables.entrySet()){
            List<RawDataEntry>  entry = this.getDecodedData(data, tbl.getValue());
            DetectorDataBank bank = this.getDecodedDetectorBank(entry, tbl.getValue());
            if(bank.getRows()>0){
                banks.add(bank);
            }
        }
        return banks;
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
