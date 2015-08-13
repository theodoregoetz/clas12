package org.jlab.geom.detector.ftof;

import org.junit.*;
import static org.junit.Assert.*;
import static java.lang.System.out;

import java.util.*;

import org.jlab.ccdb.CcdbPackage;
import org.jlab.ccdb.JDBCProvider;

import org.jlab.geom.prim.*;
import org.jlab.geom.G4VolumeMap;
import org.jlab.geom.CoordinateSystem;
import org.jlab.geom.detector.ftof.ForwardTOF;

public class ForwardTOFTest {

    @Test
    public void testConstruction() {
        ForwardTOF ftofgeom = new ForwardTOF();
    }

    @Test
    public void testFetchParametersOnConstruction() {
        JDBCProvider provider = CcdbPackage.createProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.setDefaultRun(0);
        provider.setDefaultVariation("default");
        provider.setDefaultDate(Date());
        provider.connect();
        ForwardTOF ftofgeom = new ForwardTOF(provider);
    }

    @Test
    public void testFetchParametersAfterConstruction() {
        ForwardTOF ftofgeom = new ForwardTOF();
        JDBCProvider provider = CcdbPackage.createProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.setDefaultRun(0);
        provider.setDefaultVariation("default");
        provider.setDefaultDate(Date());
        provider.connect();
        ftofgeom.fetchNominalParameters(provider);
    }

    @Test
    public void testVolumes() {
        ForwardTOF ftofgeom = new ForwardTOF();
        JDBCProvider provider = CcdbPackage.createProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.setDefaultRun(0);
        provider.setDefaultVariation("default");
        provider.setDefaultDate(Date());
        provider.connect();
        ftofgeom.fetchNominalParameters(provider);

        //G4VolumeMap vols = ftofgeom.g4Volumes();
        //out.print(vols.toString());
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("org.jlab.geom.detector.ftof.ForwardTOFTest");
    }
}
