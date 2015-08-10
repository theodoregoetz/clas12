package org.jlab.geom.detector.dc;

import static java.lang.Math.*;
import java.util.*;

import org.jlab.geom.prim.*;
import org.jlab.geom.detector.dc.*;

/**
 * \brief A layer that consists of sense and/or guard wires.
 *
 * There are typically two guard wires which surround many
 * sensewires, each of which can be on or off.
 **/
class Layer {

    Superlayer superlayer;
    int index;

    boolean isSenselayer;
    int nGuardwires;
    int nSensewires;

    Layer(Superlayer superlayer) {
        this.superlayer = superlayer;
    }

    int nWires() {
        return nGuardwires + nSensewires;
    }

    int wireIndex(int idx) {
        if (idx<0) {
            idx = this.nWires() + idx;
        }
        return idx;
    }
    int guardwireIndex(int idx) {
        return this.wireIndex(idx);
    }
    int sensewireIndex(int idx) {
        return this.wireIndex(idx) + 1;
    }

    /**
     * \brief x-position of the midpoint of a wire in this layer
     *
     * Midpoints are the intersection points of the wires with
     * the sector mid-plane.
     *
     * \param [in] w the wire index (starting from zero) in this layer
     * \return x position in sector coordinate system (cm)
     **/
    public double wireMidX(int w) {
        // r is the distance from the guard layer to this wire plane in cm.
        double r = ((double) index) * superlayer.cellthickness * superlayer.wpdist;
        double xmid = superlayer.firstWireMidX() + r * sin(superlayer.region.thtilt);
        double wmidsp = superlayer.wireMidSpacing();

        // stagger wire planes
        double nwire = (double) this.wireIndex(w);
        if ((index % 2) == 0) {
            nwire += 0.5;
        }
        xmid += nwire * wmidsp * cos(superlayer.region.thtilt);

        // mini-stagger
        if (superlayer.region.index == 2)
        {
            if ((index % 2) == 0) {
                xmid += 0.001;
            } else {
                xmid -= 0.001;
            }
        }

        return xmid;
    }

    /**
     * \brief y-position of the midpoint of a wire in this layer
     *
     * The y-position of the midpoint of all wires in the
     * sector coordinate system is zero. Midpoints are the
     * intersection points of the wires with the sector
     * mid-plane.
     *
     * \param [in] w wire index from zero (ignored)
     * \return y position in sector coordinate system (cm)
     **/
    double wireMidY() {
        return 0.;
    }
    double wireMidY(int w) {
        return this.wireMidY();
    }

    /**
     * \brief z-position of the midpoint of a wire in this layer
     *
     * Midpoints are the intersection points of the wires with
     * the sector mid-plane.
     *
     * \param [in] w the wire index (starting from zero) in this layer
     * \return z position in sector coordinate system (cm)
     **/
    double wireMidZ(int w) {
        // r is the distance from the guard layer to this wire plane in cm.
        double r = ((double) index) * superlayer.cellthickness * superlayer.wpdist;
        double zmid = superlayer.firstWireMidZ() + r * cos(superlayer.region.thtilt);
        double wmidsp = superlayer.wireMidSpacing();

        // stagger even wire planes
        double nwire = (double) this.wireIndex(w);
        if ((index % 2) == 0) {
            nwire += 0.5;
        }
        zmid -= nwire * wmidsp * sin(superlayer.region.thtilt);

        // mini-stagger
        if (superlayer.region.index == 2) {
            if ((index % 2) == 0) {
                zmid -= 0.001;
            } else {
                zmid += 0.001;
            }
        }

        return zmid;
    }

    /**
     * \brief position of the midpoint of a wire in this layer
     *
     * Midpoints are the intersection points of the wires with
     * the sector mid-plane.
     *
     * \param [in] w the wire index (starting from zero) in this layer
     * \return (x,y,z) position in sector coordinate system (cm)
     **/
    Vector3D wireMid(int w) {
        return new Vector3D(
            this.wireMidX(w),
            this.wireMidY(w),
            this.wireMidZ(w) );
    }

    /**
     * \brief x-positions of the midpoints of all wires in this layer
     *
     * Midpoints are the intersection points of the wires with
     * the sector mid-plane.
     *
     * \return x positions in sector coordinate system (cm)
     **/
    Vector<Double> wiresMidX() {
        Vector<Double> ret = new Vector<Double>(this.nWires());
        for (int w=0; w<ret.size(); w++) {
            ret.set(w,this.wireMidX(w));
        }
        return ret;
    }

