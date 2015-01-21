/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.canvas;

import java.awt.Font;
import javax.swing.JFrame;
import org.jlab.data.func.F1D;
import org.jlab.data.graph.DataSetXY;
import org.jlab.data.histogram.H1D;
import org.jlab.scichart.styles.ScGraphStyles;

/**
 *
 * @author gavalian
 */
public class ScCanvas extends JFrame {
    private ScChartCanvas  sciCanvas;
    private Integer        currentPad = 0;
    
    public ScCanvas(){
        super("CLAS sci Graph");
        this.setSize(500, 500);
        this.initComponents(1,1);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    
    public ScCanvas(int width, int height, int nc,int nr){
        super("CLAS sci Graph");
        this.setSize(width,height);
        this.initComponents(nc,nr);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    
    private void initComponents(int nc, int nr){
        int w = this.getSize().width;
        int h = this.getSize().height;
        sciCanvas = new ScChartCanvas(w,h,nc,nr);
        this.add(sciCanvas);
        this.addComponentListener(sciCanvas);
    }
    
    public ScChartCanvas  canvas(){
        return sciCanvas;
    }
    
    public void cd(int pad){
        if(pad<0||pad>=sciCanvas.getNumPads()){
            currentPad = 0;
        } else {
            currentPad = pad;
        }
        
    }
    
    public void draw(F1D func, String options, int color){
        DataSetXY data = func.getDataSet();
        double[] xdata = data.getDataX().getArray();
        double[] ydata = data.getDataY().getArray();
        sciCanvas.addLine(currentPad, xdata, ydata, color);
    }
    
    public void draw(DataSetXY ds, String options, int color){
        double[] xdata = ds.getDataX().getArray();
        double[] ydata = ds.getDataY().getArray();
        sciCanvas.addPoints(currentPad, xdata, ydata, color);       
    }
    
    public void draw(H1D h){
        this.draw(h,"*",1);
    }
    
    public void draw(H1D h, String options){
        this.draw(h, options,1);
    }
    
    public void draw(H1D h, String options, int color){
        double[] xdata = h.getAxis().getBinCenters();
        double[] ydata = h.getData();
        Boolean  isSame = false;
        if(options.contains("same")==true) isSame = true;
        sciCanvas.addH1D(currentPad, xdata, ydata, color, isSame);
    }
    
    public void setMargins(double xl, double xh, double yl, double yh){
        sciCanvas.setMargins(xl, xh, yl, yh);
    }
    
    public void setLabelSize(int size){
        ScGraphStyles.setAxisFont(new Font(Font.SANS_SERIF,Font.PLAIN,size));
        sciCanvas.repaint();
    }
}
