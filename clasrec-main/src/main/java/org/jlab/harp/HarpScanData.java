/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.harp;

import org.root.data.DataSetXY;
import org.root.data.DataTable;
import org.root.fitter.DataFitter;
import org.root.func.F1D;

/**
 *
 * @author gavalian
 */
public class HarpScanData {
    
    private DataTable  table   = null;
    private Integer    columnX = 0;
    private Integer    columnY = 0;
    private Double     columnXmin = 0.0;
    private Double     columnXmax = 0.0;
    private DataSetXY  dataGraph    = new DataSetXY();
    private F1D        dataFunction = new F1D("gaus+p2");
    private DataFitter fitter       = new DataFitter();
    
    public HarpScanData(DataTable dt, int ycol, double minx, double maxx){
        this.setDataTable(dt);
        this.setDataColumns(0, ycol);
        this.setAxisRange(minx, maxx);        
    }
    
    public final void setAxisRange(double min, double max){
        this.columnXmin = min;
        this.columnXmax = max;
    }
    
    public final void setDataTable(DataTable dt){
        this.table = dt;
    }
    
    public final void setDataColumns(int xcol, int ycol){
        this.columnX = xcol;
        this.columnY = ycol;
    }
    
    public void updateDataSet(){
        this.dataGraph = table.getDataSet(this.columnX, 
                this.columnY, this.columnX,this.columnXmin, this.columnXmax);
    }
    
    public void fit(){
        double funcrange = (this.columnXmax-this.columnXmin);
        this.dataFunction = new F1D("gaus+p1",this.columnXmin,this.columnXmax);
        this.dataFunction.setParameter(0, 1000);
        this.dataFunction.setParameter(1, this.columnXmin + 0.5*funcrange);
        this.dataFunction.setParameter(2, 0.2*funcrange);                        
        DataFitter.fit(dataGraph, dataFunction);
        this.dataFunction.show();
    }
    
    public DataSetXY getDataSet(){
        return this.dataGraph;
    }
    
    public F1D  getFitFunction(){
        return this.dataFunction;
    }
}
