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
    JDBCProvider provider;

    public ForwardTOFTest() {
        this.provider = CcdbPackage.createProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        this.provider.setDefaultRun(0);
        this.provider.setDefaultVariation("default");
        this.provider.setDefaultDate(new Date());
        this.provider.connect();
    }

    @Test
    public void testConstruction() {
        ForwardTOF ftofgeom = new ForwardTOF();
    }

    @Test
    public void testFetchParametersOnConstruction() {
        ForwardTOF ftofgeom = new ForwardTOF(this.provider);
    }

    @Test
    public void testFetchParametersAfterConstruction() {
        ForwardTOF ftofgeom = new ForwardTOF();
        ftofgeom.fetchNominalParameters(provider);
    }

    @Test
    public void testVolumes() {
        ForwardTOF ftofgeom = new ForwardTOF();
        ftofgeom.fetchNominalParameters(this.provider);

        G4VolumeMap vols = ftofgeom.g4Volumes(CoordinateSystem.CLAS);
        out.print(vols.toString());
    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("org.jlab.geom.detector.ftof.ForwardTOFTest");
    }
}
