package org.jlab.clasrec.utils;

import org.junit.*;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.*;
import static java.lang.System.out;

import java.util.*;

import org.jlab.geom.base.ConstantProvider;

public class DataBaseLoaderTest {

    @Test
    public void testConstantsDC() {
        ConstantProvider provider = DataBaseLoader.getDriftChamberConstants(0,"default",new Date());
        provider = DataBaseLoader.getConstantsDC(0,"default",new Date());
    }

    @Test
    public void testConstantsEC() {
        ConstantProvider provider = DataBaseLoader.getCalorimeterConstants(0,"default",new Date());
        provider = DataBaseLoader.getConstantsEC(0,"default",new Date());
    }

    @Test
    public void testConstantsFTOF() {
        ConstantProvider provider = DataBaseLoader.getTimeOfFlightConstants(0,"default",new Date());
        provider = DataBaseLoader.getConstantsFTOF(0,"default",new Date());
    }

    @Test
    public void testConstantsCND() {
        ConstantProvider provider = DataBaseLoader.getConstantsCND(0,"default",new Date());
    }

    @Test
    public void testConstantsFTCAL() {
        ConstantProvider provider = DataBaseLoader.getConstantsFTCAL(0,"default",new Date());
    }

    @Test
    public void testConstantsBST() {
        ConstantProvider provider = DataBaseLoader.getConstantsBST(0,"default",new Date());
    }

    public static void main(String args[]) {
        JUnitCore.main("org.jlab.clasrec.utils.DataBaseLoaderTest");
    }
}
