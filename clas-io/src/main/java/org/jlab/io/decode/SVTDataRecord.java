/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.io.decode;

/**
 *
 * @author gavalian
 */
public class SVTDataRecord {
    public Integer HALF   = 0;
    public Integer REGION = 0;
    public Integer CHIPID = 0;
    public Integer SECTOR = 0;
    public Integer CHANNEL = 0;
    public Integer LAYER = 0;
    public Integer STRIP = 0;
    public Integer BCO   = 0;
    public Integer ADC   = 0;
    
    public SVTDataRecord(){
        
    }
    
    public final void init(int sector, int region, int half, int chipid, int channel){
        this.SECTOR = sector;
        this.REGION = region;
        this.HALF   = half;
        this.CHIPID = chipid;
        this.CHANNEL = channel;
                
        //int  correctedChipID = chipid;        
        //if(this.HALF==1){
        //    correctedChipID = chipid - 4;
        //}
        
        int layerOffset = 1;
        //if(chipid>2){
        if(chipid<3){
            layerOffset = 2;
        }
        
        this.STRIP = channel;
        if(chipid%2==0){
            this.STRIP = channel + 128;
        }
        this.STRIP = this.STRIP + 1;
        this.LAYER = (this.REGION-1)*2 + (layerOffset);
    }
    
    public void setData(int _bco, int _adc){
        this.BCO = _bco;
        this.ADC = _adc;
    }
    
    @Override
    public String toString(){
        return String.format("SEC : %3d , REG = %3d , HALF = %2d , CHIPID = %2d , CHANNEL = %4d,  LAYER = %2d , STRIP = %4d",
                this.SECTOR, this.REGION, this.HALF, this.CHIPID, this.CHANNEL,this.LAYER, this.STRIP);
    }
}
