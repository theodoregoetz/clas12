/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.series;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
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
    private Integer       drawStyle     = 0;
    
    public DataSeriesPoints(){
        
    }
        
    public DataSeriesPoints(double[] x, double[] y){
        for(int loop = 0; loop < x.length; loop++){
            dataSet.add(x[loop], y[loop]);
        }
    }
    
    public void setDrawStyle(int style){
        this.drawStyle = style;
    }
    
    public void generateRandom(){
        DataVector  xvec = DataSetFactory.uniformAxis(50, 18.0, 35.0);
        DataVector  yvec = DataSetFactory.gausFunction(xvec, 200, 26.0, 1.6);
        this.dataSet = new DataSetXY();
        for(int loop = 0; loop < xvec.getSize(); loop++){
            this.dataSet.add(xvec.getValue(loop), yvec.getValue(loop));
        }
    }
    
    @Override
    public void draw(GraphAxis xaxis, GraphAxis yaxis, Graphics2D g2d){
        if(this.drawStyle==1||this.drawStyle==2){
            g2d.setStroke(new BasicStroke(2));
            GeneralPath path = new GeneralPath();
            double xcoord = xaxis.getTranslatedCoordinate(dataSet.getDataX().getValue(0));
            double ycoord = yaxis.getTranslatedCoordinate(dataSet.getDataY().getValue(0));
            path.moveTo(xcoord,ycoord);
            for(int loop = 0; loop < dataSet.getDataX().getSize(); loop++){
                xcoord = xaxis.getTranslatedCoordinate(dataSet.getDataX().getValue(loop));
                ycoord = yaxis.getTranslatedCoordinate(dataSet.getDataY().getValue(loop));
                path.lineTo(xcoord, ycoord);
            }
            g2d.draw(path);
        } else {            
            for(int loop = 0; loop < dataSet.getDataX().getSize(); loop++){
                double xcoord = xaxis.getTranslatedCoordinate(dataSet.getDataX().getValue(loop));
                double ycoord = yaxis.getTranslatedCoordinate(dataSet.getDataY().getValue(loop));
                markerPainter.drawMarker(g2d, (int) xcoord,(int) ycoord,2,8);
                //g2d.fillRect((int) xcoord, (int) ycoord, 5, 5);
            }
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
            
            double mean = this.getMinX() + 0.5*( this.getMaxX()-this.getMinX());
            func.parameter(0).set(200, 0.0, 500000.0);
            func.parameter(1).set(mean, func.getMin(), func.getMax());
            func.parameter(2).set(0.1*mean, 0.0, 1000.0);
        }
        
        func.show();
        DataFitter.fit(dataSet, func);
        DataSeriesFunc dataFunc = new DataSeriesFunc(func);
        func.show();
        return dataFunc;
    }

    public String[] getStatText() {
        return new String[0];
    }
    
}
