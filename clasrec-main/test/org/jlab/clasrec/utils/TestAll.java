package org.jlab.clasrec.utils;

import org.junit.runner.*;
import org.junit.runner.notification.Failure;
import static java.lang.System.out;

public class TestAll {
    public static void main(String args[]) {
        JUnitCore core = new JUnitCore();
        Result res = core.runClasses(
            org.jlab.clasrec.utils.CLASGeometryLoaderTest.class,
            org.jlab.clasrec.utils.DatabaseConstantProviderTest.class,
            org.jlab.clasrec.utils.DataBaseLoaderTest.class,
            org.jlab.clasrec.utils.DetectorComponentTest.class);
        for (Failure failure : res.getFailures()) {
            out.println(failure.toString());
        }
        out.print("\nTests complete.\n");
        out.print("    "+res.getRunCount()+" tests\n");
        out.print("    "+res.getFailureCount()+" failures\n");
        if (res.wasSuccessful()) {
            out.print("OK\n");
        } else {
            out.print("ERROR\n");
        }
    }
}
