package org.jlab.geom;

import static java.lang.System.out;

import org.junit.runner.Result;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class TestRunListener extends RunListener {
    public void testRunStarted(Description desc) {
        out.print("Starting unit tests\n\n");
    }

    public void testStarted(Description desc) {
        out.print(".");
    }

    public void testFailure(Failure fail) {
        out.print("E");
    }

    public void testRunFinished(Result res) {
        out.print("\n");
        int n = res.getRunCount();
        int nfail = res.getFailureCount();
        if (nfail > 0) {
            for (Failure fail : res.getFailures()) {
                out.print("\n"+fail.toString()+"\n");
            }
            out.print("\n"+nfail+" failures out of "+n+" tests.\n");
        } else {
            out.print("\n"+n+" tests all passed!\n");
        }
    }
}
