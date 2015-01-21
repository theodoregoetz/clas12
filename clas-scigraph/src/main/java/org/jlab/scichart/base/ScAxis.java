/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.base;

import java.util.ArrayList;
import org.jlab.scichart.utils.AxisNiceScale;

/**
 *
 * @author gavalian
 */
public class ScAxis {
    
    private String axisTitle = "X title";
    private Double axisMin = 0.0;
    private Double axisMax = 1.0;
    private AxisNiceScale  axisScale     = new AxisNiceScale(0.0,1.0);
    private String         sigFigFormat  = "";
    private ArrayList<String>  axisTicksString = new ArrayList<String>();
    private ArrayList<Double>  axisTicks       = new ArrayList<Double>();
    private boolean            isAxisLogarithmic = false;
    
    public ScAxis(){        
        axisScale.setMinMaxPoints(-0.02,1.02);
        axisScale.setMaxTicks(10);
        this.setMinMax(-0.02,1.02);
    }
    
    public ScAxis(double min, double max){
        axisScale.setMinMaxPoints(min, max);
        axisScale.setMaxTicks(10);
        this.setMinMax(min, max);
    }
    
    public void setLog(boolean flag){
        if(flag==true&&isAxisLogarithmic){
            //double max = Math.log10(axisMax);            
            //this.setMinMax(min, max);
        }
        isAxisLogarithmic = flag;
        
    }
    
    public boolean isLog(){ return isAxisLogarithmic;}
    
    public void    setTitle(String text) { axisTitle = text; }
    public String  getTitle() { return axisTitle; }
    public ArrayList<Double> getAxisTicks() { return axisTicks; }
    public ArrayList<String> getAxisTicksString() { return axisTicksString; }
    
    public void axisGrow(double min, double max){
        if(min<axisMin) axisMin = min;
        if(max>axisMax) axisMax = max;
        
    }
    
    public void setMinMax(double min, double max){
        axisMin = min; axisMax = max;
        axisScale.setMinMaxPoints(min, max);
        this.updateAxisTicks();
    }
    
    public double getMin(){
        return axisMin;
    }
    
    public double getMax(){
        return axisMax;
    }    
    
    public double getMiddle(){
        if(this.getLength()==0.0) return 0.0;
        return (this.getMin()+this.getLength()/2.0);
    }
    public double getLength() { return (axisMax - axisMin);}
    
    public double getRelative(double value){
        if(this.getLength()==0) return 0;
        if(isAxisLogarithmic==true){
            //System.err.println("Getting the log axis");
            double dist = Math.log10(axisMax);
            return Math.log10(value)/dist;
        }
        return (value-axisMin)/this.getLength();
    }
    
    public boolean isInRange(double value){
        return (value>=axisMin&&value<=axisMax);
    }
    
    public void  setDivisions(int ticks){
        axisScale.setMaxTicks(ticks);
        this.updateAxisTicks();
    }
    
    private void updateAxisTicks(){
        //System.err.println("updating axis ticks = " + this.getMin() + " " + this.getMax());
        axisTicks.clear();
        axisTicksString.clear();
        axisScale.setMinMaxPoints(axisMin, axisMax);
        axisTicks = axisScale.getCoordinates();
        if(axisTicks.size()>1){
            if(this.isAxisLogarithmic==false){
                int sigFigs = axisScale.getSigFig();
                this.updateFormat(sigFigs);
                for(int loop = 0; loop < axisTicks.size(); loop++){
                    //System.err.println(loop + " = " + axisTicks.get(loop));
                    try {
                        axisTicksString.add(String.format(sigFigFormat, axisTicks.get(loop)));
                    } catch (Exception e){
                        System.err.println("ERROR : sigfig format = " + sigFigFormat + 
                                " text = " + axisTicks.get(loop));
                    }
                }
            } else {
                System.err.println("Updateing LOG scale axis.....min/max = " +
                        axisMin + " " + axisMax);
                axisScale.setMinMaxPointsLog(axisMin,axisMax);
                System.err.println("Updateing LOG scale axis..... size = " + 
                        axisTicks.size());
                axisTicks = axisScale.getCoordinates();
                for(int loop = 0; loop < axisTicks.size(); loop++){
                    System.err.println(loop + " = " + axisTicks.get(loop));
                    try {
                        axisTicksString.add(String.format("%.0f",  axisTicks.get(loop)));
                    } catch (Exception e){
                        System.err.println("ERROR : sigfig format = " + sigFigFormat + 
                                " text = " + axisTicks.get(loop));
                    }
                }
            }
        }
    }
    
    private void updateFormat(int sigfig){
        int significantFigs = sigfig;
        if(sigfig<0) significantFigs = 0;
        StringBuilder str = new StringBuilder();
        str.append('%');
        str.append('.');
        str.append(significantFigs+1);
        str.append('f');            
        sigFigFormat = str.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("AXIS : ");
        str.append(String.format(" (%12.5f , %12.5f) ", this.getMin(), this.getMax()));
        for(int loop = 0; loop < axisTicksString.size(); loop++){
            str.append(axisTicksString.get(loop));
            str.append("  ");
        }
        return str.toString();
    }
}
