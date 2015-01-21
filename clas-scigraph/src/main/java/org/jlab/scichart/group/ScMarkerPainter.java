/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.group;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

/**
 *
 * @author gavalian
 */
public class ScMarkerPainter {
     public ScMarkerPainter(){
        
    }
    
    public void drawMarker(Graphics2D g2d, int x, int y, int mtype, int msize){
        //g2d.setStroke(new BasicStroke(4));
        switch (mtype){
            case 1  : g2d.drawOval(x-msize/2, y-msize/2, msize, msize); break;
            case 2  : g2d.fillOval(x-msize/2, y-msize/2, msize, msize); break;
            case 3  : g2d.fillRect(x-msize/2, y-msize/2, msize, msize); break;
            case 4  : this.drawCross(g2d, x, y, msize); ; break;
            case 5  : this.drawTriangle(g2d, x, y, msize, 1); ; break;
            case 6  : this.drawTriangle(g2d, x, y, msize,-1); ; break;
            default : g2d.drawRect(x-msize/2, y-msize/2, msize, msize); break;
        }

    }
    public void drawCross(Graphics2D g2d, int x, int y,int msize){
        double xl = x - msize/2;
        double xr = x + msize/2;
        double yt = y - msize/2;
        double yb = y + msize/2;
        g2d.drawLine((int) xl, (int) yt, (int) xr, (int) yb);
        g2d.drawLine((int) xr, (int) yt, (int) xl, (int) yb);
    }
    
    public void drawTriangle(Graphics2D g2d, int x, int y, int msize, int factor){
        double xl = x - msize/2;
        double xr = x + msize/2;
        double yt = y - factor*msize/2;
        double yb = y + factor*msize/2;
        GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        path.moveTo(xr, yt);
        path.lineTo(xl, yt);
        path.lineTo(x , yb);
        path.closePath();
        g2d.draw(path);
    }
}
