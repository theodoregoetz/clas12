/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.series;

import java.awt.Graphics2D;
import org.root.attr.MarkerPainter;
import org.root.base.IDrawableDataSeries;
import org.root.data.DataSetXY;
import org.root.data.DataVector;
import org.root.fitter.DataFitter;
import org.root.func.DataSetFactory;
import org.root.func.F1D;
import org.root.pad.GraphAxis;

/**
 *
 * @author gavalian
 */
public class DataSeriesPoints implements IDrawableDataSeries {
    
    private DataSetXY  dataSet = new DataSetXY();
    private MarkerPainter markerPainter = new MarkerPainter();
    
    public DataSeriesPoints(){
        
    }
    
    public DataSeriesPoints(double[] x, double[] y){
        for(int loop = 0; loop < x.length; loop++){
            dataSet.add(x[loop], y[loop]);
        }
    }
    
    public void generateRandom(){
        DataVector  xvec = DataSetFactory.uniformAxis(50, 0.8, 6.2);
        DataVector  yvec = DataSetFactory.gausFunction(xvec, 200, 3.5, 0.6);
        this.dataSet = new DataSetXY();
        for(int loop = 0; loop < xvec.getSize(); loop++){
            this.dataSet.add(xvec.getValue(loop), yvec.getValue(loop));
        }
    }
    
    @Override
    public void draw(GraphAxis xaxis, GraphAxis yaxis, Graphics2D g2d){
        for(int loop = 0; loop < dataSet.getDataX().getSize(); loop++){
            double xcoord = xaxis.getTranslatedCoordinate(dataSet.getDataX().getValue(loop));
            double ycoord = yaxis.getTranslatedCoordinate(dataSet.getDataY().getValue(loop));
            markerPainter.drawMarker(g2d, (int) xcoord,(int) ycoord,2,8);
            //g2d.fillRect((int) xcoord, (int) ycoord, 5, 5);
        }
    }

    @Override
    public Object dataObject() {
        return dataSet;
    }

    @Override
    public double getMinX() {
        return dataSet.getDataX().getMin();
    }

    @Override
    public double getMaxX() {
        return dataSet.getDataX().getMax();
    }

    @Override
    public double getMinY() {
        return dataSet.getDataY().getMin();
    }

    @Override
    public double getMaxY() {
        double min = this.getMinY();
        double max = dataSet.getDataY().getMax();
        return max + (max-min)*0.15;
    }

    @Override
    public IDrawableDataSeries fit(String function, String options) {
        F1D func = new F1D(function,this.getMinX(),this.getMaxX());
        if(function.contains("gaus")){
            
            //func.parameter(0).set(200, 0.0, 50000.0);
            func.parameter(1).set(3.0, 0.0, 200.0);
            func.parameter(2).set(0.2, 0.0, 1000.0);
        }
        DataFitter.fit(dataSet, func);
        DataSeriesFunc dataFunc = new DataSeriesFunc(func);
        return dataFunc;
    }
    
}
