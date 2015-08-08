package org.jlab.geom.detector.dc;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;

import org.jlab.geom.detector.dc.DriftChamber;
//import org.jlab.geom.base.ConstantProvider;

public class DriftChamberTest {

    @Test
    public void testConstruction() {
        DriftChamber dcgeom = new DriftChamber();
    }

    /*@Test
    public void testFetchParameters() {
        dcgeom = new DriftChamber();
        ConstantProvider provider = new ConstantProvider();
        dcgeom.fetch_nominal_parameters();
    }*/

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("org.jlab.geom.detector.dc.DriftChamberTest");
    }
}
