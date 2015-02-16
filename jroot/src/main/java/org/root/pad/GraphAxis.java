/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.pad;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class GraphAxis {
    
    private Point axisOrigin   = new Point();
    private Integer axisLength = 10;
    private Integer axisWidth  = 10;
    private Boolean isAxisVertical = false;
    private Integer ndivisions     = 10;
    private Integer ntickdivisiond = 5;
    private Double  axisMinimum    = 0.0;
    private Double  axisMaximum    = 1.0;
    private AxisNiceScale axisScale = new AxisNiceScale(0.0,15.0);
    
    private Font          fontAxisTicks = new Font(Font.SANS_SERIF,Font.PLAIN,12);
    private Font          fontAxisTitle = new Font(Font.SANS_SERIF,Font.PLAIN,14);
    
    
    final static float dash1[] = {5.0f};
    final static BasicStroke dashed =
        new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        15.0f, dash1, 0.0f);
    
    public GraphAxis(){
        this.axisScale.setMaxTicks(ndivisions);
    }
    
    public GraphAxis(int x, int y, int length){
        axisOrigin.move(x, y);
        this.axisScale.setMaxTicks(ndivisions);
    }
    
    public void setOrigin(int x, int y){
        axisOrigin.move(x, y);
    }
    
    public void setVertical(boolean flag){
        this.isAxisVertical = flag;
    }
    
    public void setFont(int size){
        this.fontAxisTicks = new Font(Font.SANS_SERIF,Font.PLAIN,size);
    }
    
    public void setLength(int length){
        this.axisLength = length;
    }
    
    public void setWidth(int width){
        this.axisWidth = width;
    }
    
    public void setMinMax(double min, double max){
        this.axisMinimum = min;
        this.axisMaximum = max;
        this.axisScale.setMinMaxPoints(min, max);
    }
    
    public void draw(Graphics2D g2d){
        
        g2d.setStroke(new BasicStroke(2));
        g2d.setFont(fontAxisTicks);
        FontMetrics  fm = g2d.getFontMetrics(fontAxisTicks);
        
        int xend = (int) (axisOrigin.getX() + this.axisLength);
        int yend = (int) this.axisOrigin.getY();
        
        if(this.isAxisVertical==true){
            xend = (int) this.axisOrigin.getX();
            yend = (int) (this.axisOrigin.getY() - this.axisLength);
        } 
        
        g2d.drawLine(
                (int) this.axisOrigin.getX(),
                (int) this.axisOrigin.getY(),
                xend,yend);
        
        if(this.isAxisVertical==false){
            g2d.drawLine(xend, yend, xend, yend - this.axisWidth);
        } else {
            g2d.drawLine(xend, yend, xend + this.axisWidth, yend);
        }
        
        if(this.isAxisVertical==false){
            
            //System.err.println(this.axisScale);
            ArrayList<Double> ticks = this.axisScale.getCoordinates();
            ArrayList<String> tickLabels = this.axisScale.getCoordinatesLabels();
            
            //double stepSize =  (xend - this.axisOrigin.x)/this.ndivisions;
            for(int loop = 0; loop < ticks.size(); loop++){
                double xcoord = this.getTranslatedCoordinate(ticks.get(loop));
                
                g2d.setStroke(dashed);
                g2d.setColor(Color.gray);
                g2d.drawLine((int) xcoord,
                        (int) this.axisOrigin.y, 
                        (int) xcoord, 
                        (int) this.axisOrigin.y - this.axisWidth
                );
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(Color.black);
                g2d.drawLine(
                        (int) xcoord,
                        (int) this.axisOrigin.y, 
                        (int) xcoord, 
                        (int) this.axisOrigin.y - 5);
                
                double xoffset = fm.stringWidth(tickLabels.get(loop))*0.5;
                double yoffset = fm.getHeight()*1.0;
                
                g2d.drawString(tickLabels.get(loop), 
                        (int) (xcoord-xoffset), (int) (this.axisOrigin.y + yoffset));
                        
            }
        } else {
            ArrayList<Double> ticks = this.axisScale.getCoordinates();
            ArrayList<String> tickLabels = this.axisScale.getCoordinatesLabels();
            
            //double stepSize =  (xend - this.axisOrigin.x)/this.ndivisions;
            for(int loop = 0; loop < ticks.size(); loop++){
                //double stepSize =  (this.axisOrigin.y - yend)/this.ndivisions;
                //for(int loop = 0; loop < this.ndivisions; loop++){
                double ycoord = this.getTranslatedCoordinate(ticks.get(loop));
                
                g2d.setStroke(dashed);
                g2d.setColor(Color.gray);
                 g2d.drawLine(
                        this.axisOrigin.x, 
                        (int) ycoord,
                        this.axisOrigin.x + this.axisWidth,
                        (int) ycoord
                        );
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(Color.black);
                
                g2d.drawLine(
                        this.axisOrigin.x, 
                        (int) ycoord,
                        this.axisOrigin.x + 5,
                        (int) ycoord
                        );
                
                double xoffset = fm.stringWidth(tickLabels.get(loop))+10;
                double yoffset = fm.getHeight()*0.3;
                //double yoffset = 0.0;
                g2d.drawString(tickLabels.get(loop), 
                        (int) (this.axisOrigin.x - xoffset), (int) (ycoord + yoffset));
            }
        }
    }
    
    public double getTranslatedCoordinate(double value){
        double coordinate = 0.0;
        double fraction = (value-this.axisMinimum)/
                    (this.axisMaximum - this.axisMinimum);
        if(this.isAxisVertical==false){
            coordinate = this.axisOrigin.x + (this.axisLength)*fraction;
        } else {
            coordinate = this.axisOrigin.y - (this.axisLength)*fraction;
        }
        return coordinate;
    }
}
