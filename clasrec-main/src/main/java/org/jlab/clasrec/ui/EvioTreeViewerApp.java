/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jlab.data.ui.DataBankPanel;
import org.jlab.evio.clas12.EvioDataBank;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;
import org.jlab.geom.gui.DetectorViewPanel;
import org.root.pad.EmbeddedCanvas;

/**
 *
 * @author gavalian
 */
public class EvioTreeViewerApp extends JInternalFrame implements ActionListener {
    private JTable  bankTable = null;
    private JSplitPane splitPane = null;
    private JTree  eventTree = null;
    private EvioSource reader = null;
    private EvioDataEvent evioEvent = null;
    private DataBankPanel  bankPanel = null;
    
    public EvioTreeViewerApp(){
        super();
        this.intiMenuBar();
        this.initComponents();
        
        this.pack();
        this.setVisible(true);
    }
    
    private void initComponents(){
        splitPane = new JSplitPane();
        splitPane.setSize(900, 700);
        splitPane.setPreferredSize(new Dimension(1200,900));
        splitPane.setDividerLocation(300);

        this.bankPanel = new DataBankPanel();
        //bankTable = new JTable(values,columns);
        eventTree = new JTree();
        eventTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                //System.out.println("clicked");
                doMouseClicked(me);
            }
        }
        );
        /*
        ScrollPane scrollPane = new ScrollPane();
        //scrollPane.setLayout(new BorderLayout());
        scrollPane.add(this.bankTable.getTableHeader(),BorderLayout.PAGE_START);
        scrollPane.add(this.bankTable, BorderLayout.CENTER);
        */
        //scrollPane.add(this.bankTable);
        splitPane.setLeftComponent(this.eventTree);
        splitPane.setRightComponent(this.bankPanel);
        
        this.add(splitPane);
    }
    
    public void doMouseClicked(MouseEvent me){
        if(me.getClickCount()==2){
            TreePath tp = this.eventTree.getPathForLocation(me.getX(), me.getY());
            if (tp != null){
                int ncount = tp.getPathCount();
                StringBuilder str = new StringBuilder();
                for(int loop = 1 ; loop < ncount; loop++){
                    if(loop!=1) str.append("/");
                    str.append(tp.getPathComponent(loop));
                }
                String objectname  = str.toString();
                //System.out.println("PATH = " + objectname);
                EvioDataBank bank = (EvioDataBank) evioEvent.getBank(objectname);
                this.bankPanel.setBank(bank);
                //this.bankTable.setModel(bank.getTableModel());
                //this.treeViewer.draw(objectname, "", "", sciCanvas);
                //tp.getPathComponent(loop);
            }
        }
    }
    
    public final void intiMenuBar(){
        JMenuBar menubar = new JMenuBar();
        JMenu    file = new JMenu("File");
        JMenu    plugins = new JMenu("Events");
        
        menubar.add(file);
        menubar.add(plugins);
        
        JMenuItem file_open = new JMenuItem("Open File");
        file_open.addActionListener(this);
        file.add(file_open);
        
        JMenuItem next_event = new JMenuItem("Next");
        next_event.addActionListener(this);
        JMenuItem prev_event = new JMenuItem("Previous");
        prev_event.addActionListener(this);
        plugins.add(next_event);
        plugins.add(prev_event);
        
        
        this.setJMenuBar(menubar);
    }
    
    public void openFile(){
        
    }
    
    public void nextEvent(){
        if(reader!=null){
            if(reader.hasEvent()==true){
                evioEvent = (EvioDataEvent) reader.getNextEvent();
                this.updateTree();
            }
        }
    }
    
    public void updateTree(){
        if(evioEvent!=null){
            String[]  banks = evioEvent.getBankList();
            DefaultMutableTreeNode top =
                    new DefaultMutableTreeNode("Banks");
            System.out.println(" BANKS in the EVENT = " + banks.length);
            for(int loop = 0; loop < banks.length; loop++){
                DefaultMutableTreeNode bank =
                    new DefaultMutableTreeNode(banks[loop]);
                top.add(bank);
            }
            DefaultTreeModel model = new DefaultTreeModel(top);
            this.eventTree.setModel(model);
        } 
    }
    
    public static void main(String[] args){
        EvioTreeViewerApp app = new EvioTreeViewerApp();
        
    }

    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().compareTo("Next")==0){
            this.nextEvent();
        }
        
        if(e.getActionCommand().compareTo("Open File")==0){
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new javax.swing.filechooser.FileFilter(){
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".evio")
                            || f.isDirectory();
                }
                
                public String getDescription() {
                    return "EVIO CLAS data format";
                }
            });
            String currentDir = System.getenv("PWD");
            if(currentDir!=null){
                fc.setCurrentDirectory(new File(currentDir));
            }
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
               System.out.println("Opening: " + file.getAbsolutePath() + "." );
               reader = new EvioSource();
               reader.open(file.getAbsolutePath());
            } else {
                System.out.println("Open command cancelled by user." );
            }
        }
    }
}
