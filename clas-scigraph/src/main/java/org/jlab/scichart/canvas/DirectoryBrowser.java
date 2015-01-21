/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.scichart.canvas;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jlab.data.graph.DataSet;
import org.jlab.data.histogram.HDirectory;

/**
 *
 * @author gavalian
 */
public class DirectoryBrowser extends JPanel implements TreeSelectionListener {
    private HDirectory histDirectory = new HDirectory();
    private ScChartCanvas   sciCanvas;
    private JTree           canvasTree;
    private JSplitPane      splitPane;
    private Integer         currentPad = 0;
    
    public DirectoryBrowser(){
        super();
        this.initComponents();
    }

    public DirectoryBrowser(HDirectory dir){
        super();
        this.histDirectory = dir;
        this.initComponents();
    }
    
    public DirectoryBrowser(HDirectory dir, int x, int y){
        super();
        this.histDirectory = dir;
        this.initComponents();
        this.setZone(x, y);
    }
    
    public void setDirectory(HDirectory dir){
        this.histDirectory = dir;
        this.updateTree();
    }
    
    private void initComponents(){
        splitPane = new JSplitPane();
        splitPane.setDividerLocation(200);
        canvasTree = new JTree();
        canvasTree.addTreeSelectionListener(this);
        JScrollPane scroll = new JScrollPane(canvasTree);
        splitPane.setLeftComponent(scroll);

        sciCanvas = new ScChartCanvas(1000,800,1,1);
        this.addComponentListener(sciCanvas);
        
        splitPane.setRightComponent(sciCanvas);
        this.add(splitPane);
        
        this.updateTree();
    }

    public void updateTree(){
         DefaultTreeModel treeModel = new DefaultTreeModel(histDirectory.getTree());
        canvasTree.setModel(treeModel);
    }
    
    public HDirectory getDirectory(){ return this.histDirectory; }
    
    public void setZone(int x, int y){
        sciCanvas.divide(x, y);
    }
    
    public void valueChanged(TreeSelectionEvent e) {
        System.err.println("Selection listener");
        //DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath();
        TreePath node =  e.getPath();
        int pathcount = node.getPathCount();
        String dataName = node.toString();
        System.out.println("You selected count = " + pathcount + "  " + dataName);
        if(pathcount==3){
            String groupname = (String) node.getPathComponent(1).toString();
            String dataname  = (String) node.getPathComponent(2).toString();
            System.out.println("You selected count = " + groupname + "  " + dataname);
            
            DataSet data = this.histDirectory.group(groupname).
                    getData(dataname);
            double[] x = new double[data.getSize()];
            double[] y = new double[data.getSize()];
            for(int loop = 0; loop < data.getSize(); loop++){
                x[loop] = data.getX(loop);
                y[loop] = data.getY(loop);
            }
            
            //sciCanvas.divide(1, 1);
            if(currentPad>=sciCanvas.getNumPads()) currentPad = 0;
            sciCanvas.clearPad(currentPad);
            sciCanvas.addHistogram(currentPad, x, y, 4);
            currentPad++;
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(1000, 700);
        DirectoryBrowser browser = new DirectoryBrowser();
        frame.add(browser);
        frame.pack();
        //browser.setZone(2, 2);
        browser.getDirectory().addGroup("PANEL1A");
        browser.getDirectory().addGroup("PANEL1B");
        browser.getDirectory().addGroup("PANEL2");
        
        browser.getDirectory().group("PANEL1A").add("SECTOR_1_PADDLE_1", 200, -1.0, 2000);
        browser.getDirectory().group("PANEL1A").add("SECTOR_1_PADDLE_2", 200, -1.0, 2000);
        browser.getDirectory().group("PANEL1A").add("SECTOR_1_PADDLE_3", 200, -1.0, 2000);
        
        browser.updateTree();
        
        frame.setVisible(true);
    }
}
