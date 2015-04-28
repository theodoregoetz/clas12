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
import org.root.base.IDataSet;
import org.root.data.DataSetXY;

/**
 *
 * @author gavalian
 */
public class EmbeddedPad extends JPanel {
    
    private AxisRegion  padAxisFrame = new AxisRegion();
    private DataSetCollection collection = new DataSetCollection();
    
    public EmbeddedPad(){
        super();
        this.setPreferredSize(new Dimension(500,500));
    }
    
    public void drawOnCanvas(Graphics2D g2d, int xoffset, int yoffset, int w, int h){
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.padAxisFrame.getFrame().setBounds(80, 80, w-110, h-110);
        AbsDataSetDraw.drawAxisFrame(padAxisFrame, g2d,0,0,w,h);
        
        DataRegion  region = this.collection.getDataRegion();
        this.padAxisFrame.getDataRegion().copy(region);
        System.out.println(region);
        
        for(int loop = 0; loop < this.collection.getCount(); loop++){
            System.out.println(" DRAWING COLLECTION ITEM # " + loop);
            AbsDataSetDraw.drawDataSetAsGraph(padAxisFrame, g2d, 
                    collection.getDataSet(loop), 0 , 0, w, h);
        }
    }
    
    public void add(IDataSet ds){
        this.collection.addDataSet(ds);
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
        dsXY.setMarkerSize(15);
        
        DataSetXY  dsXY2 = new DataSetXY("data",x,yn);
        dsXY2.setMarkerStyle(2);
        dsXY2.setMarkerColor(4);
        dsXY2.setMarkerSize(5);
        
        EmbeddedPad pad  = new EmbeddedPad();
        pad.add(dsXY);
        pad.add(dsXY2);
        
        frame.setLayout(new GridLayout(1,3));
        frame.setSize(800, 600);
        frame.add(pad);
        frame.pack();
        frame.setVisible(true);
    }
}
