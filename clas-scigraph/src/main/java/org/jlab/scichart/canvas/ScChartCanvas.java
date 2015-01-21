/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.canvas;

import java.awt.BasicStroke;
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
import org.jlab.data.graph.DataSetXY;
import org.jlab.data.histogram.H1D;
import org.jlab.scichart.group.ScGroupSeries;
import org.jlab.scichart.group.ScNodePaveText;
import org.jlab.scichart.group.ScNodeSeries;
import org.jlab.scichart.group.ScRegion;
import org.jlab.scichart.styles.ColorManager;
import org.jlab.scichart.styles.FontManager;
import org.jlab.scichart.utils.FunctionFactory;
import org.jlab.scichart.utils.PainterImageExport;

/**
 *
 * @author gavalian
 */
public class ScChartCanvas extends JPanel implements ComponentListener{
    private ChartCanvasPads          canvasPads   = new ChartCanvasPads();
    private ArrayList<ScGroupSeries> series       = new ArrayList<ScGroupSeries>();
            
    public ScChartCanvas(int w, int h, int cols, int rows){
        super();
        this.setBackground(Color.WHITE);
        this.initComponents(w, h, cols, rows);
    }
    
    public final void initComponents(int w, int h, int ncol, int nrow){
        this.setSize(w, h);
        this.setPreferredSize(new Dimension(w,h));
        canvasPads.setSize(w, h, ncol, nrow);
        int ncharts = canvasPads.getNPads();
        System.err.println("Creating graphs --> " + ncharts);
        for(int loop = 0; loop < ncharts; loop++){
            ScGroupSeries chart = new ScGroupSeries(new ScRegion(
                    canvasPads.getX(loop),
                    canvasPads.getY(loop),
                    canvasPads.getWidth(loop),
                    canvasPads.getHeight(loop))
            );
            //System.err.println(chart.getFrameBox().getRegion().toString());
            series.add(chart);
        }
    }
    
    
    public void divide(int ncol, int nrow){
        canvasPads.setSize(this.getSize().width, this.getSize().height, 
                ncol, nrow);
        int ncharts = canvasPads.getNPads();
        series.clear();
        for(int loop = 0; loop < ncharts; loop++){
            ScGroupSeries chart = new ScGroupSeries(new ScRegion(
                    canvasPads.getX(loop),
                    canvasPads.getY(loop),
                    canvasPads.getWidth(loop),
                    canvasPads.getHeight(loop))
            );
            //System.err.println(chart.getFrameBox().getRegion().toString());
            series.add(chart);
        }
    }
    
    public void clearPad(int pad){
        series.get(pad).clear();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(ScGroupSeries chart : series){
            try {
                chart.paintGroup(g2d);
            } catch (Exception e){
                //..... do nothing
            }
        }
    }
    
    public void setLogX(int pad, boolean flag){ series.get(pad).setLogX(flag);}
    public void setLogY(int pad, boolean flag){ series.get(pad).setLogY(flag);}
    
    public void addLine(int pad, double[] x, double[] y, int color){
        ScNodeSeries chart = new ScNodeSeries();
        chart.setData(1, x, y,color);
        series.get(pad).addSeries(chart);
        this.repaint();
    }
    
    public void addPoints(int pad, double[] x, double[] y, int color){
        this.addPoints(pad, x, y, color, 1,8,2);
    }
            
    public void addPoints(int pad, double[] x, double[] y, int color, int mtype, 
            int msize, int mstroke){
        ScNodeSeries chart = new ScNodeSeries();
        chart.setData(2, x, y,color);
        series.get(pad).addSeries(chart);
        chart.attributes().LINE_COLOR = color;
        chart.attributes().MARKER_STYLE = mtype;
        chart.attributes().MARKER_STROKE = new BasicStroke(mstroke);
        chart.attributes().MARKER_SIZE   = msize;
        this.repaint();
    }
    
    public void addHistogram(int pad, double[] x, double[] y, int color){
        ScNodeSeries chart = new ScNodeSeries();
        chart.setData(4, x, y,color);
        chart.setStroke(new BasicStroke(2));
        //chart.setStrokeColor(new Color(100,100,255));
        chart.setStrokeColor(ColorManager.getColor(1, color));
        chart.attributes().LINE_COLOR = color;
        chart.attributes().FILL_COLOR = color;
        //series.get(pad).clear();
        series.get(pad).addSeries(chart);
        this.repaint();
    }
    
    public void addH1D(int pad, double[] x, double[] y, int color, Boolean isSame){
        ScNodeSeries chart = new ScNodeSeries();
        chart.setData(4, x, y,color);
        chart.setStroke(new BasicStroke(2));
        //chart.setStrokeColor(new Color(100,100,255));
        chart.setStrokeColor(ColorManager.getColor(1, color));
        chart.attributes().LINE_COLOR = color;
        chart.attributes().FILL_COLOR = color;
        if(isSame==false){
            series.get(pad).clear();
        }
        series.get(pad).addSeries(chart);
        this.repaint();
    }
    
