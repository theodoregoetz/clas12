/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jlab.scichart.group.ScGroup;
import org.jlab.scichart.group.ScGroupSeries;


/**
 *
 * @author gavalian
 */
public class PainterImageExport {

    public byte[] getImageBytesPNG(ArrayList<ScGroupSeries> charts,
            int width, int height) throws IOException{
        
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = bi.createGraphics();
        ig2.setColor(Color.WHITE);
        ig2.fillRect(0, 0, width, height);
        for(ScGroup chart : charts){
            //swingPainter.paintChart(ig2, chart);
            chart.paintGroup(ig2);
        }
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
    
    public void exportPNG(ArrayList<ScGroupSeries> groups, int width, int height, String file){
        try {
            System.err.println("*** SAVE *** : size --> ( "
            + width + " x " + height + " ) File = " + file);
            byte[] imageBytes = this.getImageBytesPNG(groups, width, height);
            FileOutputStream output = new FileOutputStream(new File(file));
            output.write(imageBytes);
        } catch (IOException ex) {
            Logger.getLogger(PainterImageExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
