package org.jlab.geom.detector.ftof;

import static java.lang.Math.*;
import java.util.*;

import org.jlab.geom.G4Volume;
import org.jlab.geom.CoordinateSystem;
import org.jlab.geom.prim.*;
import org.jlab.geom.detector.ftof.*;

/** \class Paddle
 * \brief A single Forward TOF paddle
 **/
class Paddle {

    ForwardTOF ftof;
    Sector sector;
    Panel panel;
    int index;

    double meas_lengths;
    double slopes;
    double intercepts;

    Paddle(Panel panel) {
        this.panel = panel;
        this.sector = panel.sector;
        this.ftof = sector.ftof;
    }
}
