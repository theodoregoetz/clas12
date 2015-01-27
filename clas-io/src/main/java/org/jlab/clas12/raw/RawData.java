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
public class RawData {
    private Integer MODE = 1;
    private final ArrayList<Object> dataArray = new  ArrayList<Object>();

    public  RawData(Short tdc, Integer adc, Short pulseMin, Short pulseMax){
        MODE = 7;
        dataArray.add(tdc);
        dataArray.add(adc);
        dataArray.add(pulseMin);
        dataArray.add(pulseMax);
    }
    
    public RawData(Short tdc, Integer adc){
        MODE = 3;
        dataArray.add(tdc);
        dataArray.add(adc);
    }
    
    public RawData(Integer channel, Short tdc, Short adc){
        MODE = 4;
        dataArray.add(channel);
        dataArray.add(tdc);
        dataArray.add(adc);
    }
    
    public RawData(short[] shortData){
        this.set(shortData);        
    }
    
    public final void set(short[] shortData){
        MODE = 1;
        dataArray.clear();
        for(int loop = 0; loop < shortData.length; loop++){
            dataArray.add(shortData[loop]);
        }
    }
    
    public Integer getADC(){
        return (Integer) dataArray.get(1);
    }
    
    public Short getTDC(){
        return (Short) dataArray.get(0);
    }
    
    public Short getPulseMin(){
        if(this.MODE==3){
            System.err.println("[RawData] ** warning ** current data is in mode 3 "
            + " does not contain the pulse minimum");
            return (short) 0;
        }
        return (Short) dataArray.get(2);
    }
    
    public Short getPulseMax(){
        if(this.MODE==3){
            System.err.println("[RawData] ** warning ** current data is in mode 3 "
            + " does not contain the pulse maximum");
            return (short) 0;
        }
        return (Short) dataArray.get(3);
    }
    
    public int mode() { return MODE;}
    
    public int dataSize(){
        return dataArray.size();
    }
    
    public Object  getObject(int index){
        return dataArray.get(index);
    }
    
    public Integer getData(int index){
        return (Integer) dataArray.get(index);
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("PULSE (mode = %2d) : ", MODE ));
        if(MODE==3){
            str.append(String.format("TDC [%8d]  ADC [%8d]", this.getTDC(),this.getADC() ));
        }
        if(MODE==7){
            str.append(String.format("TDC [%8d] ADC [%8d] PULSE [ min = %8d max = %8d]",
                    this.getTDC(),this.getADC(),
                    this.getPulseMin(),this.getPulseMax()));
        }
        
        if(MODE==4){
            str.append(String.format("%8d %8d %8d", 
                    (Integer) dataArray.get(0),
                    (Short) dataArray.get(1),
                    (Short) dataArray.get(2)            
            ));
        }
        
        if(MODE==1){
            for(int loop = 0; loop < dataArray.size(); loop++){
                str.append(String.format("%8d", (Short) dataArray.get(loop)));
            }
        }
        return str.toString();
    }
}
