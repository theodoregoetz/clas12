/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.harp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import org.root.group.SpringUtilities;
import org.root.pad.RootCanvas;

/**
 *
 * @author gavalian
 */
public class HarpScanDialog extends JDialog {
    private RootCanvas  canvas = null;
    private JPanel PropertiesPanel = null;
    private Integer numberOfWires         = 0;
    public HarpScanDialog(String filename, int nwires){
        super();
        this.setPreferredSize(new Dimension(900,700));
        this.numberOfWires = nwires;
        this.initComponents();
        this.pack();
    }
    
    public final void initComponents(){
        this.PropertiesPanel = new JPanel();        
        this.add(this.PropertiesPanel,BorderLayout.LINE_START);
        
        this.PropertiesPanel.add(this.getParameterPanel());
        //this.PropertiesPanel.add(this.getParameterPanel());
        //this.PropertiesPanel.add(this.getParameterPanel());
        
        canvas = new RootCanvas(800,600,1,this.numberOfWires);
        this.add(this.canvas,BorderLayout.CENTER);
    }
    
    public JPanel getParameterPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Function"));
        panel.setLayout(new SpringLayout());
        panel.add(new JLabel("Minimum : "));
        JTextField textEntryMin = new JTextField(6);
        panel.add(textEntryMin);
        panel.add(new JLabel("Minimum : "));
        JTextField textEntryMax = new JTextField(6);
        panel.add(textEntryMax);
        SpringUtilities.makeCompactGrid(panel, 
                2 , 2 , 20, 10, 20, 10);
        return panel;
    }
    
}
