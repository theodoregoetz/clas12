/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.group;

import java.util.ArrayList;
import org.jlab.scichart.base.ScAxisPair;

/**
 *
 * @author gavalian
 */
public class ScGroupSeries extends ScGroup {
    public  ArrayList<ScNodeSeries> groupSeries = new ArrayList<ScNodeSeries>();
    private ScNodeAxisPair  groupAxis = new ScNodeAxisPair();
    private boolean         isAxisLogarithmicX = false;
    private boolean         isAxisLogarithmicY = false;
    
    public ScGroupSeries(ScRegion region){
        super();
        this.setParent(region);
        this.setDrawRange(0.15, 0.05, 0.95, 0.85);
        this.groupAxis.setAxisMinMax(2, 5, 2, 5);
        this.getChildren().add(groupAxis);
    }
    
    public void clear(){
        this.groupAxis.setAxisMinMax(0, 1, 0, 1);
        this.groupSeries.clear();
        this.getChildren().clear();
        this.getChildren().add(groupAxis);
    }
    
    public void addSeries(ScNodeSeries series){
        this.groupSeries.add(series);
        
        this.getChildren().add(series);
        this.updateSeriesAxis();
        /*
        if(this.groupSeries.size()==0){
            groupAxis.setAxisMinMax(
                    series.seriesAxis().getAxisX().getMin(),
                    series.seriesAxis().getAxisX().getMax(),
                    series.seriesAxis().getAxisY().getMin(),
                    series.seriesAxis().getAxisY().getMax());
            series.setGroupAxist(
                    series.seriesAxis().getAxisX().getMin(),
                    series.seriesAxis().getAxisX().getMax(),
                    series.seriesAxis().getAxisY().getMin(),
                    series.seriesAxis().getAxisY().getMax()
                    , false, true);
            this.getChildren().add(series);
            this.groupSeries.add(series);
            //System.err.println(" axis X = " + groupAxis.);
        } else {
            groupSeries.add(series);
            this.getChildren().add(series);
            ScAxisPair pair = new ScAxisPair();
            for(ScNodeSeries node : this.groupSeries){
                pair.getAxisX().axisGrow(node.seriesAxis().getAxisX().getMin(),
                        node.seriesAxis().getAxisX().getMax());
                pair.getAxisY().axisGrow(node.seriesAxis().getAxisY().getMin(),
                        node.seriesAxis().getAxisY().getMax());
            }
            
            //System.err.println(" AFTER GROW X : " + pair.getAxisX().toString());
            //System.err.println(" AFTER GROW Y : " + pair.getAxisY().toString());
            for(ScNodeSeries node : this.groupSeries){
                node.setGroupAxist(pair.getAxisX().getMin(), 
                        pair.getAxisX().getMax(),
                        pair.getAxisY().getMin(),
                        pair.getAxisY().getMax());
            }
            this.groupAxis.setAxisMinMax(pair.getAxisX().getMin(), 
                        pair.getAxisX().getMax(),
                        pair.getAxisY().getMin(),
                        pair.getAxisY().getMax());
        }
        */
        //this.groupAxis.getAxisPair().getAxisY().setLog(true);
    }
    
    public void setLogX(boolean flag){ 
        this.isAxisLogarithmicX = flag;
        this.groupAxis.getAxisPair().getAxisX().setLog(flag);
    }
    
    public void setLogY(boolean flag){ 
        this.isAxisLogarithmicY = flag;
        this.groupAxis.getAxisPair().getAxisY().setLog(flag);
        this.updateSeriesAxis();
    }
    
    public void setAxisTitles(String xtitle, String ytitle){
        //System.err.println("Setting axis Titles " + xtitle);
        groupAxis.getAxisPair().getAxisX().setTitle(xtitle);
        groupAxis.getAxisPair().getAxisY().setTitle(ytitle);
    }
    
    public void setAxisFontSize(int size){
        groupAxis.setFontSize(size);
    }
    
    private void updateSeriesAxis(){
        
        ScAxisPair pair = new ScAxisPair();
        
        if(this.groupSeries.size()>0){
            pair.set(this.groupSeries.get(0).seriesAxis().getAxisX().getMin(), 
                    this.groupSeries.get(0).seriesAxis().getAxisX().getMax(),
                    this.groupSeries.get(0).seriesAxis().getAxisY().getMin(),
                    this.groupSeries.get(0).seriesAxis().getAxisY().getMax());
        }
        
        for(ScNodeSeries node : this.groupSeries){
            node.updateAxis();
            pair.getAxisX().axisGrow(node.seriesAxis().getAxisX().getMin(),
                    node.seriesAxis().getAxisX().getMax());
            pair.getAxisY().axisGrow(node.seriesAxis().getAxisY().getMin(),
                    node.seriesAxis().getAxisY().getMax());
        }
        
        //System.err.println(" AFTER GROW X : " + pair.getAxisX().toString());
        //System.err.println(" AFTER GROW Y : " + pair.getAxisY().toString());
        for(ScNodeSeries node : this.groupSeries){
            node.setGroupAxist(pair.getAxisX().getMin(), 
                    pair.getAxisX().getMax(),
                    pair.getAxisY().getMin(),
                    pair.getAxisY().getMax(),isAxisLogarithmicX,
                    isAxisLogarithmicY
            );
        }
        
        this.groupAxis.setAxisMinMax(pair.getAxisX().getMin(), 
                pair.getAxisX().getMax(),
                pair.getAxisY().getMin(),
                pair.getAxisY().getMax());
    }

}
