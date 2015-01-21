/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.group;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 *
 * @author gavalian
 */
public class ScNode {
    
    private Color  strokeColor = Color.BLACK;
    private Color  fillColor   = Color.WHITE;
    private Stroke lineStroke  = new BasicStroke(2);
    
    public ScNode(){}
    
    public ScNode(Color stcol, Color fcol){
        strokeColor = stcol;
        fillColor   = fcol;
    }
    
    public ScNode(Stroke st){
        lineStroke = st;
    }
    
    public ScNode(Color stcol, Color fcol, Stroke st){
        strokeColor = stcol;
        fillColor   = fcol;
        lineStroke  = st;
    }
    
    public void setStrokeColor(Color col) { strokeColor = col;    }
    public void setFillColor(Color col)   { fillColor   = col;    }
    public void setStroke(Stroke stroke)  { lineStroke  = stroke; }
    
    public Color   getStrokeColor() { return strokeColor; }
    public Color   getFillColor()   { return fillColor;   }
    public Stroke  getLineStroke()  { return lineStroke;  }
    
    public void paintNode(Graphics2D g2d, ScRegion region){
        //System.err.println(region.toString());
        g2d.drawRect( (int) region.getX(), (int) region.getY(), 
                (int) region.getWidth(), (int) region.getHeight());
    }
}
