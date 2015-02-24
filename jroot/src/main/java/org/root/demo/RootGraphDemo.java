/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.demo;

import java.awt.GridLayout;
import javax.swing.JFrame;
import org.root.pad.RootCanvas;
import org.root.series.DataSeriesH1D;
import org.root.series.DataSeriesH2D;
import org.root.series.DataSeriesPoints;

/**
 *
 * @author gavalian
 */
public class RootGraphDemo {
    
    
    public static void main(String[] args){
        JFrame frame = new JFrame("Scigraph DEMO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        RootCanvas canvas = new RootCanvas(800,600,2,2);
        
        
        DataSeriesH1D  data = new DataSeriesH1D();
        data.generateRandom();
        
        DataSeriesH2D  data2d = new DataSeriesH2D();
        data2d.generateRandom();
        
        DataSeriesPoints  graph = new DataSeriesPoints();
        graph.generateRandom();
        graph.setDrawStyle(1);
        canvas.add(0,data2d);
        canvas.add(1,data);
        canvas.add(2,graph);
        
        
        
        frame.add(canvas);
        /*
        RootPad pad  = new RootPad(600,500);
        frame.setLayout(new GridLayout(1,3));
        F1D func = new F1D("gaus",-3.4,3.4);
        func.parameter(0).set(100, 0.0, 200.0);
        func.parameter(1).set(0.0, 0.0, 200.0);
        func.parameter(2).set(1.2, 0.0, 200.0);
        
        DataSeriesFunc series = new DataSeriesFunc(func);
        pad.addSeries(series);*/
        //frame.setSize(800, 600);                
        
        frame.pack();
        frame.setVisible(true);
    }
}
