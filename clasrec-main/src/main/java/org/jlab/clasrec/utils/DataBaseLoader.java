/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clasrec.utils;

import java.util.Date;
import org.jlab.geom.base.ConstantProvider;

/**
 *
 * @author gavalian
 */
public class DataBaseLoader {

    private DataBaseLoader() {}

    private static DatabaseConstantProvider getProvider(int run, String variation, Date date) {
        DatabaseConstantProvider provider = new DatabaseConstantProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.setDefaultRun(run);
        provider.setDefaultVariation(variation);
        provider.setDefaultDate(date);
        return provider;
    }

    /* DO NOT UNCOMMENT THIS METHOD!
     *
     * I know it's tempting to do, but it is likely to
     * cause more harm than good.
     *
    public static DatabaseConstantProvider getProvider() {
        DatabaseConstantProvider provider = new DatabaseConstantProvider();
        provider.setDefaultRun(0);
        provider.setDefaultVariation("default");
        provider.setDefaultDate(new Date());
        return provider;
    }
     */

    public static ConstantProvider getDriftChamberConstants(int run, String variation, Date date){
        DatabaseConstantProvider provider = DataBaseLoader.getProvider(run, variation, date);
        provider.loadTable("/geometry/dc/dc");
        provider.loadTable("/geometry/dc/region");
        provider.loadTable("/geometry/dc/superlayer");
        provider.loadTable("/geometry/dc/layer");
        return provider;
    }

    public static ConstantProvider getCalorimeterConstants(int run, String variation, Date date){
        DatabaseConstantProvider provider = DataBaseLoader.getProvider(run, variation, date);
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



    public static ConstantProvider getTimeOfFlightConstants(int run, String variation, Date date){
        DatabaseConstantProvider provider = DataBaseLoader.getProvider(run, variation, date);
        provider.loadTable("/geometry/ftof/panel1a/paddles");
        provider.loadTable("/geometry/ftof/panel1a/panel");
        provider.loadTable("/geometry/ftof/panel1b/paddles");
        provider.loadTable("/geometry/ftof/panel1b/panel");
        provider.loadTable("/geometry/ftof/panel2/paddles");
        provider.loadTable("/geometry/ftof/panel2/panel");
        return provider;
    }
    public static ConstantProvider getConstantsDC(int run, String variation, Date date){
        DatabaseConstantProvider provider = DataBaseLoader.getProvider(run, variation, date);
        provider.loadTable("/geometry/dc/dc");
        provider.loadTable("/geometry/dc/region");
        provider.loadTable("/geometry/dc/superlayer");
        provider.loadTable("/geometry/dc/layer");
        return provider;
    }

     public static ConstantProvider getConstantsEC(int run, String variation, Date date){
        DatabaseConstantProvider provider = DataBaseLoader.getProvider(run, variation, date);
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

    public static ConstantProvider getConstantsFTOF(int run, String variation, Date date){
        DatabaseConstantProvider provider = DataBaseLoader.getProvider(run, variation, date);
        provider.loadTable("/geometry/ftof/panel1a/paddles");
        provider.loadTable("/geometry/ftof/panel1a/panel");
        provider.loadTable("/geometry/ftof/panel1b/paddles");
        provider.loadTable("/geometry/ftof/panel1b/panel");
        provider.loadTable("/geometry/ftof/panel2/paddles");
        provider.loadTable("/geometry/ftof/panel2/panel");
        return provider;
    }

    public static ConstantProvider getConstantsCND(int run, String variation, Date date){
        DatabaseConstantProvider provider = DataBaseLoader.getProvider(run, variation, date);
        provider.loadTable("/geometry/cnd/cnd");
        provider.loadTable("/geometry/cnd/layer");
        return provider;
    }

    public static ConstantProvider getConstantsFTCAL(int run, String variation, Date date){
        DatabaseConstantProvider provider = DataBaseLoader.getProvider(run, variation, date);
        provider.loadTable("/geometry/ft/ftcal");
        return provider;
    }

    public static ConstantProvider getConstantsBST(int run, String variation, Date date){
        DatabaseConstantProvider provider = DataBaseLoader.getProvider(run, variation, date);
        provider.loadTable("/geometry/bst/region");
        provider.loadTable("/geometry/bst/sector");
        provider.loadTable("/geometry/bst/bst");
        return provider;
    }

}
