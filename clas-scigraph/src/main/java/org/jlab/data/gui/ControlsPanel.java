/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author gavalian
 */
public class ControlsPanel extends JPanel implements ActionListener {
    
    public JButton openFileButton = null;
    public JComboBox wireToFit = new JComboBox();
    public ControlsPanel(){
        super();
        TitledBorder titled = new TitledBorder("Controls");
        this.setBorder(titled);
        this.setPreferredSize(new Dimension(500,120));
        this.setLayout(new GridLayout(2,2));
        /*
        * Add open button
        */
        JLabel label = new JLabel("Open File:");
        JPanel opbpanel = new JPanel();
        openFileButton = new JButton("Choose");
        this.add(label);
        opbpanel.add(openFileButton);
        openFileButton.addActionListener(this);
        this.add(opbpanel);
        /*
        * Add combo box
        */
        JLabel label2 = new JLabel("Choose wire:");
        wireToFit = new JComboBox(new String[]{"2","3","4","5","6","7","8"});
        this.add(label2);        
        this.add(wireToFit);        
    }
    
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        
        ControlsPanel panel = new ControlsPanel();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
                frame.pack();
        frame.setSize(600,300);
        frame.add(panel);
        //for(int loop = 0 ; loop < 3 ; loop++)
        frame.pack();
        frame.setVisible(true);
        
    }   

    @Override
    public void actionPerformed(ActionEvent e) {
        System.err.println("action performed");
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Harp Scan Files", "txt","text");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName() + 
                    "  " + chooser.getSelectedFile().getAbsolutePath());
        }
    }
}
