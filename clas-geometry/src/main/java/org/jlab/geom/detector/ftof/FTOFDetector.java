package org.jlab.geom.detector.ftof;

import org.jlab.geom.DetectorId;
import org.jlab.geom.abs.AbstractDetector;

/**
 * A Forward Time of Flight (FTOF) {@link org.jlab.geom.base.Detector Detector}.
 * <p>
 * Factory: {@link org.jlab.geom.detector.ftof.FTOFFactory FTOFFactory}<br> 
 * Hierarchy: 
 * <code>
 * <b>{@link org.jlab.geom.detector.ftof.FTOFDetector FTOFDetector}</b> → 
 * {@link org.jlab.geom.detector.ftof.FTOFSector FTOFSector} → 
 * {@link org.jlab.geom.detector.ftof.FTOFSuperlayer FTOFSuperlayer} → 
 * {@link org.jlab.geom.detector.ftof.FTOFLayer FTOFLayer} → 
 * {@link org.jlab.geom.component.ScintillatorPaddle ScintillatorPaddle}
 * </code>
 * 
 * @author jnhankins
 */
public class FTOFDetector extends AbstractDetector<FTOFSector> {
    
    protected FTOFDetector() {
        super(DetectorId.FTOF);
    }
    
    /**
     * Returns "FTOF Detector".
     * @return "FTOF Detector"
     */
    @Override
    public String getType() {
        return "FTOF Detector";
    }
}
