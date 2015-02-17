/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.pad;

import javax.swing.JFrame;
import org.root.func.F1D;
import org.root.group.PlotGroup;
import org.root.histogram.H1D;
import org.root.histogram.H2D;
import org.root.histogram.PaveText;
import org.root.series.DataSeriesFunc;
import org.root.series.DataSeriesH1D;
import org.root.series.DataSeriesH2D;

/**
 *
 * @author gavalian
 */
public class TCanvas extends JFrame {
    private String canvasName = "c1";
    private Integer numberOfPads = 1;
    private Integer currentPad   = 0;
    
    private RootCanvas embededCanvas = null;
    public TCanvas(String name, String title, int xsize, int ysize){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.canvasName = name;
        this.embededCanvas = new RootCanvas(xsize,ysize,1,1);
        this.add(this.embededCanvas);
        this.pack();
        this.setVisible(true);
    }
    
    public void setFontSize(int size){
        this.embededCanvas.setFontSize(size);
    }
    
    public void draw(PaveText pave){
        this.embededCanvas.draw(currentPad, pave);
        this.repaint();
    }
    public TCanvas(String name, String title, int xsize, int ysize, int col, int rows){
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.canvasName = name;
        this.embededCanvas = new RootCanvas(xsize,ysize,col,rows);
        this.numberOfPads  = col*rows;
        this.add(this.embededCanvas);
        this.pack();
        this.setVisible(true);
    }
    
    public void divide(int x, int y){
        
    }
    
    public void cd(int pad){
        currentPad = pad;
        if (pad<0) currentPad = 0;
        if (pad>=this.numberOfPads) currentPad = this.numberOfPads -1;        
    }
    
    public void draw(H1D h, String options){
        if(options.contains("same")==false){
            this.embededCanvas.clear(currentPad);
        }
        this.embededCanvas.add(currentPad, h);
        this.repaint();
    }
    
    public void draw(H1D h){
        this.draw(h, "*");
    }
    
    public void draw(H2D h){
        DataSeriesH2D h2d = new DataSeriesH2D(h);
        this.embededCanvas.add(currentPad, h2d);
        this.embededCanvas.repaint();
    }
    
    public void draw(F1D func){       
        this.embededCanvas.draw(currentPad,func);
    }
    
    public void draw(PlotGroup group){
        this.embededCanvas.draw(group);
    }
}
