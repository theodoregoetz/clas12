/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.canvas;

import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class ChartCanvasPads {
    private int numColumns = 1;
    private int numRows    = 1;
    private ArrayList<Double> positionX  = new ArrayList<Double>();
    private ArrayList<Double> positionY  = new ArrayList<Double>();
    private ArrayList<Double> heights    = new ArrayList<Double>();
    private ArrayList<Double> widths     = new ArrayList<Double>();
    private int               sizeWidth  = 100;
    private int               sizeHeight = 100;
    
    public ChartCanvasPads(){
        
    }
    
    public double getX(int index) { return positionX.get(index);}
    public double getY(int index) { return positionY.get(index);}
    public double getWidth(int index) { return widths.get(index);}
    public double getHeight(int index) { return heights.get(index);}
    
    public void divide(int columns, int rows){
        numColumns = columns;
        numRows    = rows;
        this.updateCoordinates();
    }
    
    public void setSize(int w, int h){
        sizeWidth  = w;
        sizeHeight = h;
        this.updateCoordinates();
    }
    
    public void setSize(int w, int h,int ncol, int nrow){
        numColumns = ncol;
        numRows    = nrow;
        sizeWidth  = w;
        sizeHeight = h;
        this.updateCoordinates();
    }
    
    public int  getNPads(){
        return numColumns*numRows;
    }
    
    private void updateCoordinates(){
        positionX.clear();
        positionY.clear();
        widths.clear();
        heights.clear();
        //System.err.print(" graph Pad " + numColumns + " " + numRows);
        for(int ir = 0; ir < numRows; ir++){
            for(int ic = 0; ic < numColumns; ic++){
                positionX.add( (double) ic*sizeWidth/numColumns);
                positionY.add( (double) ir*sizeHeight/numRows);
                widths.add((double) sizeWidth/numColumns );
                heights.add((double) sizeHeight/numRows);
            }
        }
    }
}
