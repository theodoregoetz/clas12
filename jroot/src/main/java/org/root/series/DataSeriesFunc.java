/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.series;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import org.root.base.IDrawableDataSeries;
import org.root.data.DataSetXY;
import org.root.func.F1D;
import org.root.pad.GraphAxis;

/**
 *
 * @author gavalian
 */
public class DataSeriesFunc implements IDrawableDataSeries {
    private F1D seriesFunc = new F1D("p1");
    private DataSetXY funcDataset = null;
    
    public DataSeriesFunc(F1D func){
        this.seriesFunc  = func;
        this.funcDataset = func.getDataSet(200);
    }
    
    @Override
    public double getMinX() {
        return seriesFunc.getMin();
    }

    @Override
    public double getMaxX() {
        return seriesFunc.getMax();
    }

    @Override
    public double getMinY() {
        return this.funcDataset.getDataY().getMin();
    }

    @Override
    public double getMaxY() {
        return this.funcDataset.getDataY().getMax();
    }

    @Override
    public void draw(GraphAxis xaxis, GraphAxis yaxis, Graphics2D g2d) {
        GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        double xcoord = xaxis.getTranslatedCoordinate(this.funcDataset.getDataX().getValue(0));
        double ycoord = yaxis.getTranslatedCoordinate(this.funcDataset.getDataY().getValue(0));
        path.moveTo(xcoord, ycoord);
        for(int loop = 1; loop < this.funcDataset.getDataX().getSize(); loop++){
            xcoord = xaxis.getTranslatedCoordinate(this.funcDataset.getDataX().getValue(loop));
            ycoord = yaxis.getTranslatedCoordinate(this.funcDataset.getDataY().getValue(loop));
            path.lineTo(xcoord, ycoord);
        }
        
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(path);
    }

    @Override
    public Object dataObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IDrawableDataSeries fit(String function, String options) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
