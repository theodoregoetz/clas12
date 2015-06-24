/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas.detector;

/**
 *
 * @author gavalian
 */
public class DetectorDescriptor {

    private DetectorType  detectorType = DetectorType.UNDEFINED;
    private Integer hw_CRATE     = 0;
    private Integer hw_SLOT      = 0;
    private Integer hw_CHANNEL   = 0;
    private Integer dt_SECTOR    = 0;
    private Integer dt_LAYER     = 0;
    private Integer dt_COMPONENT = 0;
    
    public DetectorDescriptor(){
        
    }
    
    public DetectorDescriptor(DetectorType type){
        this.detectorType = type;
    }
    
    public DetectorDescriptor(String name){
        this.detectorType = DetectorType.getType(name);
    }
        
    public DetectorDescriptor getCopy(){
        DetectorDescriptor newDesc = new DetectorDescriptor(this.detectorType);
        newDesc.setCrateSlotChannel(this.getCrate(), this.getSlot(), this.getChannel());
        newDesc.setSectorLayerComponent(this.getSector(), this.getLayer(), this.getComponent());
        return newDesc;
    }
    
    public int getCrate(){ return this.hw_CRATE;}
    public int getChannel() { return this.hw_CHANNEL;}
    public int getComponent(){ return this.dt_COMPONENT;}
    public int getLayer(){ return this.dt_LAYER;}
    public int getSlot(){ return this.hw_SLOT;}
    public int getSector(){return this.dt_SECTOR;}
    public DetectorType getType(){ return this.detectorType;}
    
    public final void setType(DetectorType type){
        this.detectorType = type;
    }
    
    public final void setCrateSlotChannel(int crate, int slot, int channel){
        this.hw_CRATE   = crate;
        this.hw_SLOT    = slot;
        this.hw_CHANNEL = channel;
    }
    
    public final void setSectorLayerComponent(int sector, int layer, int comp){
        this.dt_SECTOR = sector;
        this.dt_LAYER  = layer;
        this.dt_COMPONENT = comp;
    }
    
    @Override
    public String toString(){
        return String.format("D [%6s ] C/S/C [%4d %4d %4d ]  S/L/C [%4d %4d %4d ]", 
                this.detectorType.getName(),
                this.hw_CRATE,this.hw_SLOT,this.hw_CHANNEL,
                this.dt_SECTOR,this.dt_LAYER,this.dt_COMPONENT);
    }
}
