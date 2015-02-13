/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.browser;

import org.jlab.evio.clas12.EvioDataEvent;
import org.root.data.DataVector;

/**
 *
 * @author gavalian
 */
public interface ITreeSelector {
    
    void configure(String config, String options);
    void processEvent(EvioDataEvent event);
    DataVector getDataVector();
    
}
