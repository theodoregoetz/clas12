/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.group;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import org.jlab.scichart.base.ScAxisPair;
import org.jlab.scichart.styles.ColorManager;
import org.jlab.scichart.styles.ScSeriesAttributes;

/**
 *
 * @author gavalian
 */
public class ScNodeSeries extends ScNode {
    
    private final ArrayList<Double>   xData = new ArrayList<Double>();
    private final ArrayList<Double>   yData = new ArrayList<Double>();
    private final ArrayList<Double>   xErrorData = new ArrayList<Double>();
    private final ArrayList<Double>   yErrorData = new ArrayList<Double>();
    private final ScAxisPair          seriesAxis = new ScAxisPair();
    private final ScAxisPair          groupAxis  = new ScAxisPair();
    private final ScSeriesAttributes  seriesAttr = new ScSeriesAttributes();
    
    private Integer seriesType     = 1;
    
    public ScNodeSeries() { }
    
    public void setData(int type, double[] xd, double[] yd, int color){
        seriesType = type;
        xData.clear();
        yData.clear();
        for(int loop = 0; loop < xd.length; loop++){
            xData.add(xd[loop]); yData.add(yd[loop]);
        }
        this.setStrokeColor(ColorManager.getColor(1, color));
        this.updateAxisLimits();
    }
    
    public ScSeriesAttributes attributes(){ return seriesAttr;}
    
    public void setData(int type, double[] xd, double[] yd){
        this.setData(type, xd, yd, 1);
    }
    
    public void setData( double[] xd, double[] yd){
        this.setData(1, xd, yd);
    }
    public void  updateAxis(){
        this.updateAxisLimits();
    }
    private void updateAxisLimits(){
        double xmin = this.getLeftEdge(0);
        double xmax = this.getRightEdge(xData.size()-1);    
        double ymin = this.getMinY();
        double ymax = this.getMaxY();
        //System.err.println(" x edges = " + xmin + " " + xmax + " " + xData.get(xData.size()-1)
        //+ " " + xData.get(xData.size()-2));
        
        if(seriesType==4){
            if(ymin>0.0) ymin = 0.0;
            seriesAxis.set(xmin, xmax, ymin, ymax + (ymax-ymin)*0.2);
            groupAxis.set(xmin, xmax, ymin, ymax + (ymax-ymin)*0.2);
        } else {
            System.err.println("Changin min max based on "
            + ymin + " " + ymax);
            if(seriesAxis.getAxisY().isLog()==false){
                seriesAxis.set(xmin - (xmax-xmin) * 0.05, 
                        xmax + (xmax-xmin) * 0.05, 
                        ymin - (ymax-ymin) * 0.05,
                        ymax + (ymax-ymin) * 0.25);
                groupAxis.set(xmin - (xmax-xmin) * 0.05, 
                        xmax + (xmax-xmin) * 0.05, 
                        ymin - (ymax-ymin) * 0.05,
                        ymax + (ymax-ymin) * 0.25);
            } else {
                System.err.println(" SET LOG AXIS MIN/MAX  "
                + ymax*10);
                seriesAxis.set(xmin - (xmax-xmin) * 0.05, 
                        xmax + (xmax-xmin) * 0.05, 
                        0.0000001,
                        ymax * 10.0);
                groupAxis.set(xmin - (xmax-xmin) * 0.05, 
                        xmax + (xmax-xmin) * 0.05, 
                        0.0000001,
                        ymax * 10.0);
            }
        }
        //System.err.println(seriesAxis.getAxisX().toString());
    }
    
    public ScAxisPair seriesAxis(){ return seriesAxis;}
    
    public void       setGroupAxist(double xmin, double xmax,
            double ymin, double ymax){
        groupAxis.set(xmin, xmax, ymin, ymax);
    }
    
    public void       setGroupAxist(double xmin, double xmax,
            double ymin, double ymax, boolean xlog, boolean ylog){
        groupAxis.set(xmin, xmax, ymin, ymax);
        groupAxis.getAxisX().setLog(xlog);
        groupAxis.getAxisY().setLog(ylog);
    }
    
    public double getMinY(){
        double ymin = yData.get(0);
        for(int loop =0 ; loop < yData.size();loop++){
            if(yData.get(loop)<ymin) ymin = yData.get(loop);
        }
        return ymin;
    }
    
    public double getMaxY(){
        double ymax = yData.get(0);
        for(int loop =0 ; loop < yData.size();loop++){
            if(yData.get(loop)>ymax) ymax = yData.get(loop);
        }
        return ymax;
    }
    
    private double getLeftEdge(int index){
        double leftEdge = xData.get(index);
        if(index==0){            
            double adjust = Math.abs(leftEdge - xData.get(index+1))/2.0;
            leftEdge = leftEdge - adjust;
        } else {
            double adjust = Math.abs(leftEdge - xData.get(index-1))/2.0;
            leftEdge = leftEdge - adjust;
        }
        return leftEdge;
    }
    
    private double getRightEdge(int index){
        double rightEdge = xData.get(index);
        if(index==xData.size()-1){
            //System.err.println("INDEX = " + index + "  " + rightEdge 
            //      + " " + xData.get(index-1)/2.0  );
            double adjust = Math.abs(rightEdge - xData.get(index-1))/2.0;
            rightEdge = rightEdge + adjust;
        } else {
            double adjust = Math.abs(rightEdge - xData.get(index+1))/2.0;
            rightEdge = rightEdge + adjust;
        }
        return rightEdge;
    }
    
