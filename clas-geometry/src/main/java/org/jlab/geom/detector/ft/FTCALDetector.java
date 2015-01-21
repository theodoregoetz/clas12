package org.jlab.geom.detector.ft;

import org.jlab.geom.DetectorId;
import org.jlab.geom.abs.AbstractDetector;


/**
 * A Forward Tagger Calorimeter (FTCAL) {@link org.jlab.geom.base.Detector Detector}.
 * <p>
 * Factory: {@link org.jlab.geom.detector.ft.FTCALFactory FTCALFactory}<br> 
 * Hierarchy: 
 * <code>
 * <b>{@link org.jlab.geom.detector.ft.FTCALDetector FTCALDetector}</b> → 
 * {@link org.jlab.geom.detector.ft.FTCALSector FTCALSector} → 
 * {@link org.jlab.geom.detector.ft.FTCALSuperlayer FTCALSuperlayer} → 
 * {@link org.jlab.geom.detector.ft.FTCALLayer FTCALLayer} → 
 * {@link org.jlab.geom.component.ScintillatorPaddle ScintillatorPaddle}
 * </code>
 * 
 * @author jnhankins
 */
public class FTCALDetector extends AbstractDetector<FTCALSector> {
    
    protected FTCALDetector() {
        super(DetectorId.FTCAL);
    }
    
    /**
     * Returns "FTCAL Detector".
     * @return "FTCAL Detector"
     */
    @Override
    public String getType() {
        return "FTCAL Detector";
    }
}
