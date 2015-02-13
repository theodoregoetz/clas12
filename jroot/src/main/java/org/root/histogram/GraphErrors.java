/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.histogram;

import org.root.attr.AttributesLine;
import org.root.attr.AttributesMarker;
import org.root.data.DataSetXY;
import org.root.fitter.DataFitter;
import org.root.func.F1D;



/**
 *
 * @author gavalian
 */
public class GraphErrors extends DataSetXY {
    
    public AttributesLine    graphLineAttributes   = new AttributesLine();
    public AttributesMarker  graphMarkerAttributes = new AttributesMarker();
    
    public GraphErrors(double[] x, double[] y){
        super(x,y);
        setName("GraphErrors");
    } 
    
    public void fit(F1D func){
        DataFitter.fit(this, func);
    }
    
    public void setMarkerStyle(int style){ 
        this.graphMarkerAttributes.MARKER_STYLE = style; 
    }
    
    public void setMarkerColor(int color){ 
        this.graphMarkerAttributes.MARKER_COLOR = color; 
    }
    
    public void setMarkerSize(int size){ 
        this.graphMarkerAttributes.MARKER_SIZE = size; 
    }
    
}
