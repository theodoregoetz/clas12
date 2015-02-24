/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.pad;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JPanel;
import org.root.base.IDrawableDataSeries;
import org.root.data.DataSetXY;
import org.root.func.F1D;
import org.root.group.PlotDescriptor;
import org.root.group.PlotGroup;
import org.root.histogram.H1D;
import org.root.histogram.H2D;
import org.root.histogram.PaveText;
import org.root.series.DataSeriesH1D;
import org.root.series.DataSeriesH2D;

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
        this.removeAll();
        this.revalidate();
        this.setLayout(new GridLayout(rows,cols));
        int xsize = this.getWidth()/cols;
        int ysize = this.getHeight()/rows;
        for(int loop = 0; loop < cols*rows; loop++){
            RootPad pad = new RootPad(xsize,ysize);
            canvasPads.add(pad);
            this.add(pad);
        }
        this.revalidate();
        this.update();
        this.repaint();
    }
    
    public void update(){
        for(RootPad pad : this.canvasPads){
            pad.repaint();
        }
    }
    
    public void draw(int pad, PaveText pave){
        this.canvasPads.get(pad).addText(pave);
    }
    
    public void draw(int pad, H1D h){
        this.draw(pad,h,"");
    }
    
    public void draw(int pad, H1D h, String options){
        if(options.contains("same")==false){
            this.canvasPads.get(pad).clear();
        }
        this.canvasPads.get(pad).addSeries(h);
        this.canvasPads.get(pad).repaint();
    }
    
    public void draw(int pad, F1D func){
        this.draw(pad, func,"");
    }
    
    public void draw(int pad, F1D func, String options){
        if(options.contains("same")==false){
            this.canvasPads.get(pad).clear();
        }
        this.canvasPads.get(pad).addSeries(func);
        this.canvasPads.get(pad).repaint();
    }
    
    public void draw(int pad, DataSetXY xydata){
        this.draw(pad, xydata,"");
    }
    
    public void draw(int pad, DataSetXY xydata, String options){
        if(options.contains("same")==false){
            this.canvasPads.get(pad).clear();
        }
        if(options.contains("L")==true){
            this.canvasPads.get(pad).addSeries(xydata,1);
        } else {
            this.canvasPads.get(pad).addSeries(xydata);
        }
        this.canvasPads.get(pad).repaint();
    }
    
    public void setFontSize(int size){
        for(RootPad pad : this.canvasPads){
            pad.setFontSize(size);
        }
    }
    
    public void draw(int pad, PlotGroup group, String objname){
        Object drawObject = group.getObjects().get(objname);
        if(drawObject instanceof H1D){
            H1D histogram = (H1D) drawObject;
            //System.out.println(histogram);
            DataSeriesH1D h1d = new DataSeriesH1D((H1D) drawObject);
            
            this.add(pad,h1d);
            return;
        }
        
        if(drawObject instanceof H2D){
            DataSeriesH2D h2d = new DataSeriesH2D((H2D) drawObject);
            this.add(pad,h2d);
            
            return;
        }
    }
    
    public void draw(PlotGroup group){
        //ArrayList<Object>  items = group.getObjects();
        this.divide(group.getColumns(), group.getRows());
        for(Map.Entry<Integer,PlotDescriptor> entry : group.getDescriptors().entrySet()){
            int pad = entry.getKey();
            ArrayList<String>  list = entry.getValue().getList();
            for(String plotName : list){
                this.draw(pad,group, plotName);
            }
        }
        this.repaint();
    }
    
    public void add(int pad, IDrawableDataSeries series){
        canvasPads.get(pad).addSeries(series);
    }
    
    public void add(int pad, H1D h){
        this.canvasPads.get(pad).addSeries(h);
    }
    
    public void clear(int pad){
        canvasPads.get(pad).clear();
    }
}
