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
    private DataSetXY  searchData   = null;
    private DataSetXY  searchSample = null;
    
    public TSpectrum(int niter){
        
    }
    
    public void setPrecision(int np){
        this.nSamples = np;
    }
    
    public void search(H1D h, double nsigma, String options){
        DataSetXY  dataset = h.getDataSet();
        DataVector ycum = dataset.getDataY().getCumulative();
        ycum.divide(ycum.getValue(ycum.getSize()-1));
        this.searchData   = new DataSetXY(dataset.getDataX().getArray(),ycum.getArray());
        this.searchSample = new DataSetXY();
        double stepSize = 1.0/nSamples;
        for(int loop = 0; loop < nSamples; loop++){
            double yvalue = loop*stepSize;
            double xvalue = this.searchData.getIntersectY(yvalue);
            this.searchSample.add(xvalue, dataset.evaluate(xvalue));
        }
        
        double  average = this.calculateAverage();
        for(int loop = 0; loop < this.searchSample.getDataX().getSize()-1; loop++){
            double diff = this.searchSample.getDataX().getValue(loop) - this.searchSample.getDataX().getValue(loop+1);
            System.out.println(loop + " = " + diff );
        }
        
    }
    
    private double calculateAverage(){
        double avg  = 0.0;
        double sum  = 0.0;
        double sumw = 0.0;
        double diff = 0.0;
        for(int loop = 0; loop< this.searchSample.getDataX().getSize()-1;loop++){
            sum  += 1.0;
            diff = this.searchSample.getDataX().getValue(loop) - 
                    this.searchSample.getDataX().getValue(loop+1);
            sumw += Math.sqrt(diff*diff);
            /*
            sum  += this.searchSample.getDataY().getValue(loop);
            sumw += this.searchSample.getDataX().getValue(loop)*
                    this.searchSample.getDataY().getValue(loop);
                    */
        }
        
        if(sum!=0.0) avg = sumw/sum;
        
        System.out.println("AVERAGE = " + avg);
        return avg;
    }
    
    public DataSetXY  getSearchGraph(){
        return this.searchData;
    }
    
    public DataSetXY  getSampleGraph(){
        return this.searchSample;
    }
}
