/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.utils;

/**
 *
 * @author gavalian
 */
public class FunctionFactory {
    public static double[]  getAxisData(int bins, double min, double max){
        double[] buffer = new double[bins];
        double step = (max-min)/bins;
        for(int loop = 0; loop < bins; loop++){
            buffer[loop] = min + step/2.0 + (loop*step);
        }
        return buffer;
    }
    
    public static double[]  getExpData(double[] axis){
        double fraction = Math.exp(-axis[0]);
        double[] buffer = new double[axis.length];
        for(int loop = 0; loop < axis.length; loop++){
            buffer[loop] = Math.exp(-axis[loop])/fraction;
        }
        return buffer;
    }
    
    public static double[]  getExpData(double[] axis, double scale){
        double fraction = Math.exp(-axis[0]);
        double[] buffer = new double[axis.length];
        for(int loop = 0; loop < axis.length; loop++){
            buffer[loop] = scale*Math.exp(-axis[loop])/fraction;
        }
        return buffer;
    }
    
    public static double[]  getGaussianData(double[] axis, double mean, double sigma){
        return FunctionFactory.getGaussianData(axis, 1.0, mean, sigma);
    }
    
    public static double[]  getGaussianData(double[] axis, double amplitude, double mean, double sigma){
        double[] buffer = new double[axis.length];
        for(int loop = 0; loop < axis.length; loop++){
            double diff = mean - axis[loop];
            double sig2 = 2.0*sigma*sigma;
            buffer[loop] = amplitude*Math.exp(-diff*diff/sig2);
        }
        return buffer;
    }
    
    public static double[] getSubtracted(double[] xo, double[] xs){
        double[] buffer = new double[xo.length];
        for(int loop = 0; loop < xo.length; loop++)
            buffer[loop] = xo[loop] - xs[loop];
        return buffer;
    }
    
}
