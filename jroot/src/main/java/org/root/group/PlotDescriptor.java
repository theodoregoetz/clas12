/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.group;

import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class PlotDescriptor {
    private Integer plotPad = 0;
    private ArrayList<String>  objectList = new ArrayList<String>();

    public PlotDescriptor(int pad, String... plots){
        this.plotPad = pad;
        for(String item: plots){
            objectList.add(item);
        }
    }
    
    public int getPad(){
        return this.plotPad;
    }
    
    public ArrayList<String> getList(){
        return this.objectList;
    }
}
