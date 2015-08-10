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
        dcgeom.fetch_nominal_parameters(provider);
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("org.jlab.geom.detector.dc.DriftChamberTest");
    }
}
