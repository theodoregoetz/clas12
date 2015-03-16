/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.pad;

import de.erichseifert.vectorgraphics2d.PDFGraphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    private Integer             numberOfRows    = 1;
    private Integer             numberOfColumns = 1;
    private Integer             currentPad = 0;
    
    public RootCanvas(){
        super();        
    }
    
    public RootCanvas(int xsize, int ysize, int nx, int ny){
        super();
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.divide(nx, ny);
    }
    
    public void incrementPad(){
        currentPad++;
        if(currentPad>=this.canvasPads.size()){
            currentPad = 0;
        }
    }
    
    public int getPadCount(){
        return this.canvasPads.size();
    }

    public void setCurrentPad(int pad){
        this.currentPad = pad;
    }
    
    public int getCurrentPad(){
        return this.currentPad;
    }
    
    public void divide(int cols, int rows){
        canvasPads.clear();
        this.numberOfRows = rows;
        this.numberOfColumns = cols;
        this.setCurrentPad(0);
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
    
    public void setAxisRange(int pad,double xmin, double xmax, double ymin, double ymax){
        this.canvasPads.get(pad).setAxisRange(xmin,xmax,"X");
        this.canvasPads.get(pad).setAxisRange(ymin,ymax,"Y");
        this.canvasPads.get(pad).repaint();
    }
    
    public void draw(int pad, PaveText pave){
        this.canvasPads.get(pad).addText(pave);
    }
    
    public void draw(int pad, H1D h){
        this.draw(pad,h,"");
    }
    
    public void draw(int pad, H2D h2){
        this.draw(pad, h2,"*");
    }
    
    public void setLogX(int pad, boolean flag){
        this.canvasPads.get(pad).setLogX(flag);
    }
    
    public void setLogY(int pad, boolean flag){
        this.canvasPads.get(pad).setLogY(flag);
    }
    
    public void setLogZ(int pad, boolean flag){
        this.canvasPads.get(pad).setLogZ(flag);
    }
    public void draw(int pad, H2D h2, String options){
        //DataSeriesH2D h2d = new DataSeriesH2D(h2);
         this.clear(pad);
         this.canvasPads.get(pad).addSeries(h2);
         //this.add(pad,h2d);
    }
    
    public void draw(int pad, H1D h, String options){
        if(options.contains("same")==false){
            this.canvasPads.get(pad).clear();
        }
        this.canvasPads.get(pad).addSeries(h);
        this.canvasPads.get(pad).repaint();
    }
    
    public void setOptStat(int pad, boolean flag){
        this.canvasPads.get(pad).setOptStat(flag);
    }
    
    public void setOptStat(boolean flag){
        this.setOptStat(this.currentPad, flag);
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
        
        System.out.println("ROOTCANVAS LINE " + xydata.getLineColor() + " " + xydata.getLineWidth());
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
    
    public void setXaxisDivisions(int pad, int div){
        this.canvasPads.get(pad).setAxisDivisions(0, div);
    }
    
    public void setYaxisDivisions(int pad, int div){
        this.canvasPads.get(pad).setAxisDivisions(1, div);
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
    


    public byte[] getImageBytesPNG() throws IOException{
        int cw = this.getSize().width;
        int ch = this.getSize().height;
        BufferedImage bi = new BufferedImage(cw, ch, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = bi.createGraphics();
        ig2.setColor(Color.WHITE);
        ig2.fillRect(0, 0, cw, ch);
        int padWidth  = cw/this.numberOfColumns;
        int padHeight = ch/this.numberOfRows;
        int padCounter = 0;
        for(int row = 0; row < this.numberOfRows; row++){
            for(int col = 0; col < this.numberOfColumns; col++){
                int padX = col*padWidth;
                int padY = row*padHeight;
                System.out.println("[DRAWING] --> " + 
                        String.format("%4d   DIM  %4d : %4d ( %5d x %5d )",
                                padCounter,
                                padX, padY,padWidth,padHeight));
                this.canvasPads.get(padCounter).drawOnCanvas(padX, padY,
                        padWidth, padHeight, ig2);
                padCounter++;
            }
        }
        //for(ScGroup chart : charts){
            //swingPainter.paintChart(ig2, chart);
        //chart.paintGroup(ig2);
        //}
        //ig2.drawLine(0, 0, width, height);
        byte[] result = new byte[1];// = ImageIO.
        //return result;
        
        //ImageIO.write(bi, "png", new File("saveFile.png"));
        ByteArrayOutputStream biStream = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", biStream);
        biStream.flush();
        result = biStream.toByteArray();
        return result;
    }
    
    public void export(String file){
        try {
            /*
            System.err.println("*** SAVE *** : size --> ( "
                    + width + " x " + height + " ) File = " + file);*/
            byte[] imageBytes = this.getImageBytesPNG();
            FileOutputStream output = new FileOutputStream(new File(file));
            output.write(imageBytes);
        } catch (IOException ex) {
            Logger.getLogger(RootCanvas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    public void exportPDF(String filename){
        
        int cw = this.getSize().width;
        int ch = this.getSize().height;
        PDFGraphics2D ig2 = new PDFGraphics2D(0.0, 0.0, cw, ch);
        // Draw a red ellipse at the position (20, 30) with a width of 100 and a height of 150
        //g.setColor(Color.RED);
        //g.fillOval(20, 30, 100, 150);
        ig2.setColor(Color.WHITE);
        ig2.fillRect(0, 0, cw, ch);
        int padWidth  = cw/this.numberOfColumns;
        int padHeight = ch/this.numberOfRows;
        int padCounter = 0;
        for(int row = 0; row < this.numberOfRows; row++){
            for(int col = 0; col < this.numberOfColumns; col++){
                int padX = col*padWidth;
                int padY = row*padHeight;
                System.out.println("[DRAWING] --> " + 
                        String.format("%4d   DIM  %4d : %4d ( %5d x %5d )",
                                padCounter,
                                padX, padY,padWidth,padHeight));
                this.canvasPads.get(padCounter).drawOnCanvas(padX, padY,
                        padWidth, padHeight, ig2);
                padCounter++;
            }
        }
        // Write the PDF output to a file
        FileOutputStream file;
        try {
            file = new FileOutputStream(filename);
            file.write(ig2.getBytes());
            file.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RootCanvas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RootCanvas.class.getName()).log(Level.SEVERE, null, ex);
        }            
    }
}
