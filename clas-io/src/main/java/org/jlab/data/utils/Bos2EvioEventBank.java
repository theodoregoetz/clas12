/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.data.utils;

import java.util.ArrayList;
import org.jlab.bos.clas6.BosDataBank;
import org.jlab.evio.clas12.EvioDataBank;

/**
 *
 * @author gavalian
 */
public class Bos2EvioEventBank {
    private ArrayList<EvioDataBank>  evioDataBanks = new ArrayList<EvioDataBank>();
    
    public Bos2EvioEventBank(){
        
    }
    
    public void clear(){
        this.evioDataBanks.clear();
    }
    
    public void processBosEvent(BosDataBank bank){
        
    }
}
