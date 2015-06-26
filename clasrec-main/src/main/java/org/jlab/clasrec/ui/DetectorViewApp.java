/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import org.jlab.clas.tools.utils.FileUtils;
import org.jlab.clasrec.loader.ClasPluginChooseDialog;
import org.jlab.clasrec.main.DetectorMonitoring;
import org.jlab.clasrec.utils.CLASGeometryLoader;
import org.jlab.clasrec.utils.DataBaseLoader;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.detector.ec.ECDetector;
import org.jlab.geom.detector.ec.ECFactory;
import org.jlab.geom.detector.ft.FTCALDetector;
import org.jlab.geom.detector.ft.FTCALFactory;
import org.jlab.geom.detector.ftof.FTOFDetector;
import org.jlab.geom.detector.ftof.FTOFFactory;
import org.jlab.geom.gui.DetectorComponentUI;
import org.jlab.geom.gui.DetectorLayerPanel;
import org.jlab.geom.gui.DetectorLayerUI;
import org.jlab.geom.gui.DetectorViewPanel;
import org.jlab.geom.gui.IDetectorComponentSelection;

import org.root.pad.EmbeddedCanvas;

/**
 *
 * @author gavalian
 */
public class DetectorViewApp extends JFrame implements IDetectorComponentSelection,ActionListener {
    
    private JSplitPane splitPane;
    private EmbeddedCanvas canvas;
    private DetectorViewPanel detectorView;
    private IDetectorHistogramDraw histogramDrawer = null;
    private DetectorMonitoring     monitoringClass = null;
    
    public DetectorViewApp(){
        super("Detector View App");
        
        this.initComponents();
        this.initMenuBar();
        this.setSize(900, 700);
        this.pack();
        this.setVisible(true);
    }
    
    public DetectorViewApp(DetectorMonitoring mon){
        super("Detector View App");
        this.initMenuBar();
        this.initComponents();
        this.initDetector(mon.getName());
        this.monitoringClass = mon;
        this.setSize(900, 700);
        this.pack();
        this.setVisible(true);
    }
    
    public void setHistogramDrawer(IDetectorHistogramDraw drawer){
        this.histogramDrawer = drawer;
    }
    
    public final void initDetector(String name){
        
        if(name.compareTo("BST")==0){
            CLASGeometryLoader  loader = new CLASGeometryLoader();
            List<DetectorComponentUI>  components = loader.getLayerUI("BST",0,0);
            if(components==null){
                System.out.println("ERROR: no components created");
            }
            DetectorLayerUI  layerUI = new DetectorLayerUI();
            layerUI.setComponents((ArrayList<DetectorComponentUI>) components);
            layerUI.updateDrawRegion();
            DetectorLayerPanel panelBST = new DetectorLayerPanel();
            panelBST.layerUI = layerUI;
            panelBST.setSelectionListener(this);
            this.detectorView.addDetectorLayer("BST", panelBST);
        }
        
        if(name.compareTo("EC")==0){
            ConstantProvider  ecdb  = DataBaseLoader.getConstantsEC();
            ECFactory   factory  = new ECFactory();
            ECDetector  detector = factory.createDetectorCLAS(ecdb);
            
            String[] names = new String[]{"PCAL U View","PCAL V View","PCAL W View"};
            
            for(int loop = 0; loop < 3; loop++){
                List<DetectorComponentUI>  components = detector.getLayerUI(0,0,loop);
                DetectorLayerUI  layerUI = new DetectorLayerUI();
                layerUI.setComponents((ArrayList<DetectorComponentUI>) components);
                layerUI.updateDrawRegion();
                DetectorLayerPanel panelEC = new DetectorLayerPanel();
                panelEC.layerUI = layerUI;
                panelEC.setSelectionListener(this);
                this.detectorView.addDetectorLayer(names[loop], panelEC);
            }
        }
        if(name.compareTo("FTOF")==0){
            ConstantProvider  ecdb  = DataBaseLoader.getConstantsFTOF();
            FTOFFactory   factory  = new FTOFFactory();
            FTOFDetector  detector = factory.createDetectorCLAS(ecdb);
            
            String[] names = new String[]{"Panel 1A","Panel 1B","Panel2"};
            
            for(int loop = 0; loop < 3; loop++){
                List<DetectorComponentUI>  FTOF = new ArrayList<DetectorComponentUI>();
                for(int sector = 0; sector < 6 ; sector++){
                    List<DetectorComponentUI>  components = detector.getLayerUI(sector,loop,0);
                    FTOF.addAll(components);
                }
                
                DetectorLayerUI  layerUI = new DetectorLayerUI();
                layerUI.setComponents((ArrayList<DetectorComponentUI>) FTOF);
                layerUI.updateDrawRegion();
                DetectorLayerPanel panelEC = new DetectorLayerPanel();
                panelEC.layerUI = layerUI;
                panelEC.setSelectionListener(this);
                this.detectorView.addDetectorLayer(names[loop], panelEC);
            }
        }
        if(name.compareTo("FTCAL")==0){
            ConstantProvider  ftcaldb  = DataBaseLoader.getConstantsFTCAL();
            FTCALFactory   factory  = new FTCALFactory();
            FTCALDetector  detector = factory.createDetectorCLAS(ftcaldb);
            List<DetectorComponentUI>  FTCAL = detector.getLayerUI(0, 0, 0);
            DetectorLayerUI  layerUI = new DetectorLayerUI();
            layerUI.componentColorEven = new Color(245,121,148);
            layerUI.componentColorOdd  = new Color(241,155,161);
            layerUI.setComponents((ArrayList<DetectorComponentUI>) FTCAL);
            layerUI.updateDrawRegion();
            DetectorLayerPanel panelFTCAL = new DetectorLayerPanel();
            panelFTCAL.layerUI = layerUI;
            panelFTCAL.setSelectionListener(this);
            this.detectorView.addDetectorLayer("FTCAL", panelFTCAL);
        }
    }
    
