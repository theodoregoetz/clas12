/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.histogram;

import org.root.data.DataSetXY;
import org.root.data.DataVector;

/**
 *
 * @author gavalian
 */
public class TSpectrum {
    
    private Integer nSamples = 100;
    
    public TSpectrum(int niter){
        
    }
    
    public void setPrecision(int np){
        this.nSamples = np;
    }
    
    public void search(H1D h, double nsigma, String options){
        DataSetXY hdata = h.getDataSet();
        DataVector ycum = hdata.getDataY().getCumulative();
        ycum.divide(ycum.getValue(ycum.getSize()-1));
        
    }
}
