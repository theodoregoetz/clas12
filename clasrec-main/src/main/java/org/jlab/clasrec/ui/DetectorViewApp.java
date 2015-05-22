/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import org.jlab.clasrec.loader.ClasPluginChooseDialog;
import org.jlab.clasrec.utils.DataBaseLoader;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.detector.ec.ECDetector;
import org.jlab.geom.detector.ec.ECFactory;
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
    
    public DetectorViewApp(){
        super("Detector View App");
        this.initMenuBar();
        this.initComponents();
        this.setSize(900, 700);
        this.pack();
        this.setVisible(true);
    }
    
    public void setHistogramDrawer(IDetectorHistogramDraw drawer){
        this.histogramDrawer = drawer;
    }
    
    public void initDetector(String name){
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

    public void detectorSelected(int sectore, int layer, int component) {
        System.out.println("I GOT CALLBACK");
        if(this.histogramDrawer!=null){
            this.histogramDrawer.drawComponent(sectore, layer, component, canvas);
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
            
        }
        
        if(e.getActionCommand().compareTo("Load Plugin")==0){
            ClasPluginChooseDialog dialog = new ClasPluginChooseDialog("");
            dialog.setModal(true);
            dialog.setVisible(true);
        }
    }
}
