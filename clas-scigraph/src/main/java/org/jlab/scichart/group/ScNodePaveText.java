/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.group;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class ScNodePaveText extends ScNode {
    private final ArrayList<String> paveTexts = new ArrayList<String>();
    private Font  paveFont         = new Font(Font.MONOSPACED,Font.PLAIN,14);
    private FontMetrics paveMetrics = null;
    private Double      textMetrixOffset = 1.0;
    private Double      positionX  = 0.0;
    private Double      positionY  = 0.0;
    private Double      paddingX   = 20.0;
    private Double      paddingY   = 30.0;
    private ScRegion    textRegion = new ScRegion();
    
    public ScNodePaveText(){
        
    }
    
    public ScNodePaveText(Font font){
        this.paveFont = font;
    }
    
    public ScNodePaveText(double xr, double yr){
        positionX  = xr;
        positionY  = yr;
    }
    
    public void setFont(Font font){
        this.paveFont = font;
    }
    
    public ScNodePaveText(double xr, double yr, Font font){
        positionX  = xr;
        positionY  = yr;
        this.paveFont = font;
    }
    
    public ScNodePaveText(double xr, double yr, String[] text){
        positionX  = xr;
        positionY  = yr;
        this.add(text);
    }
    
    public void addText(String text){
        paveTexts.add(text);
    }
    
    public final void add(String[] pave){
        for(String text : pave){
            this.addText(text);
        }
    }
    
    

    public double getTextX(FontMetrics metrics, ScRegion region, int index){
        int width  = metrics.stringWidth(paveTexts.get(index));
        int height = metrics.getHeight();
        return (paddingX + region.getNormalizedX(positionX));
    }
    
    public double getTextY(FontMetrics metrics,ScRegion region, int index){
        int height = metrics.getHeight();
        return (paddingY + region.getNormalizedY(positionY) + 
                (index)*(( double ) height*textMetrixOffset));
    }
    
    private void getPaveTextBoundaries(FontMetrics metrics, ScRegion region){
        double originX = region.getNormalizedX(positionX);
        double originY = region.getNormalizedY(positionY);
        double firstLength = metrics.stringWidth(paveTexts.get(0));
    }
    
    @Override
    public void paintNode(Graphics2D g2d, ScRegion region){
        FontMetrics metrics = g2d.getFontMetrics(this.paveFont);
        g2d.setFont(this.paveFont);
        g2d.setColor(this.getStrokeColor());
        double firstX = 0;
        double firstY = 0;
        for(int loop = 0; loop < paveTexts.size(); loop++){

            double xp = this.getTextX(metrics, region, loop);
            double yp = this.getTextY(metrics, region, loop);
            if(loop==0){
                firstX = xp;
                firstY = yp;
            }
            //System.err.println("Position X/Y : " + xp + "  " + yp);
            g2d.drawString(paveTexts.get(loop), (int) xp, (int) yp);
        }
        //g2d.drawLine((int) firstX, (int) firstY, (int) firstX+100, (int) firstY+100);
    }
}
