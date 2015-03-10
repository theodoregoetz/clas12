/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.pad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author gavalian
 */
public class GraphicsTests extends JPanel {
    public GraphicsTests(){
        super();
        this.setPreferredSize(new Dimension(500,500));
    }
    @Override
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;       
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.drawText(g2d);
    }
    
    public void drawText(Graphics2D g2d){
        g2d.drawLine(100,100,400,400);
        Font textFont = new Font(Font.SERIF,Font.PLAIN,24);
        AttributedString as = new AttributedString("\u03B5x2=\u03C72");
        as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, 1, 2);
        as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, 5, 6);
        as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, 2, 3);
        as.addAttribute(TextAttribute.SIZE, (float)36);
        as.addAttribute(TextAttribute.FAMILY, Font.MONOSPACED);
        //as.addAttribute(TextAttribute., TextAttribute.SUPERSCRIPT_SUPER, 4, 6);
        as.addAttribute(TextAttribute.FOREGROUND, Color.RED, 2, 6);
        //as.addAttribute(TextAttribute.FONT, textFont);
        g2d.drawString(as.getIterator(), 80, 200);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        GraphicsTests pad = new GraphicsTests();
        frame.add(pad);
        //frame.add(pad2);
        //frame.add(pad3);
        frame.pack();
        frame.setVisible(true);
    }
    
}
