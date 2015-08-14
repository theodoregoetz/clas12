package org.jlab.geom.detector.ftof;

import static java.lang.Math.*;
import java.util.*;

import org.jlab.geom.GemcVolume;
import org.jlab.geom.GemcVolumeMap;
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
    List<Paddle> paddles;
    int index;

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
        this.paddles = new ArrayList<Paddle>();
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

    String name() {
        return ftof.panelName(index);
    }

    Paddle paddle(int idx) {
        return paddles.get(this.paddleIndex(idx));
    }

    Vector<Vector3D> paddleCenters(CoordinateSystem coord) {
        Vector<Vector3D> ret = new Vector<Vector3D>();
        for (Paddle paddle : paddles) {
            ret.add(paddle.center(coord));
        }
        return ret;
    }

    Vector<Double> paddleLengths() {
        Vector<Double> ret = new Vector<Double>();
        for (Paddle paddle : paddles) {
            ret.add(paddle.length());
        }
        return ret;
    }


    Vector3D normal(CoordinateSystem coord) {
        Vector3D ret = Vector3D.fromSpherical(1,0,thtilt);
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

    Plane3D plane(CoordinateSystem coord) {
        return new Plane3D(this.paddle(0).center(coord).toPoint3D(), this.normal(coord));
    }


    /**
     * \brief The center-point of this panel
     * x is found as a midpoint between the first and the last paddle center-x positions
     * The y-coordinate is always zero since this is in the sector
     * coordinate system.
     * z is found as a midpoint between the first and the last paddle center-z positions
     * \return (x,y,z) position in sector-coordinates of this region (cm)
     **/
    Vector3D center(CoordinateSystem coord) {
        Vector3D first = this.paddle( 0).center(coord);
        Vector3D last  = this.paddle(-1).center(coord);
        return first.add(last).multiply(0.5);
    }

    /**
     * distance from the target to the plane defined by the centers of the paddles along the normal to the plane
     *
     **/
    double dist2tgt() {
         return dist2edge * cos(thtilt - thmin);
    }

    /**
     * \brief Overall radial extent of this panel:
     * the length along the sector midplane from the inside edge of counter #1
     * to the outside edge of the last counter
     **/
    double radialExtent() {
        double np = (double) this.nPaddles();

        double x = np * (paddle_width + 2*wrapper_thickness);

        if (this.name() == "1b") {
            x += (np/2.-1) * paddle_gap;
        } else {
            x += (np   -1) * paddle_gap;
        }

        return x;
    }

    Vector3D paddleDirection(CoordinateSystem coord) {
        Vector3D ret = new Vector3D(0,1,0); // (x,y,z) = (0,1,0)
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

    Vector3D paddleExtent(CoordinateSystem coord) {
        double ct = cos(thtilt);
        double st = sin(thtilt);

        Vector3D ret = new Vector3D(
            paddle_width*ct + paddle_thickness*st,
            0,
            paddle_width*st + paddle_thickness*ct);

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

    String gemcName() {
        return new String(sector.gemcName()+"_pan"+this.name());
    }

    String description() {
        return new String(sector.description()+" Panel "+this.name());
    }

    /**
     * \brief generate the volumes of a FTOF Sector for input into gemc/geant4
     *
     * The numbers for the mother volumes are calculated following Geant4's TRD constructor:

     *     pDx1    Half-length along x at the z surface positioned at -DZ
     *     pDx2    Half-length along x at the z surface positioned at +DZ
     *     pDy     Half-length along y
     *     pDz     Half-length along z
     *
     * \return map of map of strings: ret[volume_name][param_name] = value
     **/
    GemcVolume gemcVolume(CoordinateSystem coord) {
        // 4 mm gap between panel's mother volume and daughter volumes
        final double mothergap = 0.4; //cm

        double dx1 = 0.5 * this.paddle( 0).length() + mothergap;
        double dx2 = 0.5 * this.paddle(-1).length() + mothergap
            + 0.5 * (this.paddle(-1).length() - this.paddle(-2).length());
        double dy  = 0.5 * paddle_thickness + mothergap;
        double dz  = 0.5 * this.radialExtent() + mothergap;

        //panel's center point in CLAS coordinate system
        Vector3D panel_center = this.center(CoordinateSystem.CLAS);

        String panel_pos = new String(
            panel_center.x() + "*cm " +
            panel_center.y() + "*cm " +
            panel_center.z() + "*cm");

        String panel_rot = new String(
            "ordered: zxy " +
            (-90. - 60.*sector.index) + "*deg " +
            (-90. - toDegrees(thtilt)) + "*deg " +
            "0*deg");

        String panel_dim = new String(
            dx1 + "*cm " +
            dx2 + "*cm " +
            dy  + "*cm " +
            dy  + "*cm " +
            dz  + "*cm");

        // The Panel mother volume
        GemcVolume vol = new GemcVolume();
        vol.put("mother", "root");
        vol.put("description", this.description());
        vol.put("pos", panel_pos);
        vol.put("rotation",  panel_rot);
        vol.put("color", "ff11aa5");
        vol.put("type", "Trd");
        vol.put("dimensions", panel_dim);
        vol.put("material", "G4_AIR");
        vol.put("mfield", "no");
        vol.put("ncopy", "1");
        vol.put("pMany", "1");
        vol.put("exist", "1");
        vol.put("visible", "0");
        vol.put("style", "0");
        vol.put("sensitivity", "no");
        vol.put("hit_type", "");
        vol.put("identifiers", "");
        return vol;
    }

    GemcVolumeMap gemcVolumes(CoordinateSystem coord) {
        GemcVolumeMap vols = new GemcVolumeMap();
        vols.put(this.gemcName(),this.gemcVolume(coord));
        for (Paddle paddle : paddles) {
            vols.put(paddle.gemcName(),paddle.gemcVolume(coord));
        }
        return vols;
    }
}
