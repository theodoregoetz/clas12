/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.io.decode;

import java.util.ArrayList;
import java.util.Set;
import org.jlab.clas12.raw.EvioRawDataBank;
import org.jlab.clas12.raw.EvioRawDataSource;
import org.jlab.clas12.raw.RawData;
import org.jlab.evio.clas12.EvioDataBank;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioDataSync;
import org.jlab.evio.clas12.EvioFactory;

/**
 *
 * @author gavalian
 */
public class EvioRawDataDecoder {
    private EvioRawDataSource  reader = new EvioRawDataSource();
    private EvioDataSync       writer = new EvioDataSync();
    private TranslationTable   transTable = new TranslationTable();
    
    public EvioRawDataDecoder(){
        this.init();
    }
    
    public final void init(){
        String  envCLAS = System.getenv("CLAS12DIR");
        //String  envCLAS = "/Users/gavalian/Work/Software/Release-7.0/COATJAVA/coatjava/";
        
        String  tfile   = String.format("%s/etc/bankdefs/translation/SVT.table", envCLAS);
        transTable.readFile(tfile);
    }
    
    public EvioDataEvent  getDecodedEvent(EvioDataEvent event){
        EvioDataEvent  outEvent = writer.createEvent(EvioFactory.getDictionary());
        return outEvent;
    }
    
    
    public ArrayList<SVTDataRecord>  getRecordsSVT(EvioRawDataBank bank){
        int crate  = bank.getCrate();
        int slot   = bank.getSlot();
        ArrayList<Integer>  channelList = bank.getChannelList();
        ArrayList<SVTDataRecord>  records = new ArrayList<SVTDataRecord>();
        for(int loop = 0; loop < channelList.size(); loop++){
            
            Integer channel = channelList.get(loop);            
            ArrayList<RawData> dataList = bank.getData(channel);
            
            Integer firstShort = (Integer) dataList.get(0).getObject(0);
            Short   BCO        = (Short) dataList.get(0).getObject(1);
            Short   ADC        = (Short) dataList.get(0).getObject(2);
                        
            Integer trueChannel =  (firstShort & 0x0000007F);
            Integer chipID      =  (firstShort & 0x00000700)>>8 ;
            Integer half        =  (firstShort & 0x00000800)>>11;
            SVTDataRecord  record  = new SVTDataRecord();
            TranslationTableEntry entry = this.transTable.getEntry(crate, slot, half);
            if(entry==null){
                System.err.println("[Decoder:ERROR]----> can not find entry for "
                + " CRATE = " + crate + "  SLOT = " + slot + "  CHANNEL = " + half);
            } else {
                record.init(entry.sector, entry.layer, half, chipID, trueChannel);
                record.setData(BCO, ADC);
                records.add(record);
            }
        }
        return records;
    }
    
    public EvioDataBank  createBankSVT(EvioRawDataBank bank){        
        EvioDataBank svtBank = EvioFactory.createBank("BST::dgtz", bank.getRows());
        ArrayList<Integer>  channelList = bank.getChannelList();
        int crate  = bank.getCrate();
        int slot   = bank.getSlot();
        SVTDataRecord  record  = new SVTDataRecord();        
        for(int loop = 0; loop < channelList.size(); loop++){
            Integer channel = channelList.get(loop);            
            ArrayList<RawData> dataList = bank.getData(channel);
            
            Integer firstShort = (Integer) dataList.get(0).getObject(0);
            Short   BCO        = (Short) dataList.get(0).getObject(1);
            Short   ADC        = (Short) dataList.get(0).getObject(2);
            
            
            Integer trueChannel =  (firstShort & 0x0000007F);
            Integer chipID      =  (firstShort & 0x00000700)>>8 ;
            Integer half        =  (firstShort & 0x00000800)>>11;
            
            /*
            System.err.println(String.format("INFO ===== %5x %5x %5x %5x VALUE %5d CHAN %5d  HALF %5d CHIPID %5d  CR %4d SLOT %3d", 
                    firstShort, trueChannel,half,chipID,
                    firstShort,trueChannel,half,chipID, crate,slot));
            */
            TranslationTableEntry entry = this.transTable.getEntry(crate, slot, half);

            if(entry==null){
                //System.err.println("[Decoder:ERROR]----> can not find entry for "
                //+ " CRATE = " + crate + "  SLOT = " + slot + "  CHANNEL = " + half);
            } else {
                record.init(entry.sector, entry.layer, half, chipID, trueChannel);
                //System.out.println(record);
                //System.out.println("---------------------\n");
                //System.err.println("[Decoder: OK ]----> found "
                //        + " CRATE = " + crate + "  SLOT = " + slot + "  CHANNEL = " + half);
                svtBank.setInt("sector", loop, record.SECTOR);
                svtBank.setInt("layer" , loop, record.LAYER);
                svtBank.setInt("strip" , loop, record.STRIP);
                svtBank.setInt("bco", loop, (int) BCO);
                svtBank.setInt("ADC", loop, (int) ADC);
            }
            
        }
        return svtBank;
    }
        
