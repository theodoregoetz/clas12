package org.jlab.clasrec.utils;

import org.junit.*;
import org.junit.runner.JUnitCore;
import static org.junit.Assert.*;
import static java.lang.System.out;

import java.util.*;

public class DatabaseConstantProviderTest {

    @Test
    public void testConstruction() {
        String connstr = new String("mysql://clas12reader@clasdb.jlab.org/clas12");
        DatabaseConstantProvider provider = new DatabaseConstantProvider(connstr);
    }

    @Test
    public void testSetDefaults() {
        String connstr = new String("mysql://clas12reader@clasdb.jlab.org/clas12");
        DatabaseConstantProvider provider = new DatabaseConstantProvider(connstr);
        provider.setDefaultRun(0);
        provider.setDefaultVariation("default");
        provider.setDefaultDate(new Date());
    }

    @Test
    public void testLoadTable() {
        String connstr = new String("mysql://clas12reader@clasdb.jlab.org/clas12");
        DatabaseConstantProvider provider = new DatabaseConstantProvider(connstr);
        provider.setDefaultRun(0);
        provider.setDefaultVariation("default");
        provider.setDefaultDate(new Date());
        provider.loadTable("/geometry/dc/dc");
    }

    public static void main(String args[]) {
        JUnitCore.main("org.jlab.clasrec.utils.DatabaseConstantProviderTest");
    }
}
