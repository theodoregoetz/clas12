package org.jlab.clasrec.utils;

import org.junit.*;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.*;
import static java.lang.System.out;

import java.util.*;

public class DetectorComponentTest {

    @Test
    public void testConstruction() {
        DetectorComponent comp = new DetectorComponent();
    }

    public static void main(String args[]) {
        JUnitCore.main("org.jlab.clasrec.utils.DetectorComponentTest");
    }
}
