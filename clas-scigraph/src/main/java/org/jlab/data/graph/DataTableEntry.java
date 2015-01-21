/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.graph;

import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class DataTableEntry {
    
    private ArrayList<String>  data = new  ArrayList<String>();
    public DataTableEntry(String strLine){
        this.set(strLine, "\\s+");
    }
    
    public void set(String line){
        this.set(line, "\\s+");
    }
    
    public void set(String line,String separator){
        String[] tokens = line.split(separator);
        data.clear();
        for(String item : tokens)
            data.add(item);
    }
    
    public int size(){
        return data.size();
    }
    
    public double getDouble(int index){
        return Double.parseDouble(data.get(index));
    }
    
    public void add(String value){
        data.add(value);
    }
    
    public void setDouble(int index, Double value){
        //data.get(index) = value.toString();
    }
}
