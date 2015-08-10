package org.jlab.geom.detector.dc;

import static java.lang.Math.*;
import java.util.*;

import org.jlab.geom.prim.*;
import org.jlab.geom.detector.dc.*;

/** \class Region
 * \brief A region of superlayers in a sector of the drift chambers
 *
 * There are three regions in each sector of CLAS12
 **/
class Region {

    Sector sector;
    ArrayList<Superlayer> superlayers;
    int index;

    double dist2tgt;
    double frontgap;
    double midgap;
    double backgap;
    double thopen;
    double thtilt;
    double xdist;

    Region(Sector sector) {
        this.sector = sector;
        this.superlayers = new ArrayList<Superlayer>();
    }

    int nSuperlayers() {
        return superlayers.size();
    }

    int superlayerIndex(int idx) {
        if (idx<0) {
            idx = this.nSuperlayers() + idx;
        }
        return idx;
    }

    Superlayer superlayer(int idx) {
        return superlayers.get(this.superlayerIndex(idx));
    }

    /**
     * \brief Get the thickness of this region
     *
     * This is sum of the frontgap, midgap, backgap and the thicknesses
     * of each superlayer in this region.
     *
     * \return thickness of this region
     **/
    double thickness() {
        // sum up the gaps
        double thickness = frontgap + midgap + backgap;
        // add each superlayer's thickness
        for (Superlayer slyr : superlayers) {
            thickness += slyr.thickness();
        }
        return thickness;
    }

    /**
     * \brief The left end-plane plane
     * \return plane(point on plane, normal) in sector coordinate system (cm)
     **/
    Plane3D leftEndPlate() {
        // first, calculate the plane in sector coords
        Point3D point = new Point3D(xdist,0,0);
        Vector3D norm = new Vector3D(sin(0.5*thopen),cos(0.5*thopen),0).asUnit();
        Plane3D ret = new Plane3D(point,norm);
        return ret;
    }

    /**
     * \brief The right end-plane plane
     * \return plane(point on plane, normal) in sector coordinate system (cm)
     **/
    Plane3D rightEndPlate() {
        // first, calculate the plane in sector coords
        Point3D point = new Point3D(xdist,0,0);
        Vector3D norm = new Vector3D(sin(0.5*thopen),-cos(0.5*thopen),0).asUnit();
        Plane3D ret = new Plane3D(point,norm);
        return ret;
    }

    /**
     * \brief The center-point of this region
     *
     * The x and z coordinates are taken as the midpoint between
     * these two point:
     * p0 = endpoint of the first guard wire in the first (gaurd)
     *      wire-plane in the first superlayer of this region
     * p1 = endpoint of the last guard wire in the last (guard)
     *      wire-plane in the last superlayer of this region
     * The y-coordinate is always zero since this is in the sector
     * coordinate system.
     *
     * \return (x,y,z) position in sector-coordinates of this region (cm)
     **/
    Vector3D center() {
        // first, calculate the point in sector coords
        Vector3D p0 = this.superlayer( 0).guardlayer( 0).wire( 0).end().toVector3D();
        Vector3D p1 = this.superlayer(-1).guardlayer(-1).wire(-1).end().toVector3D();
        Vector3D regionCenter = p0.add(p1).multiply(0.5);
        regionCenter.setY(0);
        return regionCenter;
    }

}
