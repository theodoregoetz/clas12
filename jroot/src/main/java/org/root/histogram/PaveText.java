/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.histogram;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.root.base.EvioWritableTree;

/**
 *
 * @author gavalian
 */
public class PaveText implements EvioWritableTree {
    
    public ArrayList<String> paveStrings = new ArrayList<String>();
    private double pavePositionX = 0.0;
    private double pavePositionY = 0.0;
    public  Integer FontSize     = 12;
    
    public PaveText(double rx, double ry){
        this.pavePositionX = rx;
        this.pavePositionY = ry;
    }
    
    public void setPosition(double x, double y){
        this.pavePositionX = x;
        this.pavePositionY = y;
    }
    
    public double getPositionX(){
        return this.pavePositionX;
    }
    public double getPositionY(){
        return this.pavePositionY;
    }
    
    public void addText(String text){
        this.paveStrings.add(text);
    }
    
    public void setFontSize(int size){
        this.FontSize = size;
    }

    public String getName() {
        return "PaveText";
    }

    public TreeMap<Integer, Object> toTreeMap() {
        TreeMap<Integer, Object> objData = new TreeMap<Integer, Object>();
        objData.put(1, new int[]{14});
        for(int loop = 0; loop < paveStrings.size(); loop++){
            int num = 2 + loop;
            byte[] nameBytes = this.paveStrings.get(loop).getBytes();
            objData.put(num, nameBytes);
        }
        return objData;
    }

    public void fromTreeMap(TreeMap<Integer, Object> map) {
        this.paveStrings.clear();
        if(map.get(1) instanceof int[]){
            if(  ((int[]) map.get(1))[0]==14){
                for(Map.Entry<Integer,Object> entry : map.entrySet()){                    
                    if(entry.getValue() instanceof byte[]){
                        byte[] name     = (byte[]) map.get(entry.getKey());
                        this.paveStrings.add(new String(name));
                    }
                }
            }
        }
        //
    }
}
