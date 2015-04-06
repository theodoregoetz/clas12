/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.fitter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.freehep.math.minuit.FunctionMinimum;
import org.freehep.math.minuit.MnMigrad;
import org.freehep.math.minuit.MnScan;
import org.freehep.math.minuit.MnUserParameters;
import org.root.data.DataSetXY;
import org.root.func.Function1D;
import org.root.func.RealParameter;
import org.root.histogram.H1D;


/**
 *
 * @author gavalian
 */
public class DataFitter {
    
    public static Boolean FITPRINTOUT = true;
    
    public DataFitter(){
        
    }
    
    public static void fit(DataSetXY  data, Function1D func){
        FitterFunction funcFitter = new FitterFunction(
                data.getDataX(),data.getDataY(),func);
        
        
        int npars = funcFitter.getFunction().getNParams();
        ByteArrayOutputStream pipeOut = new ByteArrayOutputStream();
        PrintStream  outStream = System.out;
        PrintStream  errStream = System.err;
        
        if(DataFitter.FITPRINTOUT==false){
            PrintStream pipeStream = new PrintStream(pipeOut);
            System.setOut(pipeStream);
            System.setErr(pipeStream);
        }
        
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
        
        if(DataFitter.FITPRINTOUT==false){
            System.setOut(outStream);
            System.setErr(errStream);
        }
        /*
        System.out.println(upar);
        System.err.println("******************");
        System.err.println("*   FIT RESULTS  *");
        System.err.println("******************");

        System.err.println(min);
        */
    }
    
    
    public static double getChiSquareFunc(DataSetXY dataset, Function1D func){
        int npoints = dataset.getDataX().getSize();
        double chi2 = 0.0;
        for(int loop = 0; loop < npoints;loop++){
            double xv = dataset.getDataX().getValue(loop);
            double yv = dataset.getDataY().getValue(loop);
            double fv = func.eval(xv);
            if(fv!=0&&xv>=func.getMin()&&xv<=func.getMax()){
                chi2 += (yv-fv)*(yv-fv)/(fv);
                //System.err.println("adding " + xv + " " + chi2);
            }
        }
        return chi2;
    }
    
    public static double getChiSquareHist(H1D h1, Function1D func){
        double chi2 = 0.0;
        int npoints = h1.getXaxis().getNBins();
        for(int loop = 0; loop < npoints; loop++){
            double xv = h1.getYaxis().getBinCenter(loop);
            double yv = h1.getBinContent(loop);
            double fv = func.eval(xv);
            if(yv!=0&&fv!=0&&xv>=func.getMin()&&xv<=func.getMax()){
                chi2 += (yv-fv)*(yv-fv)/fv;
            }
        }
        return chi2;
    }
    
}
