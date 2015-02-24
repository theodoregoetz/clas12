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
import java.awt.geom.AffineTransform;
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
    private String        axisTitle = "Axis Title";
    private Font          fontAxisTicks = new Font(Font.SANS_SERIF,Font.PLAIN,12);
    private Font          fontAxisTitle = new Font(Font.SANS_SERIF,Font.PLAIN,14);
    private Color         axisGridColor = new Color(220,220,220);
    private int           axisGridStyle = 1;
    
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
    
    public void setTitle(String title){
        this.axisTitle = title;
    }
    
    public void setOrigin(int x, int y){
        axisOrigin.move(x, y);
    }
    
    public void setNdivisions(int div){
        this.axisScale.setMaxTicks(div);
    }
    
    public int getOriginX(){ return axisOrigin.x;}
    public int getOriginY(){ return axisOrigin.y;}
    
    public void setVertical(boolean flag){
        this.isAxisVertical = flag;
    }
    
    public void setFont(int size){
        this.fontAxisTicks = new Font(Font.SANS_SERIF,Font.PLAIN,size);
    }
    
    public void setLength(int length){
        this.axisLength = length;
    }
    
    public int getLength(){ return this.axisLength;}
    public int getWidth(){ return this.axisWidth;}
    
    
    public void setWidth(int width){
        this.axisWidth = width;
    }
    
    public void setMinMax(double min, double max){
        this.axisMinimum = min;
        this.axisMaximum = max;
        if(min==max){
            this.axisMinimum = 0.0;
            this.axisMaximum = 1.0;
        }
        this.axisScale.setMinMaxPoints(this.axisMinimum, this.axisMaximum);
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
                
    }
    
    public void drawGrid(Graphics2D g2d){
        
        FontMetrics  fm = g2d.getFontMetrics(fontAxisTicks);
        if(this.axisGridStyle==2){
            this.drawFancyGrid(g2d);
        }
        
        if(this.isAxisVertical==false){
            
            //System.err.println(this.axisScale);
            ArrayList<Double> ticks = this.axisScale.getCoordinates();
            ArrayList<String> tickLabels = this.axisScale.getCoordinatesLabels();
            
            //double stepSize =  (xend - this.axisOrigin.x)/this.ndivisions;
            for(int loop = 0; loop < ticks.size(); loop++){
                
                double xcoord = this.getTranslatedCoordinate(ticks.get(loop));                
                
                g2d.setStroke(dashed);
                g2d.setColor(this.axisGridColor);
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
            
            double axisX = this.axisOrigin.x + 0.5*this.axisLength-0.5*fm.stringWidth(axisTitle) ;
            double axisY = this.axisOrigin.y + fm.getHeight()*2.5;
            g2d.drawString(axisTitle, (int) axisX, (int) axisY);
        } else {
            ArrayList<Double> ticks = this.axisScale.getCoordinates();
            ArrayList<String> tickLabels = this.axisScale.getCoordinatesLabels();
            
            //double stepSize =  (xend - this.axisOrigin.x)/this.ndivisions;
            for(int loop = 0; loop < ticks.size(); loop++){
                //double stepSize =  (this.axisOrigin.y - yend)/this.ndivisions;
                //for(int loop = 0; loop < this.ndivisions; loop++){
                double ycoord = this.getTranslatedCoordinate(ticks.get(loop));               
                g2d.setStroke(dashed);
                g2d.setColor(this.axisGridColor);
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
            AffineTransform orig = g2d.getTransform();
            g2d.rotate(-Math.PI/2);
            double axisX = -this.axisOrigin.y + (0.5*this.getLength() - 0.5*fm.stringWidth(axisTitle));
            double axisY = fm.getHeight() ;//- fm.stringWidth(axisTitle);
            g2d.setFont(this.fontAxisTicks);
            g2d.drawString(axisTitle, (int) axisX, (int) axisY);
            g2d.setTransform(orig);
        }
    }
    
    public void drawFancyGrid(Graphics2D g2d){
        ArrayList<Double> ticks = this.axisScale.getCoordinates();
        ArrayList<String> tickLabels = this.axisScale.getCoordinatesLabels();
        if(this.isAxisVertical==false){
            for(int loop = 0; loop < ticks.size(); loop++){
                
                double xcoord = this.getTranslatedCoordinate(ticks.get(loop));
                if(loop%2==0&&loop!=ticks.size()-1){
                    double xcoordNext = this.getTranslatedCoordinate(ticks.get(loop+1));
                    g2d.setColor(new Color(250,250,250));
                    g2d.fillRect( (int) xcoord, (int) this.axisOrigin.y - this.axisWidth, 
                            (int) (xcoordNext-xcoord) ,this.axisWidth );
                }
            }
        } else {
            for(int loop = 0; loop < ticks.size(); loop++){
                //double stepSize =  (this.axisOrigin.y - yend)/this.ndivisions;
                //for(int loop = 0; loop < this.ndivisions; loop++){
                double ycoord = this.getTranslatedCoordinate(ticks.get(loop));
                if(loop%2==0&&loop!=ticks.size()-1){
                    double ycoordNext = this.getTranslatedCoordinate(ticks.get(loop+1));
                    g2d.setColor(new Color(250,250,250));
                    int xr = (int) this.axisOrigin.x;
                    int yr = (int) ( ycoord);
                    int wr = (int) this.axisWidth;
                    int hr =  (int) (ycoord- ycoordNext);
                    //System.err.println(" - drawing fancy - " + xr + "  " + yr + " " + wr + " " +hr);
                    g2d.fillRect( xr,yr,wr,hr);
                            
                }
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
