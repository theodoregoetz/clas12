/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gavalian
 */
public class DataTable {
    
    private ArrayList<DataTableEntry>  tableEntries = new ArrayList<DataTableEntry>();
    private ArrayList<String>          columnNames  = new ArrayList<String>();
    private ArrayList<String>          tableHeader  = new ArrayList<String>();
    
    public DataTable(){
        
    }
    
    public void add(String line){
        this.add(line,"\\s+");
    }
    
    public void add(String line, String separator){
        if(line.startsWith("#")==true){
            tableHeader.add(line);
            return;
        }
        DataTableEntry entry = new DataTableEntry(line);
        if(tableEntries.size()>0){
            int nvars = tableEntries.get(0).size();
            if(nvars!=entry.size()){
                System.err.println("*** WARNING *** the size of the entry ("
                + entry.size() + ") does not match the size of array entries ("
                +nvars + ").");
            } else {
                tableEntries.add(entry);
            }
        } else {
            tableEntries.add(entry);
        }
    }
    
    public double[] getColumnAsDouble(int column, int cond_col, double cmin, double cmax){
        ArrayList<Double> buffer = new ArrayList<Double>();
        for(int loop = 0 ; loop < tableEntries.size(); loop++){
            double cond = tableEntries.get(loop).getDouble(cond_col);
            if(cond>=cmin&&cond<=cmax){
                buffer.add(tableEntries.get(loop).getDouble(column));
            }
        }
        double[] filteredBuffer = new double[buffer.size()];
        for(int loop = 0; loop < buffer.size(); loop++){
            filteredBuffer[loop] = buffer.get(loop);
        }
        return filteredBuffer;
    }
    
    public DataSetXY getDataSet(int xcol, int ycol){
        double[] xarray = this.getColumnAsDouble(xcol);
        double[] yarray = this.getColumnAsDouble(ycol);
        return new DataSetXY(xarray,yarray);
    }
    
    public DataSetXY getDataSet(int xcol, int ycol, int ccol, double min, double max){
        double[] xarray = this.getColumnAsDouble(xcol, ccol, min, max);
        double[] yarray = this.getColumnAsDouble(ycol, ccol, min, max);
        return new DataSetXY(xarray,yarray);
    }
    
    public double[]  getColumnAsDouble(int column){
        double[] buffer = new double[tableEntries.size()];
        for(int loop = 0 ; loop < tableEntries.size(); loop++){
            try {
                buffer[loop] = tableEntries.get(loop).getDouble(column);
            } catch (Exception e){
                System.err.println("** ERROR ** : invalid entry on line "
                + loop + " = [" + "]");
            }
        }
        return buffer;
    }
    
    public void createColumnMult(int colx, int coly){
        for(DataTableEntry entry : tableEntries){
            Double newValue = entry.getDouble(colx)*entry.getDouble(coly);
            entry.add(newValue.toString());
        }
    }
    
    public void createColumnAdd(int colx, int coly){
        for(DataTableEntry entry : tableEntries){
            Double newValue = entry.getDouble(colx)+entry.getDouble(coly);
            entry.add(newValue.toString());
        }
    }
    
    public void createColumnSub(int colx, int coly){
        for(DataTableEntry entry : tableEntries){
            Double newValue = entry.getDouble(colx)-entry.getDouble(coly);
            entry.add(newValue.toString());
        }
    }
    
    public void createColumnDiv(int colx, int coly){
        for(DataTableEntry entry : tableEntries){
            double denom = entry.getDouble(coly);
            if(denom==0){
                entry.add("0.0");
            } else {
                Double newValue = entry.getDouble(colx)/denom;
                entry.add(newValue.toString());
            }
        }
    }
    
    public List getHeader(){
        return tableHeader;
    }
    
    public void readFile(String filename){
        BufferedReader in = null;
        tableEntries.clear();
        tableHeader.clear();
        try {
            in = new BufferedReader(new FileReader(filename));
            while (in.ready()) {
                String s = in.readLine();
                this.add(s);
            }               
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataTable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(DataTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(DataTableEntry entry : tableEntries){
            for(int loop = 0; loop < entry.size(); loop++){
                str.append(String.format("%12.5f ",entry.getDouble(loop)));
            }
            str.append("\n");
        }
        return str.toString();
    }
    
    public void show(){
        int columns = 0;
        if(tableEntries.size()>0) columns = tableEntries.get(0).size();
        System.err.println("**** DATA TABLE : SIZE = " + tableEntries.size() 
        + "  COLUMNS = " + columns);
    }
    
}
