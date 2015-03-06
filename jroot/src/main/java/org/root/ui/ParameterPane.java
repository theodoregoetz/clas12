/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.ui;

import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.root.func.RealParameter;

/**
 *
 * @author gavalian
 */
public class ParameterPane extends JPanel {
    GroupLayout layout = null;
    public ParameterPane(){
        super();
        layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
    }
    
    public void initWithParameters(ArrayList<RealParameter> pars){
        
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        
        for(RealParameter par : pars){
            Double value = par.value();
            Double pmin  = par.min();
            Double pmax  = par.max();
            //layout.setHorizontalGroup(
            //        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            //                .addGroup(layout.createSequentialGroup()
            //                .addComponent(new JButton("OK"))
            /*
            .addComponent(new JLabel(par.name()))
            .addComponent(new JLabel(value.toString()))
            .addComponent(new JLabel(pmin.toString()))
            .addComponent(new JLabel(pmax.toString())) */
            //));
        }
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        
        ArrayList<RealParameter>  pars = new ArrayList<RealParameter>();
        pars.add(new RealParameter("p0",10.0,0.0,100.0));
        pars.add(new RealParameter("p1",15.0,0.0,100.0));
        pars.add(new RealParameter("p2",20.0,0.0,100.0));
        pars.add(new RealParameter("p3",25.0,0.0,100.0));
        ParameterPane  pane = new ParameterPane();
        pane.initWithParameters(pars);
        frame.add(pane);
        //frame.add(pad2);
        //frame.add(pad3);
        frame.pack();
        frame.setVisible(true);
    }
}
