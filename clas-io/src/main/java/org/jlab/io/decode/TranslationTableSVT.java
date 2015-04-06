/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.io.decode;

import java.util.ArrayList;
import org.jlab.clas12.raw.RawDataEntry;

/**
 *
 * @author gavalian
 */
public class TranslationTableSVT extends TranslationTable {
    
    public TranslationTableSVT(){
        
    }
    
    @Override
    public void translateEntries(ArrayList<RawDataEntry>  rawdata){
        SVTDataRecord  record = new SVTDataRecord();
        for(RawDataEntry entry : rawdata){
            TranslationTableEntry conversion = this.getEntry(entry.getCrate(),
                    entry.getSlot(),entry.getSVTHalf());
            if(conversion!=null){
                record.init(conversion.sector, conversion.layer, entry.getSVTHalf(), 
                        entry.getSVTChipID(), entry.getSVTChannel());
                entry.setSectorLayerComponent(record.SECTOR, record.LAYER,
                        record.STRIP);
            } else {
                System.err.println(" ERROR : cant find entry " + entry.getCrate()
                        + "  " + 
                    entry.getSlot() + "  " + entry.getSVTHalf());
            }
        }
    }
}
