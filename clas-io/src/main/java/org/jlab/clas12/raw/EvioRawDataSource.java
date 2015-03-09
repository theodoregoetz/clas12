/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clas12.raw;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.coda.jevio.CompositeData;
import org.jlab.coda.jevio.DataType;
import org.jlab.coda.jevio.EvioException;
import org.jlab.coda.jevio.EvioNode;
import org.jlab.data.io.DataEvent;
import org.jlab.data.io.DataEventList;
import org.jlab.data.io.DataSource;
import org.jlab.data.utils.EvioDataConvertor;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class EvioRawDataSource implements DataSource {
    
    private EvioSource reader = new EvioSource();
    
    @Override
    public boolean hasEvent() {
        return reader.hasEvent();
    }

    @Override
    public void open(File file) {
        reader.open(file);
    }

    @Override
    public void open(String filename) {
        reader.open(filename);
    }

    @Override
    public void open(ByteBuffer buff) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        reader.close();
    }

    @Override
    public int getSize() {
        return reader.getSize();
    }

    @Override
    public DataEventList getEventList(int start, int stop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataEventList getEventList(int nrecords) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataEvent getNextEvent() {
        return reader.getNextEvent();
    }

    
    public ADCData compositeToADC(CompositeData cdata){
        ADCData adcdata = new ADCData(0,0,(long) 0);
        List<Object>    cdataItems = cdata.getItems();
        List<DataType>  cdataTypes = cdata.getTypes();
        
        //adcdata.slot       = (Integer) cdataItems.get(0);
        adcdata.trigger    = (Integer) cdataItems.get(1);
        adcdata.timestamp  = (Long) cdataItems.get(2);
        
        return adcdata;
    }
    
    public ADCData getADCData(EvioDataEvent event, int tag, int num){

        ADCData data = new ADCData(0,0,(long) 0);
        EvioNode node = event.getNodeFromTree(tag, num, DataType.COMPOSITE);
        if(node!=null){
            
            ByteBuffer buffer = node.getByteData(true);
            
            try {
                CompositeData  cdata = new CompositeData(buffer.array(),event.getByteOrder());
                return this.compositeToADC(cdata);
                
            } catch (EvioException ex) {
                Logger.getLogger(EvioRawDataSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return data;
    }
    
    @Override
    public DataEvent getPreviousEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
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
    
    public EvioTreeBranch  getEventBranch(ArrayList<EvioTreeBranch> branches, int tag){
        for(EvioTreeBranch branch : branches){
            if(branch.getTag()==tag) return branch;
        }
        return null;
    }
    
    
    public void showBranches(ArrayList<EvioTreeBranch>  branches){
        for(EvioTreeBranch branch : branches){
            System.out.println(branch.toString());
        }
    }
    
    public TreeMap<Integer,EvioNode>  getEvenetNodeTree(EvioDataEvent event){
        TreeMap<Integer,EvioNode> nodeTree = new TreeMap<Integer,EvioNode>();
        EvioNode evNODE = event.getStructureHandler().getScannedStructure();
        List<EvioNode>  eventNodes = evNODE.getChildNodes();
        if(eventNodes!=null){
            for(EvioNode node : eventNodes){
                List<EvioNode>  childNodes = node.getChildNodes();
                System.err.println("BANK NODE ------ " + node.getTag());
                if(childNodes!=null){
                    for(EvioNode child : childNodes){
                        System.err.println("\t NODE " + child.getTag() + "  " + child.getNum() );
                    }
                }
            }
        }
        return nodeTree;
    }
    
    public void list(EvioDataEvent event){
        
        
    }
    
    
    @Override
    public DataEvent gotoEvent(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getCurrentIndex() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
    
    public EvioRawDataBank  getDataBankFromNodeRawPulse(Integer crate, EvioNode node, EvioDataEvent event){
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
                
                Byte    slot = (Byte)     cdataitems.get(0);
                Integer trig = (Integer)  cdataitems.get(1);
                Long    time = (Long)     cdataitems.get(2);
                EvioRawDataBank  dataBank = new EvioRawDataBank(crate, slot.intValue(),trig,time);
                Integer nchannels = (Integer) cdataitems.get(3);
                //System.out.println("Retrieving the data size = " + cdataitems.size()
                //+ "  " + cdatatypes.get(3) + " number of channels = " + nchannels);
                int position = 4;
                int counter  = 0;
                while(counter<nchannels){
                    //System.err.println("Position = " + position + " type =  "
                    //+ cdatatypes.get(position));
                    Byte channel   = (Byte) cdataitems.get(position);
                    Integer length = (Integer) cdataitems.get(position+1);
                    dataBank.addChannel(channel.intValue());
                    short[] shortbuffer = new short[length];
                    for(int loop = 0; loop < length; loop++){
                        Short sample    = (Short) cdataitems.get(position+2+loop);
                        shortbuffer[loop] = sample;
                        //dataBank.addData(channel.intValue(), 
                        //        new RawData(tdc,adc,pmin,pmax));
                    }
                    dataBank.addData(channel.intValue(), 
                                new RawData(shortbuffer));
                    position += 2+length;
                    counter++;
                }
                
                return dataBank;
            } catch (EvioException ex) {
                Logger.getLogger(EvioRawDataSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public EvioRawDataBank  getDataBankFromNodeMode17(Integer crate, EvioNode node, EvioDataEvent event){
        if(node.getTag()==57617){
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
                
                Byte    slot = (Byte)     cdataitems.get(0);
                Integer trig = (Integer)  cdataitems.get(1);
                Long    time = (Long)     cdataitems.get(2);
                EvioRawDataBank  dataBank = new EvioRawDataBank(crate, slot.intValue(),trig,time);
                Integer nchannels = (Integer) cdataitems.get(3);
                
                int position = 4;
                int counter  = 0;
                while(counter<nchannels){
                    //System.err.println("Position = " + position + " type =  "
                    //+ cdatatypes.get(position));
                    Short   half    = EvioDataConvertor.getShortFromByte((Byte) cdataitems.get(position));
                    Short   channel = EvioDataConvertor.getShortFromByte((Byte) cdataitems.get(position+1));
                    Short   tdc     = EvioDataConvertor.getShortFromByte((Byte) cdataitems.get(position+2));
                    //Short   adc     = (Short)  cdataitems.get(position+3);
                    Short   adc     = EvioDataConvertor.getShortFromByte((Byte)  cdataitems.get(position+3));
                    
                    Integer channelKey = (half<<8)|channel;
                    //System.err.println(" HALF = " + half + "  CHANNEL = " + channel + " KEY = " + channelKey  );
                    dataBank.addChannel(channelKey);
                    dataBank.addData(channelKey, new RawData(channelKey,tdc,adc));
                    position += 4;
                    counter++;
                }
                
                return dataBank;
                
            } catch (EvioException ex) {
                //Logger.getLogger(EvioRawDataSource.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException ex){
                //System.out.println("[ERROR] ----> ERROR DECODING COMPOSITE DATA FOR ONE EVENT");
            }
        }
        return null;
    }
    
    public EvioRawDataBank  getDataBankFromNode(Integer crate, EvioNode node, EvioDataEvent event){
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
                
                Byte    slot = (Byte)     cdataitems.get(0);
                Integer trig = (Integer)  cdataitems.get(1);
                Long    time = (Long)     cdataitems.get(2);
                EvioRawDataBank  dataBank = new EvioRawDataBank(crate,slot.intValue(),trig,time);
                Integer nchannels = (Integer) cdataitems.get(3);
                //System.out.println("Retrieving the data size = " + cdataitems.size()
                //+ "  " + cdatatypes.get(3) + " number of channels = " + nchannels);
                int position = 4;
                int counter  = 0;
                while(counter<nchannels){
                    //System.err.println("Position = " + position + " type =  "
                    //+ cdatatypes.get(position));
                    Byte channel   = (Byte) cdataitems.get(position);
                    Integer length = (Integer) cdataitems.get(position+1);
                    dataBank.addChannel(channel.intValue());
                    for(int loop = 0; loop < length; loop++){
                        Short tdc    = (Short) cdataitems.get(position+2);
                        Integer adc  = (Integer) cdataitems.get(position+3);
                        Short pmin   = (Short) cdataitems.get(position+4);
                        Short pmax   = (Short) cdataitems.get(position+5);
                        dataBank.addData(channel.intValue(), 
                                new RawData(tdc,adc,pmin,pmax));
                    }
                    position += 6;
                    counter++;
                }
                
                return dataBank;
            } catch (EvioException ex) {
                Logger.getLogger(EvioRawDataSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public EvioRawDataBank  getDataBank(EvioDataEvent event, int crate){
        ArrayList<EvioTreeBranch> branches = this.getEventBranches(event);
        
        EvioTreeBranch cbranch = this.getEventBranch(branches, crate);
        if(cbranch == null ) return null;
        for(EvioNode node : cbranch.getNodes()){
            if(node.getTag()==57602){
                return this.getDataBankFromNode(crate,node, event);
            }
            if(node.getTag()==57601){
                return this.getDataBankFromNodeRawPulse(crate,node, event);
            }
            if(node.getTag()==57617){
                return this.getDataBankFromNodeMode17(crate,node, event);
            }
        }
        //EvioRawDataBank dataBank = new EvioRawDataBank();
        return null;
    }
    
    public static void main(String[] args){
        
        
        EvioRawDataSource  source = new EvioRawDataSource();
        //source.open("/Users/gavalian/Work/DataSpace/LTCC/ltcc0test_000195.evio");
        //source.open("/Users/gavalian/Work/DataSpace/HPS/hps_003001_mode7.evio");
        //source.open("/Users/gavalian/Work/DataSpace/SVT/svt257test.dat_000869.evio");
        source.open("/Users/gavalian/Work/DataSpace/SVT/svt257test.dat_000869.evio");
        for(int loop = 0; loop < 100000; loop++){
            EvioDataEvent event = (EvioDataEvent) source.getNextEvent();
            //ADCData  adc = source.getADCData(event, 57601,43);
            System.out.println(" EVENT # = " + (loop+1));
            EvioRawDataBank bank = source.getDataBank(event, 2);
            if(bank!=null){
                //System.out.println("EVENT = " + loop);
                //ArrayList<EvioTreeBranch> branches = source.getEventBranches(event);
                //source.showBranches(branches);            
                //System.out.println(bank);
            }
            //source.getEvenetNodeTree(event);
            //System.err.println(adc);
        }
    }
}
