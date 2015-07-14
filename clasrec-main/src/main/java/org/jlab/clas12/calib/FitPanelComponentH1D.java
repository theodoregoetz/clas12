/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.calib;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.root.pad.EmbeddedPad;

/**
 *
 * @author gavalian
 */
public class FitPanelComponentH1D extends JPanel {
    private ComponentH1D  compH1D = null;
    private EmbeddedPad   pad     = null;
    public FitPanelComponentH1D(ComponentH1D ch){
        super();
        this.compH1D = ch;
        this.pad = new EmbeddedPad(300,300);
        this.add(this.pad);
        //this.add(this.pad,BorderLayout.CENTER);
    }
    
    public static void main(String[] args){
        JFrame  frame = new JFrame();
        ComponentH1D  ch1d = new ComponentH1D(1,1,1,100,0.4,0.7);
        ch1d.setFunction("gaus+p2");
        
        FitPanelComponentH1D panel = new FitPanelComponentH1D(ch1d);
        
        frame.add(panel,BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
    
}
