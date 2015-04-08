/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.root.data.NTuple;
import org.root.pad.RootCanvas;

/**
 *
 * @author gavalian
 */
public class InterpreterDialog extends JDialog {
    JTextArea  commandsHistory = null;
    JTextField commandLine     = null;
    private RootCanvas canvas  = null;
    
    private TreeMap<Integer,NTuple> ntuples = new TreeMap<Integer,NTuple>();
    
    public InterpreterDialog(){
        super();
        this.setTitle("JROOT Interpreter");
        this.initComponents();
        this.pack();
        /*
        JDialog dialog = new JDialog();
        RootCanvas  canvas = new RootCanvas(600,600,1,1);

        dialog.add(canvas);
        dialog.pack();
        dialog.setVisible(true);
        */
    }
    
    private void initComponents(){
        commandsHistory = new JTextArea();
        commandLine     = new JTextField();
        //TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "History");
        //commandsHistory.setBorder(border);
        //commandLine.setBorder(new EmptyBorder(8,8,8,8));
        commandsHistory.setPreferredSize(new Dimension(600,400));
        commandsHistory.setEditable(false);
        commandLine.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        commandsHistory.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
        
        commandLine.addKeyListener(new KeyAdapter() {
    
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    JTextField textField = (JTextField) e.getSource();
                    String text = textField.getText();
                    processLine(text);
                }
                //JTextField textField = (JTextField) e.getSource();
                //String text = textField.getText();
                //textField.setText(text.toUpperCase());
            }
            
            @Override
            public void keyTyped(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        
        canvas = new RootCanvas(600,600,1,1);
        this.add(canvas,BorderLayout.LINE_END);
        this.add(commandsHistory,BorderLayout.CENTER);
        this.add(commandLine,BorderLayout.PAGE_END);
    }
    
    public void processLine(String line){
        System.out.println("Processing Line = " + line);
        if(line.length()>2){
            String[] tokens = line.split("\\s+");
            this.commandLine.setText("");
            this.commandsHistory.append("prompt[] ");
            this.commandsHistory.append(line);
            this.commandsHistory.append("\n");
            this.processCommand(tokens);
        }
    }
    
    public void processCommand(String[] tokens){
        if(tokens[0].compareTo("zone")==0){
            if(tokens.length<3){
                this.canvas.divide(1, 1);
            } else {
                this.canvas.divide(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
            }
        }
        
        if(tokens[0].compareTo("open/text")==0){
            if(tokens.length>2){
                int lund = Integer.parseInt(tokens[1]);
                NTuple  ntuple = new NTuple(tokens[1],28);
                ntuple.readFile(tokens[2]);
                this.ntuples.put(lund, ntuple);
            }
        }
        
        if(tokens[0].compareTo("ntuple/scan")==0){
            if(tokens.length>1){
                int lund = Integer.parseInt(tokens[1]);
                NTuple t = this.ntuples.get(lund);
                t.scan();
            }
        }
        
        if(tokens[0].compareTo("ntuple/plot")==0){
            if(tokens.length>2){
                String condition = "";
                if(tokens.length>3){
                    condition = tokens[3];
                }
                int lund = Integer.parseInt(tokens[1]);
                NTuple t = this.ntuples.get(lund);
                t.draw(tokens[2], condition, "", canvas);
            }
        }
    }
    
    public static void main(String[] args){
        InterpreterDialog dialog = new InterpreterDialog();
        dialog.setVisible(true);
    }
}
