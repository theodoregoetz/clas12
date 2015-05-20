/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.root.attr.ColorPalette;
import org.root.attr.MarkerPainter;

/**
 *
 * @author gavalian
 */
public class AbsDataSetDraw {
    
    public static void drawAxisBackGround(AxisRegion axis, Graphics2D g2d, 
            int startX, int startY, int gWidth, int gHeight){
        Color background  = ColorPalette.getColor(axis.getAttributes().getAsInt("background-color"));
        Color linecolor   = ColorPalette.getColor(axis.getAttributes().getAsInt("line-color"));
        Integer lineWidth = axis.getAttributes().getAsInt("line-width");
       
        g2d.setColor(background);
        g2d.fillRect(startX,startY,gWidth,gHeight);
        g2d.setColor(linecolor);
        
    }
    
    public static void drawText(AxisRegion axis, Graphics2D g2d, LatexText text, 
            int startX, int startY, int gWidth, int gHeight){
        g2d.setColor(ColorPalette.getColor(text.getColor()));
        double x = axis.getFramePointX(text.getX());
        double y = axis.getFramePointY(text.getY());
        g2d.drawString(text.getText().getIterator(),(int) x,(int) y);
    }
    
    public static void drawAxisFrame(AxisRegion axis, Graphics2D g2d, 
            int startX, int startY, int gWidth, int gHeight){
        
        Color background  = ColorPalette.getColor(axis.getAttributes().getAsInt("background-color"));
        Color linecolor   = ColorPalette.getColor(axis.getAttributes().getAsInt("line-color"));
        Integer lineWidth = axis.getAttributes().getAsInt("line-width");
       
        //g2d.setColor(background);
        //g2d.fillRect(startX,startY,gWidth,gHeight);
        g2d.setColor(linecolor);
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.drawRect(axis.getFrame().x, axis.getFrame().y, 
                axis.getFrame().width, axis.getFrame().height);
        
        ArrayList<Double>  ticksX = axis.getAxisX().getCoordinates();
        ArrayList<Double>  ticksY = axis.getAxisY().getCoordinates();
        
        ArrayList<String>  stringX = axis.getAxisX().getCoordinatesLabels();
        ArrayList<String>  stringY = axis.getAxisY().getCoordinatesLabels();
        
        Font  axisFont = new Font("Helvetica",Font.PLAIN,axis.axisLabelSize);
        //Font  axisFont = new Font(Font.SANS_SERIF,Font.PLAIN,axis.axisLabelSize);
        FontMetrics  fm = g2d.getFontMetrics(axisFont);
        g2d.setFont(axisFont);
        
        for(int loop = 0; loop < ticksX.size(); loop++){
            double xr = axis.getDataRegion().fractionX(ticksX.get(loop));
            double xv = axis.getFramePointX(xr);
            double yv = axis.getFrame().height + axis.getFrame().y;
            
            g2d.drawLine((int) xv, (int) yv, (int) xv, (int) yv-10);
            String label = stringX.get(loop);
            int xoff = (int) fm.stringWidth(label)/2;
            int yoff = fm.getHeight();
            g2d.drawString(stringX.get(loop), (int) xv-xoff, (int) yv+yoff);
        }
        //System.out.println(" SIZE OF Y = " + ticksY.size());
        for(int loop = 0; loop < ticksY.size(); loop++){
            double yr = axis.getDataRegion().fractionY(ticksY.get(loop));
            double yv = axis.getFramePointY(yr);
            double xv = axis.getFrame().x;            
            g2d.drawLine((int) xv, (int) yv, (int) xv+10, (int) yv);
            String label = stringY.get(loop);
            int xoff = (int) fm.stringWidth(label) + 10;
            int yoff = (int) (fm.getHeight()/2.0 - fm.getHeight()*0.2);
            g2d.drawString(label, (int) xv-xoff, (int) yv+yoff);
        }
        

        FontMetrics  fma = g2d.getFontMetrics();
        
        Rectangle2D rect = fma.getStringBounds(axis.getTitle().getText().getIterator(), 0,
                axis.getTitle().getText().getIterator().getEndIndex(),g2d);
        
        double xt = axis.getFramePointX(axis.getXTitle().getX());
        double yt = axis.getFramePointY(axis.getXTitle().getY());
        
        g2d.drawString(axis.getXTitle().getText().getIterator(), (int) xt, (int) yt+48);
        
        xt = axis.getFramePointX(axis.getTitle().getX());
        yt = axis.getFramePointY(axis.getTitle().getY());
        
        xt = axis.getFrame().x + axis.getFrame().width/2 - rect.getWidth()/2;
        yt = axis.getFrame().y - 0.8*rect.getHeight();
        g2d.drawString(axis.getTitle().getText().getIterator(), (int) xt, (int) yt);
        
        xt = -axis.getFrame().height+100;
        yt = 50;
        AffineTransform orig = g2d.getTransform();
        g2d.rotate(-Math.PI/2);
        //g2d.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
        g2d.drawString(axis.getYTitle().getText().getIterator(), (int) xt, (int) yt);
        g2d.setTransform(orig);
    }
    
