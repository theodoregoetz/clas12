/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.utils;

import java.util.ArrayList;
import java.util.List;
import org.jlab.data.graph.DataSet;
import org.jlab.data.graph.DataSetXY;
import org.jlab.data.graph.DataVector;

/**
 *
 * @author gavalian
 */
public class PeakFinder {
    private ArrayList<DataSet>  foundPeaks = new ArrayList<DataSet>();
    private DataSet             transformedDataSet = new DataSet();
    private Integer             nSamples    = 100;
    private ArrayList<DataVector>  clusters = new ArrayList<DataVector>();
    private ArrayList<DataSetXY>   clustersData = new ArrayList<DataSetXY>();
    
    private ArrayList<DataSet>     peakDataSets = new ArrayList<DataSet>();
    private Double                 peakClusteringWidth = 0.2;
    private Integer                maxHitsInCluster    = 10;
    
    public PeakFinder(){
        
    }
    
    public PeakFinder(int mx, double width){
        this.maxHitsInCluster = mx;
        this.peakClusteringWidth = width;
    }
    
    public PeakFinder(double width){
        peakClusteringWidth = width;
    }
    
    public PeakFinder(int smpl){
        nSamples = smpl;
    }
    
    public void analyze(DataVector xvec, DataVector yvec){
        DataVector yvecCum = yvec.getCumulative();
        yvecCum.divide(yvecCum.getValue(yvecCum.getSize()-1));

        DataSet  peakData = new DataSet();
        double step = 1.0/nSamples;
        for(int loop = 0; loop < nSamples; loop++){
            double yn = loop*step;
            int bin   = yvecCum.findBin(yn);
            System.err.println(" find bin = " + bin + " yn = " + yn);
            double ybin  = yvecCum.getValue(bin);
            double yprev = 0.0;
            if(bin!=0) yprev = yvecCum.getValue(bin-1);
            double xbin  = xvec.getValue(bin);
            double xprev = xvec.getLowEdge(bin);
            if(bin!=0) xprev = xvec.getValue(bin-1);
            double xvalue    = this.getNormalizedX(yn, xprev, xbin, yprev, ybin);
            double yRealBin  = yvec.getValue(bin);
            double yRealPrev = 0.0;//yvec.getLowEdge(bin);
            if(bin!=0) yRealPrev = yvec.getValue(bin-1);
            double yvalue    = this.getNormalizedY(xvalue, xprev, xbin, yRealPrev,yRealBin);
            peakData.add(xvalue,yvalue);            
        }
        this.transformedDataSet = peakData;        
        double delta = this.getAverageDelta();
        System.err.println("Average delta = " + delta);
    }
    
    public double[] getPeakDataX(){
        double[] xdata = new double[this.transformedDataSet.getSize()];
        for(int loop =0; loop < xdata.length; loop++)
            xdata[loop] = this.transformedDataSet.getX(loop);
        return xdata;
    }
    
    public double[] getPeakDataY(){
        double[] xdata = new double[this.transformedDataSet.getSize()];
        for(int loop =0; loop < xdata.length; loop++)
            xdata[loop] = this.transformedDataSet.getY(loop);
        return xdata;
    }
    
    public DataSet getPeakDataSet(){ return this.transformedDataSet; }
    
    private double getNormalizedY(double xvalue, double xl, double xh, double yrL, double yrH){
        double result = 0.0;
        double slope2 = (xh-xl)*(xh-xl) + (yrH-yrL)*(yrH-yrL);
        double    th  = Math.acos((xh-xl)/Math.sqrt(slope2));
        double    ys  = (xvalue-xl)*Math.tan(th);
        return yrL+ys;
    }
    
    private double getNormalizedX(double yvalue, double xl, double xh, double yl, double yh){
        double result = 0.0;
        double slope2 = (xh-xl)*(xh-xl) + (yh-yl)*(yh-yl);
        double    th  = Math.acos((yh-yl)/Math.sqrt(slope2));
        double    xs  = (yvalue-yl)*Math.tan(th);
        return xl+xs;
    }
    
    
    public List<DataSetXY>  getPeaks(){ return clustersData;}
    public List<DataVector> getClusters(){ return clusters;}
    
    public double getAverageDelta(){
        double delta = 0.0;
        for(int loop = 1; loop < this.transformedDataSet.getSize(); loop++){
            delta += Math.abs(this.transformedDataSet.getX(loop)-
                    this.transformedDataSet.getX(loop-1));
        }
        return delta/this.transformedDataSet.getSize();
    }
    
    
    public void analyze(DataSetXY data){
        
    }
    
