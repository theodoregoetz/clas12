/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.group;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
import org.root.pad.RootCanvas;

/**
 *
 * @author gavalian
 */
public class TBrowserDrawDialog extends JDialog implements ActionListener {
    private ArrayList<String> branches = new ArrayList<String>();
    private JComboBox         comboVariableX = null;
    private JComboBox         comboVariableY = null;
    private JTextField        conditionEntry = null;
    private JTextField        optionsEntry   = null;
    private JButton           buttonDraw     = null;
    private JButton           buttonClose    = null;
    private ITreeViewer       treeViewer    = null;
    private RootCanvas        drawCanvas     = null;
    
    public TBrowserDrawDialog(ITreeViewer treeV, RootCanvas canvas){
        super();
        this.setTitle("Tree PLOT");
        this.setPreferredSize(new Dimension(400,280));
        this.setMinimumSize(new Dimension(400,280));
        this.setMaximumSize(new Dimension(800,280));
        List<String> variables = treeV.getVariables();
        for(String var : variables){
            this.branches.add(var);
        }
        this.treeViewer = treeV;
        this.drawCanvas  = canvas;
        this.initComponents();
        this.pack();
    }
    
    public TBrowserDrawDialog(String[] vars){
        super();
        this.setTitle("Tree PLOT");
        this.setPreferredSize(new Dimension(400,280));
        this.setMinimumSize(new Dimension(400,280));
        this.setMaximumSize(new Dimension(800,280));
        for(String var : vars){
            this.branches.add(var);
        }
        this.initComponents();
        this.pack();
        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    
    
    private void initComponents(){
        this.comboVariableX = new JComboBox();
        this.comboVariableY = new JComboBox();
        
        for(String var : this.branches){
            this.comboVariableX.addItem(var);
            this.comboVariableY.addItem(var);
        }
        
        /*
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(this.comboVariableX)
                .addComponent(this.comboVariableY));*/
        JPanel panelVariableChoice = new JPanel();
        panelVariableChoice.setBorder(BorderFactory.createTitledBorder("Variables"));
        SpringLayout layout = new SpringLayout();
        panelVariableChoice.setLayout(layout);
        JLabel labelX = new JLabel("X : ");
        JLabel labelY = new JLabel("Y : ");
        panelVariableChoice.add(labelX);
        panelVariableChoice.add(this.comboVariableX);
        panelVariableChoice.add(labelY);
        panelVariableChoice.add(this.comboVariableY);
        SpringUtilities.makeCompactGrid(panelVariableChoice, 
                2 , 2 , 40, 10, 40, 10);
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new SpringLayout());
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Cuts & Options"));
        this.conditionEntry = new JTextField(10);
        this.optionsEntry   = new JTextField();
        JLabel labelCut = new JLabel("Cut : ");
        selectionPanel.add(labelCut);       
        selectionPanel.add(this.conditionEntry);
        selectionPanel.add(new JLabel("Options : "));
        selectionPanel.add(this.optionsEntry);
        
        SpringUtilities.makeCompactGrid(selectionPanel, 
                2 , 2 , 40, 10, 40, 10);
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        this.buttonClose = new JButton("Close");
        this.buttonDraw = new JButton("Draw");
        this.buttonDraw.addActionListener(this);
        buttonPanel.add(this.buttonDraw);
        buttonPanel.add(this.buttonClose);
        this.add(panelVariableChoice,BorderLayout.PAGE_START);
        this.add(selectionPanel,BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.PAGE_END);        
    }
    
    public static void main(String[] args){
        
        String[] vars = {"a","b","c","d","e"};
        TBrowserDrawDialog dialog = new TBrowserDrawDialog(vars);
        dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Draw")==0){
            String xvar = (String) this.comboVariableX.getSelectedItem();
            String yvar = (String) this.comboVariableY.getSelectedItem();          
            String selection = this.conditionEntry.getText();
            String drawVariable = yvar + "%" + xvar;
            if(xvar.compareTo(yvar)==0) drawVariable = xvar;
            this.treeViewer.draw(drawVariable, selection,"", drawCanvas);
        }
    }
}
