/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.physics.oper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jlab.clas.physics.PhysicsEvent;

/**
 *
 * @author gavalian
 */
public class PhysicsEventProcessor {
    
    private final TreeMap<String,PhysicsEventOperator>  operators = new TreeMap<String,PhysicsEventOperator>();
        
    public PhysicsEventProcessor(){
        
    }
    
    public void addOperator(PhysicsEventOperator oper){
        this.operators.put(oper.getName(), oper);
    }
    
    public void apply(PhysicsEvent event){
        for(Map.Entry<String,PhysicsEventOperator> entry : this.operators.entrySet()){
            entry.getValue().apply(event);
        }
    }
    
    public PhysicsEventOperator getOperator(String name){
        return this.operators.get(name);
    }
    
    public List<String>  getOperatorList(){
        ArrayList<String>  opList = new ArrayList<String> ();
        for(Map.Entry<String,PhysicsEventOperator> entry : this.operators.entrySet()){
            opList.add(entry.getKey());
        }
        return opList;
    }
    
    public double[]  getOperatorValues(){
        double[] values = new double[this.operators.size()];
        int counter = 0;
        for(Map.Entry<String,PhysicsEventOperator> entry : this.operators.entrySet()){
            values[counter] = entry.getValue().getValue();
            counter++;
        }
        return values;
    }
    
    public boolean isValid(String oper){
        if(this.operators.containsKey(oper)==false) return false;
        PhysicsEventOperator evOper = this.operators.get(oper);
        for(String cut : evOper.getCuts()){
            if(this.operators.containsKey(cut)==true){
                if(this.operators.get(cut).isValid()==false) return false;
            } else {
                System.out.println("[PhysicsEventProcessor] ERROR : processing operator [" 
                        + oper + "]. Could not find cut named [" + cut + "]" );
            }
        }
        return true;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<String,PhysicsEventOperator> entry : this.operators.entrySet()){
            str.append(entry.getValue().stringValue());
            str.append("\n");
        }
        return str.toString();
    }
}
