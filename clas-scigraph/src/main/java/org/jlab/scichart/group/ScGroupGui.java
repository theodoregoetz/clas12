/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.group;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jlab.scichart.utils.FunctionFactory;
import org.jlab.scichart.utils.PainterImageExport;

/**
 *
 * @author gavalian
 */
public class ScGroupGui extends JPanel implements ComponentListener {
    
    private ScGroup  group  = new ScGroup();
    private ScRegion region = new ScRegion();
    ScNodeAxisPair   axis   = new ScNodeAxisPair();
    
    public ScGroupGui(int w, int h){
        super();
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(w,h));
        region.setSize(0.0,0.0, w, h);
        
        group = new ScGroupSeries(region);
        //group.setParent(region);
        //group.setDrawRange(0.15, 0.05, 0.95, 0.85);
        
        //ScNode node = new ScNode();
        //ScNodePaveText pave = new ScNodePaveText(0.,0.0,
        //new String[]{ "H1000" ,
        //"mean     23.567",
        //"sigma     2.045",
        //"error     0.024"});
        //group.getChildren().add(node);
        //axis.setAxisMinMax(-0.1, 5.1, -0.1, 6.0);
        //group.getChildren().add(axis);
        //group.getChildren().add(pave);
        double[] xdata  = FunctionFactory.getAxisData(120, 0.0,6.5);
        double[] ydata  = FunctionFactory.getGaussianData(xdata,  10.5, 2.5, 0.5);
        double[] ydata2 = FunctionFactory.getGaussianData(xdata, 13.5, 2.5, 0.5);
        double[] yexp   = FunctionFactory.getExpData(xdata, 8.5);
        double[] ydiff  = FunctionFactory.getSubtracted(yexp, ydata);
        
        ScNodeSeries series = new ScNodeSeries();
        series.setData(2, xdata, ydiff,1);
        ScNodeSeries series2 = new ScNodeSeries();
        series2.setData(1, xdata, ydiff,2);
        ScNodeSeries seriesLine = new ScNodeSeries();
        seriesLine.setData(1, xdata, ydata,0);
        ScNodeSeries seriesGaus = new ScNodeSeries();
        seriesGaus.setData(2, xdata, ydata2,3);
        
        
        ScGroupSeries groupSeries = (ScGroupSeries) group;
        //groupSeries.setLogY(true);
        //groupSeries.setLogX(true);
        //groupSeries.addSeries(seriesGaus);
        //groupSeries.addSeries(seriesLine);
        //groupSeries.addSeries(series2);
        //groupSeries.addSeries(series);
        groupSeries.addSeries(seriesGaus);
        //group.getChildren().add(seriesGaus);
        //group.getChildren().add(series);
        
    }
    
    
    
    public void saveFile(){
        /*
        ArrayList<ScGroup> groups = new ArrayList<ScGroup>();
        groups.add(group);
        PainterImageExport export = new PainterImageExport();
        export.exportPNG(groups, this.getSize().width,
                this.getSize().height,"temp.png");*/
    }
    @Override
    public void paint(Graphics g) {        
        super.paint(g);
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2d.setRenderingHints(rh);
        group.paintGroup(g2d);
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        region.setSize(0.0, 0.0, this.getSize().width, 
                this.getSize().height);
        //System.err.println("Parent: " + group.toString());
        this.repaint();
        this.saveFile();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        
    }

    @Override
    public void componentShown(ComponentEvent e) {
        
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        
    }
    
    public static void main(String[] args) {        
        int wd = 800;
        int hd = 700;
        JFrame frame = new JFrame("LightWitghtChart");
        frame.setBackground(Color.WHITE);        
        frame.setLayout(new BorderLayout());
        ScGroupGui pad = new ScGroupGui(wd,hd);
        frame.setSize(wd,hd);
        frame.add(pad,BorderLayout.CENTER);
        frame.addComponentListener(pad);
        frame.pack();                
        frame.setVisible(true);        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
