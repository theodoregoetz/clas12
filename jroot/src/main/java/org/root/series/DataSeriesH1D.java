/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.series;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import org.root.attr.ColorPalette;
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
        //double length = this.dataHistogram.getxAxis().max() - this.dataHistogram.getxAxis().min();
        return this.dataHistogram.getxAxis().max();
    }

    @Override
    public double getMinY() {
        return 0.0;
    }
    
    @Override
    public double getMaxY() {
        //double length = this.dataHistogram.getxAxis().max() - this.dataHistogram.getxAxis().min();
        double maxC = this.dataHistogram.getBinContent(this.dataHistogram.getMaximumBin());
        double minC = 0;
        return (1.15*maxC);
    }

    @Override
    public void draw(GraphAxis xaxis, GraphAxis yaxis, Graphics2D g2d) {
        int lineWidth = this.dataHistogram.getLineWidth();
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.setColor(ColorPalette.getColor(this.dataHistogram.getLineColor()));
        GeneralPath path = new GeneralPath();
        
        double bw = this.dataHistogram.getxAxis().getBinWidth(0)*0.5;
        double xc = xaxis.getTranslatedCoordinate(this.dataHistogram.getxAxis().getBinCenter(0)-bw);
        double yc = yaxis.getTranslatedCoordinate(this.dataHistogram.getBinContent(0));
        path.moveTo(xc, yc);
        for(int loop = 0; loop < this.dataHistogram.getxAxis().getNBins(); loop++){
            bw = this.dataHistogram.getxAxis().getBinWidth(loop)*0.5;
            xc = xaxis.getTranslatedCoordinate(this.dataHistogram.getxAxis().getBinCenter(loop)+bw);
            yc = yaxis.getTranslatedCoordinate(this.dataHistogram.getBinContent(loop));
            path.lineTo(xc, yc);
            xc = xaxis.getTranslatedCoordinate(this.dataHistogram.getxAxis().getBinCenter(loop)+bw);
            
            if(loop!=this.dataHistogram.getxAxis().getNBins()-1){
                yc = yaxis.getTranslatedCoordinate(this.dataHistogram.getBinContent(loop+1));
            } else {
                yc = yaxis.getTranslatedCoordinate(0.0);
            }
            path.lineTo(xc, yc);
        }        
        
        g2d.draw(path);
        g2d.setColor(Color.BLACK);
        /*
        GeneralPath path = new GeneralPath();
        double bw = this.dataHistogram.getxAxis().getBinWidth(0);
        double xc = xaxis.getTranslatedCoordinate(this.dataHistogram.getxAxis().getBinCenter(0));
        double yc = yaxis.getTranslatedCoordinate(this.dataHistogram.getBinContent(0));
        path.moveTo(xc, yc);
        for(int loop = 1; loop < this.dataHistogram.getxAxis().getNBins(); loop++){
            xc = xaxis.getTranslatedCoordinate(this.dataHistogram.getxAxis().getBinCenter(loop));
            yc = yaxis.getTranslatedCoordinate(this.dataHistogram.getBinContent(loop));
            path.lineTo(xc, yc);
        }
        g2d.draw(path);
        */
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

    public String[] getStatText() {
        String[] labels = new String[3];
        labels[0] = String.format("%-7s %9d", "Entries",this.dataHistogram.getEntries());
        labels[1] = String.format("%-7s %9.4f", "Mean",this.dataHistogram.getMean());
        labels[2] = String.format("%-7s %9.4f", "RMS",this.dataHistogram.getRMS());
        return labels;
    }
}
