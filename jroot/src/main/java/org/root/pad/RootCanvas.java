/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.pad;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.root.base.IDrawableDataSeries;

/**
 *
 * @author gavalian
 */
public class RootCanvas extends JPanel {
    private ArrayList<RootPad>  canvasPads = new ArrayList<RootPad>();
    public RootCanvas(){
        super();        
    }
    
    public RootCanvas(int xsize, int ysize, int nx, int ny){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.divide(nx, ny);
    }
    
    public void divide(int cols, int rows){
        canvasPads.clear();
        this.setLayout(new GridLayout(rows,cols));
        int xsize = this.getWidth()/cols;
        int ysize = this.getHeight()/rows;
        for(int loop = 0; loop < cols*rows; loop++){
            RootPad pad = new RootPad(xsize,ysize);
            canvasPads.add(pad);
            this.add(pad);
        }
    }
    
    public void add(int pad, IDrawableDataSeries series){
        canvasPads.get(pad).addSeries(series);
    }
    
    public void clear(int pad){
        canvasPads.get(pad).clear();
    }
}
