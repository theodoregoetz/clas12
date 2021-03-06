/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.io.decode;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.clas12.raw.EvioRawDataBank;
import org.jlab.clas12.raw.EvioRawDataSource;
import org.jlab.clas12.raw.EvioTreeBranch;
import org.jlab.clas12.raw.RawData;
import org.jlab.clas12.raw.RawDataEntry;
import org.jlab.clas12.raw.RawDataTDC;
import org.jlab.coda.jevio.ByteDataTransformer;
import org.jlab.coda.jevio.CompositeData;
import org.jlab.coda.jevio.DataType;
import org.jlab.coda.jevio.EvioException;
import org.jlab.coda.jevio.EvioNode;
import org.jlab.data.detector.DetectorDataBank;
import org.jlab.data.detector.DetectorType;
import org.jlab.evio.clas12.EvioDataEvent;

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
    
    public EvioTreeBranch  getEventBranch(ArrayList<EvioTreeBranch> branches, int tag){
        for(EvioTreeBranch branch : branches){
            if(branch.getTag()==tag) return branch;
        }
        return null;
    }
    
    public List<RawDataTDC>  getTDCEntries(EvioDataEvent event){
        ArrayList<RawDataTDC> rawTDCs = new ArrayList<RawDataTDC>();
        ArrayList<EvioTreeBranch> branches = this.getEventBranches(event);
        for(EvioTreeBranch branch : branches){
            int  crate = branch.getTag();
            EvioTreeBranch cbranch = this.getEventBranch(branches, branch.getTag());
            for(EvioNode node : cbranch.getNodes()){ 
                if(node.getTag()==57607){
                    int[] intData = ByteDataTransformer.toIntArray(node.getStructureBuffer(false));
                    for(int loop = 0; loop < intData.length; loop++){
                        RawDataTDC  tdc = new RawDataTDC(crate);
                        tdc.setData(intData[loop]);
                        rawTDCs.add(tdc);
                    }
                }
            }
        }
        return rawTDCs;
    }
    
    public ArrayList<RawDataEntry> getDataEntries(EvioDataEvent event){
        ArrayList<RawDataEntry>  rawEntries = new ArrayList<RawDataEntry>();
        List<EvioTreeBranch> branches = this.getEventBranches(event);
        for(EvioTreeBranch branch : branches){
            ArrayList<RawDataEntry>  list = this.getDataEntries(event,branch.getTag());
            if(list != null){
                rawEntries.addAll(list);
            }
        }
        return rawEntries;
    }
    
    public ArrayList<RawDataEntry> getDataEntries(EvioDataEvent event, Integer crate){
        
        ArrayList<EvioTreeBranch> branches = this.getEventBranches(event);
        
        EvioTreeBranch cbranch = this.getEventBranch(branches, crate);
        if(cbranch == null ) return null;
        
        for(EvioNode node : cbranch.getNodes()){ 
            if(node.getTag()==57617){
                // This MODE is used for SVT
                return this.getDataEntries_57617(crate, node, event);
                //return this.getDataEntriesSVT(crate,node, event);                
            }
            
            if(node.getTag()==57602){
                //  This is regular integrated pulse mode, used for FTOF
                // FTCAL and EC/PCAL
                //return this.getDataEntries_57602(crate, node, event);
                return this.getDataEntries_57602(crate, node, event);
                //return this.getDataEntriesMode_7(crate,node, event);
            }
            if(node.getTag()==57601){
                //  This is regular integrated pulse mode, used for FTOF
                // FTCAL and EC/PCAL
                return this.getDataEntries_57601(crate, node, event);
                //return this.getDataEntriesMode_7(crate,node, event);
            }
            if(node.getTag()==57622){
                // This is MODE=10 is used for DC, it contains only TDC
                return this.getDataEntries_57622(crate, node, event);
            }
        }
        //EvioRawDataBank dataBank = new EvioRawDataBank();
        return null;
    }
    
    public int[]  getRawBuffer(EvioDataEvent event, int crate){
        ArrayList<EvioTreeBranch> branches = this.getEventBranches(event);
        
        EvioTreeBranch cbranch = this.getEventBranch(branches, crate);
        if(cbranch == null ) return new int[1];
        
        for(EvioNode node : cbranch.getNodes()){ 
            if(node.getTag()==57604){
                // This MODE is used for SVT
                System.out.println("FOUND A NODE WITH TAG 57604 TYPE === " + node.getDataTypeObj());
                if(node.getDataTypeObj() == DataType.INT32 || node.getDataTypeObj()==DataType.UINT32){
                    ByteBuffer buff  =  node.getByteData(true);
                    int[] nodedata = ByteDataTransformer.toIntArray(buff);
                    if(nodedata!=null){
                        return nodedata;
                    }
                }
                //return this.getDataEntriesSVT(crate,node, event);                
            }
            
        }
        //EvioRawDataBank dataBank = new EvioRawDataBank();
        return new int[0];        
    }
    
    public ArrayList<RawDataEntry>  getDataEntries_57601_DEBUG(Integer crate, EvioNode node, EvioDataEvent event){
        ArrayList<RawDataEntry>  entries = new ArrayList<RawDataEntry>();
        if(node.getTag()==57601){
            try {
                ByteBuffer     compBuffer = node.getByteData(true);
                CompositeData  compData = new CompositeData(compBuffer.array(),event.getByteOrder());
                
                List<DataType> cdatatypes = compData.getTypes();
                List<Object>   cdataitems = compData.getItems();

                if(cdatatypes.get(3) != DataType.NVALUE){
                    System.err.println("[EvioRawDataSource] ** error ** corrupted "
                    + " bank. tag = " + node.getTag() + " num = " + node.getNum());
                    return null;
                }
                System.out.println("DEBUG :  SIZE = " + cdatatypes.size());
                
                
                
                
                Byte    slot = (Byte)     cdataitems.get(0);
                Integer trig = (Integer)  cdataitems.get(1);
                Long    time = (Long)     cdataitems.get(2);
                //EvioRawDataBank  dataBank = new EvioRawDataBank(crate, slot.intValue(),trig,time);
                
                
            } catch (EvioException ex) {
                Logger.getLogger(EvioRawEventDecoder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return entries;
    }
    
    public ArrayList<RawDataEntry>  getDataEntries_57601(Integer crate, EvioNode node, EvioDataEvent event){
        ArrayList<RawDataEntry>  entries = new ArrayList<RawDataEntry>();
        if(node.getTag()==57601){
            try {
                
                ByteBuffer     compBuffer = node.getByteData(true);
                CompositeData  compData = new CompositeData(compBuffer.array(),event.getByteOrder());
                
                List<DataType> cdatatypes = compData.getTypes();
                List<Object>   cdataitems = compData.getItems();

                if(cdatatypes.get(3) != DataType.NVALUE){
                    System.err.println("[EvioRawDataSource] ** error ** corrupted "
                    + " bank. tag = " + node.getTag() + " num = " + node.getNum());
                    return null;
                }
                
                int position = 0;
                
                while(position<cdatatypes.size()-4){
                    
                    
                    
                    Byte    slot = (Byte)     cdataitems.get(position+0);
                    Integer trig = (Integer)  cdataitems.get(position+1);
                    Long    time = (Long)     cdataitems.get(position+2);
                    //EvioRawDataBank  dataBank = new EvioRawDataBank(crate, slot.intValue(),trig,time);
                    
                    Integer nchannels = (Integer) cdataitems.get(position+3);
                    //System.out.println("Retrieving the data size = " + cdataitems.size()
                    //+ "  " + cdatatypes.get(3) + " number of channels = " + nchannels);
                    position += 4;
                    int counter  = 0;
                    while(counter<nchannels){
                        //System.err.println("Position = " + position + " type =  "
                        //+ cdatatypes.get(position));
                        Byte channel   = (Byte) cdataitems.get(position);
                        Integer length = (Integer) cdataitems.get(position+1);
                        RawDataEntry bank = new RawDataEntry(crate,slot.intValue(),channel.intValue());
                        //dataBank.addChannel(channel.intValue());
                        short[] shortbuffer = new short[length];
                        for(int loop = 0; loop < length; loop++){
                            Short sample    = (Short) cdataitems.get(position+2+loop);
                            shortbuffer[loop] = sample;
                            //dataBank.addData(channel.intValue(), 
                            //        new RawData(tdc,adc,pmin,pmax));
                        }
                        bank.setRawPulse(shortbuffer);
                        //dataBank.addData(channel.intValue(), 
                        //            new RawData(shortbuffer));
                        entries.add(bank);
                        position += 2+length;
                        counter++;
                    }
                }
                return entries;
                
            } catch (EvioException ex) {
                Logger.getLogger(EvioRawEventDecoder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return entries;
    }
    
    public ArrayList<RawDataEntry>  getDataEntries_57602_DEBUG(Integer crate, EvioNode node, EvioDataEvent event){
        ArrayList<RawDataEntry>  entries = new ArrayList<RawDataEntry>();
        
        if(node.getTag()==57602){
            try {
                ByteBuffer     compBuffer = node.getByteData(true);
                CompositeData  compData = new CompositeData(compBuffer.array(),event.getByteOrder());
                
                List<DataType> cdatatypes = compData.getTypes();
                List<Object>   cdataitems = compData.getItems();
                System.out.println("DEBUG :  SIZE = " + cdatatypes.size());
            } catch (EvioException ex) {
                Logger.getLogger(EvioRawEventDecoder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return entries;
    }
    
    public ArrayList<RawDataEntry>  getDataEntries_57602(Integer crate, EvioNode node, EvioDataEvent event){
        ArrayList<RawDataEntry>  entries = new ArrayList<RawDataEntry>();
        
        if(node.getTag()==57602){
            try {
                ByteBuffer     compBuffer = node.getByteData(true);
                CompositeData  compData = new CompositeData(compBuffer.array(),event.getByteOrder());
                
                List<DataType> cdatatypes = compData.getTypes();
                List<Object>   cdataitems = compData.getItems();
                
                if(cdatatypes.get(3) != DataType.NVALUE){
                    System.err.println("[EvioRawDataSource] ** error ** corrupted "
                    + " bank. tag = " + node.getTag() + " num = " + node.getNum());
                    return null;
                }

                int position = 0;
                
                while(position<cdatatypes.size()){                    
                
                    Byte    slot = (Byte)     cdataitems.get(position+0);
                    Integer trig = (Integer)  cdataitems.get(position+1);
                    Long    time = (Long)     cdataitems.get(position+2);
                    
                    //EvioRawDataBank  dataBank = new EvioRawDataBank(crate,slot.intValue(),trig,time);
                    Integer nchannels = (Integer) cdataitems.get(position+3);
                    //System.out.println("Retrieving the data size = " + cdataitems.size()
                    //+ "  " + cdatatypes.get(3) + " number of channels = " + nchannels);
                    position += 4;
                    int counter  = 0;
                    while(counter<nchannels){
                        //System.err.println("Position = " + position + " type =  "
                        //+ cdatatypes.get(position));
                        //Byte    slot = (Byte)     cdataitems.get(0);
                        //Integer trig = (Integer)  cdataitems.get(1);
                        //Long    time = (Long)     cdataitems.get(2);
                        Byte channel   = (Byte) cdataitems.get(position);
                        Integer length = (Integer) cdataitems.get(position+1);
                        //dataBank.addChannel(channel.intValue());
                        
                        for(int loop = 0; loop < length; loop++){
                            Short tdc    = (Short) cdataitems.get(position+2);
                            Integer adc  = (Integer) cdataitems.get(position+3);
                            Short pmin   = (Short) cdataitems.get(position+4);
                            Short pmax   = (Short) cdataitems.get(position+5);
                            RawDataEntry  entry = new RawDataEntry(crate,slot,channel);
                            entry.setPulse(tdc, adc, pmin, pmax);
                            entries.add(entry);
                            //position+=4;
                            //dataBank.addData(channel.intValue(), 
                            //       new RawData(tdc,adc,pmin,pmax));
                        }
                        position += 6;
                        counter++;
                    }
                }
                return entries;
            } catch (EvioException ex) {
                Logger.getLogger(EvioRawEventDecoder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return entries;
    }
    
    public ArrayList<RawDataEntry>  getDataEntries_57622(Integer crate, EvioNode node, EvioDataEvent event){
        ArrayList<RawDataEntry>  rawdata = new ArrayList<RawDataEntry>();
        if(node.getTag()==57622){
            try {
                ByteBuffer     compBuffer = node.getByteData(true);
                CompositeData  compData = new CompositeData(compBuffer.array(),event.getByteOrder());                
                List<DataType> cdatatypes = compData.getTypes();
                List<Object>   cdataitems = compData.getItems();
                int  totalSize = cdataitems.size();
                int  position  = 0;
                while( (position + 4) < totalSize){
                    Byte    slot = (Byte)     cdataitems.get(position);
                    Integer trig = (Integer)  cdataitems.get(position+1);
                    Long    time = (Long)     cdataitems.get(position+2);
                    //EvioRawDataBank  dataBank = new EvioRawDataBank(crate, slot.intValue(),trig,time);
                    Integer nchannels = (Integer) cdataitems.get(position+3);
                    int counter  = 0;
                    position = position + 4;
                    while(counter<nchannels){
                        //System.err.println("Position = " + position + " type =  "
                        //+ cdatatypes.get(position));
                        Byte   channel    = (Byte) cdataitems.get(position);
                        Short  tdc     = (Short) cdataitems.get(position+1);

                        position += 2;
                        counter++;
                        RawDataEntry  entry = new RawDataEntry(crate,slot,channel);
                        //entry.setSVT(half, channel, tdc, adc);
                        entry.setTDC(tdc);
                        rawdata.add(entry);
                    }
                }
            } catch (EvioException ex) {
                //Logger.getLogger(EvioRawDataSource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException ex){
                //System.out.println("[ERROR] ----> ERROR DECODING COMPOSITE DATA FOR ONE EVENT");
            }
        }
        return rawdata;
    }
    
    
    
    public ArrayList<RawDataEntry>  getDataEntries_57617(Integer crate, EvioNode node, EvioDataEvent event){
        ArrayList<RawDataEntry>  rawdata = new ArrayList<RawDataEntry>();
        if(node.getTag()==57617){
            try {
                ByteBuffer     compBuffer = node.getByteData(true);
                CompositeData  compData = new CompositeData(compBuffer.array(),event.getByteOrder());                
                List<DataType> cdatatypes = compData.getTypes();
                List<Object>   cdataitems = compData.getItems();
                int  totalSize = cdataitems.size();
                //ArrayList<EvioRawDataBank> bankArray = new ArrayList<EvioRawDataBank>();
                int  position  = 0;
                while( (position + 4) < totalSize){
                    Byte    slot = (Byte)     cdataitems.get(position);
                    Integer trig = (Integer)  cdataitems.get(position+1);
                    Long    time = (Long)     cdataitems.get(position+2);
                    //EvioRawDataBank  dataBank = new EvioRawDataBank(crate, slot.intValue(),trig,time);
                    Integer nchannels = (Integer) cdataitems.get(position+3);
                    int counter  = 0;
                    position = position + 4;
                    while(counter<nchannels){
                        //System.err.println("Position = " + position + " type =  "
                        //+ cdatatypes.get(position));
                        Byte   half    = (Byte) cdataitems.get(position);
                        Byte   channel = (Byte) cdataitems.get(position+1);
                        Byte   tdc     = (Byte) cdataitems.get(position+2);
                        //Short   adc     = (Short)  cdataitems.get(position+3);
                        Byte   adc     = (Byte)  cdataitems.get(position+3);
                        
                        Integer channelKey = (half<<8)|channel;
                        //System.err.println(" HALF = " + half + "  CHANNEL = " + channel + " KEY = " + channelKey  );
                        //dataBank.addChannel(channelKey);
                        //dataBank.addData(channelKey, new RawData(channelKey,tdc,adc));
                        position += 4;
                        counter++;
                        RawDataEntry  entry = new RawDataEntry(crate,slot,channelKey);
                        entry.setSVT(half, channel, tdc, adc);
                        rawdata.add(entry);
                    }
                    //bankArray.add(dataBank);
                }
                
            } catch (EvioException ex) {
                //Logger.getLogger(EvioRawDataSource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException ex){
                //System.out.println("[ERROR] ----> ERROR DECODING COMPOSITE DATA FOR ONE EVENT");
            }
        }
        return rawdata;
    }
    
    public void list(EvioDataEvent event){
        ArrayList<EvioTreeBranch> branches = this.getEventBranches(event);
        for(EvioTreeBranch branch : branches){
            System.out.println(">>>>>>>>>>> HEAD NODE " + branch.getTag());
            for(EvioNode node : branch.getNodes()){
                System.out.println("\t\t -----> NODE " + node.getTag() + " : " + node.getDataTypeObj());
            }
        }
    }
    /**
     * Returns an array of the branches in the event.
     * @param event
     * @return 
     */
    public ArrayList<EvioTreeBranch>  getEventBranches(EvioDataEvent event){
        ArrayList<EvioTreeBranch>  branches = new ArrayList<EvioTreeBranch>();
        EvioNode mainNODE = event.getStructureHandler().getScannedStructure();
        List<EvioNode>  eventNodes = mainNODE.getChildNodes();
        if(eventNodes==null){
            return branches;
        }
        
        for(EvioNode node : eventNodes){
            EvioTreeBranch eBranch = new EvioTreeBranch(node.getTag(),node.getNum());
            List<EvioNode>  childNodes = node.getChildNodes();
            if(childNodes!=null){
                for(EvioNode child : childNodes){
                        eBranch.addNode(child);
                }
                branches.add(eBranch);
            }
        }
        return branches;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        System.out.println("[RAW DATA DECODER] ----->  DECODER TRANSLATION TABLE LIST");
        for(Map.Entry<String,IDetectorTranslationTable> entry : this.translationTables.entrySet()){
            str.append(String.format("TRANSLATION TABLE : [%12s]", entry.getKey()));
        }
        return str.toString();
    }
        
}
