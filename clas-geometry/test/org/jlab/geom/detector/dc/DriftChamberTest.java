package org.jlab.geom.detector.dc;

import org.junit.*;
import static org.junit.Assert.*;
import static java.lang.System.out;

import java.util.*;

import org.jlab.ccdb.CcdbPackage;
import org.jlab.ccdb.JDBCProvider;

import org.jlab.geom.prim.*;
import org.jlab.geom.G4VolumeMap;
import org.jlab.geom.CoordinateSystem;
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
    public void testVolumes() {
        DriftChamber dcgeom = new DriftChamber();
        JDBCProvider provider = CcdbPackage.createProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.setDefaultRun(0);
        provider.setDefaultVariation("default");
        provider.connect();
        dcgeom.fetchNominalParameters(provider);

        G4VolumeMap vols = dcgeom.g4Volumes();
        out.print(vols.toString());
    }

    @Test
    public void testEndpoints() {
        DriftChamber dcgeom = new DriftChamber();
        JDBCProvider provider = CcdbPackage.createProvider("mysql://clas12reader@clasdb.jlab.org/clas12");
        provider.setDefaultRun(0);
        provider.setDefaultVariation("default");
        provider.connect();
        dcgeom.fetchNominalParameters(provider);

        int sectors[] = {0};
        int regions[] = {0,1,2};
        int superlayers[] = {0,1};
        int layers[] = {0,1,5};
        int wires[] = {0,1,111};

        StringBuilder msg = new StringBuilder();
        for (int sec : sectors) {
            Sector sector = dcgeom.sector(sec);
            for (int reg : regions) {
                Region region = sector.region(reg);
                for (int slyr : superlayers) {
                    Superlayer superlayer = region.superlayer(slyr);
                    for (int lyr : layers) {
                        Layer layer = superlayer.senselayer(lyr);
                        for (int wr : wires) {
                            Line3D wire = layer.sensewire(wr,CoordinateSystem.CLAS);
                            msg.append(
                                (sec+1) + " " +
                                (reg+1) + " " +
                                (slyr+1) + " " +
                                (lyr+1) + " " +
                                (wr+1) + " " +
                                wire.origin().toStringBrief() + " " +
                                wire.midpoint().toStringBrief() + " " +
                                wire.end().toStringBrief() + "\n");
                        }
                    }
                }
            }
        }
        System.out.print(msg.toString());

        /*
        Layer layer0 = dcgeom.sector(0).region(0).superlayer(0).senselayer(0);
        Layer layer1 = dcgeom.sector(0).region(0).superlayer(0).senselayer(1);
        Line3D wire00 = layer0.sensewire(0,CoordinateSystem.CLAS);
        Line3D wire10 = layer1.sensewire(0,CoordinateSystem.CLAS);
        out.print("sec,reg,slyr,senselyr,sensewire = 0,0,0,0,0\n");
        out.print("left,right endpoints: "+wire00.origin().toStringBrief()+" "+wire00.end().toStringBrief()+"\n");
        out.print("layer index: "+layer0.index+"\n");
        out.print("wireMidX: "+layer0.wireMidX(0)+"\n");
        out.print("sec,reg,slyr,senselyr,sensewire = 0,0,0,1,0\n");
        out.print("left,right endpoints: "+wire10.origin().toStringBrief()+" "+wire10.end().toStringBrief()+"\n");
        out.print("layer index: "+layer1.index+"\n");
        out.print("wireMidX: "+layer1.wireMidX(0)+"\n");
        */

    }

    public static void main(String args[]) {
        org.junit.runner.JUnitCore.main("org.jlab.geom.detector.dc.DriftChamberTest");
    }
}
