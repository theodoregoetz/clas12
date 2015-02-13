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
public class VariableTreeSelector implements ITreeSelector {
    private DataVector internalVector = new DataVector();
    private String     bankName       = "";
    private String     varName        = "";
    
    public void configure(String config, String options) {
        this.bankName = config;
        this.varName  = options;
    }
    
    public void processEvent(EvioDataEvent event) {
        
    }

    public DataVector getDataVector() {
        return internalVector;
    }
}
