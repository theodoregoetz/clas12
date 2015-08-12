package org.jlab.geom.detector.ftof;

import static java.lang.Math.*;
import java.util.*;

import org.jlab.geom.G4Volume;
import org.jlab.geom.CoordinateSystem;
import org.jlab.geom.prim.*;
import org.jlab.geom.detector.ftof.*;

/** \class Panel
 * \brief A panel of paddles in a sector of the forward TOF
 *
 * There are three panels in each sector of CLAS12
 **/
class Panel {

    ForwardTOF ftof;
    Sector sector;
    ArrayList<Paddle> paddles;
    int index;
    String name;

    double paddle_width;
    double paddle_thickness;
    double thtilt;
    double thmin;
    double dist2edge;
    double paddle_gap;
    double paddle_pairgap;
    double wrapper_thickness;

    Panel(Sector sector) {
        this.sector = sector;
        this.ftof = sector.ftof;
    }

    int nPaddles() {
        return paddles.size();
    }

    int paddleIndex(int idx) {
        if (idx<0) {
            idx = this.nPaddles() + idx;
        }
        return idx;
    }

    Paddle paddle(int idx) {
        return paddles.get(this.paddleIndex(idx));
    }
}
