/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.pad;

import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpringLayout;
import org.root.group.SpringUtilities;

/**
 *
 * @author gavalian
 */
public class ParameterPanel extends JPanel {
    private ArrayList<JSpinner>  parameterInputs = new ArrayList<JSpinner>();
    public ParameterPanel(int nparams){
        super();
        this.initComponents(nparams);
    }
    
    public final void initComponents(int nparams){
        this.removeAll();
        this.parameterInputs.clear();
        this.setBorder(BorderFactory.createTitledBorder("Parameters"));
        this.setLayout(new SpringLayout());
        for(int loop = 0; loop < nparams; loop++){
            String labelName = loop + " : ";
            this.add(new JLabel(labelName));
            JSpinner spinner = new JSpinner();
            this.add(spinner);
            
            this.add(new JLabel("Fixed (NO)"));
        }
        SpringUtilities.makeCompactGrid(this, 
                nparams , 3 , 20, 10, 20, 10);
        this.updateUI();
        //this.repaint();
    }
}

