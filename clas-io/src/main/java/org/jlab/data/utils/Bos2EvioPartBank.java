/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.data.utils;

import java.util.ArrayList;
import java.util.TreeMap;
import org.jlab.bos.clas6.BosDataBank;
import org.jlab.bos.clas6.BosDataEvent;
import org.jlab.evio.clas12.EvioDataBank;
import org.jlab.evio.clas12.EvioDataDictionary;
import org.jlab.evio.clas12.EvioFactory;

/**
 *
 * @author gavalian
 */

public class Bos2EvioPartBank {
    
    private TreeMap<String,EvioDataBank>  evioDataBanks = new TreeMap<String,EvioDataBank>();
    private EvioDataDictionary            dictionary    = null;
    public Bos2EvioPartBank(){
        dictionary = EvioFactory.getDictionary();
    }
    
    public void clear(){
        this.evioDataBanks.clear();
    }
    
    public void processBosEvent(BosDataEvent event){
        evioDataBanks.clear();
        this.addParticleBank(event);
    }
    
    public void addParticleBank(BosDataEvent event){
        
        if(event.hasBank("PART:1")==true&&event.hasBank("TBID:1")){
            BosDataBank  partBank = (BosDataBank) event.getBank("PART:1");
            BosDataBank  tbidBank = (BosDataBank) event.getBank("TBID:1");
            int nrows = partBank.rows();
            EvioDataBank  evioPART = (EvioDataBank) dictionary.createBank("EVENTHB::particle",nrows);
            for(int loop = 0; loop < nrows; loop++){
                //int pid = partBank.getInt("pid")[loop];                
                float q = partBank.getFloat("q")[loop];
                float energy = partBank.getFloat("E")[loop];
                float px     = partBank.getFloat("px")[loop];
                float py     = partBank.getFloat("py")[loop];
                float pz     = partBank.getFloat("pz")[loop];
                float mass2  = energy*energy - (px*px+py*py+pz*pz);
                int  charge = (int) 0;
                if(q>0) charge = (int) 1;
                if(q<0) charge = (int) -1;
                
                evioPART.setInt("pid", loop,partBank.getInt("pid")[loop]);
                evioPART.setFloat("px", loop,partBank.getFloat("px")[loop]);
                evioPART.setFloat("py", loop,partBank.getFloat("py")[loop]);
                evioPART.setFloat("pz", loop,partBank.getFloat("pz")[loop]);
                evioPART.setFloat("vx", loop,partBank.getFloat("x")[loop]);
                evioPART.setFloat("vy", loop,partBank.getFloat("y")[loop]);
                evioPART.setFloat("vz", loop,partBank.getFloat("z")[loop]);
                evioPART.setFloat("chi2pid", loop,partBank.getFloat("qpid")[loop]);
                evioPART.setFloat("mass", loop,mass2);
                evioPART.setInt("charge", loop, charge);
                evioPART.setInt("status", loop, 1);
                
            }
            //partBank.show();
            evioPART.show();
            tbidBank.show();
        }
    }
    
}
