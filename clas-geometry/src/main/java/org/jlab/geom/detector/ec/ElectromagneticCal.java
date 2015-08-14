package org.jlab.geom.detector.ec;

import java.util.*;
import static java.lang.Math.*;

import org.jlab.ccdb.JDBCProvider;
import org.jlab.ccdb.Assignment;

import org.jlab.geom.G4VolumeMap;
import org.jlab.geom.CoordinateSystem;
import org.jlab.geom.detector.ec.*;

/**
 * \brief The electromagnetic calorimeter geometry class for CLAS12
 *
 * The EC consists of six sectors
 **/
public class ElectromagneticCal {

    List<Sector> sectors;

    /**
     * \brief constructor which fetches the nominal geometry from the database
     *
     * This calls ElectromagneticCal.fetchNominalParameters(provider)
     **/
    public ElectromagneticCal(JDBCProvider provider) {
        this.sectors = new ArrayList<Sector>();
        this.fetchNominalParameters(provider);
    }

    /**
     * \brief fills the ElectromagneticCal class with the nominal geometry
     *
     * The nominal geometry is identical for each sector and therefore
     * after this call, there is much redundancy in the geometry. The
     * parameters obtained are the so-called "core" paramters and
     * it is expected that additional alignment paramters will be
     * obtained from the database in a later method-call.
     *
     * \param [in] dataprovider the CCDB provider object
     **/
    public void fetchNominalParameters(JDBCProvider provider) {

        // the nominal geometry parameters for the EC.
        // These numbers come from four tables:
        ConstantsTable table_ec(calib,"/geometry/ec/ec");
        ConstantsTable table_view_u(calib,"/geometry/ec/Uview");
        ConstantsTable table_view_v(calib,"/geometry/ec/Vview");
        ConstantsTable table_view_w(calib,"/geometry/ec/Wview");

        int    nsectors      = table_ec.elem<size_t>("nsectors"); // n
        int    nviews        = table_ec.elem<size_t>("nviews"); // n
        int    nlayers       = table_ec.elem<size_t>("nlayers"); // n
        double view_angle    = table_ec.elem<double>("view_angle");//n
        double thtilt        = table_ec.elem<double>("thtilt");//n
        double wrapper_thick = table_ec.elem<double>("wrapper_thick");
        double strip_thick   = table_ec.elem<double>("strip_thick");
        double alum_thick    = table_ec.elem<double>("alum_thick");
        double lead_thick    = table_ec.elem<double>("lead_thick");
        double dist2tgt      = table_ec.elem<double>("dist2tgt");
        double dist2cnt      = table_ec.elem<double>("dist2cnt");
        double a1            = table_ec.elem<double>("a1");
        double a2            = table_ec.elem<double>("a2");
        double a3            = table_ec.elem<double>("a3");
        double d2            = table_ec.elem<double>("d2");
        double d2prime       = table_ec.elem<double>("d2prime");

        double u_nstrips    =  table_view_u.elem<double>("nstrips");
        double u_shift      =  table_view_u.elem<double>("a4");
        double u_halfwidth  =  table_view_u.elem<double>("a5");
        double u_deltahw    =  table_view_u.elem<double>("a6");

        double v_nstrips    =  table_view_v.elem<double>("nstrips");
        double v_halfwidth  =  table_view_v.elem<double>("a5");
        double v_deltahw    =  table_view_v.elem<double>("a6");

        double w_nstrips    =  table_view_w.elem<double>("nstrips");
        double w_halfwidth  =  table_view_w.elem<double>("a5");
        double w_deltahw    =  table_view_w.elem<double>("a6");

        // Now we fill the sectors object which holds all these
        // core parameters. Here, many numbers will be redundant.
        // It is expected that this will change once efficiency
        // alignment and other calibrations are taken into effect.
        sectors.clear();

        for (size_t sec=0; sec<nsectors; sec++)
        {
            _sectors.emplace_back(new ECSector(this,sec));
            ECSector& sector = *_sectors[sec];
            sector._layers.clear();

            sector._nlayers     = nlayers;
            sector._alum_thick  = alum_thick*0.1;
            sector._thtilt      = thtilt*deg2rad ;
            sector._dist2tgt    = dist2tgt*0.1;
            sector._dist2cnt    = dist2cnt*0.1;
            sector._a1          = a1*0.1;
            sector._a2          = a2*0.1;
            sector._a3          = a3*0.1;
            sector._d2          = d2*0.1;
            sector._d2prime     = d2prime*0.1;

            for (size_t lyr=0; lyr<nlayers; lyr++)
            {
                sector._layers.emplace_back(new Layer(&sector,lyr));
                Layer& layer = *sector._layers[lyr];

                layer._views.clear();
                layer._nviews          = nviews;
                layer._view_angle      = view_angle*deg2rad ;
                layer._wrapper_thick   = wrapper_thick*0.1;
                layer._strip_thick     = strip_thick*0.1;
                layer._lead_thick      = lead_thick*0.1;

                for (size_t iview=0; iview<nviews; iview++)
                {
                    layer._views.emplace_back(new View(&layer,iview));
                    View& view = *layer._views[iview];

                    view._strips.clear();

                       if (view.name() == "u")
                       {
                           view._nstrips    = u_nstrips;
                           view._shift      = u_shift*0.1;
                           view._halfwidth  = u_halfwidth*0.1;
                           view._deltahw    = u_deltahw*0.1;

                           view._strips.assign(view.nstrips(),true);
                        }
                        else if (view.name() == "v")
                        {
                           view._nstrips    = v_nstrips;
                           view._shift      = 0;
                           view._halfwidth  = v_halfwidth*0.1;
                           view._deltahw    = v_deltahw*0.1;

                           view._strips.assign(view.nstrips(),true);
                        }
                        else if (view.name() == "w")
                        {
                           view._nstrips    = w_nstrips;
                           view._shift      = 0;
                           view._halfwidth  = w_halfwidth*0.1;
                           view._deltahw    = w_deltahw*0.1;


                           view._strips.assign(view.nstrips(),true);
                        }


                }

            }
        }
    }

    int nSectors() {
        return sectors.size();
    }

    int sectorIndex(int idx) {
        if (idx<0) {
            idx = this.nSectors() + idx;
        }
        return idx;
    }

    Sector sector(int idx) {
        return sectors.get(this.sectorIndex(idx));
    }

    String description() {
        return new String("Electromagnetic Calorimeter");
    }

    public G4VolumeMap g4Volumes(CoordinateSystem coord) {
        G4VolumeMap vols = new G4VolumeMap();
        for (Sector sector : sectors) {
            vols.putAll(sector.g4Volumes(coord));
        }
        return vols;
    }
}
