package org.jlab.clasrec.utils;

import org.junit.*;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.*;
import static java.lang.System.out;

import java.util.*;

public class CLASGeometryLoaderTest {

    @Test
    public void testConstruction() {
        CLASGeometryLoader loader = new CLASGeometryLoader();
    }

    @Test
    public void testLoadGeometryEC() {
        CLASGeometryLoader loader = new CLASGeometryLoader();
        int run = 0;
        String variation = new String("default");
        Date date = new Date();
        loader.loadGeometry("EC",run,variation,date);
    }

    @Test
    public void testLoadGeometryBST() {
        CLASGeometryLoader loader = new CLASGeometryLoader();
        int run = 0;
        String variation = new String("default");
        Date date = new Date();
        loader.loadGeometry("BST",run,variation,date);
    }

    @Test
    public void testLoadGeometryFTOF() {
        CLASGeometryLoader loader = new CLASGeometryLoader();
        int run = 0;
        String variation = new String("default");
        Date date = new Date();
        loader.loadGeometry("FTOF",run,variation,date);
    }

    public static void main(String args[]) {
        JUnitCore.main("org.jlab.clasrec.utils.CLASGeometryLoaderTest");
    }
}
