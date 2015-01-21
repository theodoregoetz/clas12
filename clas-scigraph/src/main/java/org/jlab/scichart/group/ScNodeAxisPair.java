/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.group;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import org.jlab.scichart.base.ScAxisPair;
import org.jlab.scichart.styles.ScGraphStyles;

/**
 *
 * @author gavalian
 */
public class ScNodeAxisPair extends ScNode {
    
    private ScAxisPair  axisPair = new ScAxisPair();
    private Font        axisFont = new Font(Font.SANS_SERIF,Font.PLAIN,14);
    
    public ScNodeAxisPair(){
        
    }
    
    public ScNodeAxisPair(Stroke stroke){
        super(stroke);
    }
    
    
    public void setAxisMinMax(double xmin, double xmax, double ymin, double ymax){
        this.axisPair.getAxisX().setMinMax(xmin, xmax);
        this.axisPair.getAxisY().setMinMax(ymin, ymax);
    }
    
    public void setFontSize(int size){
        axisFont = new Font(Font.SANS_SERIF,Font.PLAIN,size);
        
    }
    
    public ScAxisPair getAxisPair(){ return this.axisPair;}
    
    @Override
    public void paintNode(Graphics2D g2d, ScRegion region){
        //System.err.println(" X " + this.axisPair.getAxisX().toString());
        //System.err.println(" Y " + this.axisPair.getAxisY().toString());
        
        g2d.setStroke(ScGraphStyles.getFrameStroke());
        g2d.setColor(this.getStrokeColor());
        
        GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        path.moveTo( (int) region.getX(),(int) (region.getY() + region.getHeight()));
        path.lineTo( (int) (region.getX() + region.getWidth()),
                (int) (region.getY() + region.getHeight()));
        path.lineTo((int) (region.getX() + region.getWidth()),
                (int) region.getY() );
        path.lineTo((int) (region.getX()),
                (int) region.getY() );
        path.closePath();
        g2d.draw(path);
        
        
        //FontMetrics metrics = g2d.getFontMetrics(axisFont);        
        //g2d.setFont(axisFont);
        FontMetrics metrics = g2d.getFontMetrics(ScGraphStyles.getAxisFont());
        g2d.setFont(ScGraphStyles.getAxisFont());
        double textHeight   = metrics.getHeight();        
        double yPositionFixed = region.getNormalizedY(1.0);
        int xloop = 0;
        for(Double xc : this.axisPair.getAxisX().getAxisTicks()){
            double xcr = this.axisPair.getAxisX().getRelative(xc);
            if(xcr>=0&&xcr<=1.0){
                double xcA = region.getNormalizedX(xcr);
                g2d.drawLine( (int) xcA, (int) yPositionFixed, 
                        (int) xcA, (int) yPositionFixed-10);
                String text = this.axisPair.getAxisX().getAxisTicksString().get(xloop);
                double width = metrics.stringWidth(text);
                g2d.drawString(text, (int) (xcA - width/2.0), 
                        (int) (yPositionFixed + textHeight*1.2));                
            }
            xloop++;
        }
        
        double xPositionFixed = region.getNormalizedX(0.0);
        int yloop = 0;
        for(Double yc : this.axisPair.getAxisY().getAxisTicks()){
            //System.err.println("drawing axis...");
            double ycr = this.axisPair.getAxisY().getRelative(yc);
            if(ycr>=0&&ycr<=1.0){
                double ycA = region.getNormalizedY(1.0-ycr);
                g2d.drawLine( (int) xPositionFixed, (int) ycA, 
                        (int) xPositionFixed+10, (int) ycA);
                String text = this.axisPair.getAxisY().getAxisTicksString().get(yloop);
                double width = metrics.stringWidth(text);
                g2d.drawString(text, (int) (xPositionFixed - width - 10.0), 
                        (int) (ycA + 0.5*textHeight/2.0)); 
            }
            yloop++;
        }
        
        String xtitle  = this.axisPair.getAxisX().getTitle();
        double xmiddle = this.axisPair.getAxisX().getMiddle();
        //System.err.println(" X TITLE = " + xtitle);
        double xcoord  = region.getNormalizedX(this.axisPair.getAxisX().getRelative(xmiddle));
        double xtitleWidth = metrics.stringWidth(xtitle);
        g2d.drawString(xtitle, (int) (xcoord - xtitleWidth/2.0),
                (int) (yPositionFixed + 4.5*textHeight/2.0));

//System.err.println(region.toString());
        /*
        g2d.drawLine((int) region.getX(), 
        (int) (region.getY() + region.getHeight()),
        (int) (region.getX() + region.getWidth()),
        (int) (region.getY() + region.getHeight()));
        
        g2d.drawLine((int) region.getX(), 
                (int) (region.getY()),
                (int) region.getX(),
                (int) (region.getY() + region.getHeight()));
        //g2d.drawRect( (int) region.getX(), (int) region.getY(), 
        //        (int) region.getWidth(), (int) region.getHeight());
        */
    }
}
