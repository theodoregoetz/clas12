/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.fitter;

import org.freehep.math.minuit.FCNBase;
import org.root.data.DataVector;
import org.root.func.Function1D;


/**
 *
 * @author gavalian
 */
public class FitterFunction implements FCNBase {
    
    private DataVector  vectorX;
    private DataVector  vectorY;
    private Function1D  function;
    
    public FitterFunction(DataVector x, DataVector y, Function1D func){
        vectorX  = x;
        vectorY  = y;
        function = func;
    }
    
    public FitterFunction(double[] x, double[] y, Function1D func){
        vectorX = new DataVector();
        vectorY = new DataVector();
        vectorX.set(x);
        vectorY.set(y);
        function = func;
    }
    
    public Function1D getFunction(){return function;}
    
    @Override
    public double valueOf(double[] pars) {
        double chi2 = 0.0;
        function.setParameters(pars);
        for(int loop = 0; loop < vectorX.getSize(); loop++){
            double xv = vectorX.getValue(loop);
            double yv = vectorY.getValue(loop);
            //System.err.println(" DATA VALUES = " + xv + "  " + yv);
            if(xv>=function.getMin()&&xv<=function.getMax()){
                double fv = function.eval(xv);
                //System.err.println(" FUNCTION VALUE " + fv);
                if(yv!=0){
                    //System.err.println("adding to chi2 " + chi2
                    //+ "  " + (yv-fv)*(yv-fv)/fv + " x = " + xv 
                    //+ " y = " + yv + " fv = " + fv);
                    //double chi2step  = (yv-fv)*(yv-fv)/fv;
                    //double chi2step2 = (yv-fv)*(yv-fv)/yv;
                    //System.out.println(" CHI TEST " + chi2step + "  "
                    //        + chi2step2);                    
                    chi2 += (yv-fv)*(yv-fv);
                }
            }
        }
        
        //function.show();
        //System.err.println("\n************ CHI 2 = " + chi2);
        return chi2;
        
    }
    
}
