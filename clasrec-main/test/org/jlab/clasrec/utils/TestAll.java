package org.jlab.clasrec.utils;

import org.junit.runner.JUnitCore;

public class TestAll {
    public static void main(String args[]) {
        JUnitCore.main("org.jlab.clasrec.utils.CLASGeometryLoaderTest");
        JUnitCore.main("org.jlab.clasrec.utils.DatabaseConstantProviderTest");
        JUnitCore.main("org.jlab.clasrec.utils.DataBaseLoaderTest");
        JUnitCore.main("org.jlab.clasrec.utils.DetectorComponentTest");
    }
}