    public double averageDelta(DataSetXY data){
        double delta = 0.0;
        for(int loop = 1; loop < data.getDataX().getSize(); loop++){
            delta += Math.abs(
                    data.getDataX().getValue(loop)-
                            data.getDataX().getValue(loop-1)
                    );
        }
        return delta/data.getDataX().getSize();
    }
    
    
    
    public void findPeaks(DataSetXY dataSet){
        
        DataSetXY rieman = dataSet.getDataSetRieman(this.nSamples);
        clustersData.clear();
        double delta = this.averageDelta(rieman);
        System.err.println("=====> AVERAGE DELTA = " + delta);
        for(int loop = 0; loop < rieman.getDataX().getSize(); loop++){
            double  xvalue = rieman.getDataX().getValue(loop);
            double  yvalue = dataSet.evaluate(xvalue);
            boolean  addNewDataset = true;
            for(DataSetXY d : clustersData){
                if(this.isInCluster(d, xvalue,peakClusteringWidth*delta)){
                    d.add(xvalue, yvalue);
                    addNewDataset = false;
                }
            }
            if(addNewDataset==true){
                DataSetXY set = new DataSetXY();
                set.add(xvalue, yvalue);
                clustersData.add(set);
            }            
        }
        
        ArrayList<DataSetXY>  clustersFiltered = new ArrayList<DataSetXY>();
        
        for(DataSetXY vec : clustersData){
            if(vec.getDataX().getSize()>=this.maxHitsInCluster){
                clustersFiltered.add(vec);
            }
        }
        
        this.clustersData = clustersFiltered;
        for(DataSetXY vec : clustersData){
            System.err.println("DATA SIZE = " + vec.getDataX().getSize() + "  mean = "
            + vec.getMean() + "  rms = " + vec.getRMS());
            //vec.show();
        }
        
        for(DataSetXY vec : clustersData){
            System.err.println("DATA SIZE = " + vec.getDataX().getSize() + "  mean = "
            + vec.getMean() + "  rms = " + vec.getRMS());
            //vec.show();
        }
    }
    
    public void doClustering(DataSetXY data){
        
        clusters.clear();
        clustersData.clear();
        
        double delta = this.averageDelta(data);
        System.err.println("=====> AVERAGE DELTA = " + delta);
        for(int loop = 0; loop < data.getDataX().getSize(); loop++){
            boolean addNewCluster = true;
            boolean addNewData    = true;
            double  xvalue = data.getDataX().getValue(loop);
            for(DataVector vec : clusters){
                if(this.isInCluster(vec, xvalue, peakClusteringWidth*delta)==true){
                    //System.err.println("Adding value " + xvalue +
                    //        " to cluster " + vec.geatMean());
                    
                    vec.add(xvalue);
                    addNewCluster = false;
                } 
            }
            
            if(addNewCluster==true){
                DataVector vec = new DataVector();
                vec.add(xvalue);
                clusters.add(vec);
            }
        }
        
        ArrayList<DataVector>  clusterPassed = new ArrayList<DataVector>();
        
        for(DataVector vec : clusters){
            if(vec.getSize()>=this.maxHitsInCluster){
                clusterPassed.add(vec);
            }
        }
        
        clusters = clusterPassed;
        for(DataVector vec : clusters){
            System.err.println("VECTOR SIZE = " + vec.getSize() + "  mean = "
            + vec.getMean() + "  rms = " + vec.getRMS());
        }
                
        
    }
    
    public void doClustering(){
        clusters.clear();
        double delta = this.getAverageDelta();
        for(int loop = 0; loop < this.transformedDataSet.getSize(); loop++){
            boolean addNewCluster = true;
            double  xvalue = this.transformedDataSet.getX(loop);
            for(DataVector data : clusters){
                if(this.isInCluster(data, xvalue, 5.0*delta)==true){
                    data.add(xvalue);
                    addNewCluster = false;
                } 
            }
            
            if(addNewCluster==true){
                DataVector vec = new DataVector();
                vec.add(xvalue);
                clusters.add(vec);
            }
        }
        
        for(DataVector vec : clusters){
            System.err.println("VECTOR SIZE = " + vec.getSize() + "  mean = "
            + vec.getMean() + "  rms = " + vec.getRMS());
        }
    }
    
    
    public void doDataSets(){
        
    }
    
    public ArrayList<DataSet>  getDataSets(){ return this.peakDataSets;}
    
    public boolean isInCluster(DataVector vec, double value, double tolerance){
        for(int loop = 0; loop < vec.getSize(); loop++){
            if(Math.abs(vec.getValue(loop)-value)<tolerance) return true;
        }
        return false;
    }
    
    public boolean isInCluster(DataSetXY data, double value, double tolerance){
        for(int loop = 0; loop < data.getDataX().getSize(); loop++){
            if(Math.abs(data.getDataX().getValue(loop)-value)<tolerance) return true;
        }
        return false;
    }
    
}
