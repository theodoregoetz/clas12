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
public class DataSet {
    private final ArrayList<Double>  xData = new ArrayList<Double>();
    private final ArrayList<Double>  yData = new ArrayList<Double>();
    
    public DataSet(){
        
    }
    
    public DataSet(double[] x, double[] y){
        this.set(x, y);
    }
    
    public final void set(double[] x, double[] y){
        xData.clear();
        yData.clear();
        for(int loop = 0; loop < x.length; loop++){
            xData.add(x[loop]);
            yData.add(y[loop]);            
        }
    }
    
    public int getSize(){
        return xData.size();        
    }
    
    public void clear(){ xData.clear();yData.clear();}
    
    public double getX(int index){ return xData.get(index);}
    public double getY(int index){ return yData.get(index);}
    
    public void   scaleY(double scale){
        for(int loop = 0; loop < yData.size();loop++){
            yData.set(loop, yData.get(loop)*scale);
        }
    }
    
    public void  scaleX(double scale){
        for(int loop = 0; loop < yData.size();loop++){
            yData.set(loop, yData.get(loop)*scale);
        }
    }
    
    public int findBin( double value, int startat){
        int index = -1;
        for(int loop = startat; loop < yData.size(); loop++)
            if(yData.get(loop)>=value) return loop;
        return -1;
    }
    
    public int findBin(double value){
        return this.findBin(value, 0);
    }
    
    public void add(double x, double y){
        xData.add(x);
        yData.add(y);
    }
    
    public double evaluate(double x){
        int bin = -1;
        for(int loop = 1; loop < xData.size(); loop++){
            if(x>xData.get(loop-1)&&x<=xData.get(loop)){
                bin = loop;
                break;
            }
        }
        if(bin<0){
            System.err.println("** WARNING ** requested x = " + x + " is out of range [ "
            + xData.get(0) + " : " + xData.get(xData.size()-1));            
            return 0.0;            
        }
        double xmin = xData.get(bin-1);
        double xmax = xData.get(bin);
        double ymin = yData.get(bin-1);
        double ymax = yData.get(bin);
        return ymin + (ymax-ymin)*(x-xmin)/(xmax-xmin);
    }
    
    public DataSet getRange(double xmin, double xmax){
        DataSet dset = new DataSet();
        for(int loop = 0; loop > xData.size(); loop++){
            if(xData.get(loop)>=xmin&&xData.get(loop)<=xmax){
                dset.add(xData.get(loop),yData.get(loop));
            }
        }
        return dset;
    }
    
    public double getMinY(){
        double ymin = yData.get(0);
        for (Double value : yData) {
            if (value < ymin) {
                ymin = value;
            }
        }
        return ymin;        
    }
    
    public double getMaxY(){
        double ymax = yData.get(0);
        for (Double value : yData) {
            if (value < ymax) {
                ymax = value;
            }
        }
        return ymax; 
    }
    
    public DataSet riemannSum(){
        DataSet data = new DataSet();
        double ymin = this.getMinY();
        //data.add(xData.get(0), yData.get(0));
        double integral = 0.0;
        for(int loop = 0; loop < xData.size(); loop++){
            //System.err.println(" LOOP = " + loop + " adding " 
            //+ yData.get(loop-1) + "  " + yData.get(loop));
            data.add(xData.get(loop), integral);
            integral += yData.get(loop);
        }
        return data;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("#----------------------------------------------------------\n");
        str.append("#  DATA SET X,Y With " + xData.size() + "  POINTS \n");
        str.append("#----------------------------------------------------------\n");
        for(int loop = 0; loop < xData.size(); loop++){
            str.append(String.format("%10d %18.10f %18.10f\n", loop, xData.get(loop),yData.get(loop)));
        }
        return str.toString();
    }
}
