package org.jlab.geom;

import static java.lang.System.out;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        JUnitCore core = new JUnitCore();
        core.addListener(new TestRunListener());
        Result res = core.run(
           org.jlab.geom.detector.dc.DriftChamberTest.class,
           org.jlab.geom.detector.ftof.ForwardTOFTest.class
        );
        if (res.wasSuccessful()) {
            out.print("OK\n");
        } else {
            out.print("ERROR\n");
        }
    }
}
