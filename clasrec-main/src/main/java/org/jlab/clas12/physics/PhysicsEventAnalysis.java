/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.physics;

import java.util.List;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.physics.oper.PhysicsEventOperator;
import org.jlab.physics.oper.PhysicsEventProcessor;
import org.root.group.TDirectory;
import org.root.histogram.H1D;

/**
 *
 * @author gavalian
 */
public class PhysicsEventAnalysis {
    
    private String  analysisName  = "CLAS12PhysicsAnalysis";
    private PhysicsEventProcessor  processor = new PhysicsEventProcessor();
    private TDirectory             directory = new TDirectory();
    
    public PhysicsEventAnalysis(){
        
    }
    
    public void addOperator(PhysicsEventOperator oper){
        this.processor.addOperator(oper);
    } 
    
    public void intiHistograms(){
        List<String>  operators = this.processor.getOperatorList();
        for(String operator : operators){
            PhysicsEventOperator physOper = this.processor.getOperator(operator);
            H1D  histo = new H1D(physOper.getName(),"",100,physOper.getMin(),physOper.getMax());
            directory.add(operator, histo);
        }
    }
    
    public void processEvent(PhysicsEvent event){
        this.processor.apply(event);
        List<String> operators = this.processor.getOperatorList();
        for(String operator : operators){
            if(processor.isValid(operator)==true){
                H1D histo = (H1D) directory.getObject(operator);
                histo.fill(processor.getOperator(operator).getValue());                
            }
        }
    }
    
    
}
