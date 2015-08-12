package org.jlab.geom.detector.dc;

import java.util.*;

import org.jlab.geom.CoordinateSystem;
import org.jlab.geom.prim.*;
import org.jlab.geom.detector.dc.*;

/**
 * \brief a sector of the drift chamber which consists of several regions
 *
 * There are six sectors of drift chambers in CLAS12 which hold
 * three regions
 **/
class Sector {

    DriftChamber dc;
    ArrayList<Region> regions;
    int index;

    Sector(DriftChamber dc) {
        this.dc = dc;
        this.regions = new ArrayList<Region>();
    }

    int nRegions() {
        return regions.size();
    }

    int regionIndex(int idx) {
        if (idx<0) {
            idx = this.nRegions() + idx;
        }
        return idx;
    }

    Region region(int idx) {
        return regions.get(this.regionIndex(idx));
    }

    Vector3D sectorToCLAS(Vector3D v) {
        final double sector_phi = 3.1415926535898 / 3.;
        Vector3D ret = v.clone();
        if (index != 0) {
            ret.rotateZ(index * sector_phi);
        }
        return ret;
    }
    Point3D sectorToCLAS(Point3D p) {
        return this.sectorToCLAS(p.toVector3D()).toPoint3D();
    }
    Line3D sectorToCLAS(Line3D l) {
        return new Line3D(
            this.sectorToCLAS(l.origin()),
            this.sectorToCLAS(l.end()) );
    }
    Plane3D sectorToCLAS(Plane3D p) {
        return new Plane3D(
            this.sectorToCLAS(p.point()),
            this.sectorToCLAS(p.normal()) );
    }

    String name() {
        return new String("S"+(index+1));
    }

    String description() {
        return new String(dc.description()+" Sector "+(index+1));
    }

    Map<String,Map<String,String>> volumes(CoordinateSystem coord) {
        Map<String,Map<String,String>> vols = new HashMap<String,Map<String,String>>();
        for (Region region : regions) {
            vols.put(region.name(),region.volume(coord));
            for (Superlayer superlayer : region.superlayers) {
                for (Layer layer : superlayer.senselayers()) {
                    vols.put(layer.name(),layer.volume(coord));
                }
            }
        }
        return vols;
    }
}