    /**
     * \brief y-positions of the midpoints of all wires in this layer
     *
     * Midpoints are the intersection points of the wires with
     * the sector mid-plane.
     *
     * \return y positions in sector coordinate system (cm)
     **/
    Vector<Double> wiresMidY() {
        Vector<Double> ret = new Vector<Double>(this.nWires());
        for (int w=0; w<ret.size(); w++) {
            ret.set(w,this.wireMidY());
        }
        return ret;
    }

    /**
     * \brief z-positions of the midpoints of all wires in this layer
     *
     * Midpoints are the intersection points of the wires with
     * the sector mid-plane.
     *
     * \return z positions in sector coordinate system (cm)
     **/
    Vector<Double> wiresMidZ() {
        Vector<Double> ret = new Vector<Double>(this.nWires());
        for (int w=0; w<ret.size(); w++) {
            ret.set(w,this.wireMidZ(w));
        }
        return ret;
    }

    /**
     * \brief vector of midpoints of all wires in this layer
     *
     * Midpoints are the intersection points of the wires with
     * the sector mid-plane.
     *
     * \return (x,y,z) positions in sector coordinate system (cm)
     **/
    Vector<Vector3D> wiresMid() {
        Vector<Vector3D> ret = new Vector<Vector3D>(this.nWires());
        for (int w=0; w<ret.size(); w++) {
            ret.set(w,this.wireMid(w));
        }
        return ret;
    }

    /**
     * \brief position of the center point of a wire in this layer
     *
     * This is the wire's center point. Not to be confused with
     * the "midpoint" which is the intersection point of this
     * wire with the sector's mid-plane.
     *
     * \param [in] w the wire index (starting from zero) in this layer
     * \return (x,y,z) position in sector coordinate system (cm)
     **/
    Vector3D wireCenter(int w) {
        return this.wire(w).midpoint().toVector3D();
    }

    /**
     * \brief the center-point of this sense-layer volume
     * \return (x,y,z) position in sector coordinate system (cm)
     **/
    Vector3D center() {
        return this.wireCenter(0).add(this.wireCenter(-1)).multiply(0.5);
    }

    /**
     * \brief distance from the origin to this wire plane
     *
     * The origin is (0,0,0) in the CLAS coordinate system
     *
     * \return distance (cm)
     **/
    double dist2tgt() {
        return superlayer.dist2tgt() + index * superlayer.layerThickness();
    }

    /**
     * \brief all 3D line segments representing guard and sense wires in this layer
     * \return vector of line segments in sector coordinate system (cm)
     **/
    Vector<Line3D> wires() {
        Vector<Line3D> ret = new Vector<Line3D>();

        // end plates of this region
        Plane3D lplate = superlayer.region.leftEndPlate();
        Plane3D rplate = superlayer.region.rightEndPlate();

        Vector3D wd = superlayer.wireDirection();
        Point3D ileft = new Point3D();
        Point3D iright = new Point3D();
        for (int idx=0; idx<this.nWires(); idx++) {
            // wire as a line
            Line3D wireLine = new Line3D(
                new Point3D(this.wireMid(idx)),
                new Point3D(wd) );

            // get the intersection and create line segment from one
            // point to the other.
            lplate.intersection(wireLine, ileft);
            rplate.intersection(wireLine, iright);
            ret.add(new Line3D(ileft,iright));
        }
        return ret;
    }

    /**
     * \brief 3D line segment representing a wire
     * \param [in] w the wire index (starting from zero) in this layer
     * \return line segment with end-points in sector coordinate system (cm)
     **/
    Line3D wire(int w) {
        // end plates of this region
        Plane3D lplate = superlayer.region.leftEndPlate();
        Plane3D rplate = superlayer.region.rightEndPlate();

        // wire as a line
        Line3D wireLine = new Line3D(
            new Point3D(this.wireMid(w)),
            new Point3D(superlayer.wireDirection()) );

        // get the intersection and create line segment from one
        // point to the other.
        Point3D ileft = new Point3D();
        Point3D iright = new Point3D();
        lplate.intersection(wireLine, ileft);
        rplate.intersection(wireLine, iright);
        return new Line3D(ileft,iright);
    }

    /**
     * \brief length of a given sense wire
     * \param [in] w the wire index (starting from zero) in this layer
     * \return length (cm)
     **/
    double wireLength(int w) {
        return this.wire(w).length();
    }

    /**
     * \brief this sense layer's wire-plane
     * \return plane(point on plane, normal) in sector coordinate system (cm)
     **/
    Plane3D wirePlane() {
        return new Plane3D(
            new Point3D(this.wireMid(0)),
            superlayer.wireDirection() );
    }

}