    public void process(String filename, String outfilename){                
        
        reader.open(filename);
        writer.open(outfilename);
        Set<Integer>  decoderCreates = this.transTable.getCreateList();
        int icounter = 0;
        //for(int loop = 0; loop < 200; loop++){
        while(reader.hasEvent()){
            EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
            //System.err.println("************ EVENT " + icounter + " ********** ");
            ArrayList<SVTDataRecord>  svtRecords = new ArrayList<SVTDataRecord>();
            for(Integer create : decoderCreates){
                //System.err.println("LOOKING FOR CREATE ------------> " + create);
                EvioRawDataBank bank = reader.getDataBank(event, create);
                if(bank!=null){
                    ArrayList<SVTDataRecord> crateRecords = this.getRecordsSVT(bank);
                    
                    for(SVTDataRecord item : crateRecords){
                        svtRecords.add(item);
                    }
                    //System.err.println("--------->  FOUND CREATE " + create);
                    //System.err.println(bank.toString());
                    
                    //EvioDataBank svtBank = this.createBankSVT(bank);
                    /*
                    if(svtBank.rows()>0){
                        EvioDataEvent outputEvent = writer.createEvent(EvioFactory.getDictionary());
                        outputEvent.appendBanks(svtBank);
                        writer.writeEvent(outputEvent);
                    }*/
                    //svtBank.show();
                }
            }
            
            if(svtRecords.size()>0){
                int irows = svtRecords.size();
                EvioDataEvent outputEvent = writer.createEvent(EvioFactory.getDictionary());
                EvioDataBank  svtBank = EvioFactory.createBank("BST::dgtz", irows);
                for(int loop = 0; loop < irows; loop++){
                    svtBank.setInt("sector", loop, svtRecords.get(loop).SECTOR);
                    svtBank.setInt("layer", loop, svtRecords.get(loop).LAYER);
                    svtBank.setInt("strip", loop, svtRecords.get(loop).STRIP);
                    svtBank.setInt("bco", loop, svtRecords.get(loop).BCO);
                    svtBank.setInt("ADC", loop, svtRecords.get(loop).ADC);
                }
                outputEvent.appendBanks(svtBank);
                writer.writeEvent(outputEvent);
            }
            icounter++;
        }
        writer.close();
    }
    
    
    public static void main(String[] args){
        //System.setProperty("CLAS12DIR", "/Users/gavalian/Work/Software/Release-7.0/COATJAVA/coatjava/");
        EvioFactory.loadDictionary();
        EvioRawDataDecoder decoder = new EvioRawDataDecoder();
        String inputFile = args[0];
        decoder.process(inputFile, "svt_decoded_data.evio");
        //decoder.process("/Users/gavalian/Work/DataSpace/SVT/svt257test.dat_000869.evio", "test.evio");        
    }
}
