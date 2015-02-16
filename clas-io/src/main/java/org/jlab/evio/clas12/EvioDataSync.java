/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.evio.clas12;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.coda.jevio.DataType;
import org.jlab.coda.jevio.EventBuilder;
import org.jlab.coda.jevio.EventWriter;
import org.jlab.coda.jevio.EvioBank;
import org.jlab.coda.jevio.EvioCompactEventWriter;
import org.jlab.coda.jevio.EvioCompactStructureHandler;
import org.jlab.coda.jevio.EvioEvent;
import org.jlab.coda.jevio.EvioException;
import org.jlab.data.io.DataEvent;
import org.jlab.data.io.DataSync;

/**
 *
 * @author gavalian
 */
public class EvioDataSync implements DataSync {
    private ByteOrder writerByteOrder = ByteOrder.LITTLE_ENDIAN;
    private EvioCompactEventWriter evioWriter    = null;
    
    @Override
    public void open(String filename) {
        File file = new File(filename);
        try {
            evioWriter = new EvioCompactEventWriter(filename, null,
                    0, 0, 
                    15*300, 
                    2000, 
                    8*1024*1024, 
                    writerByteOrder, null, true);
//new EventWriter(file, 1000000, 2,
            //ByteOrder.BIG_ENDIAN, null, null);
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void writeEvent(DataEvent event) {
        try {
            //System.err.println("[sync] ---> buffer size = " + event.getEventBuffer().limit());
            ByteBuffer original = event.getEventBuffer();
            ByteBuffer clone = ByteBuffer.allocate(original.capacity());
            clone.order(original.order());
            original.rewind();
            clone.put(original);
            original.rewind();
            clone.flip();
            evioWriter.writeEvent(clone);
//            event.getEventBuffer().flip();
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataSync.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EvioDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openWithDictionary(String filename,String dictionary){
        File file = new File(filename);
        try {
            evioWriter = new EvioCompactEventWriter(filename, dictionary,
                    0, 0, 
                    4*300, 1000, 2*1024*1024, writerByteOrder, null, true);
//new EventWriter(file, 1000000, 2,
            //ByteOrder.BIG_ENDIAN, null, null);
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void close() {
        evioWriter.close();
    }
    
    
    public static void writeFileWithDictionary(String inputfile, String outputfile){
        EvioSource reader = new EvioSource();
        reader.open(inputfile);
        EvioDataSync writer = new EvioDataSync();
        EvioDataDictionary dict = new EvioDataDictionary("CLAS12DIR","lib/bankdefs/clas12new/");
        String dictString = dict.getXML();
        
    }
    
    public EvioDataEvent createEvent(EvioDataDictionary dict){
        
        try {
            //EvioEvent baseBank = new EvioEvent(1, DataType.BANK, 0);
            EventBuilder builder = new EventBuilder(1,DataType.BANK,0);
            EvioEvent event = builder.getEvent();
            EvioBank baseBank = new EvioBank(10, DataType.ALSOBANK, 0);
            
            builder.addChild(event, baseBank);
            
            ByteOrder byteOrder = writerByteOrder;
            
            int byteSize = event.getTotalBytes();
            //System.out.println("base bank size = " + byteSize);
            ByteBuffer bb = ByteBuffer.allocate(byteSize);
            bb.order(byteOrder);
            event.write(bb);
            bb.flip();
         
            return new EvioDataEvent(bb,dict);
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public EvioDataEvent createEvent(){
        
        try {
            //EvioEvent baseBank = new EvioEvent(1, DataType.BANK, 0);
            EventBuilder builder = new EventBuilder(1,DataType.BANK,0);
            EvioEvent event = builder.getEvent();
            EvioBank baseBank = new EvioBank(10, DataType.ALSOBANK, 0);
            
            builder.addChild(event, baseBank);
            
            ByteOrder byteOrder = writerByteOrder;
            
            int byteSize = event.getTotalBytes();
            //System.out.println("base bank size = " + byteSize);
            ByteBuffer bb = ByteBuffer.allocate(byteSize);
            bb.order(byteOrder);
            event.write(bb);
            bb.flip();
         
            return new EvioDataEvent(bb);
        } catch (EvioException ex) {
            Logger.getLogger(EvioDataSync.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static void main(String[] args){
        String inputfile  = args[0];
        
    }
}