    public int getNumPads(){
        return series.size();
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
         canvasPads.setSize(this.getSize().width, this.getSize().height);
        int ncharts = canvasPads.getNPads();
        for( int loop = 0; loop < ncharts; loop++){
            series.get(loop).parent().setSize(
                    canvasPads.getX(loop),
                    canvasPads.getY(loop),
                    canvasPads.getWidth(loop),
                    canvasPads.getHeight(loop) 
            );
            //series.get(loop).setAxisTitles("x", "");
        }
        this.repaint();
    }

    public void exportPNG(String file){
        PainterImageExport exporter = new PainterImageExport();
        exporter.exportPNG(series, this.getSize().width,
                this.getSize().height, file);
    }
    
    public void setAxisFontSize(int pad, int size){
        series.get(pad).setAxisFontSize(size);
    }
    public void setAxisFontSize( int size){
        for(ScGroupSeries chart : series)
            chart.setAxisFontSize(size);
    }
    public void setMargins(int pad, double mLeft, double mRight, 
            double mTop, double mBottom){
        series.get(pad).setDrawRange(mLeft, mTop, 1.0-mRight, 1.0-mBottom);
    }
    public void setMargins( double mLeft, double mRight, 
            double mTop, double mBottom){
        for(ScGroupSeries chart : series)
            chart.setDrawRange(mLeft, mTop, 1.0-mRight, 1.0-mBottom);
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
    
    public void draw(int pad,H1D hist){
        int color = 4;
        series.get(pad).clear();
        DataSetXY dataset = hist.getDataSet();
        double[] x = dataset.getDataX().getArray();
        double[] y = dataset.getDataY().getArray();
        ScNodeSeries chart = new ScNodeSeries();
        chart.setData(4, x, y,color);
        chart.setStroke(new BasicStroke(2));
        //chart.setStrokeColor(new Color(100,100,255));
        chart.setStrokeColor(ColorManager.getColor(1, color));
        chart.attributes().LINE_COLOR = color;
        chart.attributes().FILL_COLOR = color;
        //series.get(pad).clear();
        
        series.get(pad).addSeries(chart);
        //System.err.println(" XTITLE HIST = " + hist.getXTitle());
        series.get(pad).setAxisTitles(hist.getXaxis().getTitle(), "");
        String[] legend = hist.getStatText();
        ScNodePaveText statBox = new ScNodePaveText(0.01,0.01,legend);
        series.get(pad).getChildren().add(statBox);
        this.repaint();
        //this.repaint();
    }
    
    public void addLegend(int pad, double xr, double yr, String[] texts){
        ScNodePaveText legend = new ScNodePaveText(xr,yr,texts);
        series.get(pad).getChildren().add(legend);
        this.repaint();
    }
    
    public static void main(String[] args){
        int w = 1000;
        int h = 800;
        JFrame frame = new JFrame("Chart Canvas Test");
        frame.setBackground(Color.WHITE);
        frame.setSize(w,h);        
        frame.setLayout(new BorderLayout());
        
        ScChartCanvas canvas = new ScChartCanvas(w,h,2,2);
        
        H1D H1 = new H1D("BSTPaddles",100,0,10);
        H1.getXaxis().setTitle("title new");
        H1.getYaxis().setTitle("B");
        H1.setTitle("BST Paddles ");
        for(int loop =0; loop < 1000; loop++){
            H1.fill(Math.random()*8.0+1);
        }
        //System.err.println("Ku-Ku");
        //double[] xdata  = FunctionFactory.getAxisData(80, 0.0,6.5);
        //double[] ygaus  = FunctionFactory.getGaussianData(xdata,  10.5, 2.5, 0.52);
        //double[] yexpo  = FunctionFactory.getExpData(xdata, 8.5);
        canvas.setAxisFontSize(24);
        //canvas.setAxisFontSize(1, 14);
        //canvas.setAxisFontSize(2, 14);
        //canvas.setMargins( 0.1,0.02,0.05,0.2);
        //canvas.setMargins( 0.0,0.0,0.0,0.0);
        //canvas.setLogY(0, true);
        //canvas.setLogY(1, true);
        //canvas.setLogY(2, true);        
        //canvas.addLine(0,xdata, ygaus, 4);
        //canvas.addLine(1,xdata, yexpo, 3);
        FontManager fonts = new FontManager();
        for(int loop = 0; loop < 4; loop++)
            canvas.draw(loop, H1);
        //fonts.list();
        frame.add(canvas,BorderLayout.CENTER);
        frame.addComponentListener(canvas);
        frame.pack();                
        frame.setVisible(true);        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //canvas.exportPNG("/Users/gavalian/Work/test.png");
    }
}
