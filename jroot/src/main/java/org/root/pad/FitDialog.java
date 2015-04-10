/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.pad;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import org.root.func.F1D;
import org.root.group.SpringUtilities;

/**
 *
 * @author gavalian
 */
public class FitDialog extends JDialog implements ActionListener {
    private JComboBox functionTypes = null;
    private JSpinner functionMinimum = null;
    private JSpinner functionMaximum = null;
    private Double   minValue        = 0.0;
    private Double   maxValue        = 1.0;
    private RootPad  rootPad         = null;
    private JButton  buttonFit       = null;
    private JButton  buttonClose       = null;
    private ParameterPanel  paramsPanel = null;
    
    public FitDialog(RootPad pad, double min, double max){
        super();
        this.rootPad = pad;
        this.minValue = min;
        this.maxValue = max;
        this.setTitle("Fit Dialog");
        this.setPreferredSize(new Dimension(400,300));
        this.initComponents();
        this.pack();
    }
    
    private void initComponents(){
        
        //this.rootPad.
        
        JPanel functionpanel = new JPanel();
        this.functionTypes   = new JComboBox();
        this.functionTypes.addItem("gaus");
        this.functionTypes.addItem("gaus+p1");
        this.functionTypes.addItem("gaus+p2");
        this.functionTypes.addItem("p1");
        this.functionTypes.addItem("p2");
        this.functionTypes.addItem("p3");
        
        
        this.functionTypes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                //doSomething();
                System.out.println(" Selection Changed To " + functionTypes.getSelectedItem());
                String functionname = (String) functionTypes.getSelectedItem();
                F1D func = new F1D(functionname, 0.0,1.0);
                int nparams = func.getNParams();
                paramsPanel.initComponents(nparams);
                
            }
        });
        functionpanel.setBorder(BorderFactory.createTitledBorder("Function"));
        functionpanel.setLayout(new SpringLayout());
        functionpanel.add(new JLabel("Function : "));
        functionpanel.add(this.functionTypes);
        SpringUtilities.makeCompactGrid(functionpanel, 
                1 , 2 , 40, 10, 40, 10);
        
        JPanel rangepanel = new JPanel();
        rangepanel.setLayout(new SpringLayout());
        rangepanel.setBorder(BorderFactory.createTitledBorder("Range"));
        this.functionMinimum = new JSpinner();
        this.functionMaximum = new JSpinner();
        Double startValue = this.minValue ;
        Double stepValue = 0.05*(this.maxValue - this.minValue);
        
        SpinnerModel modelMin =      
                
        new SpinnerNumberModel(startValue, //initial value
                               this.minValue, //min
                               this.maxValue, //max
                               stepValue);
        SpinnerModel modelMax =      
                
        new SpinnerNumberModel(this.maxValue, //initial value
                               this.minValue, //min
                               this.maxValue, //max
                               stepValue);
        /*new SpinnerNumberModel(0.1, //initial value
                               0.0, //min
                               0.5, //max
                               0.05);
        */
        this.functionMinimum.setModel(modelMin);
        this.functionMaximum.setModel(modelMax);       
        
        rangepanel.add(new JLabel("Minimum : "));
        rangepanel.add(this.functionMinimum);
        rangepanel.add(new JLabel("Maximum : "));
        rangepanel.add(this.functionMaximum);
        SpringUtilities.makeCompactGrid(rangepanel, 
                2 , 2 , 40, 10, 40, 10);
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        this.buttonClose = new JButton("Close");
        this.buttonFit = new JButton("Fit");
        this.buttonFit.addActionListener(this);
        buttonPanel.add(this.buttonFit);
        buttonPanel.add(this.buttonClose);
        
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        paramsPanel = new ParameterPanel(3);
        //this.add(buttonPanel,BorderLayout.PAGE_END);
        /*
        this.add(functionpanel,BorderLayout.PAGE_START);
        this.add(rangepanel,BorderLayout.CENTER);
        this.add(paramsPanel,BorderLayout.PAGE_END);
        */
        this.add(functionpanel);
        this.add(rangepanel);
        this.add(paramsPanel);
    }
    
    public static void main(String[] args){
        //FitDialog dialog = new FitDialog();
        //dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Fit")==0){
            String funcLine = (String) this.functionTypes.getSelectedItem();
            double minf = (Double) this.functionMinimum.getValue();
            double maxf = (Double) this.functionMaximum.getValue();
            F1D func = new F1D(funcLine,minf,maxf);
            func.setParameter(0, 2000);
            func.setParameter(1, minf + 0.5*(maxf-minf));
            func.setParameter(2, 0.5*(maxf-minf));
            this.rootPad.fitPadData(func);
        }
    }
}
