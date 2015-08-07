/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.data.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jlab.data.io.DataBank;

/**
 *
 * @author gavalian
 */
public class DataBankPanel extends JPanel {
    
    private JScrollPane  scrollPane = null;
    private JTable        table     = null;

    public DataBankPanel(){
        super();
        this.setPreferredSize(new Dimension(600,400));   
        this.initComponents();
    }
    
    public DataBankPanel(DataBank bank){
        super();
        this.setPreferredSize(new Dimension(600,400));   
        this.initComponents();
        this.setBank(bank);
    }
    
    private void initComponents(){
        this.setLayout(new BorderLayout());

        String[]   cnames = {"id","p"};
        String[][] cdata = {{"11","0.45"},{"2212","0.65"}};
        this.table = new JTable(cdata,cnames);
        
        //scrollPane.add(table);
        scrollPane = new JScrollPane(table);
        //this.table.setFillsViewportHeight(true);
        this.add(scrollPane,BorderLayout.CENTER);        
    }
    
    public void setBank(DataBank bank){
        this.table.setModel(bank.getTableModel());
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        DataBankPanel panel = new DataBankPanel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
