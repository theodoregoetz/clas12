/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import org.root.data.DataSetXY;
import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class DataSetPad {
    
    private  DataSetCollection   collection = new DataSetCollection();
    private  AxisRegion        padAxisFrame = new AxisRegion();    
    private  ArrayList<LatexText>  textCollection = new ArrayList<LatexText>();
    
    public DataSetPad(){
        this.padAxisFrame.getAxisX().setMaxTicks(6);
        this.padAxisFrame.getAxisY().setMaxTicks(8);
    }
    
    public void drawOnCanvas(Graphics2D g2d, int xoffset, int yoffset, int w, int h){
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.padAxisFrame.getFrame().setBounds(120, 50, w-150, h-130);
        
        DataRegion  region = this.collection.getDataRegion();
        this.padAxisFrame.setDataRegion(region);
        //System.out.println(region);
        AbsDataSetDraw.drawAxisBackGround(padAxisFrame, g2d,0,0,w,h);
        for(int loop = 0; loop < this.collection.getCount(); loop++){
            //System.out.println(" DRAWING COLLECTION ITEM # " + loop);
            IDataSet  ds = collection.getDataSet(loop);
            if(ds instanceof DataSetXY){
                AbsDataSetDraw.drawDataSetAsGraph(padAxisFrame, g2d, 
                        collection.getDataSet(loop), 0 , 0, w, h);
            }
            
            if(ds instanceof H1D){
                AbsDataSetDraw.drawDataSetAsHistogram1D(padAxisFrame, g2d, 
                        ds, 0 , 0, w, h);
            }
        }
        AbsDataSetDraw.drawAxisFrame(padAxisFrame, g2d,0,0,w,h);
        
        for(LatexText txt : this.textCollection){
            AbsDataSetDraw.drawText(padAxisFrame, g2d, txt, 0,0,w,h);
        }
        /*
        Font textFont = new Font("Helvetica",Font.PLAIN,24);
        g2d.setFont(textFont);
        String latex = LatexTextTools.convertUnicode("x^2 + #pi^3 = #gamma");
        AttributedString latexString = LatexTextTools.converSuperScript(latex);
        //AttributedString latexString = new AttributedString(latex);
        latexString.addAttribute(TextAttribute.SIZE, (float)36);
        latexString.addAttribute(TextAttribute.FAMILY, "Helvetica");
        //latexString.addAttribute(TextAttribute., "Helvetica");
        //latexString.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, 10,11);
        //g2d.drawString(latex, 200,30);
        g2d.drawString(latexString.getIterator(), 250,30);
        */
    }
    
    public void add(IDataSet ds){
        this.collection.addDataSet(ds);
        if(this.collection.getCount()==1){
            //this.padAxisFrame.setXTitle();
        }
    }
    
    public void setAutoScale(Boolean flag){
        this.collection.setAutoScale(flag);
    }
    
    public void addText(LatexText txt){
        this.textCollection.add(txt);
    }
}
