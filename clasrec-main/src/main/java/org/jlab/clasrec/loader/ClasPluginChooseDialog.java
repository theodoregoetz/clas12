/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.loader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.util.Collections.list;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jlab.clasrec.main.DetectorMonitoring;
import org.jlab.coda.clara.core.ICService;

/**
 *
 * @author gavalian
 */
public class ClasPluginChooseDialog extends JDialog implements ActionListener {
    
    private JList classList;
    DefaultListModel model;
    private String  className = "";
    private ClasPluginLoader loader = new ClasPluginLoader();
    
    public ClasPluginChooseDialog(String classname){
        super();
        loader.loadPluginDirectoryMonitoring(); 
        
        this.setPreferredSize(new Dimension(600,400));        
        this.initDialog();
        this.pack();
    }
    
    public ClasPluginChooseDialog(){
        super();
        this.setPreferredSize(new Dimension(600,400));        
        this.initDialog();
        this.pack();
    }
    
    private void initDialog(){
        
        model = new DefaultListModel();
        TreeMap<String,DetectorMonitoring> classes = loader.getPluginLoader().getMonitoringClassMap();
        for(Map.Entry<String,DetectorMonitoring> entry : classes.entrySet()){
            model.addElement(entry.getKey());
        }
        
        //for (int i = 0; i < 15; i++)
        //    model.addElement("Element " + i);
        
        classList = new JList(model);
        JScrollPane pane = new JScrollPane(classList);
        pane.setBorder(BorderFactory.createTitledBorder("Plugins"));
        //pane.setPreferredSize(new Dimension(600,400));
        JButton cancelButton = new JButton("Cancel");
        JButton chooseButton = new JButton("Choose");
        
        cancelButton.addActionListener(this);
        chooseButton.addActionListener(this);
        
        add(pane, BorderLayout.CENTER);
        JPanel  bottomPanel = new JPanel();
        bottomPanel.add(cancelButton);
        bottomPanel.add(chooseButton);
        this.add(bottomPanel,BorderLayout.PAGE_END);
    }
    
    public static void  main(String[] args){
        System.getProperties().setProperty("CLAS12DIR", "/Users/gavalian");
        ClasPluginChooseDialog dialog = new ClasPluginChooseDialog("org.jlab.clasrec.main.DetectorReconstruction");
        dialog.setVisible(true);
    }
    
    public String getSelectedClass(){
        return this.className;
    }
    
    public DetectorMonitoring getMonitoringClass(){
        return this.loader.getPluginLoader().getMonitoringClassMap().get(this.className);
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Cancel")==0){
            this.className = "";
            this.setVisible(false);
        }
        
        if(e.getActionCommand().compareTo("Choose")==0){
            int index = classList.getSelectedIndex();
            this.className = (String) classList.getSelectedValue();
            
            System.out.println("Selected = " + this.className);
            this.setVisible(false);
        }
    }
}
