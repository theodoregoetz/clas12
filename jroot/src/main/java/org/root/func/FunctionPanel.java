/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.func;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 *
 * @author gavalian
 */
public class FunctionPanel extends JPanel implements ActionListener {

    private TreeMap<String,JSpinner>  valueSpinner = new TreeMap<String,JSpinner>();
    private TreeMap<String,JSpinner>  minSpinner   = new TreeMap<String,JSpinner>();
    private TreeMap<String,JSpinner>  maxSpinner   = new TreeMap<String,JSpinner>();
    private TreeMap<String,RealParameter> parameters = new TreeMap<String,RealParameter>();
    
    public FunctionPanel(List<RealParameter> params){
        super();
        this.setBorder(BorderFactory.createTitledBorder("Parameters"));
        this.initComponents();
        for(RealParameter par : params){
            this.add(this.createParameter(par));
        }
    }
    
    public FunctionPanel(){
        super();
        this.setBorder(BorderFactory.createTitledBorder("Parameters"));
        this.initComponents();
    }
    
    public final void initComponents(){
        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        //this.add(this.createParameter(new RealParameter("mean",0.53)));
        //this.add(this.createParameter("sigma"));
        //this.add(this.createParameter("amplitude"));
        //this.add(this.createParameter("d"));
    }
    
    public final JPanel createParameter(RealParameter param){
        
        JPanel pp = new JPanel();
        pp.setLayout(new FlowLayout());
        JLabel label = new JLabel(param.name());
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setPreferredSize(new Dimension(100,20));
        pp.add( label );
        
        JSpinner sv = new JSpinner(new SpinnerNumberModel(0,-Double.MAX_VALUE,Double.MAX_VALUE,0.05));
        Dimension d = new Dimension(100,30);
        sv.setPreferredSize(d);
        sv.setValue(param.value());                
        pp.add(sv);
        
        JCheckBox limitButton = new JCheckBox("Limits");
        limitButton.setActionCommand(param.name());
        limitButton.addActionListener(this);
        pp.add(limitButton);
        
        JSpinner smin = new JSpinner(new SpinnerNumberModel(0,-Double.MAX_VALUE,Double.MAX_VALUE,0.05));
        smin.setPreferredSize(new Dimension(100,30));
        smin.setEnabled(false);
        
        JSpinner smax = new JSpinner(new SpinnerNumberModel(0,-Double.MAX_VALUE,Double.MAX_VALUE,0.05));
        smax.setPreferredSize(new Dimension(100,30));
        smax.setEnabled(false);
        
        pp.add(smin);
        pp.add(smax);
        
        this.parameters.put(param.name(), param);
        this.valueSpinner.put(param.name(), sv);
        this.minSpinner.put(param.name(), smin);
        this.maxSpinner.put(param.name(), smax);
        return pp;
    }
    
    public JPanel createParameter(String name){
        JPanel pp = new JPanel();
        pp.setLayout(new FlowLayout());

        JLabel label = new JLabel(name);
        pp.add( label);
        label.setPreferredSize(new Dimension(100,20));
        for(int loop = 0; loop < 3 ; loop++){
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0,-Double.MAX_VALUE,Double.MAX_VALUE,0.05));
            
            Dimension d = new Dimension(100,30);
            spinner.setPreferredSize(d);
            if(loop>0) spinner.setEnabled(false);
            pp.add(spinner);
        }
        
        JCheckBox limitButton = new JCheckBox("Limits");
        limitButton.setActionCommand(name);
        limitButton.addActionListener(this);
        pp.add(limitButton);
        
        return pp;
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action is performed by " + e.getActionCommand());
        RealParameter param = this.parameters.get(e.getActionCommand());
        JSpinner      sv    = this.valueSpinner.get(e.getActionCommand());
        param.setValue((Double) sv.getValue());
        if(this.parameters.get(e.getActionCommand()).isLimited()==false){
            this.minSpinner.get(e.getActionCommand()).setEnabled(true);
            this.maxSpinner.get(e.getActionCommand()).setEnabled(true);
            this.parameters.get(e.getActionCommand()).setLimits(param.value()-1.0,param.value()+1.0);
            this.minSpinner.get(e.getActionCommand()).setValue(param.min());
            this.maxSpinner.get(e.getActionCommand()).setValue(param.max());
        } else {
            this.minSpinner.get(e.getActionCommand()).setEnabled(false);
            this.maxSpinner.get(e.getActionCommand()).setEnabled(false);
            this.parameters.get(e.getActionCommand()).setFree();
        }
    }
    
    
    public static void main(String[] args){
        
        ArrayList<RealParameter>  params = new ArrayList<RealParameter>();
        
        params.add(new RealParameter("amp",0.5));
        params.add(new RealParameter("mean",0.2));
        params.add(new RealParameter("sigma",0.3));
        params.add(new RealParameter("p0",0.1));
        
        JFrame  frame = new JFrame();
        
        FunctionPanel  panel = new FunctionPanel(params);
        
        frame.add(panel,BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
