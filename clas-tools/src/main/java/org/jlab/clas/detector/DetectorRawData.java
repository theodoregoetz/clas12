/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.detector;

import java.util.ArrayList;
import org.jlab.clas.tools.utils.DataUtils;

/**
 *
 * @author gavalian
 */
public class DetectorRawData {
    
    private final DetectorDescriptor  descriptor = new DetectorDescriptor();
    private final ArrayList<Object>   detectorData = new ArrayList<Object>();
    private Integer             dataMODE     = 0;
    
    public DetectorRawData(int crate, int slot, int channel){
        this.descriptor.setCrateSlotChannel(crate, slot, channel);
    }
    
    public void setType(DetectorType type){
        this.descriptor.setType(type);
    }
    
    public void set(Short tdc, Integer adc, Short min, Short max){
        this.dataMODE = 7;
        this.detectorData.clear();
        this.detectorData.add(DataUtils.getIntFromShort(tdc));
        this.detectorData.add(adc);
        this.detectorData.add(DataUtils.getIntFromShort(min));
        this.detectorData.add(DataUtils.getIntFromShort(max));
    }
    
    public DetectorDescriptor getDescriptor(){return this.descriptor;}
    
    public int getMode(){
        return this.dataMODE;
    }
    
    public void set(Short tdc){
        this.dataMODE = 10;
        this.detectorData.clear();
        this.detectorData.add(DataUtils.getIntFromShort(tdc));
    }
    
    public int getTDC(){
        if(this.dataMODE==7||this.dataMODE==4||this.dataMODE==10){
            return (Integer) this.detectorData.get(0);
        }
        System.out.println("[DetectorRawData::ERROR] MODE = " + this.dataMODE +
                 "  does not have TDC data.");
        return -1;
    }
    
    public int getADC(){
        if(this.dataMODE==7||this.dataMODE==4||this.dataMODE==10){
            return (Integer) this.detectorData.get(1);
        }
        System.out.println("[DetectorRawData::ERROR] MODE = " + this.dataMODE +
                 "  does not have TDC data.");
        return -1;
    }
    
    public int get(int index){
        if(index<0||index>=this.detectorData.size()) return -1;
        return (Integer) this.detectorData.get(index);
    }
    
    public void setBST(Byte half, Byte channel, Byte tdc, Byte adc){
        this.dataMODE = 4;
        this.detectorData.clear();
        this.detectorData.add(DataUtils.getIntFromByte(tdc));
        this.detectorData.add(DataUtils.getIntFromByte(adc));
        this.detectorData.add(DataUtils.getIntFromByte(half));
        this.detectorData.add(DataUtils.getIntFromByte(channel));        
    }
    
    public void set(short[] pulse){
        this.dataMODE = 1;
        this.detectorData.clear();
        this.detectorData.add(pulse);
    }
    
    public int    getDataSize(){ return this.detectorData.size();}
    
    public Object getDataObject(int index){
        return this.detectorData.get(index);
    }
    
    public int getIntegral(int min, int max){
        int integral = 0;
        if(this.dataMODE==1){
            Short[] array = (Short[]) this.detectorData.get(0);
            for(int loop = min; loop < max; loop++){
                if(loop>=0&&loop<this.detectorData.size()){
                    integral += array[loop];
                }
            }
        }
        return integral;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(this.descriptor.toString());
        str.append(String.format(" MODE [%3d] ",this.dataMODE));
        if(this.dataMODE==7){
            str.append(String.format(" T/A/Min/Max [%8d %8d %8d %8d ]", 
                    this.detectorData.get(0),
                    this.detectorData.get(1),
                    this.detectorData.get(2),
                    this.detectorData.get(3)
            ));
        }
        
        if(this.dataMODE==4){
            str.append(String.format(" H/C/T/A [%8d %8d %8d %8d ]", 
                    this.detectorData.get(2),
                    this.detectorData.get(3),
                    this.detectorData.get(0),
                    this.detectorData.get(1)
            ));
        }
               
        if(this.dataMODE==10){
            str.append(String.format(" TDC [%8d ]", 
                    this.detectorData.get(0)
                    ));
        }
        
        if(this.dataMODE==1){
            str.append(String.format(" PULSE PED/ALL [%8d %8d ]", 
                    this.getIntegral(0, 10),
                    this.getIntegral(0, 200)
                    ));
        }
        return str.toString();
    }
}
