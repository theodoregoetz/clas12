/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.raw;

/**
 *
 * @author gavalian
 */
public class RawDataTDC {
    
    public Integer CRATE = 0;
    public Integer SLOT = 0;
    public Integer CHANNEL = 0;
    public Integer EDGE    = 0;
    public Integer TDC     = 0;
    
    public RawDataTDC(int crate){
        this.CRATE = crate;
    }
    
    public final void setData(Integer data){
        this.SLOT     = DataUtilities.getInteger(data, 27,31);
        this.CHANNEL  = DataUtilities.getInteger(data, 19,25);
        this.EDGE  = DataUtilities.getInteger(data, 26,26);
        this.TDC      = DataUtilities.getInteger(data, 0,18);
    }
    
    public int getCrate(){ return this.CRATE;}
    public int getSlot(){ return this.SLOT;}
    public int getChannel(){ return this.CHANNEL;}
    public int getEdge(){ return this.EDGE;}
    public int getTDC(){ return this.TDC;}
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        System.out.println(String.format("C/S/C [ %4d %4d %4d ]  EDGE/TDC [%5d %6d]", 
                this.CRATE,this.SLOT,this.CHANNEL,this.EDGE,this.TDC));
        return str.toString();
    }
}
