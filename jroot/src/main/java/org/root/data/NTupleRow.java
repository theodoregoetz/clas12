/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.data;

import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class NTupleRow {
    
    private final ArrayList<Double>  tupleData = new ArrayList<Double>();

    public NTupleRow(double[] data){
        this.init(data);
    }
    
    public NTupleRow(String[] data){
        this.init(data);
    }
    
    public NTupleRow(String data){
        this.init(data);
    }
    
    public final void init(double[] data){
        tupleData.clear();
        for(double item : data){
            tupleData.add(item);
        }
    }
    
    public final void init(String[] data){
        tupleData.clear();
        for(String item : data){
            tupleData.add(Double.parseDouble(item));
        }
    }
    
    public final void init(String data){
        String[] tokens = data.split("\\s+");
        this.init(tokens);
    }
    
    public int columns(){
        return this.tupleData.size();
    }
    
    public double get(int index){
        return this.tupleData.get(index);
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Double value : this.tupleData){
            str.append(String.format("  %e  ", value));
        }
        return str.toString();
    }
}
