/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clas12.raw;

import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class ADCChannelRaw {
    Integer channel  = 0;
    ArrayList<Short> samples = new ArrayList<Short>();
    
    public ADCChannelRaw(int ac){
        this.channel = ac;
    }
    
    public ADCChannelRaw(int ac, short[] adc){
        this.channel = ac;
        
    }
    
    public void set(int ac, short[] adc){
        this.channel = ac;
        this.samples.clear();
        for(int loop =0; loop < adc.length; loop++){
            
        }
    }
    public ArrayList<Short>  getSamples(){
        return samples;
    }
    
    public void reset(){
        samples.clear();
    }
    
    public void add(Short adc){
        samples.add(adc);
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format(" CHAN %4d  SAMPLES %6d", this.channel,
                this.samples.size()));
        return str.toString();
    }
}
