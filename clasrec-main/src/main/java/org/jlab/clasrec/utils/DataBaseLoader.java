/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clasrec.utils;

import org.jlab.geom.base.ConstantProvider;

/**
 *
 * @author gavalian
 */
public class DataBaseLoader {
    
    public static ConstantProvider getDriftChamberConstants(){
        DatabaseConstantProvider provider = new DatabaseConstantProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.loadTable("/geometry/dc/dc");
        provider.loadTable("/geometry/dc/region");
        provider.loadTable("/geometry/dc/superlayer");
        provider.loadTable("/geometry/dc/layer");
        return provider;
    }
    
    public static ConstantProvider getCalorimeterConstants(){
        DatabaseConstantProvider provider = new DatabaseConstantProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.loadTable("/geometry/pcal/pcal");
        provider.loadTable("/geometry/pcal/UView");
        provider.loadTable("/geometry/pcal/VView");
        provider.loadTable("/geometry/pcal/WView");
        provider.loadTable("/geometry/ec/ec");
        provider.loadTable("/geometry/ec/uview");
        provider.loadTable("/geometry/ec/vview");
        provider.loadTable("/geometry/ec/wview");
        return provider;
    }
    
   
    
    public static ConstantProvider getTimeOfFlightConstants(){
        DatabaseConstantProvider provider = new DatabaseConstantProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.loadTable("/geometry/ftof/panel1a/paddles");        
        provider.loadTable("/geometry/ftof/panel1a/panel");
        provider.loadTable("/geometry/ftof/panel1b/paddles");
        provider.loadTable("/geometry/ftof/panel1b/panel");
        provider.loadTable("/geometry/ftof/panel2/paddles");
        provider.loadTable("/geometry/ftof/panel2/panel");
        return provider;
    }
    public static ConstantProvider getConstantsDC(){
        DatabaseConstantProvider provider = new DatabaseConstantProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.loadTable("/geometry/dc/dc");
        provider.loadTable("/geometry/dc/region");
        provider.loadTable("/geometry/dc/superlayer");
        provider.loadTable("/geometry/dc/layer");
        return provider;
    }
    
     public static ConstantProvider getConstantsEC(){
        DatabaseConstantProvider provider = new DatabaseConstantProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.loadTable("/geometry/pcal/pcal");
        provider.loadTable("/geometry/pcal/UView");
        provider.loadTable("/geometry/pcal/VView");
        provider.loadTable("/geometry/pcal/WView");
        provider.loadTable("/geometry/ec/ec");
        provider.loadTable("/geometry/ec/uview");
        provider.loadTable("/geometry/ec/vview");
        provider.loadTable("/geometry/ec/wview");
        return provider;
    }
     
    public static ConstantProvider getConstantsFTOF(){
        DatabaseConstantProvider provider = new DatabaseConstantProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.loadTable("/geometry/ftof/panel1a/paddles");        
        provider.loadTable("/geometry/ftof/panel1a/panel");
        provider.loadTable("/geometry/ftof/panel1b/paddles");
        provider.loadTable("/geometry/ftof/panel1b/panel");
        provider.loadTable("/geometry/ftof/panel2/paddles");
        provider.loadTable("/geometry/ftof/panel2/panel");
        return provider;
    }
    
    public static ConstantProvider getConstantsCND(){
        DatabaseConstantProvider provider = new DatabaseConstantProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.loadTable("/geometry/cnd/cnd");
        provider.loadTable("/geometry/cnd/layer");
        return provider;
    }
    
    public static ConstantProvider getConstantsFTCAL(){
        DatabaseConstantProvider provider = new DatabaseConstantProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.loadTable("/geometry/ft/ftcal");
        return provider;
    }

    public static ConstantProvider getConstantsBST(){
        DatabaseConstantProvider provider = new DatabaseConstantProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.loadTable("/geometry/bst/region");
        provider.loadTable("/geometry/bst/sector");
        provider.loadTable("/geometry/bst/bst");
        return provider;
    }
    
}
