/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.func;

import org.jlab.data.graph.DataVector;

/**
 *
 * @author gavalian
 */
public class DataSetFactory {
    
    public static DataVector uniformAxis(int bins, double min, double max){
        DataVector dataAxis = new DataVector();
        double width = (max-min)/bins;
        for(int loop = 0; loop < bins; loop++){
            dataAxis.add(min + 0.5*width + loop * width);
        }
        return dataAxis;
    }
    
    public static DataVector gausFunction(DataVector axis, double amp,
            double mean, double sigma){
        DataVector gaus = new DataVector();
        for(int loop = 0; loop < axis.getSize(); loop++){
            double diff = mean - axis.getValue(loop);
            double value = amp*Math.exp(-diff*diff/(2.0*sigma*sigma));
            gaus.add(value);
        }
        return gaus;
    }
}
