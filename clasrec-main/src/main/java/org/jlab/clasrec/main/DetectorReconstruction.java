/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clasrec.main;

import java.nio.ByteOrder;
import java.util.HashMap;
import org.jlab.clas12.tools.MimeType;
import org.jlab.clasrec.utils.DataBaseLoader;
import org.jlab.clasrec.utils.ServiceConfiguration;
import org.jlab.coda.clara.core.ICService;
import org.jlab.coda.clara.core.JioSerial;
import org.jlab.coda.clara.system.CConstants;
import org.jlab.coda.clara.system.CUtil;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioFactory;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.base.Detector;
import org.jlab.geom.detector.dc.DCFactory;
import org.jlab.geom.detector.ec.ECFactory;
import org.jlab.geom.detector.ftof.FTOFFactory;

/**
 *
 * @author gavalian
 */

public abstract class DetectorReconstruction implements ICService {
    
    private String serviceName     = "undefined";
    private String serviceVersion  = "1.0";
    private String serviceAuthor  = "undefined";
    private String serviceDescription = "No description";
    private final String mainModuleName     = "[CLASREC-MAIN] ====>>>>> ";
    private final HashMap<String,Detector>  detectorGeometry = new HashMap<String,Detector>();
    private final ServiceConfiguration serviceConfig = new ServiceConfiguration();
    private Integer serviceDebugLevel   = 0;    
    
    public abstract void processEvent(EvioDataEvent event);
    public abstract void init();
    public abstract void configure(ServiceConfiguration c);
    
    /**
     * Returns the debug level.
     * @return debug level
     */
    public int debugLevel(){
        return this.serviceDebugLevel;
    }
    /**
     * Set the debug level for the service. 
     * @param level debug level
     */
    public void setDebugLevel(int level){
        this.serviceDebugLevel = level;
        if(level<0)  serviceDebugLevel = 0;
        if(level>10) serviceDebugLevel = 10;
    }
    
    /**
     * Returns geometry object for detector "geom". To use this function
     * first the geometry for given detector has to be loaded through
     * requireGeometry(geomname)
     * @param geom detector name.
     * @return 
     */
    public Detector getGeometry(String geom){
        if(detectorGeometry.containsKey(geom)==false){
            System.err.println(mainModuleName + "ERROR : requested geometry "
            + " for detector " + geom + " is not loaded.");
            System.err.println(mainModuleName + "ERROR : use requireGeometry(\""
                    + geom + "\") function first");
            return null;
        }
        return detectorGeometry.get(geom);
    }
    /**
     * Loads geometry package for given detector into local map. To access 
     * it use getGeometry(geomname)
     * @param geometryPackage  geometry package name ("EC","FTOF","DC","FTCAL" etc.)
     */
    public void requireGeometry(String geometryPackage){
        
        if(geometryPackage.compareTo("DC::Tilted")==0){
            DCFactory factory = new DCFactory();
            ConstantProvider  data = DataBaseLoader.getConstantsDC();
            Detector geomFTOF = factory.createDetectorTilted(data);
            detectorGeometry.put("DC::Tilted", geomFTOF);
            System.err.println(mainModuleName + "geometry for detector " +
                    geometryPackage + " is loaded...");
            return;
        }
        
        if(geometryPackage.compareTo("DC")==0){
            DCFactory factory = new DCFactory();
            ConstantProvider  data = DataBaseLoader.getConstantsDC();
            Detector geomFTOF = factory.createDetectorCLAS(data);
            detectorGeometry.put("DC", geomFTOF);
            System.err.println(mainModuleName + "geometry for detector " +
                    geometryPackage + " is loaded...");
            return;
        }
        
        if(geometryPackage.compareTo("FTOF")==0){
            FTOFFactory factory = new FTOFFactory();
            ConstantProvider  data = DataBaseLoader.getTimeOfFlightConstants();
            Detector geomFTOF = factory.createDetectorCLAS(data);
            detectorGeometry.put("FTOF", geomFTOF);
            System.err.println(mainModuleName + "geometry for detector " +
                    geometryPackage + " is loaded...");
            return;
        }
        
        if(geometryPackage.compareTo("EC")==0){
            ECFactory factory = new ECFactory();
            ConstantProvider  data = DataBaseLoader.getCalorimeterConstants();
            Detector geomEC = factory.createDetectorCLAS(data);
            detectorGeometry.put("EC", geomEC);
            System.err.println(mainModuleName + "geometry for detector " +
                    geometryPackage + " is loaded...");
            return;
        }
        
        System.err.println(mainModuleName + "WARRNING : geometry package with name "
        + geometryPackage + " does not exist..");
    }
    
    public void requireCalibration(String calibPackage){
        
    }
    
    public DetectorReconstruction(String name, String author,String version){
        serviceName = name;
        serviceAuthor = author;
        serviceVersion = version;
    }
    
    public void setDescription(String desc){
        serviceDescription = desc;
    }
    
    @Override
    public void configure(JioSerial js) {
        this.init();
    }

    @Override
    public JioSerial execute(JioSerial data) {
        JioSerial output = data;
 
        // Validate input type
        MimeType mt = data.getMimeType();
        if (mt != MimeType.EVIO) {
            String msg = String.format("Wrong input type: %s", mt);
            output.setStatus(CConstants.error);
            output.setDataDescription(msg);
            return output;
        }
        
        EvioDataEvent dataevent = null;
        try {
            byte[] buffer = data.getDataAsByteArray();
            ByteOrder endianness = data.getDataEndianness();
            dataevent = new EvioDataEvent(buffer, endianness, EvioFactory.getDictionary());
        } catch (/* EvioException */ Exception e) {
            // Actually, EvioDataEvent is not throwing any exception, but I think it should.
            // Why having an EvioDataEvent that failed to extract the event?
            String msg = String.format("Error reading input event%n%n%s", CUtil.reportException(e));
            output.setStatus(CConstants.error);
            output.setDataDescription(msg);
            return output;
        }
        try {
            this.processEvent(dataevent);
        } catch (Exception e) {
            String msg = String.format("Error processing input event%n%n%s", CUtil.reportException(e));
            output.setStatus(CConstants.error);
            output.setDataDescription(msg);
            return output;
        }
 
        // Save event.
        output.setData(dataevent.getEventBuffer(), MimeType.EVIO);
 
        return output;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JioSerial execute(JioSerial[] jss) {
        return null;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destruct() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return serviceName;
    }

    @Override
    public String getAuthor() {
        return serviceAuthor;
    }

    @Override
    public String getDescription() {
        return serviceDescription;
    }

    @Override
    public String getVersion() {
        return serviceVersion;
    }

    @Override
    public String getLanguage() {
        return "java";
    }
}
