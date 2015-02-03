/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.func;

import org.jlab.data.graph.DataSetXY;

/**
 *
 * @author gavalian
 */
public class RandomFunc {
    private DataSetXY dataFunc = null;
    public RandomFunc(F1D func){
        DataSetXY xy    = func.getDataSet(200);
        dataFunc  = xy.getDataSetRieman();
    }
    
    public double random(){
        double number = Math.random();
        int bin  = dataFunc.getDataY().findBin(number);
        double xlow = dataFunc.getDataX().getLowEdge(bin);
        double xhi  = dataFunc.getDataX().getHighEdge(bin);
        return xlow + Math.random()*(xhi-xlow);
    }
}
