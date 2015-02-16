/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.pad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.root.base.IDrawableDataSeries;
import org.root.series.DataSeriesPoints;

/**
 *
 * @author gavalian
 */
public class RootPad extends JPanel implements MouseListener,ActionListener {
    
    private int         drawMargin = 40;
    private int         drawMarginRightX = 60;
    private int         drawMarginRightY = 60;
    private int         drawMarginLeftX  = 40;
    private int         drawMarginLeftY  = 60;
    
    private String      titleString      = "Graph";
    private String      axisString       = "0123456";
    
    private GraphAxis   graphAxisX = new GraphAxis();
    private GraphAxis   graphAxisY = new GraphAxis();
    private Font        graphAxisFont = new Font(Font.SANS_SERIF,Font.PLAIN,12);
    private Font        graphAxisTitleFont = new Font(Font.SANS_SERIF,Font.PLAIN,14);

    private ArrayList<IDrawableDataSeries> padSeries = new ArrayList<IDrawableDataSeries>();
    private ArrayList<IDrawableDataSeries> padFits   = new ArrayList<IDrawableDataSeries>();
    
    public RootPad(){
        super();
        this.setPreferredSize(new Dimension(500,500));
    }
    
    public RootPad(int xsize, int ysize){
        super();
        this.graphAxisX.setMinMax(0.0, 5.);
        this.graphAxisY.setMinMax(0.0, 5.);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.addMouseListener(this);
    }
    
    public void addSeries(IDrawableDataSeries series){
        padSeries.add(series);
        this.repaint();
    }
    
    public void clear(){
        padSeries.clear();
        this.graphAxisX.setMinMax(0.0, 5.0);
        this.graphAxisY.setMinMax(0.0, 5.0);
        this.repaint();
    }
    
    @Override
    public void paint(Graphics g){

        Graphics2D g2d = (Graphics2D) g;        

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(Color.black);
        FontMetrics axisFM   = g2d.getFontMetrics( this.graphAxisFont);
        FontMetrics titleFM  = g2d.getFontMetrics( this.graphAxisTitleFont);
        
        this.calculateMargins(axisFM, titleFM);
        
        int axisX = this.drawMarginLeftX;
        int axisY = this.getSize().height - this.drawMarginRightY;
        int w = this.getSize().width  - this.drawMarginRightX - this.drawMarginLeftX;
        int h = this.getSize().height - this.drawMarginLeftY - this.drawMarginRightY;
        
        if(padSeries.size()>0){
            this.graphAxisX.setMinMax(padSeries.get(0).getMinX(), 
                    padSeries.get(0).getMaxX());
            this.graphAxisY.setMinMax(padSeries.get(0).getMinY(), 
                    padSeries.get(0).getMaxY());

        }

        for(int loop = 0; loop < padSeries.size(); loop++){
            padSeries.get(loop).draw(graphAxisX, graphAxisY, g2d);
        }
        
        for(int loop = 0; loop < padFits.size(); loop++){
            padFits.get(loop).draw(graphAxisX, graphAxisY, g2d);
        }
        
        // Drawing Axis
        //this.graphAxisX.setMinMax(0.0, 1.0);
        this.graphAxisX.setOrigin(axisX,axisY);
        this.graphAxisX.setLength(w);
        this.graphAxisX.setWidth(h);
        
        this.graphAxisX.draw(g2d);
        //this.graphAxisY.setMinMax(0.0, 250.0);
        this.graphAxisY.setOrigin(axisX, axisY);
        this.graphAxisY.setLength(h);
        this.graphAxisY.setWidth(w);
        this.graphAxisY.setVertical(true);
        this.graphAxisY.draw(g2d);
        
        
        //DataSeriesPoints xyData = new DataSeriesPoints();        
        //xyData.draw(graphAxisX, graphAxisY, g2d);
        //g2d.drawRect(this.drawMargin,this.drawMargin, w, h);
        //this.paintColorMap(g2d, 500, 500);
    }
    
    public void paintColorMap(Graphics2D g2d, int xsize, int ysize){
        double w = this.getSize().width  - this.drawMargin*2;
        double h = this.getSize().height - this.drawMargin*2;
        double stepSizeX = w/xsize;
        double stepSizeY = h/ysize;
        for(int nc = 0 ; nc < xsize ; nc++){
            for(int nr = 0 ; nr < ysize; nr++){
               int startX = (int) (this.drawMargin + nc*stepSizeX);
               int startY = (int) (this.drawMargin + nr*stepSizeY);
               int nextX  = (int) (this.drawMargin + (nc+1)*stepSizeX);
               int nextY  = (int) (this.drawMargin + (nr+1)*stepSizeY);
               int shade = (int) (Math.random()*255);
               g2d.setPaint(new Color(shade,0,0));
               g2d.fillRect(startX, startY, nextX - startX, nextY - startY);
            }
        }
    }
    
