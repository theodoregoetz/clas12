package org.jlab.geom.detector.dc;

import java.util.*;
import static java.lang.Math.*;

import org.jlab.ccdb.JDBCProvider;
import org.jlab.ccdb.Assignment;

import org.jlab.geom.detector.dc.*;

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

    ArrayList<Sector> sectors;

    /**
     * \brief default constructor
     **/
    public DriftChamber() {
        this.sectors = new ArrayList<Sector>();
    }

    /**
     * \brief constructor which fetches the nominal geometry from the database
     *
     * This calls DriftChamber.fetch_nominal_parameters(provider)
     **/
    public DriftChamber(JDBCProvider provider) {
        this.sectors = new ArrayList<Sector>();
        this.fetch_nominal_parameters(provider);
    }

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
     **/
    public void fetch_nominal_parameters(JDBCProvider provider) {
        final double deg = 3.14159265358979 / 180.;

        // here we connect to the CCDB (MySQL) databse and request
        // the nominal geometry parameters for the Drift Chamber.
        // These numbers come from four tables: dc, region, superlayer,
        // and layer.
        Assignment asgmt = provider.getData("/geometry/dc/dc");
        int nsectors = asgmt.getColumnValuesInt("nsectors").get(0);
        int nregions = asgmt.getColumnValuesInt("nregions").get(0);

        asgmt = provider.getData("/geometry/dc/region");
        Vector<Integer> nsuperlayers = asgmt.getColumnValuesInt   ("nsuperlayers");
        Vector<Double > dist2tgt     = asgmt.getColumnValuesDouble("dist2tgt"    );
        Vector<Double > frontgap     = asgmt.getColumnValuesDouble("frontgap"    );
        Vector<Double > midgap       = asgmt.getColumnValuesDouble("midgap"      );
        Vector<Double > backgap      = asgmt.getColumnValuesDouble("backgap"     );
        Vector<Double > thopen       = asgmt.getColumnValuesDouble("thopen"      );
        Vector<Double > thtilt       = asgmt.getColumnValuesDouble("thtilt"      );
        Vector<Double > xdist        = asgmt.getColumnValuesDouble("xdist"       );

        asgmt = provider.getData("/geometry/dc/superlayer");
        Vector<Integer> nsenselayers  = asgmt.getColumnValuesInt   ("nsenselayers" );
        Vector<Integer> nguardlayers  = asgmt.getColumnValuesInt   ("nguardlayers" );
        Vector<Integer> nfieldlayers  = asgmt.getColumnValuesInt   ("nfieldlayers" );
        Vector<Double > thster        = asgmt.getColumnValuesDouble("thster"       );
        Vector<Double > thmin         = asgmt.getColumnValuesDouble("thmin"        );
        Vector<Double > wpdist        = asgmt.getColumnValuesDouble("wpdist"       );
        Vector<Double > cellthickness = asgmt.getColumnValuesDouble("cellthickness");

        asgmt = provider.getData("/geometry/dc/layer");
        int nsensewires = asgmt.getColumnValuesInt("nsensewires").get(0);
        int nguardwires = asgmt.getColumnValuesInt("nguardwires").get(0);

        // Now we fill the sectors object which holds all these
        // core parameters. Here, many numbers will be redundant.
        // It is expected that this will change once efficiency
        // alignment and other calibrations are taken into effect.
        this.sectors.clear();

        for (int sec=0; sec<nsectors; sec++) {
            this.sectors.add(new Sector(this));
            Sector sector = this.sectors.get(sec);
            sector.index = sec;

            for (int reg=0; reg<nregions; reg++) {
                sector.regions.add(new Region(sector));
                Region region = sector.regions.get(reg);
                region.index = reg;

                region.dist2tgt = dist2tgt.get(reg);
                region.frontgap = frontgap.get(reg);
                region.midgap   = midgap.get(reg)  ;
                region.backgap  = backgap.get(reg) ;
                region.thopen   = toRadians(thopen.get(reg));
                region.thtilt   = toRadians(thtilt.get(reg));
                region.xdist    = xdist.get(reg)   ;

                for (int slyr=0; slyr<nsuperlayers.get(reg); slyr++) {

                    // nslyr is the "global" superlayer number starting
                    // from 0 and going to 5
                    int nslyr = slyr;
                    for (int n=0; n<reg; n++) {
                        nslyr += nsuperlayers.get(n);
                    }

                    region.superlayers.add(new Superlayer(region));
                    Superlayer superlayer = region.superlayers.get(slyr);

                    superlayer.nfieldlayers  = nfieldlayers.get(nslyr);
                    superlayer.thster        = toRadians(thster.get(nslyr));
                    superlayer.thmin         = toRadians(thmin.get(nslyr));
                    superlayer.wpdist        = wpdist.get(nslyr)       ;
                    superlayer.cellthickness = cellthickness.get(nslyr);

                    int nlayers = nguardlayers.get(slyr) + nsenselayers.get(slyr);
                    for (int lyr=0; lyr<nlayers; lyr++) {
                        superlayer.layers.add(new Layer(superlayer));
                        Layer layer = superlayer.layers.get(lyr);
                        if (lyr == 0 || lyr == (nlayers-1)) {
                            layer.isSenselayer = true;
                        } else {
                            layer.isSenselayer = false;
                        }
                    }
                }
            }
        }
    }

    public int nSectors() {
        return sectors.size();
    }

    private int sectorIndex(int idx) {
        if (idx<0) {
            idx = this.nSectors() + idx;
        }
        return idx;
    }

    public Sector sector(int idx) {
        return sectors.get(this.sectorIndex(idx));
    }





    /**
     * \brief generate the volumes of a DC Sector for input into gemc/geant4
     *
     * The numbers calculated are following Geant4's G4Trap constructor:
     *     pDz     Half-length along the z-axis
     *     pTheta  Polar angle of the line joining the centres of the faces
     *             at -/+pDz
     *     pPhi    Azimuthal angle of the line joing the centre of the face
     *             at -pDz to the centre of the face at +pDz
     *     pDy1    Half-length along y of the face at -pDz
     *     pDx1    Half-length along x of the side at y=-pDy1 of the face at -pDz
     *     pDx2    Half-length along x of the side at y=+pDy1 of the face at -pDz
     *     pAlp1   Angle with respect to the y axis from the centre of the
     *             side at y=-pDy1 to the centre at y=+pDy1 of the face at -pDz
     *
     *     pDy2    Half-length along y of the face at +pDz
     *     pDx3    Half-length along x of the side at y=-pDy2 of the face at +pDz
     *     pDx4    Half-length along x of the side at y=+pDy2 of the face at +pDz
     *     pAlp2   Angle with respect to the y axis from the centre of the
     *             side at y=-pDy2 to the centre at y=+pDy2 of the face at +pDz
     *
     * \return map of map of strings: ret[volume_name][param_name] = value
     ** /
    HashMap<> sectorVolumes() {
        double dz;
        double theta;
        double phi;
        double dy1;
        double dx1;
        double dx2;
        double alp1;
        double dy2;
        double dx3;
        double dx4;
        double alp2;

        // vols[name][var] = val
        volmap_t vols;

        stringstream sector_name_ss;
        sector_name_ss << "sector" << sector.index()+1;
        string sector_name = sector_name_ss.str();

        stringstream sector_desc;
        sector_desc << "Drift Chamber"
                    << " Sector " << sector.index()+1;

        for (size_t reg=0; reg<sector.regions().size(); reg++)
        {
            const drift_chamber::Region& region = sector.region(reg);

            stringstream region_name_ss;
            region_name_ss << "R" << reg+1 << "_S" << sector.index()+1;
            string region_name = region_name_ss.str();

            stringstream region_desc;
            sector_desc << "Drift Chamber"
                        << " Sector " << sector.index()+1
                        << " Region " << reg+1;

            // end plates of this region
            // used in sense-layer volume calculations.
            plane<> lplate = region.left_end_plate();
            plane<> rplate = region.right_end_plate();

            // first and last guard wire endpoints
            euclid_vector<> guardwire0_endpoint = \
                region.superlayer( 0).guardlayer( 0).wire( 0).end_point();
            euclid_vector<> guardwire1_endpoint = \
                region.superlayer(-1).guardlayer(-1).wire(-1).end_point();

            // region center-point in sector coordinates
            euclid_vector<> region_center = region.center();

            // x and y are reversed for gemc's coordinate system
            dz    = 0.5 * region.thickness();
            theta = - region.thtilt();
            phi   = 0.5 * cons::pi<double>();
            dy1   = 0.5 * (guardwire1_endpoint.x() - guardwire0_endpoint.x())
                    / cos(region.thtilt());
            dx1   = guardwire0_endpoint.y();
            dx2   = guardwire1_endpoint.y();
            alp1  = 0.;
            dy2   = dy1;
            dx3   = dx1;
            dx4   = dx2;
            alp2  = alp1;

            // x and y are reversed for gemc's coordinate system
            stringstream region_pos;
            region_pos << region_center.y() << "*cm "
                       << region_center.x() << "*cm "
                       << region_center.z() << "*cm";

            stringstream region_rot;
            region_rot << "ordered: zxy "
                      << " " <<  90. + region.thtilt()*rad2deg << "*deg"
                      << " " << -90. - 60.*sector.index() << "*deg"
                      << " " << 0 << "*deg";

            stringstream region_dim;
            region_dim << dz << "*cm "
                       << theta * rad2deg << "*deg "
                       << phi * rad2deg << "*deg "
                       << dy1 << "*cm "
                       << dx1 << "*cm "
                       << dx2 << "*cm "
                       << alp1 * rad2deg << "*deg "
                       << dy2 << "*cm "
                       << dx3 << "*cm "
                       << dx4 << "*cm "
                       << alp2 * rad2deg << "*deg";

            // The Region mother volume
            vols[region_name] = {
                {"mother", "root"},
                {"description", region_desc.str()},
                {"pos", region_pos.str()},
                {"rotation", region_rot.str()},
                {"color", "aa0000"},
                {"type", "G4Trap"},
                {"dimensions", region_dim.str()},
                {"material", "DCgas"},
                {"mfield", "no"},
                {"ncopy", "1"},
                {"pMany", "1"},
                {"exist", "1"},
                {"visible", "1"},
                {"style", "0"},
                {"sensitivity", "no"},
                {"hit_type", ""},
                {"identifiers", ""}
            };

            for(size_t slyr=0; slyr<region.superlayers().size(); slyr++)
            {
                const drift_chamber::Superlayer& superlayer = region.superlayer(slyr);

                for (size_t lyr=0; lyr<superlayer.senselayers().size(); lyr++)
                {

    /// begin testing senselayer methods ///////////////////////////////////
    const drift_chamber::Senselayer& senselayer = superlayer.senselayer(lyr);

    /*
    generating the trapezoid parameters for this senselayer
    following the G4Trap constructor:
        pDz     Half-length along the z-axis
        pTheta  Polar angle of the line joining the centres of the faces
                at -/+pDz
        pPhi    Azimuthal angle of the line joing the centre of the face at
                -pDz to the centre of the face at +pDz
        pDy1    Half-length along y of the face at -pDz
        pDx1    Half-length along x of the side at y=-pDy1 of the face at -pDz
        pDx2    Half-length along x of the side at y=+pDy1 of the face at -pDz
        pAlp1   Angle with respect to the y axis from the centre of the side
                at y=-pDy1 to the centre at y=+pDy1 of the face at -pDz

        pDy2    Half-length along y of the face at +pDz
        pDx3    Half-length along x of the side at y=-pDy2 of the face at +pDz
        pDx4    Half-length along x of the side at y=+pDy2 of the face at +pDz
        pAlp2   Angle with respect to the y axis from the centre of the side
                at y=-pDy2 to the centre at y=+pDy2 of the face at +pDz
    * /

    // all done in sector coordinate system. ///////////////////////////////


    // 100 um gap between layers (to avoid G4 volume overlap)
    static const double microgap = 0.01;

    double hflyrthk = 0.5 * superlayer.layer_thickness();
    euclid_vector<double,3> half_lyr_thickness{
        hflyrthk * sin(region.thtilt()),
        0.,
        hflyrthk * cos(region.thtilt()) };

    // volume edges as infinitely-extending lines
    // The first two edges are the wire-lines displaced by
    // half a layer-thickness in the direction of (0,0,0).
    // The last two are displaced away from the origin.
    line<double,3> edge00_line{
        senselayer.wire_mid( 0) - half_lyr_thickness,
        superlayer.wire_direction() };
    line<double,3> edge01_line{
        senselayer.wire_mid(-1) - half_lyr_thickness,
        superlayer.wire_direction() };
    line<double,3> edge10_line{
        senselayer.wire_mid( 0) + half_lyr_thickness,
        superlayer.wire_direction() };
    line<double,3> edge11_line{
        senselayer.wire_mid(-1) + half_lyr_thickness,
        superlayer.wire_direction() };

    // get the intersection and create line segment from one
    // point to the other. These are the same lines as above
    // but with endpoints at the left and right end-planes.
    line_segment<double,3> edge00{
        intersection(edge00_line, lplate),
        intersection(edge00_line, rplate) };
    line_segment<double,3> edge01{
        intersection(edge01_line, lplate),
        intersection(edge01_line, rplate) };
    line_segment<double,3> edge10{
        intersection(edge10_line, lplate),
        intersection(edge10_line, rplate) };
    line_segment<double,3> edge11{
        intersection(edge11_line, lplate),
        intersection(edge11_line, rplate) };

    // Layer closer to the origin:
    // p0 = projection of the 1st edge's midpoint onto the
    //      line representing the longer edge on the same layer.
    // d00 = direction from the 1st edges's midpoint to p0
    // d01 = direction from the 1st to the 2nd edges's midpoint
    euclid_vector<double,3> p0 = projection(edge00.mid_point(), edge01_line);
    direction_vector<double,3> d00{p0 - edge00.mid_point()};
    direction_vector<double,3> d01{edge01.mid_point() - edge00.mid_point()};

    // Layer farther away from the origin:
    // p1 = projection of the 1st edge's midpoint onto the
    //      line representing the longer edge on the same layer.
    euclid_vector<double,3> p1 = projection(edge10.mid_point(), edge11_line);

    // get the sign of the alp angle for the trapezoid
    double sign_of_alp1 = 1;
    if ((edge01.mid_point().y() - p0.y()) < 0.)
    {
        sign_of_alp1 = -1;
    }

    dz    = hflyrthk - microgap;
    theta = - region.thtilt();
    phi   = 0.5 * cons::pi<double>();
    dy1   = 0.5 * line_segment<double,3>(edge00.mid_point(), p0).length();
    dx1   = 0.5 * edge00.length();
    dx2   = 0.5 * edge01.length();
    alp1  = sign_of_alp1 * d00.angle(d01);
    dy2   = 0.5 * line_segment<double,3>(edge10.mid_point(), p1).length();
    dx3   = 0.5 * edge10.length();
    dx4   = 0.5 * edge11.length();
    alp2  = alp1;

    stringstream layer_name_ss;
    layer_name_ss << "L" << lyr+1 << "_SL" << slyr+1 << "_R" << reg+1 << "_S" << sector.index()+1;
    string layer_name = layer_name_ss.str();

    stringstream layer_desc;
    layer_desc << "Drift Chamber"
               << " Sector " << sector.index()+1
               << " Region " << reg+1
               << " Superlayer " << slyr+1
               << " Senselayer " << lyr+1;

    // d = position of layer volume relative to the region (mother volume)
    euclid_vector<> d = senselayer.center() - region_center;

    // rotate about the y-axis in sector coordinates by the region's tilt
    // this is done because the trapezoids are defined by the edges
    // and the whole volume is then rotated to get the final position
    d = { cos(region.thtilt())*d.x() - sin(region.thtilt())*d.z(),
          d.y(),
          cos(region.thtilt())*d.z() + sin(region.thtilt())*d.x() };

    // x and y are reversed for gemc's coordinate system
    stringstream layer_pos;
    layer_pos << d.y() << "*cm " << d.x() << "*cm " << d.z() << "*cm";

    stringstream layer_rot;
    layer_rot << "0*deg 0*deg " << superlayer.thster() * rad2deg << "*deg";

    stringstream layer_dim;
    layer_dim << dz << "*cm "
              << theta * rad2deg << "*deg "
              << phi * rad2deg << "*deg "
              << dy1 << "*cm "
              << dx1 << "*cm "
              << dx2 << "*cm "
              << alp1 * rad2deg << "*deg "
              << dy2 << "*cm "
              << dx3 << "*cm "
              << dx4 << "*cm "
              << alp2 * rad2deg << "*deg";

    stringstream layer_ids;
    layer_ids << "sector ncopy 0 superlayer manual " << slyr+1 << " layer manual " << lyr+1 << " wire manual 1";

    // The (Sense)Layer volume
    vols[layer_name] = {
        {"mother", region_name},
        {"description", layer_desc.str()},
        {"pos", layer_pos.str()},
        {"rotation", layer_rot.str()},
        {"color", "66aadd"},
        {"type", "G4Trap"},
        {"dimensions", layer_dim.str()},
        {"material", "DCgas"},
        {"mfield", "no"},
        {"ncopy", "1"},
        {"pMany", "1"},
        {"exist", "1"},
        {"visible", "1"},
        {"style", "1"},
        {"sensitivity", "DC"},
        {"hit_type", "DC"},
        {"identifiers", layer_ids.str()}
    };

    /// end testing senselayer methods /////////////////////////////////////
                }
            }
        }

        return vols;
    }
    */


}
