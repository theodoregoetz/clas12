/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.pad;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.root.group.PlotDirectory;

/**
 *
 * @author gavalian
 */
public class DirectoryViewer extends JFrame implements TreeSelectionListener{
    
    private RootCanvas       sciCanvas;
    private JTree           canvasTree;
    private JSplitPane      splitPane;
    private PlotDirectory   hDirectory = null;
    
    public DirectoryViewer(PlotDirectory dir){
        super();
        this.hDirectory = dir;
        this.initComponents();
        this.pack();
        this.setVisible(true);
    }
    
    private void initComponents(){
        splitPane = new JSplitPane();
        splitPane.setDividerLocation(200);
        canvasTree = new JTree();
        canvasTree.addTreeSelectionListener(this);
        JScrollPane scroll = new JScrollPane(canvasTree);
        splitPane.setLeftComponent(scroll);

        sciCanvas = new RootCanvas(1000,800,1,1);
        //this.addComponentListener(sciCanvas);
        
        splitPane.setRightComponent(sciCanvas);
        this.add(splitPane);
        
        this.updateTree();
    }
    
    public void updateTree(){
         DefaultTreeModel treeModel = new DefaultTreeModel(hDirectory.getTree());
        canvasTree.setModel(treeModel);
    }
    
    public void valueChanged(TreeSelectionEvent e) {
        TreePath node =  e.getPath();
        int pathcount = node.getPathCount();
        String dataName = node.toString();
        System.out.println("You selected count = " + pathcount + "  " + dataName);
        if(pathcount==2){
            String dataname  = (String) node.getPathComponent(1).toString();
            System.out.println("Drawing group" + dataname);
            sciCanvas.draw(this.hDirectory.getGroup(dataname));
            sciCanvas.update();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