    private void calculateMargins(FontMetrics axisTicks, FontMetrics title){
        this.drawMarginLeftX = title.getHeight()*2 + axisTicks.stringWidth(axisString);
        this.drawMarginLeftY = title.getHeight()*2;
        this.drawMarginRightX  = 20;
        this.drawMarginRightY  = title.getHeight()*2 + axisTicks.getHeight()*2;
    }
    
    public void fitPadData(String function){
        IDrawableDataSeries fitFunction = this.padSeries.get(0).fit(function, "");
        //this.addSeries(fitFunction);
        this.padFits.clear();
        this.padFits.add(fitFunction);
        this.repaint();
    }
    
    public void showPopupMenu(int x, int y){
        JPopupMenu menu = new JPopupMenu();
        menu.add(new JLabel("    Pad"));
        menu.addSeparator();
        JMenuItem itemFit = new JMenuItem("Fit...");
        itemFit.addActionListener(this);        
        menu.add(itemFit);
        
        /**
         * Fit Menu Description
         */
        JMenu menuFit = new JMenu("Fit");
        
        JMenuItem fitGaus = new JMenuItem("Gaus");
        fitGaus.addActionListener(this);
        menuFit.add(fitGaus);
        
        JMenuItem fitPol = new JMenuItem("Polynomial");
        fitPol.addActionListener(this);
        menuFit.add(fitPol);
        
        JMenuItem fitPolGaus = new JMenuItem("Gaus+Polynomial");
        fitPolGaus.addActionListener(this);
        menuFit.add(fitPolGaus);
        
        menu.add(menuFit);
        /*
        * END Of Fit Menu
        */
        menu.addSeparator();
        menu.add(new JMenuItem("Options"));
        menu.addSeparator();
        menu.add(new JMenuItem("Properties"));
        menu.addSeparator();
        menu.add(new JMenuItem("Export PDF"));
        menu.add(new JMenuItem("Export PNG"));
        
        JMenu fontSizeMenu = new JMenu("Font Sizse");
        String[] fontSizes = new String[]{"12","14","18","24"};
        for(int loop = 0; loop < fontSizes.length;loop++){
            JMenuItem itemsize = new JMenuItem(fontSizes[loop]);
            itemsize.addActionListener(this);
            fontSizeMenu.add(itemsize);
        }

        menu.addSeparator();
        menu.add(fontSizeMenu);
        menu.show(this, x, y);        
    }
    
    /*
    public void addMouseFunctions(){
        this.addMouseListener(new MouseAdapter(){
            @Override
                public void mousePressed(MouseEvent e) {
                    System.out.println("--- mouse was pressed ---" 
                     + e.getButton());
                    if(e.getButton()==3){
                        JPopupMenu menu = new JPopupMenu();
                        menu.add(new JLabel("    Pad"));
                        menu.addSeparator();
                        menu.add(new JMenuItem("Fit..."));
                        menu.add(new JMenuItem("Options"));
                        menu.addSeparator();
                        menu.add(new JMenuItem("Properties"));
                        menu.show(RootPad.this, e.getX(), e.getY());
                        //showPopupMenu();
                    }
                }
        });
    }
    */
    public static void main(String[] args){
        JFrame frame = new JFrame();

        RootPad pad  = new RootPad(600,500);
        RootPad pad2 = new RootPad(400,300);
        RootPad pad3 = new RootPad(400,300);
        DataSeriesPoints points = new DataSeriesPoints();
        points.generateRandom();
        pad.addSeries(points);
        frame.setLayout(new GridLayout(1,3));
        frame.setSize(800, 600);
        frame.add(pad);
        //frame.add(pad2);
        //frame.add(pad3);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println("--- mouse was clicked ---" 
        //                + e.getButton());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("--- mouse was pressed ---" 
                     + e.getButton());
        if(e.getButton()==3){
            this.showPopupMenu(e.getX(), e.getY());
        }
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action = " + e.getActionCommand());
        if(e.getActionCommand().startsWith("1")==true||
                e.getActionCommand().startsWith("2")==true){
            this.graphAxisX.setFont(Integer.parseInt(e.getActionCommand()));
            this.graphAxisY.setFont(Integer.parseInt(e.getActionCommand()));
            this.repaint();            
        }
        
        if(e.getActionCommand().compareTo("Gaus")==0){
            this.fitPadData("gaus");
        }
        
        if(e.getActionCommand().compareTo("Polynomial")==0){
            this.fitPadData("p2");
        }
        
        if(e.getActionCommand().compareTo("Gaus+Polynomial")==0){
            this.fitPadData("gaus+p1");
        }
        
    }
}
