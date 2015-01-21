/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.group;

import java.util.ArrayList;
import org.jlab.scichart.base.ScAxisPair;

/**
 *
 * @author gavalian
 */
public class ScNodeSeries2D extends ScNodeSeries {
    //private ArrayList<Double> xAxisDivisions = new ArrayList<Double>();
    //private ArrayList<Double> yAxisDivisions = new ArrayList<Double>();
    private ArrayList<Double[]> xyData       = new ArrayList<Double[]>();
    private ScAxisPair          seriesAxis   = new ScAxisPair();
    private ScAxisPair          groupAxis    = new ScAxisPair();
    
    public ScNodeSeries2D(){
        
    }
    
    public void setData2D(double[] x, double[] y, double[][] xy){
        this.setData(x, y);
        for(int loop = 0; loop < x.length; loop++){
            
        }
    }
    
    
    
}
