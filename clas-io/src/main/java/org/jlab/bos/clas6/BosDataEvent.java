/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.bos.clas6;

import java.nio.ByteBuffer;
import org.jlab.data.io.DataBank;
import org.jlab.data.io.DataDictionary;
import org.jlab.data.io.DataEvent;

/**
 *
 * @author gavalian
 */
public class BosDataEvent implements DataEvent {
    
    private ByteBuffer bosBCS = null;
    private BosDataDictionary bankDictionary = null;
    private int               RunNumber;
    private int               EventNumber;
    
    public BosDataEvent(){
        
    }
    
    public BosDataEvent(ByteBuffer buffer){
        bosBCS = buffer;
    }
    
    public BosDataEvent(ByteBuffer buffer, BosDataDictionary dict){
        bosBCS = buffer;
        bankDictionary = dict;
        this.updateRunEventNumber();
    }
    
    @Override
    public String[] getBankList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void findStructure(){
        String name = "RUNEVENT";
        byte[] bufferBytes = bosBCS.array();
        byte[] nameBytes   = name.getBytes();
        for(int loop = 0; loop < bufferBytes.length; loop++){
            if(bufferBytes[loop]==nameBytes[0]){
                String header = new String(bufferBytes,loop,8);
                //System.out.println("[StrcutureFinder] ----> Found R at pos = "
                //+ loop + " header = " + header);
                if(header.compareTo(name)==0){
                    System.out.println("[StrcutureFinder] ----> Found events at pos = "
                    + loop);
                }
            }
        }
    }
    
    public void showBankInfo(String bank){
        int index = this.getBankIndex(bank, 0);
        if(index>=0){
            BosBankHeader header = new BosBankHeader(index);
            int ncols = this.unsignedIntFromBuffer(bosBCS, header.NCOLS_WORD_INDEX);
            int nrows = this.unsignedIntFromBuffer(bosBCS, header.NROWS_WORD_INDEX);
            int nword = this.unsignedIntFromBuffer(bosBCS, header.NWORDS_WORD_INDEX);
            int ndata = this.unsignedIntFromBuffer(bosBCS, header.NDATA_START_INDEX);
            System.out.println(
                    String.format("BANK [%6s] i=%8d c=%8d r=%8d w=%8d d=%8d l=%8d", 
                            bank,index,ncols,nrows,nword,ndata,bosBCS.array().length)
            );
        } else {
            System.out.println("[showBankInfo]----> marker for bank "
            + bank + " not found " );
        }
        
    }
    
    final void updateRunEventNumber(){
        if(this.checkBankConsistency("HEAD",0)==false) return;
        BosDataBank bank = (BosDataBank) this.getBank("HEAD");        
        if(bank!=null){
            RunNumber = bank.getInt("NRUN")[0];
            EventNumber = bank.getInt("NEVENT")[0];
        }
    }
    
    int getEventNumber(){
        if(this.checkBankConsistency("HEAD",0)==false) return 0;
        BosDataBank bank = (BosDataBank) this.getBank("HEAD");
        return bank.getInt("NEVENT")[0];
    }
    
    int unsignedIntFromBuffer(ByteBuffer b, int offset){
        byte b1 = b.get(offset);
        byte b2 = b.get(offset+1);
        byte b3 = b.get(offset+2);
        byte b4 = b.get(offset+3);
        /*
        System.err.println("b = " 
                + Integer.toHexString(b1) + "-"
                + Integer.toHexString(b2) + "-"
                + Integer.toHexString(b3) + "-"
                + Integer.toHexString(b4) + "-"
        );*/
        int s = 0;
        s = s | (b4 & 0xff);
        s = (s << 8);
        s = s | (b3 & 0xff);
        s = (s << 8);
        s = s | (b2 & 0xff);
        s = (s << 8);
        s = s | (b1 & 0xff);
        return s;
        //int s = 0;
        //s = s | b4;
        //s = (s << 8);
        //s = s | b3;
        //s = (s << 8);
        //s = s | b2;
        //s = (s << 8);
        //s = s | b1;
        //return s;
    }
    
