/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clas12.raw;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class EvioRawDataBank {
    private Integer slotID = 0;
    private Integer trigger = 0;
    private Long    timestamp = (long) 0;
    //private Integer channel = 0;
    
    private TreeMap<Integer, ArrayList<RawData> >  bankData = new 
        TreeMap<Integer, ArrayList<RawData> >();
    
    public EvioRawDataBank(Integer slot, Integer trig, Long time){
        this.timestamp = time;
        this.slotID = slot;
        this.trigger = trig;        
    }
    
    public void addChannel(Integer channel){
        bankData.put(channel, new ArrayList<RawData>());
    }
    
    public void addData(Integer channel, RawData data){
        bankData.get(channel).add(data);
    }
    
    public int getHits(int channel){
        if(bankData.containsKey(channel)==false) return 0;
        return bankData.get(channel).size();
    }
    
    public ArrayList<Integer>  getChannelList(){
        ArrayList<Integer> channels = new ArrayList<Integer>();
        for(Map.Entry<Integer, ArrayList<RawData>> entry : bankData.entrySet()){
            channels.add(entry.getKey());
        }
        return channels;
    }
    
    public ArrayList<RawData>  getData(int channel){
        if(bankData.containsKey(channel)==false) return new ArrayList<RawData>();
        return bankData.get(channel);
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("[RAW Data Bank] SLOT = %4d , TRIGGER = %8d , TIME = %18d\n",
                this.slotID,this.trigger,this.timestamp));
        for(Map.Entry<Integer, ArrayList<RawData>> entry : bankData.entrySet()){
            str.append(String.format("\t CHANNEL : %4d\n",entry.getKey()));
            int counter = 0;
            for(RawData rd : entry.getValue()){
                str.append(String.format("\t\t --> %3d : %s\n",counter,
                        rd.toString()));
                counter++;
            }
        }
        return str.toString();
    }
}
