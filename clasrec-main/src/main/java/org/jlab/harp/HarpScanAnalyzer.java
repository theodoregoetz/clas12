/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.harp;

import java.util.ArrayList;
import org.root.data.DataTable;

/**
 *
 * @author gavalian
 */
public class HarpScanAnalyzer {
    
    private DataTable dataTable = new DataTable();
    private ArrayList<HarpScanData>  harpWires = new ArrayList<HarpScanData>();
    
    public HarpScanAnalyzer(){
        
    }
    
    public void openFile(String name){
        System.out.println("Openning File : " + name);
        this.dataTable.readFile(name);
        System.out.println("Columns Read  : " );
    }
    
    public void addWire(int wireColumn, double xmin, double xmax){
        HarpScanData data = new HarpScanData(dataTable,wireColumn,xmin,xmax);
        data.updateDataSet();
        harpWires.add(data);
    }
    
    public void changeWire(int wireColumn){
        for(HarpScanData data : this.harpWires){
            data.setDataColumns(0, wireColumn);
            data.updateDataSet();
        }
    }
    
    public HarpScanData getHarpWire(int index){
        return this.harpWires.get(index);
    }
    
    public void analyze(){
        for(HarpScanData data : this.harpWires){           
            data.updateDataSet();
            data.fit();
        }
    }
}
