/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.data.utils;

import java.util.ArrayList;
import java.util.Map;
import org.jlab.bos.clas6.BosDataEvent;
import org.jlab.bos.clas6.BosDataSource;
import org.jlab.evio.clas12.EvioDataBank;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioDataSync;
import org.jlab.evio.clas12.EvioFactory;

/**
 *
 * @author gavalian
 */
public class Bos2Evio {
    public static void main(String[] args){
        
        EvioFactory.getDictionary().getDescriptor("HEADER::info").show();
        EvioFactory.getDictionary().getDescriptor("PART::particle").show();
        
        String method = args[0];
        String output = args[1];
        ArrayList<String> inputfiles = new ArrayList<String>();
        for(int loop = 2; loop < args.length; loop++){
            inputfiles.add(args[loop]);
        }
        
        Bos2EvioPartBank  convertPART = new Bos2EvioPartBank();
        EvioDataSync  writer = new EvioDataSync();
        writer.open(output);
        
        for(String inFile : inputfiles){
            BosDataSource reader = new BosDataSource();                      
            reader.open(inFile);
            while(reader.hasEvent()){
                BosDataEvent bosEvent = (BosDataEvent) reader.getNextEvent();
                convertPART.processBosEvent(bosEvent);
                EvioDataEvent outevent = writer.createEvent(EvioFactory.getDictionary());
                for(Map.Entry<String,EvioDataBank> banks : convertPART.bankStore().entrySet()){
                    outevent.appendBank(banks.getValue());
                }
                writer.writeEvent(outevent);
            }
            reader.close();
        }
        
        writer.close();
    }
    
}
