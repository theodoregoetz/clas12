package org.jlab.clasrec.utils;

import org.junit.*;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.*;
import static java.lang.System.out;

import java.util.*;

public class DataBaseLoaderTest {

    @Test
    public void testConstruction() {
        DataBaseLoader loader = new DataBaseLoader();
    }

    public static void main(String args[]) {
        JUnitCore.main("org.jlab.clasrec.utils.DataBaseLoaderTest");
    }
}
