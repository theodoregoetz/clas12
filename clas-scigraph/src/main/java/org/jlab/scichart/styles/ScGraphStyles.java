/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.styles;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Stroke;

/**
 *
 * @author gavalian
 */
public class ScGraphStyles {
    
    private static Integer axisDivisionsX = 8;
    private static Integer axisDivisionsY = 8;
    private static Font    axisFontX      = new Font(Font.SANS_SERIF,Font.PLAIN,14);
    private static Font    axisFontY      = new Font(Font.SANS_SERIF,Font.PLAIN,14);
    private static Stroke  frameStroke    = new BasicStroke(2);
    private static Font    axisTitleFont  = new Font(Font.SANS_SERIF, Font.PLAIN,18);
    private static Integer axisLabelOffset = 8;
    private static Integer axisTitleOffset = 32;
    
    
    public static void setAxisLabelOffset(int offset){
        axisLabelOffset = offset;
    }
    public static int  getAxisLabelOffset(){ return axisLabelOffset; }
    
    public static void setAxisTitleOffset(int offset){
        axisTitleOffset = offset;
    }
    public static int  getAxisTitleOffset(){ return axisTitleOffset; }
    
    public static void setAxisDivisions(int x, int y){
        axisDivisionsX = x;
        axisDivisionsY = y;
    }
    
    public static void setTitleFont(Font font){
        axisTitleFont = font;
    }
    
    public static Font getTitleFont() { return axisTitleFont; }
    
    public static void setAxisFont(Font font){
        axisFontX = font;
        axisFontY = font;
    }
    
    public static Font getAxisFont(){
        return axisFontX;
    }
    
    public static void setFrameBorderWith(int width){
        frameStroke = new BasicStroke(width);
    }
    
    public static void    setFrameStroke(Stroke str){
        frameStroke = str;
    }
    
    public static Stroke  getFrameStroke(){
        return frameStroke;
    }
    
}
