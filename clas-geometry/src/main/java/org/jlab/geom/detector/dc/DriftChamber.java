package org.jlab.geom.detector.dc;

import java.util.*;

//import org.jlab.geom.detector.dc;
//import org.jlab.geom.base.ConstantProvider;

/**
 * \brief The drift chamber geometry class for CLAS12
 *
 * The DC consists of six sectors, each with three regions.
 * Each region consists of two superlayers which hold the 22
 * wire-planes: There are six sense-wire planes in a superlayer
 * surrounded by two guard-wire planes, and between each of these
 * there are two "field-wire" planes.
 **/
public class DriftChamber {

    public ArrayList sectors;

    /**
     * \brief default constructor
     **/
    public DriftChamber() {
        this.sectors = new ArrayList();
    }

    /**
     * \brief constructor which fetches the nominal geometry from the database
     *
     * This calls DriftChamber.fetch_nominal_parameters(provider)
     ** /
    public DriftChamber(ConstantProvider provider) {
        this.sectors = new ArrayList();
        this.fetch_nominal_parameters(provider);
    }*/

    /**
     * \brief fills the DriftChamber class with the nominal geometry
     *
     * The nominal geometry is identical for each sector and therefore
     * after this call, there is much redundancy in the geometry. The
     * parameters obtained are the so-called "core" paramters and
     * it is expected that additional alignment paramters will be
     * obtained from the database in a later method-call.
     *
     * \param [in] dataprovider the ccdb::DataProvider object
     ** /
    public void fetch_nominal_parameters(ConstantProvider provider) {
        static final double deg = 3.14159265358979 / 180.;
        static final double cm = 1.;

        // here we connect to the CCDB (MySQL) databse and request
        // the nominal geometry parameters for the Drift Chamber.
        // These numbers come from four tables: dc, region, superlayer,
        // and layer.
        int nsectors = provider.getInteger("/geometry/dc/dc/nsectors",0);
        int nregions = provider.getInteger("/geometry/dc/dc/nregions",0);

        int[]    superlayers = provider.getIntegerArray("/geometry/dc/region/nsuperlayers");
        double[] dist2tgt    = provider.getDoubleArray( "/geometry/dc/region/dist2tgt"    );
        double[] frontgap    = provider.getDoubleArray( "/geometry/dc/region/frontgap"    );
        double[] midgap      = provider.getDoubleArray( "/geometry/dc/region/midgap"      );
        double[] backgap     = provider.getDoubleArray( "/geometry/dc/region/backgap"     );
        double[] thopen      = provider.getDoubleArray( "/geometry/dc/region/thopen"      );
        double[] thtilt      = provider.getDoubleArray( "/geometry/dc/region/thtilt"      );
        double[] xdist       = provider.getDoubleArray( "/geometry/dc/region/xdist"       );

        int[]    nsenselayers  = provider.getIntegerArray("/geometry/dc/superlayer/nsenselayers" );
        int[]    nguardlayers  = provider.getIntegerArray("/geometry/dc/superlayer/nguardlayers" );
        int[]    nfieldlayers  = provider.getIntegerArray("/geometry/dc/superlayer/nfieldlayers" );
        double[] thster        = provider.getDoubleArray( "/geometry/dc/superlayer/thster"       );
        double[] thmin         = provider.getDoubleArray( "/geometry/dc/superlayer/thmin"        );
        double[] wpdist        = provider.getDoubleArray( "/geometry/dc/superlayer/wpdist"       );
        double[] cellthickness = provider.getDoubleArray( "/geometry/dc/superlayer/cellthickness");

        int nsensewires = provider.getDouble("/geometry/dc/layer/nsensewires",0);
        int nguardwires = provider.getDouble("/geometry/dc/layer/nguardwires",0);


        // Now we fill the sectors object which holds all these
        // core parameters. Here, many numbers will be redundant.
        // It is expected that this will change once efficiency
        // alignment and other calibrations are taken into effect.
        this.sectors.clear();

        for (int sec=0; sec<nsectors; sec++) {
            this.sectors.add(new dc.Sector());
            dc.Sector sector = this.sectors[sec];

            for (int reg=0; reg<nregions; reg++) {
                sector.region.add(new dc.Region());
                dc.Region region = sector.regions[reg];

                region.dist2tgt = dist2tgt[reg]*cm;
                region.frontgap = frontgap[reg]*cm;
                region.midgap   = midgap[reg]  *cm;
                region.backgap  = backgap[reg] *cm;
                region.thopen   = thopen[reg]  *deg;
                region.thtilt   = thtilt[reg]  *deg;
                region.xdist    = xdist[reg]   *cm;

                for (int slyr=0; slyr<nsuperlayers[reg]; slyr++) {

                    // nslyr is the "global" superlayer number starting
                    // from 0 and going to 5
                    int nslyr = slyr;
                    for (int n=0; n<reg; n++) {
                        nslyr += nsuperlayers[n];
                    }

                    region.superlayers.add(new dc.Superlayer());
                    dc.Superlayer superlayer = region.superlayers[slyr];

                    superlayer.nfieldlayers  = nfieldlayers[nslyr];
                    superlayer.thster        = thster[nslyr]        *deg;
                    superlayer.thmin         = thmin[nslyr]         *deg;
                    superlayer.wpdist        = wpdist[nslyr]        *cm;
                    superlayer.cellthickness = cellthickness[nslyr] *cm;

                    for (int lyr=0; lyr<nsenselayers[slyr]; lyr++) {
                        superlayer.layers.add(new dc.Layer());
                        dc.Layer layer = superlayer.layers[lyr];

                        layer.sensewires.assign(nsensewires,true);
                        layer.nguardwires = nguardwires;
                    }

                    for (int lyr=0; lyr<nguardlayers[slyr]; lyr++)
                    {
                        superlayer._guardlayers.emplace_back(new Guardlayer(&superlayer,lyr));
                        Guardlayer& layer = *superlayer._guardlayers[lyr];

                        layer._nwires = nsensewires + nguardwires;
                    }
                }
            }
        }


        LOG(debug) << "done fetching numbers from database for DC.";

    }

      public DriftChamber();
        DriftChamber(const DriftChamber& that);
        DriftChamber( ccdb::Calibration* calib,
                      bool quiet = false,
                      bool verbose = false );
        DriftChamber& operator=(const DriftChamber& that);

        // inline members
        const vector<unique_ptr<DCSector>>& sectors() const;
        const DCSector& sector(int sec) const;

        // members in cpp file
        void fetch_nominal_parameters(ccdb::Calibration* calib);

      private:
        /// \brief the sectors of the DC
        vector<unique_ptr<DCSector>> _sectors;

    };
    * */

    /**
     * \brief Get a vector of the sectors in the drift chamber
     * \return const reference to DriftChamber::_sectors
     ** /
    inline
    const vector<unique_ptr<DCSector>>& DriftChamber::sectors() const
    {
        return _sectors;
    }*/

    /**
     * \brief Get a sector in the DC
     * \param [in] sec The sector index within the drift chamber (counting from zero)
     * \return const reference to DriftChamber::_sectors[sec]
     ** /
    inline
    const DCSector& DriftChamber::sector(int sec) const
    {
        return *_sectors[sec];
    }

    }*/
}