    boolean checkBankConsistency(String bankname, int banknumber){
        boolean consistency = true;
        int index = this.getBankIndex(bankname, banknumber);
        if(index<0) return false;
        BosBankHeader header = new BosBankHeader(index);
        int ncols  = bosBCS.get(header.NCOLS_WORD_INDEX);
        int nrows  = bosBCS.get(header.NROWS_WORD_INDEX);
        //int nwords = bosBCS.get(header.NWORDS_WORD_INDEX);
        int nwords = this.unsignedIntFromBuffer(bosBCS, header.NWORDS_WORD_INDEX);
        //bosBCS.get(header.NWORDS_WORD_INDEX);
        
        int banksize = this.bankDictionary.getDescriptor(bankname).getProperty("banksize");
        int nbytes   = nwords*4;
        Integer nwords_i = nwords;
        int nwords_correct = nwords & 0x000000FF;
            //int nevent = this.getEventNumber();
            int nevent = 0;
            if(nrows*banksize>nbytes){
                /*
            System.out.println("[check-bank]---> ERROR : consistency check "
                    + "failed run # " + RunNumber + 
                    " event # " + EventNumber + " for bank " + bankname 
                    + " ncols = " + nrows 
                    + " nrows = " + nrows 
                    + " banksize = " + banksize
                    + " bytes = " + nrows*banksize
                    + " entry bytes = " + nbytes 
                    + " bin = " + Integer.toBinaryString(nwords)
                    + " hex = " + Integer.toHexString(nwords)
            );*/
            return false;
            }
        return consistency;
    }
    
    public void findBank(String name){
        byte[] bufferBytes = bosBCS.array();
        byte[] nameBytes   = name.getBytes();
        for(int loop = 0; loop < bufferBytes.length; loop++){
            
            if(bufferBytes[loop]==nameBytes[0]){
                String bankHeader = new String(bufferBytes,loop,4);
                if(bankHeader.compareTo(name)==0){
                    System.out.println("----> found bank "
                    + name + " in position " + loop);
                    int startIndex = loop + 4;
                    int number = bosBCS.getInt(startIndex + 4);
                    int ncols  = bosBCS.getInt(startIndex + 8);
                    int nrows  = bosBCS.getInt(startIndex + 12);
                    int nwords = bosBCS.getInt(startIndex + 24);
                    System.out.println("----> found bank "
                    + name + " in position " + loop 
                    + " rows = " + nrows + " cols = "
                    + ncols + "  number = " + number + " words = "
                    + nwords);
                }
            }
        }
    }
    
    
    int getBankIndex(String bank,int nr){
        int index = -1;
        byte[] bufferBytes = bosBCS.array();
        byte[] nameBytes   = bank.getBytes();
        for(int loop = 0; loop < bufferBytes.length; loop++){
            if(bufferBytes[loop]==nameBytes[0] && loop<bufferBytes.length-24){
                String bankHeader = new String(bufferBytes,loop,4);
                int number = bosBCS.getInt(loop + 8);
                if(number==nr && bankHeader.compareTo(bank)==0) return loop;
            }
        }
        return index;
    }
    
