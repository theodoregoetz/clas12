/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clas12.raw;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class ADCData {
    Integer slot = 0;
    Integer trigger = 0;
    Long    timestamp = (long) 0;    
    
    TreeMap<Short,ADCChannelRaw>  rawData = new TreeMap<Short,ADCChannelRaw>();
    
    public ADCData(Integer s, Integer t, Long time){
        this.slot    = s;
        this.trigger = t;
        this.timestamp = time;
    }
    
    public void addChannel(int channel){
        Short chan = (short) channel;
        rawData.put(chan, new ADCChannelRaw(channel));
    }
    
    public void addChannel(ADCChannelRaw ch){
        int channel = ch.channel;
        rawData.put((short) channel, ch);
    }
    
    
    public boolean hasChannel(int channel){
        short chan = (short) channel;
        return rawData.containsKey(chan); 
    }
    
    public ADCChannelRaw  getChannel(int channel){
        return rawData.get((short) channel);
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("ADC SLOT %5d TRIGGER %5d TIME %12d", 
                this.slot, this.trigger, this.timestamp));
        for(Map.Entry<Short,ADCChannelRaw> entry : rawData.entrySet()){
            str.append(entry.getValue().toString());
            str.append("\n");
        }
        return str.toString();
    }
}
