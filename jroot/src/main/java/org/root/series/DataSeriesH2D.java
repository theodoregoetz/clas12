/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.series;


import java.awt.Color;
import java.awt.Graphics2D;
import org.root.attr.ColorPalette;
import org.root.base.IDrawableDataSeries;
import org.root.func.F1D;
import org.root.func.RandomFunc;
import org.root.histogram.H2D;
import org.root.pad.GraphAxis;

/**
 *
 * @author gavalian
 */
public class DataSeriesH2D implements IDrawableDataSeries {
    private H2D seriesHistogram = new H2D();
    ColorPalette palette = new ColorPalette();
    
    public DataSeriesH2D(){
        
    }
    
    public DataSeriesH2D(H2D hist){
        this.seriesHistogram = hist;
    }
    public void generateRandom(){
        this.seriesHistogram.set(200, 0.0, 5.0, 200, 0.0, 5.0);
         F1D func = new F1D("gaus",0.0,5.0);
        func.parameter(0).set( 200.0, 0.0, 200.0);
        func.parameter(1).set(   2.5, 0.0, 200.0);
        func.parameter(2).set(   0.75, 0.0, 200.0);
        RandomFunc rand = new RandomFunc(func);
        for(int loop = 0; loop < 1000000; loop++){
            double x = rand.random();
            double y = rand.random();
            this.seriesHistogram.fill(x, y);
        }
    }
    
    @Override
    public double getMinX() {
        return this.seriesHistogram.getXAxis().min();
    }

    @Override
    public double getMaxX() {
        return this.seriesHistogram.getXAxis().max();
    }

    @Override
    public double getMinY() {
        return this.seriesHistogram.getYAxis().min();
    }

    @Override
    public double getMaxY() {
        return this.seriesHistogram.getYAxis().max();
    }

    @Override
    public void draw(GraphAxis xaxis, GraphAxis yaxis, Graphics2D g2d) {
        double hmax = this.seriesHistogram.getMaximum();

        for(int loopX = 0; loopX < this.seriesHistogram.getXAxis().getNBins(); loopX++){
            for(int loopY = 0; loopY < this.seriesHistogram.getYAxis().getNBins(); loopY++){
                
                double bwx = this.seriesHistogram.getXAxis().getBinWidth(loopX)*0.5;
                double bwy = this.seriesHistogram.getYAxis().getBinWidth(loopY)*0.5;
                
                double xpoint = xaxis.getTranslatedCoordinate(
                        this.seriesHistogram.getXAxis().getBinCenter(loopX) - bwx);
                double ypoint = yaxis.getTranslatedCoordinate(
                        this.seriesHistogram.getYAxis().getBinCenter(loopY) - bwy);
                double xnext = xaxis.getTranslatedCoordinate(
                        this.seriesHistogram.getXAxis().getBinCenter(loopX)+bwx);
                double ynext = yaxis.getTranslatedCoordinate(
                        this.seriesHistogram.getYAxis().getBinCenter(loopY) + bwy);
                
                double dataValue = this.seriesHistogram.getBinContent(loopX, loopY);
                double colorFraction = (dataValue/hmax);
                //System.err.println("color = " + colorFraction);
                int nc = (int) colorFraction;
                if(nc>255) nc = 255;
                if(nc<=0) nc = 1;
                g2d.setColor(palette.getRange(colorFraction));
                //if(dataValue==0)
                //    g2d.setColor(new Color(80,80,80));
                //System.err.println( (int) xpoint + " " + (int) ypoint 
                //+ " " + (int) xnext + " " + (int) ynext);
                g2d.fillRect((int) xpoint, (int) ynext,
                        (int) (xnext-xpoint)+1,
                        (int) (ypoint-ynext)+1
                        );
            }
        }
        g2d.setColor(Color.black);
    }

    @Override
    public Object dataObject() {
        return this.seriesHistogram;
    }

    @Override
    public IDrawableDataSeries fit(String function, String options) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String[] getStatText() {
        return new String[0];
    }
    
}
