/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.evio.clas12;

import org.jlab.data.io.DataEntryType;

/**
 *
 * @author gavalian
 */
public class EvioDataDescriptorEntry {
    String  section;
    String  name;
    Integer tag;
    Integer num;
    DataEntryType type;
    
    public EvioDataDescriptorEntry(String _s,String _n, Integer _t, Integer _nn, String _type)
    {
        section = _s;
        name = _n;
        tag  = _t;
        num  = _nn;
        type = DataEntryType.getType(_type);
    }
}
