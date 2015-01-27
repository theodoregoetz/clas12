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
        //String  envCLAS = System.getenv("CLAS12DIR");
        String  envCLAS = "/Users/gavalian/Work/Software/Release-7.0/COATJAVA/coatjava/";
        
        String  tfile   = String.format("%s/etc/bankdefs/translation/SVT.table", envCLAS);
        transTable.readFile(tfile);
    }
    
    public EvioDataEvent  getDecodedEvent(EvioDataEvent event){
        EvioDataEvent  outEvent = writer.createEvent(EvioFactory.getDictionary());
        return outEvent;
    }
    
    public EvioDataBank  createBankSVT(EvioRawDataBank bank){        
        EvioDataBank svtBank = EvioFactory.createBank("BST::dgtz", bank.getRows());
        ArrayList<Integer>  channelList = bank.getChannelList();
        int crate  = bank.getCrate();
        int slot   = bank.getSlot();
        
        for(int loop = 0; loop < channelList.size(); loop++){
            Integer channel = channelList.get(loop);            
            ArrayList<RawData> dataList = bank.getData(channel);
            
            Integer firstShort = (Integer) dataList.get(0).getObject(0);
            
            Integer trueChannel =  (firstShort & 0x000000FF);
            Integer half        =  (firstShort & 0x00000A00)>>8;
            System.err.println(String.format("INFO ===== %x %x %x %d %d %d", 
                    firstShort, trueChannel,half,
                    firstShort,trueChannel,half));
            TranslationTableEntry entry = this.transTable.getEntry(crate, slot, channel);
            if(entry==null){
                System.err.println("[Decoder:ERROR]----> can not find entry for "
                + " CRATE = " + crate + "  SLOT = " + slot + "  CHANNEL = " + channel);
            } else {
                svtBank.setInt("sector", loop, entry.sector);
                svtBank.setInt("layer" , loop, entry.layer);
            }
        }
        return svtBank;
    }
        
    public void process(String filename, String outfilename){                
        
        reader.open(filename);
        writer.open(outfilename);
        Set<Integer>  decoderCreates = this.transTable.getCreateList();
        int icounter = 0;
        while(reader.hasEvent()){
            EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
            //System.err.println("************ EVENT " + icounter + " ********** ");
            for(Integer create : decoderCreates){
                //System.err.println("LOOKING FOR CREATE ------------> " + create);
                EvioRawDataBank bank = reader.getDataBank(event, create);
                if(bank!=null){
                    //System.err.println("--------->  FOUND CREATE " + create);
                    //System.err.println(bank.toString());
                    EvioDataBank svtBank = this.createBankSVT(bank);
                    //svtBank.show();
                }
            }
            icounter++;
        }
    }
    
    
    public static void main(String[] args){
        System.setProperty("CLAS12DIR", "/Users/gavalian/Work/Software/Release-7.0/COATJAVA/coatjava/");
        EvioFactory.loadDictionary();
        EvioRawDataDecoder decoder = new EvioRawDataDecoder();
        decoder.process("/Users/gavalian/Work/DataSpace/SVT/svt25test.dat_000856.evio", "test.evio");
    }
}