    public static void drawDataSetAsGraph(AxisRegion axis, Graphics2D g2d, IDataSet ds,
            int startX, int startY, int gWidth, int gHeight){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        MarkerPainter  mPainter = new MarkerPainter();
        int markerStyle = ds.getAttributes().getAsInt("marker-style");
        int markerColor = ds.getAttributes().getAsInt("marker-color");
        int markerSize  = ds.getAttributes().getAsInt("marker-size");
        int npoints = ds.getDataSize();
        //System.out.println("Graph points = " + npoints);
        for(int loop = 0; loop < npoints; loop++){
            if(axis.getDataRegion().contains(ds.getDataX(loop),ds.getDataY(loop)) == true){
                double xr = axis.getDataRegion().fractionX(ds.getDataX(loop));
                double yr = axis.getDataRegion().fractionY(ds.getDataY(loop));
                double xf = axis.getFramePointX(xr);
                double yf = axis.getFramePointY(yr);
                //System.out.println(" POINT = " + loop + "  " + xr + "  " + yr
                //+ " " + xf + " " + yf);
                mPainter.drawMarker(g2d, (int) xf, (int) yf, markerStyle, 
                        markerSize, markerColor, 1,1);

            }
        }
    }
    
    public static void drawDataSetAsHistogram1D(AxisRegion axis, Graphics2D g2d, IDataSet ds,
            int startX, int startY, int gWidth, int gHeight){
        int lineColor   = ds.getAttributes().getAsInt("line-color");
        int lineWidth   = ds.getAttributes().getAsInt("line-width");
        
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.setColor(ColorPalette.getColor(lineColor));
        
        int npoints = ds.getDataSize();
        GeneralPath path = new GeneralPath();
        double xf = axis.getDataRegion().fractionX(ds.getDataX(0)-ds.getErrorX(0)/2.0);
        double yf = axis.getDataRegion().fractionY(0.0);
        double xv = axis.getFramePointX(xf);
        double yv = axis.getFramePointY(yf);
        path.moveTo(xv, yv);
        for(int loop = 0; loop < npoints; loop++){
            xf = axis.getDataRegion().fractionX(ds.getDataX(loop)-ds.getErrorX(loop)/2.0);
            yf = axis.getDataRegion().fractionY(ds.getDataY(loop));
            xv = axis.getFramePointX(xf);
            yv = axis.getFramePointY(yf);
            //System.out.println(" POINT " + loop + "  " + ds.getDataX(loop) 
            //        + "  " + ds.getDataY(loop));
            path.lineTo(xv, yv);
            xf = axis.getDataRegion().fractionX(ds.getDataX(loop)+ds.getErrorX(loop)/2.0);
            xv = axis.getFramePointX(xf);
            path.lineTo(xv, yv);
        }
        xf = axis.getDataRegion().fractionX(ds.getDataX(npoints-1)+ds.getErrorX(npoints-1)/2.0);
        yf = axis.getDataRegion().fractionY(0.0);
        xv = axis.getFramePointX(xf);
        yv = axis.getFramePointY(yf);
        path.lineTo(xv, yv);
        
        int fillColor = ds.getAttributes().getAsInt("fill-color");
        if(fillColor>0){
            g2d.setColor(ColorPalette.getColor(fillColor));
            g2d.fill(path);
        }
        g2d.setColor(ColorPalette.getColor(lineColor));
        g2d.draw(path);
        //g2d.fill(path);
    }
}
