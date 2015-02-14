/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.series;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import org.root.base.IDrawableDataSeries;
import org.root.data.DataSetXY;
import org.root.fitter.DataFitter;
import org.root.func.F1D;
import org.root.func.RandomFunc;
import org.root.histogram.H1D;
import org.root.pad.GraphAxis;

/**
 *
 * @author gavalian
 */
public class DataSeriesH1D implements IDrawableDataSeries {
    private H1D dataHistogram = new H1D("H",10,0.0,1.0);
    
    public DataSeriesH1D(){
        
    }
    
    public DataSeriesH1D(H1D hist){
        this.dataHistogram = hist;
    }
    
    public void generateRandom(){
        F1D func = new F1D("gaus+p2",0.0,120.0);
        func.parameter(0).set( 4000.0, 0.0, 200.0);
        func.parameter(1).set(   60.0, 0.0, 200.0);
        func.parameter(2).set(    5.0, 0.0, 200.0);
        func.parameter(3).set(   15.0, 0.0, 200.0);
        func.parameter(4).set(   25.0, 0.0, 200.0);
        //func.parameter(5).set(   5.0, 0.0, 200.0);
        
        RandomFunc rand = new RandomFunc(func);
        dataHistogram.set(240, 0.0, 120.0);
        for(int loop = 0; loop < 8000; loop++){
            dataHistogram.fill(rand.random());
        }
    }
    
    @Override
    public double getMinX() {
        return this.dataHistogram.getxAxis().min();
    }

    @Override
    public double getMaxX() {
        return this.dataHistogram.getxAxis().max();
    }

    @Override
    public double getMinY() {
        return 0.0;
    }

    @Override
    public double getMaxY() {
        return this.dataHistogram.getBinContent(this.dataHistogram.getMaximumBin());
    }

    @Override
    public void draw(GraphAxis xaxis, GraphAxis yaxis, Graphics2D g2d) {
        GeneralPath path = new GeneralPath();
        double xc = xaxis.getTranslatedCoordinate(this.dataHistogram.getxAxis().getBinCenter(0));
        double yc = yaxis.getTranslatedCoordinate(this.dataHistogram.getBinContent(0));
        path.moveTo(xc, yc);
        for(int loop = 1; loop < this.dataHistogram.getxAxis().getNBins(); loop++){
            xc = xaxis.getTranslatedCoordinate(this.dataHistogram.getxAxis().getBinCenter(loop));
            yc = yaxis.getTranslatedCoordinate(this.dataHistogram.getBinContent(loop));
            path.lineTo(xc, yc);
        }
        g2d.draw(path);
    }

    @Override
    public Object dataObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IDrawableDataSeries fit(String function, String options) {        
        F1D fitfunc = new F1D(function,this.getMinX(),this.getMaxX());
        DataSetXY  data = this.dataHistogram.getDataSet();
        double mean = this.dataHistogram.getMean();
        double rms  = this.dataHistogram.getRMS();
        //System.out.println("mean = " + mean + "  rms = " + rms);
        fitfunc.parameter(0).set(400, 0.0, 5000000);
        fitfunc.parameter(1).set(mean,this.dataHistogram.getxAxis().min(),
                this.dataHistogram.getxAxis().max());
        fitfunc.parameter(2).set(rms,0.0,5.0*rms);
        fitfunc.show();
        DataFitter.fit(data, fitfunc);
        fitfunc.show();
        DataSeriesFunc  dataFunc = new DataSeriesFunc(fitfunc);
        return dataFunc;
    }    
}
