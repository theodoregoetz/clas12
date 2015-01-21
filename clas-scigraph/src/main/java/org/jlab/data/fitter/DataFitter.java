/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.fitter;

import org.freehep.math.minuit.FunctionMinimum;
import org.freehep.math.minuit.MnMigrad;
import org.freehep.math.minuit.MnScan;
import org.freehep.math.minuit.MnUserParameters;
import org.jlab.data.func.Function1D;
import org.jlab.data.func.RealParameter;
import org.jlab.data.graph.DataSetXY;

/**
 *
 * @author gavalian
 */
public class DataFitter {
    
    public DataFitter(){
        
    }
    
    public static void fit(DataSetXY  data, Function1D func){
        FitterFunction funcFitter = new FitterFunction(
                data.getDataX(),data.getDataY(),func);
        
        int npars = funcFitter.getFunction().getNParams();
        
        MnUserParameters upar = new MnUserParameters();
        for(int loop = 0; loop < npars; loop++){
            RealParameter par = funcFitter.getFunction().parameter(loop);
            upar.add(par.name(),par.value(),0.001);
            if(par.min()>-1e9&&par.max()<1e9){
                upar.setLimits(par.name(), par.min(), par.max());
            }
        }
        
        
        MnScan  scanner = new MnScan(funcFitter,upar);
        FunctionMinimum scanmin = scanner.minimize(); 
        //System.err.println("******************");
        //System.err.println("*   SCAN RESULTS  *");
        //System.err.println("******************");
        System.out.println("minimum : " + scanmin);
        System.out.println("pars    : " + upar);
        //System.out.println(upar);
        System.err.println("*******************************************");
        MnMigrad migrad = new MnMigrad(funcFitter, upar);
        FunctionMinimum min = migrad.minimize();
        
        MnUserParameters userpar = min.userParameters();
        
        for(int loop = 0; loop < npars; loop++){
            RealParameter par = funcFitter.getFunction().parameter(loop);
            par.setValue(userpar.value(par.name()));
            par.setError(userpar.error(par.name()));
        }
        /*
        System.out.println(upar);
        System.err.println("******************");
        System.err.println("*   FIT RESULTS  *");
        System.err.println("******************");

        System.err.println(min);
        */
    }
}
