/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.jlab.clasrec.main.DetectorMonitoring;
import org.jlab.evio.clas12.EvioDataChain;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class ProcessMonitoringDialog extends JDialog implements Runnable {
    
    private DetectorMonitoring monitorClass = null;
    private String  inputFileName = "";
    private ArrayList<String>  inputFileList = new ArrayList<String>();
    private JProgressBar  bar = null;
    private JLabel        statusText = null;
    
    public ProcessMonitoringDialog(JFrame frame){
        super(frame);
        this.initComponents();
        this.setPreferredSize(new Dimension(600,600));
        this.pack();
        this.setVisible(true);
    }
    
    public ProcessMonitoringDialog(){
        super();
        this.initComponents();
        this.setPreferredSize(new Dimension(600,600));
        this.pack();
        this.setVisible(true);
    }

    public ProcessMonitoringDialog(JFrame frame,String filename, DetectorMonitoring dm){
        super(frame);
        this.initComponents();
        this.setTitle("Processing File....");
        this.setPreferredSize(new Dimension(600,250));
        this.pack();
        this.inputFileName = filename;
        this.monitorClass = dm;
        this.setVisible(true);
    }
    
    public ProcessMonitoringDialog(JFrame frame,List<String> filename, DetectorMonitoring dm){
        super(frame);
        this.initComponents();
        this.setTitle("Processing File....");
        this.setPreferredSize(new Dimension(600,250));
        this.pack();
        for(String file : filename){
            this.inputFileList.add(file);
        }
        this.monitorClass = dm;
        this.setVisible(true);
    }
    public ProcessMonitoringDialog(String filename, DetectorMonitoring dm){
        super();
        this.initComponents();
        this.pack();
        this.inputFileName = filename;
        this.monitorClass = dm;
        this.setVisible(true);
    }
    
    private void initComponents(){
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createBevelBorder(10));
               
        bar = new JProgressBar();
                
        statusText = new JLabel("Status:");
        panel.add(bar,BorderLayout.CENTER);
        panel.add(statusText,BorderLayout.PAGE_END);
        //this.add(bar,BorderLayout.CENTER);
        //this.add(statusText,BorderLayout.PAGE_END);
        this.add(panel,BorderLayout.CENTER);
    }
    
    public void run() {
        if(this.inputFileList.size()==0){
            EvioSource reader = new EvioSource();
            reader.open(inputFileName);
            this.monitorClass.init();
            bar.setMaximum(0);
            bar.setMaximum(reader.getSize());
            
            int counter = 0;
            while(reader.hasEvent()){
                counter++;
                EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
                try {
                    this.monitorClass.processEvent(event);
                } catch (Exception e){
                    System.out.println("SOMETHING WRONG WITH THE EVENT");
                }
                bar.setValue(counter);
                statusText.setText("Status : processed " + counter);
            }
            this.monitorClass.analyze();
        } else {
            EvioDataChain reader = new EvioDataChain();
            for(String file : this.inputFileList){
                reader.addFile(file);
            }
            reader.open();
            this.monitorClass.init();
            bar.setMaximum(0);
            bar.setMaximum(this.inputFileList.size());
            
            int counter = 0;
            while(reader.hasEvent()){
                counter++;
                EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
                try {
                    this.monitorClass.processEvent(event);
                } catch (Exception e){
                    System.out.println("SOMETHING WRONG WITH THE EVENT");
                }
                bar.setValue(counter);
                statusText.setText("Status : processed " + counter);
            }
            this.monitorClass.analyze();
        }                
    }
    
    
}
