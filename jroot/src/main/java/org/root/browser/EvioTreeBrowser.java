/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.browser;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jlab.evio.clas12.EvioDataBank;
import org.jlab.evio.clas12.EvioDataDictionary;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioFactory;
import org.jlab.evio.clas12.EvioSource;
import org.root.base.IDrawableDataSeries;
import org.root.data.DataVector;
import org.root.histogram.H1D;
import org.root.pad.RootCanvas;
import org.root.series.DataSeriesH1D;

/**
 *
 * @author gavalian
 */
public class EvioTreeBrowser extends JFrame implements TreeSelectionListener,ActionListener {
    private JSplitPane splitPane;
    private JTree  evioTree = null;
    private RootCanvas rootCanvas =  null;
    private EvioSource  reader    = new EvioSource();
    final JFileChooser fc = new JFileChooser();
    private String currentDirectory = ".";
    private Integer defaultHistogramBins = 100;
    private String[] bankFilter = new String[]{"EVENTHB","HitBasedTrkg",
        "TimeBasedTrkg","FTOF"};
    
    public EvioTreeBrowser(String cd){
        super("Evio Graphics Browser");
        this.currentDirectory = cd;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.initMenuBar();
        this.initComponents();
        this.pack();
        //this.setVisible(true);
    }
    
    public  void initTreeViewer(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("EVIO");
        String[]  descriptors = EvioFactory.getDictionary().getDescriptorList();
        for(String desc : descriptors){
            DefaultMutableTreeNode node_desc = new DefaultMutableTreeNode(desc);
            String[] entries = EvioFactory.getDictionary().getDescriptor(desc).getEntryList();
            for(String entry : entries){
                node_desc.add(new DefaultMutableTreeNode(entry));
            }
            for(String filter : bankFilter){
                if( desc.contains(filter)==true){
                    root.add(node_desc);
                }
            }

        }
        evioTree.setModel(new DefaultTreeModel(root));
    }
    
    private void initComponents(){
        
        splitPane = new JSplitPane();
        splitPane.setDividerLocation(300);
        evioTree = new JTree();
        evioTree.addTreeSelectionListener(this);
        JScrollPane scroll = new JScrollPane(evioTree);
        //scroll.setBackground(Color.blue);
        //evioTree.setBackground(Color.red);
        splitPane.setLeftComponent(scroll);

        rootCanvas = new RootCanvas(1000,800,1,1);
        //this.addComponentListener(rootCanvas);
        
        splitPane.setRightComponent(rootCanvas);
        this.add(splitPane);
    }
    
    private void initMenuBar(){
        JMenuBar menubar = new JMenuBar();
        JMenu    file = new JMenu("File");
        menubar.add(file);
        JMenuItem file_open = new JMenuItem("Open File..");
        file_open.addActionListener(this);
        file.add(file_open);
        
        this.setJMenuBar(menubar);
    }
    
    public void processFile(String bank, String var){
        reader.reset();
        DataVector dataVector = new DataVector();
        int ecounter = 0;
        int icounter = 0;
        while(reader.hasEvent()){
            EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
            //System.out.println(event.getDictionary().toString());
            //EvioDataDictionary dict = (EvioDataDictionary) event.getDictionary();
            //dict.show();
            ecounter++;
            if(event.hasBank(bank)==true){
                icounter++;
                EvioDataBank evioBank = (EvioDataBank) event.getBank(bank);
                int rows = evioBank.rows();
                for(int loop = 0; loop < rows; loop++){                    
                    dataVector.add(evioBank.getFloat(var, loop));
                }
            }
        }
        
        if(dataVector.getSize()>0){
        
            H1D histogram = new H1D(var,this.defaultHistogramBins,
                    dataVector.getMin(),dataVector.getMax()
            );
            System.out.println("Create histogram = " + 
                   defaultHistogramBins + "  " +
                    dataVector.getMin() + "   " + dataVector.getMax() );
            for(int loop = 0; loop < dataVector.getSize(); loop++){
                histogram.fill(dataVector.getValue(loop));
            }
            
            DataSeriesH1D h1d = new DataSeriesH1D(histogram);
            this.rootCanvas.clear(0);
            this.rootCanvas.add(0,h1d);
        }
        System.out.println("done.... events = " + ecounter + " banks = " + icounter
               + " result = " + dataVector.getSize());
    }
    
    public void valueChanged(TreeSelectionEvent e) {
        TreePath node =  e.getPath();
        int pathcount = node.getPathCount();
        if(pathcount==3){
            String bankName = (String) node.getPathComponent(1).toString();
            String varName  = (String) node.getPathComponent(2).toString();
            System.out.println("Path = " + pathcount + " BANK [" + bankName + 
                    "]  VAR = ["
            +  varName + "]");
            this.processFile(bankName, varName);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Open File..")==0){
            fc.setCurrentDirectory(new File(this.currentDirectory));
            int returnVal = fc.showOpenDialog(this); 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                 File file = fc.getSelectedFile();
                 reader = new EvioSource();
                 reader.open(file.getPath());            
                 //This is where a real application would open the file.
                 System.out.println("Opening: " + file.getPath() + "." );
                 this.initTreeViewer();
            } else {
                System.out.println("Open command cancelled by user." );
            }
        }
    }
    
    public static void main(String[] args){
        
        String pwd = System.getenv("PWD");
        System.err.println(" Directory = " + pwd);
        EvioTreeBrowser viewer = new EvioTreeBrowser(pwd);
        System.setProperty("CLAS12DIR", "/Users/gavalian/Work/Software/Release-7.0/COATJAVA/coatjava");       
        viewer.setVisible(true);
    }

}