    private ArrayList<Double> getDataX(){ return xData; }
    
    public void paintNodeHistogram(Graphics2D g2d, ScRegion region){
        int firstPointAdded = 0;
        
        /*g2d.setStroke(this.getLineStroke());
        g2d.setColor(this.getStrokeColor());
        */
        
        g2d.setStroke(this.attributes().LINE_STROKE);
        g2d.setColor(ColorManager.getColor(1, this.attributes().LINE_COLOR));
        
        GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        
        for(int loop = 0; loop < xData.size(); loop++){
            double xdataLeft  = this.getLeftEdge(loop);
            double xdataRight = this.getRightEdge(loop);
            //System.err.println("DATA " + loop + "  " + xData.get(loop) + 
            //        "  " + xdataLeft + "  " + xdataRight);
            //double xdataLeft = this.getLeftEdge(loop);
            double ydata     = yData.get(loop);
            double xleftRe   = groupAxis.getAxisX().getRelative(xdataLeft);
            double xrightRe  = groupAxis.getAxisX().getRelative(xdataRight);
            double ytopRe    = groupAxis.getAxisY().getRelative(ydata);
            double xcoordLeft    = region.getNormalizedX(xleftRe);
            double xcoordRight   = region.getNormalizedX(xrightRe);
            double ycoord    = region.getNormalizedY(1.0-ytopRe);
            if(region.isInside(xcoordLeft,ycoord)==true){
                if(firstPointAdded==0){
                    path.moveTo(xcoordLeft,ycoord);
                    path.lineTo(xcoordRight, ycoord);
                    firstPointAdded++;
                } else {
                    path.lineTo(xcoordLeft, ycoord);
                    path.lineTo(xcoordRight, ycoord);
                }
            }
        }
        path.closePath();
        if(this.attributes().FILL_COLOR>0){
            g2d.setPaint(ColorManager.getColor(400, this.attributes().FILL_COLOR));
            g2d.fill(path);
        }
        g2d.setColor(ColorManager.getColor(1, this.attributes().LINE_COLOR));
        g2d.draw(path);      
    }
   
    public void paintNodeLine(Graphics2D g2d, ScRegion region){
        g2d.setStroke(this.getLineStroke());
        GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO);        
        int isFirstTimeAdded = 0;
        for(int loop = 0; loop < xData.size(); loop++){
            double xcoordRe = groupAxis.getAxisX().getRelative(xData.get(loop));
            double ycoordRe = groupAxis.getAxisY().getRelative(yData.get(loop));
            double xcoord = region.getNormalizedX(xcoordRe);
            double ycoord = region.getNormalizedY(1.0-ycoordRe);
            
            if(region.isInside(xcoord, ycoord)){
                //System.err.println("drawing line # "+ loop + " = " + xcoord + " " + ycoord);
                if(isFirstTimeAdded==0){
                    path.moveTo(xcoord, ycoord);
                    isFirstTimeAdded++;
                } else { path.lineTo(xcoord, ycoord);}
            }
        }
        g2d.draw(path);
    }
    
    public void paintNodePoints(Graphics2D g2d, ScRegion region){
        GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        ScMarkerPainter markers = new ScMarkerPainter();
        g2d.setStroke(this.attributes().MARKER_STROKE);
        g2d.setColor(ColorManager.getColor(1,this.attributes().LINE_COLOR));       
        for(int loop = 0; loop < xData.size(); loop++){
            double xcoordRe = groupAxis.getAxisX().getRelative(xData.get(loop));
            double ycoordRe = groupAxis.getAxisY().getRelative(yData.get(loop));
            double xcoord = region.getNormalizedX(xcoordRe);
            double ycoord = region.getNormalizedY(1.0-ycoordRe);
            double yErrorStart = groupAxis.getAxisY().getRelative(
                    yData.get(loop)-Math.sqrt(Math.abs(yData.get(loop)))
            );
            double yErrorEnd = groupAxis.getAxisY().getRelative(
                    yData.get(loop)+Math.sqrt(Math.abs(yData.get(loop)))
            );
            double yErrorStartCoord = region.getNormalizedY(1.0-yErrorStart);
            double yErrorEndCoord = region.getNormalizedY(1.0-yErrorEnd);
            if(region.isInside(xcoord, ycoord)){
                g2d.drawLine((int) xcoord, (int) yErrorStartCoord,
                        (int) xcoord, (int) yErrorEndCoord
                        );
                markers.drawMarker(g2d, (int) xcoord, (int) ycoord,
                        this.attributes().MARKER_STYLE,this.attributes().MARKER_SIZE);
                
            }
        }
    }
    
    @Override
    public void paintNode(Graphics2D g2d, ScRegion region){
        //System.err.println(" X = " + groupAxis.getAxisX().toString());
        //System.err.println(" Y = " + groupAxis.getAxisY().toString());
        
        g2d.setStroke(this.getLineStroke());
        g2d.setColor(this.getStrokeColor());        
        if(seriesType == 1){
            this.paintNodeLine(g2d, region);
        }        
        
        if(seriesType == 2){
            this.paintNodePoints(g2d, region);
        }
        
        if(seriesType==4){
            this.paintNodeHistogram(g2d, region);
        }
    }
}
