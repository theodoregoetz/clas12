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
        double p2pdist;
        double gaptot;

        double costhtilt = cos(panel.thtilt);
        double dist2edge = panel.dist2edge;
        double thmin = panel.thmin;
        double wrapper_thickness = panel.wrapper_thickness;
        double width = panel.paddle_width;
        double gap = panel.paddle_gap;
        double pairgap = panel.paddle_pairgap;

        double x = dist2edge * sin(thmin) + (0.5*width+wrapper_thickness) * costhtilt;

        if (panel.name() == "1b") {
            p2pdist = width + 2.*wrapper_thickness;
            gaptot = ((index+1)/2) * gap + (index/2) * pairgap;
            x += (index * p2pdist + gaptot) * costhtilt;
        } else {
            p2pdist = width + gap + 2.*wrapper_thickness;
            x += index * p2pdist * costhtilt;
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
        double p2pdist;
        double gaptot;

        double dist2edge = panel.dist2edge;
        double thmin = panel.thmin;
        double thtilt = panel.thtilt;
        double wrapper_thickness = panel.wrapper_thickness;
        double width = panel.paddle_width;
        double gap = panel.paddle_gap;
        double pairgap = panel.paddle_pairgap;

        double z = dist2edge * cos(thmin) - (0.5*width+wrapper_thickness) * sin(thtilt);

        if (panel.name() == "1b") {
            p2pdist = width + 2.*wrapper_thickness;
            gaptot = ((index+1)/2) * gap + (index/2) * pairgap;
            z -= (index * p2pdist + gaptot) * sin(thtilt);
        } else {
            p2pdist = width + gap + 2.*wrapper_thickness;
            z -= index * p2pdist * sin(thtilt);
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

    String g4Name() {
        return new String(panel.g4Name()+"_pad"+(index+1));
    }

    String description() {
        return new String(panel.description()+" Paddle "+(index+1));
    }

    G4Volume g4Volume(CoordinateSystem coord) {
        /*
        generating parameters for the paddle volumes following
        the BOX constructor:
            pDx     Half-length along the x-axis
            pDy     Half-length along the y-axis
            pDz     Half-length along the z-axis
        */

        double dx = 0.5*this.length();
        double dy = 0.5*panel.paddle_thickness;
        double dz = 0.5*panel.paddle_width;

        // posz is the GEANT z position of each paddle
        // (corresponding to x in sector coords)
        double x = this.centerX();
        double posz = (x - panel.center(CoordinateSystem.SECTOR).x()) / cos(panel.thtilt);

        //paddle's position relative to the panel (mother volume)
        Vector3D paddle_position = new Vector3D(0,0,posz);

        String paddle_pos = new String(
            paddle_position.x() + "*cm " +
            paddle_position.y() + "*cm " +
            paddle_position.z() + "*cm");

        String paddle_rot = new String("0*deg 0*deg 0*deg");
        String paddle_dim = new String(dx+"*cm "+dy+"*cm "+dz+"*cm");
        String paddle_ids  = new String("sector ncopy 0 paddle manual "+(index+1));
        String paddle_sens = new String("FTOF_"+panel.name());

        // The paddle volume
        G4Volume vol = new G4Volume();
        vol.put("mother", panel.g4Name());
        vol.put("description", this.description());
        vol.put("pos", paddle_pos);
        vol.put("rotation", paddle_rot);
        vol.put("color", "ff11aa");
        vol.put("type", "Box");
        vol.put("dimensions", paddle_dim);
        vol.put("material", "scintillator");
        vol.put("mfield", "no");
        vol.put("ncopy", "1");
        vol.put("pMany", "1");
        vol.put("exist", "1");
        vol.put("visible", "1");
        vol.put("style", "1");
        vol.put("sensitivity", paddle_sens);
        vol.put("hit_type", paddle_sens);
        vol.put("identifiers", paddle_ids);
        return vol;
    }
}
