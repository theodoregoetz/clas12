/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.group;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.root.histogram.H1D;
import org.root.histogram.H2D;
import org.root.pad.RootCanvas;

/**
 *
 * @author gavalian
 */
public class TBrowser extends JFrame implements ActionListener {
    private TDirectory browserDirectory = new TDirectory();
    private RootCanvas       sciCanvas;
    private JTree           canvasTree;
    private JSplitPane      splitPane;
    private JMenuBar        browserMenuBar = null;
    
    public TBrowser(){
        
    }
    
    public TBrowser(TDirectory dir){
        super("TBrowser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIManager.put("Tree.rendererFillBackground", false);
        this.browserDirectory = dir;
        this.initMenuBar();
        this.initComponents();
        this.pack();
        this.setVisible(true);
    }
    
    public final void initComponents(){
        splitPane = new JSplitPane();
        splitPane.setDividerLocation(200);
        canvasTree = new JTree();
        //canvasTree.addTreeSelectionListener(this);
        JScrollPane scroll = new JScrollPane(canvasTree);
        splitPane.setLeftComponent(scroll);

        sciCanvas = new RootCanvas(1000,800,1,1);
        //this.addComponentListener(sciCanvas);
        
        splitPane.setRightComponent(sciCanvas);
        this.add(splitPane);
        
        canvasTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                //System.out.println("clicked");
                doMouseClicked(me);
            }
        });
        
        DefaultTreeCellRenderer renderer =
                (DefaultTreeCellRenderer) canvasTree.getCellRenderer();
        renderer.setTextSelectionColor(Color.red);
        renderer.setBackgroundSelectionColor(Color.blue);
        renderer.setBorderSelectionColor(Color.black);
               
        canvasTree.setBackground(new Color(240,240,240));
        this.updateTreeView();
        //this.updateTree();
    }
    
    public void updateTreeView(){
         DefaultTreeModel treeModel = new DefaultTreeModel(this.browserDirectory.getTree());
        canvasTree.setModel(treeModel);
    }
    
    public final void initMenuBar(){
        this.browserMenuBar = new JMenuBar();
        JMenu  fileMenu   = new JMenu("File");
        JMenuItem fileOpen  = new JMenuItem("Open File");
        fileOpen.addActionListener(this);
        JMenuItem fileQuit  = new JMenuItem("Quit");
        fileMenu.add(fileOpen);
        fileMenu.addSeparator();
        fileMenu.add(fileQuit);
        
        JMenu  editMenu   = new JMenu("Edit");
        
        JMenu  canvasMenu   = new JMenu("Canvas");
        
        JMenu  splitMenu    = new JMenu("Split");
        JMenuItem ssize1x1  = new JMenuItem("1x1");
        JMenuItem ssize1x2  = new JMenuItem("1x2");
        JMenuItem ssize2x2  = new JMenuItem("2x2");
        JMenuItem ssize1x3  = new JMenuItem("1x3");
        JMenuItem ssize2x3  = new JMenuItem("2x3");
        JMenuItem ssize3x3  = new JMenuItem("3x3");
        
        ssize1x1.addActionListener(this);
        ssize1x2.addActionListener(this);
        ssize1x3.addActionListener(this);
        ssize2x2.addActionListener(this);
        ssize2x3.addActionListener(this);
        ssize3x3.addActionListener(this);
        
        splitMenu.add(ssize1x1);
        splitMenu.add(ssize1x2);
        splitMenu.add(ssize2x2);
        splitMenu.add(ssize1x3);
        splitMenu.add(ssize2x3);
        splitMenu.add(ssize3x3);
        canvasMenu.add(splitMenu);
        
        

        this.browserMenuBar.add(fileMenu);
        this.browserMenuBar.add(editMenu);
        this.browserMenuBar.add(canvasMenu);
        
        this.setJMenuBar(browserMenuBar);
    }
    
    public void doMouseClicked(MouseEvent me){
        if(me.getClickCount()==2){
            //System.out.println(" Mouse was clicked");
            TreePath tp = canvasTree.getPathForLocation(me.getX(), me.getY());
            if (tp != null){
                int ncount = tp.getPathCount();
                StringBuilder str = new StringBuilder();
                for(int loop = 1 ; loop < ncount-1; loop++){
                    if(loop!=1) str.append("/");
                    str.append(tp.getPathComponent(loop));
                }
                String directory  = str.toString();
                String objectname = tp.getPathComponent(ncount-1).toString();                
                System.out.println("Tree Selection dir : " + directory + 
                        "  obj : " + objectname);
                if(this.browserDirectory.hasDirectory(directory)==true){
                    if(this.browserDirectory.getDirectory(directory).hasObject(objectname)==true){
                        Object obj = this.browserDirectory.getDirectory(directory).getObject(objectname);
                        if(obj instanceof H1D){
                            this.sciCanvas.clear(this.sciCanvas.getCurrentPad());
                            this.sciCanvas.draw(this.sciCanvas.getCurrentPad(), (H1D) obj);
                            this.sciCanvas.incrementPad();
                        }
                        
                        if(obj instanceof H2D){
                            this.sciCanvas.clear(this.sciCanvas.getCurrentPad());
                            this.sciCanvas.draw(this.sciCanvas.getCurrentPad(), (H2D) obj);
                            this.sciCanvas.incrementPad();
                        }
                    }
                }
            }
        }
    }

    public void openFile(String fullpath, String filename){
        this.browserDirectory = new TDirectory(filename);
        this.browserDirectory.readFile(fullpath);
        
        this.updateTreeView();
    }
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().compareTo("Open File")==0){
            JFileChooser fc = new JFileChooser();
            String pwd = System.getenv("PWD");
            if(pwd!=null){
                fc.setCurrentDirectory(new File(pwd));
            }
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                
                //This is where a real application would open the file.
                System.out.println("Opening: " + file.getAbsolutePath() + "." );
                this.openFile(file.getAbsolutePath(),file.getName());
            } else {
                //log.append("Open command cancelled by user." + newline);
            }
        }
        
        if(e.getActionCommand().compareTo("1x1")==0){
            this.sciCanvas.divide(1, 1);
        }
        if(e.getActionCommand().compareTo("1x2")==0){
            this.sciCanvas.divide(1, 2);
        }
        if(e.getActionCommand().compareTo("1x3")==0){
            this.sciCanvas.divide(1, 3);
        }
        if(e.getActionCommand().compareTo("2x2")==0){
            this.sciCanvas.divide(2, 2);
        }
        
        if(e.getActionCommand().compareTo("2x3")==0){
            this.sciCanvas.divide(2, 3);
        }
        
        if(e.getActionCommand().compareTo("3x3")==0){
            this.sciCanvas.divide(3, 3);
        }
        
    }
}
