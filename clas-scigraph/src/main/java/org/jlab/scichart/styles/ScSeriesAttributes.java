/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.styles;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 *
 * @author gavalian
 */
public class ScSeriesAttributes {
    
    public int  FILL_COLOR   = -1;
    public int  LINE_COLOR   =  1;
    public int  LINE_WIDTH   =  2;
    public int  MARKER_STYLE =  1;
    public int  MARKER_SIZE  =  8;
    public Stroke LINE_STROKE = new BasicStroke(2);
    public Stroke MARKER_STROKE = new BasicStroke(2);
    
    public void setLineWidth(int width){
        LINE_STROKE = new BasicStroke(width);        
    }
    
    
    
}
