/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.root.attr.ColorPalette;
import org.root.attr.MarkerPainter;

/**
 *
 * @author gavalian
 */
public class AbsDataSetDraw {
    
    public static void drawAxisFrame(AxisRegion axis, Graphics2D g2d, 
            int startX, int startY, int gWidth, int gHeight){
        
        Color background  = ColorPalette.getColor(axis.getAttributes().getAsInt("background-color"));
        Color linecolor   = ColorPalette.getColor(axis.getAttributes().getAsInt("line-color"));
        Integer lineWidth = axis.getAttributes().getAsInt("line-width");
       
        g2d.setColor(background);
        g2d.fillRect(startX,startY,gWidth,gHeight);
        g2d.setColor(linecolor);
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.drawRect(axis.getFrame().x, axis.getFrame().y, 
                axis.getFrame().width, axis.getFrame().height);
        
    }
    
    public static void drawDataSetAsGraph(AxisRegion axis, Graphics2D g2d, IDataSet ds,
            int startX, int startY, int gWidth, int gHeight){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        MarkerPainter  mPainter = new MarkerPainter();
        int markerStyle = ds.getAttributes().getAsInt("marker-style");
        int markerColor = ds.getAttributes().getAsInt("marker-color");
        int markerSize  = ds.getAttributes().getAsInt("marker-size");
        int npoints = ds.getDataSize();
        System.out.println("Graph points = " + npoints);
        for(int loop = 0; loop < npoints; loop++){
            if(axis.getDataRegion().contains(ds.getDataX(loop),ds.getDataY(loop)) == true){
                double xr = axis.getDataRegion().fractionX(ds.getDataX(loop));
                double yr = axis.getDataRegion().fractionY(ds.getDataY(loop));
                double xf = axis.getFramePointX(xr);
                double yf = axis.getFramePointY(yr);
                System.out.println(" POINT = " + loop + "  " + xr + "  " + yr
                + " " + xf + " " + yf);
                mPainter.drawMarker(g2d, (int) xf, (int) yf, markerStyle, 
                        markerSize, markerColor, 1,1);

            }
        }
    }
    
    
    
}
