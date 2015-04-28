/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class DataSetCollection {
    
    private ArrayList<IDataSet>  dsCollection = new ArrayList<IDataSet>();
    private Boolean              collectionDataRangeScale = false;
    
    public DataSetCollection(){
        
    }
    
    public DataRegion getDataRegion(){
        if(this.dsCollection.isEmpty()){
            return new DataRegion(0.0,1.0,0.0,1.0);
        }
        
        DataRegion region = new DataRegion(this.dsCollection.get(0).getDataRegion());
        if(this.collectionDataRangeScale==true){
            for(int loop = 0; loop < this.dsCollection.size(); loop++){
                region.combine(this.dsCollection.get(loop).getDataRegion());
            }
        }
        return region;
    }
    
    public void setAutoScale(boolean flag){
        this.collectionDataRangeScale = flag;
    }
    
    public void addDataSet(IDataSet ds){
        this.dsCollection.add(ds);
    }
    
    public int  getCount(){ return this.dsCollection.size();}
    public IDataSet  getDataSet(int index){ return this.dsCollection.get(index);}
    
}