    @Override
    public String[] getColumnList(String bank_name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataDictionary getDictionary() {
        return bankDictionary;
    }

    @Override
    public void appendBank(DataBank bank) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasBank(String name) {
        //int index = this.getBankIndex(name, 0);
        //if(index>=0) return true;
        String __bankname   = name;
        int    __banknumber = 0;
        if(name.contains(":")==true){
            String[] tokens = name.split(":");
            __bankname = tokens[0];
            __banknumber = Integer.parseInt(tokens[1]);
        }
        return this.checkBankConsistency(__bankname,__banknumber);
    }

    @Override
    public DataBank getBank(String bank_name) {
        
        //System.out.println("---> Looking for " + bank_name +
        //        " total size = " + bosBCS.array().length);
        String __bankname   = bank_name;
        int    __banknumber = 0;
        if(bank_name.contains(":")==true){
            String[] tokens = bank_name.split(":");
            __bankname = tokens[0];
            __banknumber = Integer.parseInt(tokens[1]);
        }
        BosDataDescriptor desc = (BosDataDescriptor) 
                bankDictionary.getDescriptor(__bankname);
        
        int index = this.getBankIndex(__bankname, __banknumber);
        BosBankHeader header = new BosBankHeader(index);
        BosDataBank bank = new BosDataBank(desc);
        
        if(index>0){
            int nwords = bosBCS.getInt(header.NWORDS_WORD_INDEX);
            int nrows  = bosBCS.getInt(header.NROWS_WORD_INDEX);
            int ncols  = bosBCS.getInt(header.NCOLS_WORD_INDEX);
            
            //System.out.println("-----> found bank [] = " + bank_name
            //+  "  position = " + index + " words = " + nwords + 
            //"  nrows = " + nrows + "  total size = " + bosBCS.array().length);
            int dataoffset = (bosBCS.getInt(header.NDATA_START_INDEX)-1)*4;
            int banksize = desc.getProperty("banksize");
            int dataFirstByte = index + dataoffset;
            /*
            System.out.println("[GETBANK:DEBUG] ----> " + bank_name 
                    + " index  = " + index 
                    + " offset = " + dataoffset
                    + " rows = " + nrows 
                    + " cosl = " + ncols
                    + " blen = " + bosBCS.array().length
                    + " nwords = " + nwords 
                    + " bsize = " + banksize
                    + " firstbyte = " + dataFirstByte);            
            */
            String[] entryNames = desc.getEntryList();
            for(String entry : entryNames){
                if(desc.getProperty("type", entry)==3){                    
                    int entry_offset = desc.getProperty("offset", entry);
                    int[] int_data = new int[nrows];
                    //short[] short_data = new short[nrows];
                    //System.out.println("----> Filling entry " + entry + " size = "
                    //+ nrows + " index " + index + " first byte = " +
                    //dataFirstByte + " offset = " + entry_offset);
                    for(int loop = 0; loop < nrows; loop++){

                        int entry_index = entry_offset + loop*banksize + dataFirstByte;
                        //System.out.println("[]--> filling entry int " + entry + 
                        //        " offset = " + entry_offset +
                        //        " position " + entry_index + " loop = " + loop);
                        //System.out.println("-------------> loop " + loop 
                        //+ " banksize = " + banksize + "  index = " + entry_index);
                        int_data[loop] = bosBCS.getInt(entry_index);
                    }
                    bank.setInt(entry, int_data);
                }
                
                if(desc.getProperty("type", entry)==5){
                    int entry_offset = desc.getProperty("offset", entry);
                    float[] float_data = new float[nrows];
                    //short[] short_data = new short[nrows];
                    //System.out.println("----> Filling entry " + entry + " size = "
                    //+ nrows + " index " + index + " first byte = " +
                    //dataFirstByte + " offset = " + entry_offset);
                    for(int loop = 0; loop < nrows; loop++){
                        int entry_index = entry_offset + loop*banksize + dataFirstByte;
                       /*
                        System.out.println("[]--> filling entry float " + entry + 
                                " position " + entry_index + " loop = " + loop);
                               */
                        float_data[loop] = bosBCS.getFloat(entry_index);
                    }
                    bank.setFloat(entry, float_data);
                }                
            }
        }
        //int rowlen = 
                //int[] pid  = new int[nrows];
                //int dataStartIndex = ;
                //for()
        return bank;
    }

    @Override
    public void getBank(String bank_name, DataBank bank) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[] getDouble(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDouble(String path, double[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendDouble(String path, double[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float[] getFloat(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFloat(String path, float[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendFloat(String path, float[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] getInt(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setInt(String path, int[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendInt(String path, int[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public short[] getShort(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setShort(String path, short[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendShort(String path, short[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ByteBuffer getEventBuffer() {
        return bosBCS;
    }

    @Override
    public void setProperty(String property, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getProperty(String property) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] getByte(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setByte(String path, byte[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendByte(String path, byte[] arr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appendBanks(DataBank... bank) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
