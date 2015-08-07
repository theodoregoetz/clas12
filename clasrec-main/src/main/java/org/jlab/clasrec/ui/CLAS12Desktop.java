/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author gavalian
 */
public class CLAS12Desktop extends JFrame implements ActionListener {
    JDesktopPane  desktop = null;
    public CLAS12Desktop(){
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,600);
        this.initDesktop();
        this.initMenuBar();
        this.setVisible(true);
        
    }
    
    
    void initDesktop(){
        desktop = new JDesktopPane();
        this.add(desktop);
    }
    
    void initMenuBar(){
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        bar.add(fileMenu);
        
        JMenu pluginMenu = new JMenu("Plugins");
        JMenuItem loadPlugin = new JMenuItem("Load");
        loadPlugin.addActionListener(this);
        pluginMenu.add(loadPlugin);
        
        bar.add(pluginMenu);
        
        this.setJMenuBar(bar);
    }
    
    public static void main(String[] args){
        CLAS12Desktop desktop = new CLAS12Desktop();
        //desktop.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Load")==0){
            desktop.add(new CLAS12MonitoringPlugin());
            System.out.println("Adding new plugin window");
            
        }
    }
}
