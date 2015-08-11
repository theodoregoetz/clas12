package org.jlab.geom.detector.dc;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;

import org.jlab.ccdb.CcdbPackage;
import org.jlab.ccdb.JDBCProvider;

import org.jlab.geom.detector.dc.DriftChamber;

public class DriftChamberTest {

    @Test
    public void testConstruction() {
        DriftChamber dcgeom = new DriftChamber();
    }

    @Test
    public void testFetchParametersOnConstruction() {
        JDBCProvider provider = CcdbPackage.createProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.setDefaultRun(0);
        provider.setDefaultVariation("default");
        provider.connect();
        DriftChamber dcgeom = new DriftChamber(provider);
    }

    @Test
    public void testFetchParametersAfterConstruction() {
        DriftChamber dcgeom = new DriftChamber();
        JDBCProvider provider = CcdbPackage.createProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.setDefaultRun(0);
        provider.setDefaultVariation("default");
        provider.connect();
        dcgeom.fetchNominalParameters(provider);
    }

    @Test
    public void testPrint() {
        DriftChamber dcgeom = new DriftChamber();
        JDBCProvider provider = CcdbPackage.createProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.setDefaultRun(0);
        provider.setDefaultVariation("default");
        provider.connect();
        dcgeom.fetchNominalParameters(provider);

        Map<String,Map<String,String>> vols = dcgeom.volumes();
        StringBuilder msg = new StringBuilder();
        for (Map.Entry<String,Map<String,String>> vol : vols.entrySet()) {
            msg.append(vol.getKey()+":\n");
            for (Map.Entry<String,String> e : vol.getValue().entrySet()) {
                msg.append("    "+e.getKey()+": "+e.getValue()+"\n");
            }
        }
        System.out.print(msg.toString());
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("org.jlab.geom.detector.dc.DriftChamberTest");
    }
}
