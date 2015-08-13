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

    double meas_length;
    double slope;
    double intercept;

    Paddle(Panel panel) {
        this.panel = panel;
        this.sector = panel.sector;
        this.ftof = sector.ftof;
    }

    double centerX() {
        size_t pidx = this->paddle_index(p);
        double costhtilt = cos(_thtilt);
        double p2pdist;
        double gaptot;

        double x = _dist2edge * sin(_thmin) + (0.5*_paddle_width+_wrapper_thickness) * costhtilt;

        if ((_idx == 0) || (_idx == 2)) // Panels 1a and 2
        {
            p2pdist = _paddle_width + _paddle_gap + 2.*_wrapper_thickness;
            x += pidx * p2pdist * costhtilt;
        }
        else // Panel 1b
        {
            p2pdist = _paddle_width + 2.*_wrapper_thickness;
            gaptot = double(div(pidx+1,2).quot) * _paddle_gap + double(div(pidx,2).quot) * _paddle_pairgap;
            x += (pidx * p2pdist + gaptot) * costhtilt;
        }

        // at this point, we have the face edge (outside of the wrapper)
        // we now move to the center of the volume
        //x += (0.5*_paddle_thickness + _wrapper_thickness) * sin(_thtilt);

        return x;
    }
    double centerY() {
        return 0;
    }
    double centerZ() {
        size_t pidx = this->paddle_index(p);
        double p2pdist;
        double gaptot;

        double z = _dist2edge * cos(_thmin) - (0.5*_paddle_width+_wrapper_thickness) * sin(_thtilt);

        if ((_idx == 0) || (_idx == 2)) // Panels 1a and 2
        {
            p2pdist = _paddle_width + _paddle_gap + 2.*_wrapper_thickness;
            z -= pidx * p2pdist * sin(_thtilt);
        }
        else // Panel 1b
        {
            p2pdist = _paddle_width + 2.*_wrapper_thickness;
            gaptot = div(pidx+1,2).quot * _paddle_gap + div(pidx,2).quot * _paddle_pairgap;
            z -= (pidx * p2pdist + gaptot) * sin(_thtilt);
        }

        // at this point, we have the face edge (outside of the wrapper)
        // we now move to the center of the volume
        //z -= (0.5*_paddle_thickness + _wrapper_thickness) * cos(_thtilt);

        return z;
    }

    Vector3D center(CoordinateSystem coord) {
        Vector3D ret = new Vector3D(this.centerX(), this.centerY(), this.centerZ());
        switch (coord) {
            case SECTOR:
                // do nothing
                break;
            case CLAS:
                ret = sector.sectorToCLAS(ret);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return ret;
    }

    double length() {
        return slope*(index+1) + intercept;
    }
}