    private void initComponents(){
        splitPane = new JSplitPane();
        splitPane.setSize(900, 700);
        splitPane.setPreferredSize(new Dimension(1200,900));
        splitPane.setDividerLocation(900);
        
        canvas = new EmbeddedCanvas(800,400,3,1);
        detectorView = new DetectorViewPanel();
        
        splitPane.setLeftComponent(this.detectorView);
        splitPane.setRightComponent(this.canvas);
        
        this.add(splitPane);
    }
    
    public static void main(String[] args){
        DetectorViewApp app = new DetectorViewApp();
        app.initDetector("FTOF");
    }

    public void processFile(String filename){
        ProcessMonitoringDialog dialog = new ProcessMonitoringDialog(this,filename,this.monitoringClass);
        dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(this);
        SwingUtilities.invokeLater(dialog);
        //dialog.run();
        /*
        while(dialog.isActive()==true){
            // Waiting
        }*/
        System.out.println("DONE PROCESSING FILE");
    }
    public void processDir(List<String> filename){
        ProcessMonitoringDialog dialog = new ProcessMonitoringDialog(this,filename,this.monitoringClass);
        dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(this);
        SwingUtilities.invokeLater(dialog);
        //dialog.run();
        /*
        while(dialog.isActive()==true){
            // Waiting
        }*/
        System.out.println("DONE PROCESSING FILE");
    }
    
    public void detectorSelected(int sectore, int layer, int component) {
        //System.out.println("I GOT CALLBACK");
        //if(this.histogramDrawer!=null){
        //    this.histogramDrawer.drawComponent(sectore, layer, component, canvas);
        //}
        if(this.monitoringClass!=null){
            try {
                this.monitoringClass.drawComponent(sectore, layer, component, canvas);
            } catch (Exception e){
                System.out.println("Oppps ! problem with SECTOR/LAYER/COMPONENT "
                + sectore + " " + layer + "  " + component );
            }
        }
    }
    
    private void initMenuBar(){
        JMenuBar menubar = new JMenuBar();
        JMenu    file = new JMenu("File");
        JMenu    plugins = new JMenu("Plugins");
        
        menubar.add(file);
        menubar.add(plugins);
        
        JMenuItem file_open = new JMenuItem("Process File..");
        file_open.addActionListener(this);
        file.add(file_open);
        
        JMenuItem load_plugin = new JMenuItem("Load Plugin");
        load_plugin.addActionListener(this);
        plugins.add(load_plugin);
        
        
        this.setJMenuBar(menubar);
    }
    
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().compareTo("Process File..")==0){
            final JFileChooser fc = new JFileChooser();
            /*
            fc.setFileFilter(new javax.swing.filechooser.FileFilter(){
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".evio")
                            || f.isDirectory();
                }
                
                public String getDescription() {
                    return "EVIO CLAS data format";
                }
            });*/
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            String currentDir = System.getenv("PWD");
            if(currentDir!=null){
                fc.setCurrentDirectory(new File(currentDir));
            }
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if(file.isDirectory()){
                    List<String>  files = FileUtils.getFilesInDir(
                            file.getAbsolutePath());
                    this.processDir(files);
                } else {
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getAbsolutePath() + "." );
                }
               this.processFile(file.getAbsolutePath());
            } else {
                System.out.println("Open command cancelled by user." );
            }
        }
        
        if(e.getActionCommand().compareTo("Load Plugin")==0){
            ClasPluginChooseDialog dialog = new ClasPluginChooseDialog("");
            dialog.setModal(true);
            dialog.setVisible(true);
            this.monitoringClass = (DetectorMonitoring) dialog.getMonitoringClass();
            System.out.println("SELECTED PLUGIN FOR MONITORING : " + this.monitoringClass.getName());
            this.initDetector(this.monitoringClass.getName());
            
        }
    }
}
