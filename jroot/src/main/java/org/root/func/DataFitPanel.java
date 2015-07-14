/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.func;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.root.base.IDataSet;
import org.root.histogram.H1D;
import org.root.pad.EmbeddedPad;

/**
 *
 * @author gavalian
 */
public class DataFitPanel extends JDialog implements ActionListener {
    EmbeddedPad   dataPad    = null;
    FunctionPanel funcPanel  = null;
    IDataSet      dataSet    = null;
    
    public DataFitPanel(IDataSet ds, F1D func){
        super();
        this.dataSet = ds;
        this.initComponents(func.parameters());
        this.pack();
    }
    
    public void initComponents(List<RealParameter> params){
        
        this.setLayout(new BorderLayout());
        dataPad = new EmbeddedPad(400,400);
        this.add(this.dataPad,BorderLayout.CENTER);

        this.funcPanel = new FunctionPanel(params);
        this.add(this.funcPanel,BorderLayout.PAGE_START);
        
        JPanel  buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        JButton fitButton   = new JButton("Fit");
        buttonPanel.add(fitButton);
        this.add(buttonPanel,BorderLayout.PAGE_END);
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Fit")==0){
            System.out.println("Fitting the distribution");
        }
    }
    
    public static void main(String[] args){
        DataFitPanel panel = new DataFitPanel(new H1D("h1",100,0.3,0.6) ,new F1D("gaus+p1",10,15));
        panel.setVisible(true);
    }

}
