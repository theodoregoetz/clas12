/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.ui;

import java.awt.Dimension;
import javax.swing.JInternalFrame;

/**
 *
 * @author gavalian
 */
public class CLAS12MonitoringPlugin extends JInternalFrame {
    public CLAS12MonitoringPlugin(){
        super("Plugin",true,true,true,true);
        this.setSize(300,300);
        this.setPreferredSize(new Dimension(300,300));
        this.pack();
        this.setVisible(true);
    }
    
}
