package org.jlab.geom.detector.ec;

import org.jlab.geom.DetectorId;
import org.jlab.geom.abs.AbstractDetector;

/**
 * An Electromagnetic Calorimeter (EC) {@link org.jlab.geom.base.Detector Detector}.
 * <p>
 * Factory: {@link org.jlab.geom.detector.ec.ECFactory ECFactory}<br> 
 * Hierarchy: 
 * <code>
 * <b>{@link org.jlab.geom.detector.ec.ECDetector ECDetector}</b> → 
 * {@link org.jlab.geom.detector.ec.ECSector ECSector} → 
 * {@link org.jlab.geom.detector.ec.ECSuperlayer ECSuperlayer} → 
 * {@link org.jlab.geom.detector.ec.ECLayer ECLayer} → 
 * {@link org.jlab.geom.component.ScintillatorPaddle ScintillatorPaddle}
 * </code>
 * 
 * @author jnhankins
 */
public class ECDetector extends AbstractDetector<ECSector> {
    
    protected ECDetector() {
        super(DetectorId.EC);
    }
    
    /**
     * Returns "EC Detector".
     * @return "EC Detector"
     */
    @Override
    public String getType() {
        return "EC Detector";
    }
}
