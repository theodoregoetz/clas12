/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.pad;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.root.base.IDataSet;

/**
 *
 * @author gavalian
 */
public class EmbeddedCanvas extends JPanel {
    public   ArrayList<EmbeddedPad>  canvasPads = new  ArrayList<EmbeddedPad>();
    private  Integer currentPad = 0;
    
    public EmbeddedCanvas(){
     super();
     this.setPreferredSize(new Dimension(500,500));
     this.divide(1, 1);
    }
    
    public EmbeddedCanvas(int xsize, int ysize){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.divide(1, 1);
    }
    
     public EmbeddedCanvas(int xsize, int ysize, int rows, int cols){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.divide(rows,cols);
    }
     
     public void cd(int pad){
         if(pad<0){
             this.currentPad = 0;
             return;
         }
         if(pad>=this.canvasPads.size()){
             this.currentPad = this.canvasPads.size()-1;
         }
     }
     
     public void draw(IDataSet dataset){
         this.canvasPads.get(this.currentPad).getPad().add(dataset);
         this.canvasPads.get(this.currentPad).repaint();
     }
     
     
     
    public final void divide(int rows, int cols){
        this.canvasPads.clear();
        this.removeAll();
        this.revalidate();
        this.setLayout(new GridLayout(rows,cols));
        int xsize = this.getWidth()/cols;
        int ysize = this.getHeight()/rows;
        for(int loop = 0; loop < cols*rows; loop++){
            EmbeddedPad pad = new EmbeddedPad(xsize,ysize);
            canvasPads.add(pad);
            this.add(pad);
        }
        this.revalidate();
        //this.update();
        this.repaint();
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1,3));
        
        EmbeddedCanvas canvas = new EmbeddedCanvas(500,500,2,3);
        frame.setSize(800, 600);
        
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }
}
