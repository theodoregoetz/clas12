/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.scichart.group;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class ScGroup {
    //private Rectangle 
    private ScRegion    groupParent     = new ScRegion();
    private ScRange     groupRange      = new ScRange();
    
    private final ArrayList<ScNode>  children = new ArrayList<ScNode>();
    
    public ScGroup(){
        
    }
    
    public ScGroup(ScRegion rect){
        groupParent = rect;
    }
    
    public void clearChildren(){
        children.clear();
    }
    
    public void setParent(ScRegion rect){
        groupParent = rect;
    }
    
    public void setDrawRange(double xc, double yc, double xce, double yce){
        groupRange.set(xc, yc, xce, yce);
    }
    
    public ScRegion parent() { return groupParent; }
    
    public ScRegion getBounds() {
        ScRegion rect = new ScRegion(
                groupParent.getNormalizedX(this.groupRange.getLowBoundX()),
                groupParent.getNormalizedY(this.groupRange.getLowBoundY()),
                groupParent.getNormalizedX(this.groupRange.getHighBoundX())
                - groupParent.getNormalizedX(this.groupRange.getLowBoundX()),               
                groupParent.getNormalizedY(this.groupRange.getHighBoundY())
                - groupParent.getNormalizedY(this.groupRange.getLowBoundY())
        );
        return rect;
    }
    
    
    public ArrayList<ScNode> getChildren() { return children;} 
    
    public void paintGroup(Graphics2D g2d){
        ScRegion region = this.getBounds();
        for(ScNode node : children){
            node.paintNode(g2d,region);
        }
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(groupParent.toString());
        return str.toString();
    }
}
