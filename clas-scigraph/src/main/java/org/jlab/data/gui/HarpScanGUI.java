/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

/**
 *
 * @author gavalian
 */
public class HarpScanGUI extends JFrame {
    private ControlsPanel  controls = null;
    private ParameterPanel wirePositions = null;
    private ParameterPanel peakPositions = null;
    public HarpScanGUI(String type){
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.setSize(600, 800);
        
        if(type.compareTo("HARP3WIRE")==0){
            this.initComponentsHarp3Wire();
        }
            
        this.pack();
        this.setVisible(true);
    }
    
    private void initComponentsHarp3Wire(){
        controls = new ControlsPanel();
        this.add(controls);
        wirePositions = new ParameterPanel("Wire Positions",
                new String[]{"X wire position (min/max):","Y wire position (min/max):",
                    "Scan wire position (min/max):"});
        wirePositions.setMinMax(0,  3.0, 20.0);
        wirePositions.setMinMax(1, 32.0, 40.0);
        wirePositions.setMinMax(2, 40.0, 60.0);
        this.add(wirePositions);
        
        peakPositions = new ParameterPanel("Peak Positions",
                new String[]{"X wire peak (pos/sigma):","Y wire peak (pos/sigma):",
                    "Scan wire peak (pos/sigma):"});
        peakPositions.setMinMax(0,  9.5, 10.0);
        peakPositions.setMinMax(1, 20.0, 10.0);
        peakPositions.setMinMax(2, 53.0, 10.0);
        this.add(peakPositions);
    }
    
    public static void main(String[] args){
        new HarpScanGUI("HARP3WIRE");
    }
    
}
