/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.canvas;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.jlab.data.base.TreeBrowser;
import org.jlab.data.graph.DataSet;
import org.jlab.data.histogram.HGroup;

/**
 *
 * @author gavalian
 */
public class Browser extends JFrame implements TreeSelectionListener {
    
    private ScChartCanvas   sciCanvas;
    private JTree           canvasTree;
    private JSplitPane      splitPane;
    private TreeBrowser     objBrowser;
    private Integer         currentPad = 0;

    public Browser(){
        super("CLASBrowser");
        this.setSize(1000, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.initComponents(3,2);
        this.pack();
        this.setVisible(true);
    }
    
    public Browser(TreeBrowser br, int x, int y){
        
        super("CLASBrowser");
        this.setSize(1000, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.initComponents(x,y);
        this.setTreeBrowser(br);
        
        this.pack();
        this.setVisible(true);
    }
    
    public void divide(int x, int y){
        
    }
    
    public final void initComponents(int x, int y){
        splitPane = new JSplitPane();
        splitPane.setDividerLocation(200);
        canvasTree = new JTree();
        canvasTree.addTreeSelectionListener(this);
        JScrollPane scroll = new JScrollPane(canvasTree);
        splitPane.setLeftComponent(scroll);

        sciCanvas = new ScChartCanvas(800,700,x,y);
        this.addComponentListener(sciCanvas);
        
        splitPane.setRightComponent(sciCanvas);
        this.add(splitPane);
    }
    
    public final void setTreeBrowser(TreeBrowser br){
        objBrowser = br;
        DefaultTreeModel treeModel = new DefaultTreeModel(br.getTree());
        canvasTree.setModel(treeModel);
    }
    
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        //System.err.println("selection has changed");
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
                        .getPath().getLastPathComponent();
        String dataName = node.toString();
        System.out.println("You selected " + dataName);
        DataSet  data = objBrowser.getData(dataName);
        
        double[] x = new double[data.getSize()];
        double[] y = new double[data.getSize()];
        for(int loop = 0; loop < data.getSize(); loop++){
            x[loop] = data.getX(loop);
            y[loop] = data.getY(loop);
        }
        if(currentPad>=sciCanvas.getNumPads()) currentPad = 0;
        sciCanvas.addHistogram(currentPad, x, y, 1);
        currentPad++;
    }

    public static void main(String[] args){
        Browser b = new Browser();
        HGroup group = new HGroup();
        group.add("H100", 120, 0.1, 0.8);
        group.add("H101", 120, 0.1, 0.8);
        group.add("H102", 120, 0.1, 0.8);
        
        for(int loop = 0; loop < 1000; loop++){
            group.getH1D("H100").fill(Math.random()*0.3+Math.random()*0.3+0.15);
            group.getH1D("H101").fill(Math.random()*0.5+0.15);
            group.getH1D("H102").fill(Math.random()*0.5+0.15);
        }
        b.setTreeBrowser(group);
    }
}
