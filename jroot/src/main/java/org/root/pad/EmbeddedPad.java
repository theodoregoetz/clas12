/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.pad;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.root.base.AbsDataSetDraw;
import org.root.base.AxisRegion;
import org.root.base.DataRegion;
import org.root.base.DataSetCollection;
import org.root.base.DataSetPad;
import org.root.base.IDataSet;
import org.root.base.LatexText;
import org.root.data.DataSetXY;
import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class EmbeddedPad extends JPanel {
    
    private  DataSetPad   dPad = new DataSetPad();
    
    public EmbeddedPad(){
        super();
        this.setPreferredSize(new Dimension(500,500));
    }
    
    public EmbeddedPad(int xsize, int ysize){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
    }
    
    
    public DataSetPad getPad(){ return this.dPad;}
    
    public void drawOnCanvas(Graphics2D g2d, int xoffset, int yoffset, int w, int h){
        dPad.drawOnCanvas(g2d, xoffset, yoffset, w, h);
    }
    
    public void add(IDataSet ds){
        this.dPad.add(ds);
    }
    
    public void addText(LatexText txt){
        this.dPad.addText(txt);
    }
    
    public void setAutoScale(Boolean flag){
        this.dPad.setAutoScale(flag);
    }
    
    public void setAxisRange(double xmin, double xmax, double ymin, double ymax){
        this.dPad.setAxisRange(xmin, xmax, ymin, ymax);
    }
    
    @Override
    public void paint(Graphics g){        
        Graphics2D g2d = (Graphics2D) g;

        int w = this.getSize().width;
        int h = this.getSize().height;
        this.drawOnCanvas(g2d, 0,0, w, h);
        g2d.setStroke(new BasicStroke(1));
        /*
        for(int loop = 0; loop < 120000; loop++){
            int rX = (int) (100+Math.random()*300);
            int rY = (int) (100+Math.random()*300);
            g2d.drawLine(rX,rY,rX,rY);
        }*/
    }
    
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        
        double[] x = new double[]{1.0,2.0,3.0,4.0,5.0};
        double[] y = new double[]{0.5,0.2,0.1,0.45,0.7};
        double[] yn = new double[]{0.9,1.2,1.6,2.45,1.7};
        
        DataSetXY  dsXY = new DataSetXY("data",x,y);
        dsXY.setMarkerStyle(2);
        dsXY.setMarkerColor(3);
        dsXY.setMarkerSize(10);
        
        DataSetXY  dsXY2 = new DataSetXY("data",x,yn);
        dsXY2.setMarkerStyle(2);
        dsXY2.setMarkerColor(4);
        dsXY2.setMarkerSize(10);
        
        H1D  h1 = new H1D("h1",50,0.0,2.0);
        H1D  h2 = new H1D("h1",50,0.0,2.0);
        for(int loop = 0; loop < 5000; loop++){
            h1.fill(Math.random()*2.0);
        }
        
        for(int loop = 0; loop < 3000; loop++){
            h2.fill(Math.random()*2.0);
        }
        
        h1.setLineColor(1);
        h1.setFillColor(3);
        h1.setLineWidth(1);
        
        h2.setFillColor(9);
        
        EmbeddedPad pad  = new EmbeddedPad(); 
        pad.add(h1);
        pad.add(h2);
        LatexText tex = new LatexText("M^x(ep#rarrow e^'p #pi^+ #pi^- (#gamma) (e^#uarrow^#darrow) )",0.05,0.1);
        tex.setColor(0);
        tex.setFontSize(24);
        pad.addText(tex);
        
        //pad.add(dsXY);
        //pad.add(dsXY2);
        pad.setAutoScale(Boolean.TRUE);
        frame.setLayout(new GridLayout(1,3));
        frame.setSize(800, 600);
        frame.add(pad);
        frame.pack();
        frame.setVisible(true);
    }
}
