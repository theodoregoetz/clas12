/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clas12.physics;

import org.jlab.clas.pdg.PDGDatabase;
import org.jlab.clas.physics.EventFilter;
import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.data.io.DataEvent;
import org.jlab.evio.clas12.EvioDataBank;
import org.jlab.evio.clas12.EvioDataEvent;

/**
 *
 * @author gavalian
 */
public class GenericKinematicFitter {
    
    private final   EventFilter filter = new EventFilter();
    private Double  beamEnergy  = 11.0;
    private Boolean forceFilter = false;
    
    public GenericKinematicFitter(double beam, String filterString){
        this.beamEnergy = beam;
        this.filter.setFilter(filterString);
    }
    
    /**
     * Returns PhysicsEvent object with generated particles in the event.
     * @param event
     * @return 
     */
    public PhysicsEvent getGeneratedEvent(DataEvent  event){
        if(event instanceof EvioDataEvent){
            if(event.hasBank("GenPart::true")==true){
                return this.getGeneratedEventClas12((EvioDataEvent) event);
            }
        }
        return new PhysicsEvent(this.beamEnergy);
    }
    /**
     * Returns PhysicsEvent object with reconstructed particles.
     * @param event - DataEvent object
     * @return PhysicsEvent : event containing particles.
     */
    public PhysicsEvent  getPhysicsEvent(DataEvent  event){
        if(event instanceof EvioDataEvent){
            if(event.hasBank("EVENTHB::particle")==true){
                PhysicsEvent genEvent = this.getGeneratedEvent(event);
                PhysicsEvent recEvent =  this.getPhysicsEventClas12((EvioDataEvent) event);
                this.matchGenerated(genEvent, recEvent);
                return recEvent;
            }
            if(event.hasBank("EVENT::particle")==true){
                PhysicsEvent recEvent =  this.getPhysicsEventClas6((EvioDataEvent) event);
                return recEvent;
            }
        }
        return new PhysicsEvent(this.beamEnergy);
    }    
    
    public void matchGenerated(PhysicsEvent gen, PhysicsEvent rec){
        int nrows = rec.count();
        for(int loop = 0; loop < nrows; loop++){
            Particle rpart = rec.getParticle(loop);
            Particle gpart = gen.closestParticle(rpart);
            if(rpart.cosTheta(gpart)>0.998){
                if(rpart.charge()!=0){
                    rpart.changePid(gpart.pid());
                } else {
                    double px = rpart.px()*gpart.p();
                    double py = rpart.py()*gpart.p();
                    double pz = rpart.pz()*gpart.p();
                    rpart.vector().setPxPyPzM(px, py, pz, 0.0);
                    rpart.changePid(22);
                }
            }
        }
    }
    
    private PhysicsEvent  getGeneratedEventClas12(EvioDataEvent event){
        PhysicsEvent physEvent = new PhysicsEvent();
        physEvent.setBeam(this.beamEnergy);
        if(event.hasBank("GenPart::true")){
            EvioDataBank evntBank = (EvioDataBank) event.getBank("GenPart::true");
            int nrows = evntBank.rows();
            for(int loop = 0; loop < nrows; loop++){
                Particle genParticle = new Particle(
                        evntBank.getInt("pid", loop),
                        evntBank.getDouble("px", loop)*0.001,
                        evntBank.getDouble("py", loop)*0.001,
                        evntBank.getDouble("pz", loop)*0.001,
                        evntBank.getDouble("vx", loop),
                        evntBank.getDouble("vy", loop),
                        evntBank.getDouble("vz", loop));
                if(genParticle.p()<10.999&&
                        Math.toDegrees(genParticle.theta())>2.0){
                    physEvent.addParticle(genParticle);    
                }
            }
        }
        return physEvent;
    }
    
    private PhysicsEvent  getPhysicsEventClas6(EvioDataEvent event){
        PhysicsEvent physEvent = new PhysicsEvent();
        physEvent.setBeam(this.beamEnergy);
        if(event.hasBank("EVENT::particle")){
            EvioDataBank evntBank = (EvioDataBank) event.getBank("EVENT::particle");
            int nrows = evntBank.rows();
            for(int loop = 0; loop < nrows; loop++){
                int status = evntBank.getByte("status", loop);
                int pid    = evntBank.getInt("pid", loop);
                if(PDGDatabase.isValidPid(pid)==true){
                    Particle part = new Particle(
                            evntBank.getInt("pid", loop),
                            evntBank.getFloat("px", loop),
                            evntBank.getFloat("py", loop),
                            evntBank.getFloat("pz", loop),
                            evntBank.getFloat("vx", loop),
                            evntBank.getFloat("vy", loop),
                            evntBank.getFloat("vz", loop));
                    if(status>0) physEvent.addParticle(part);
                } else {
                    Particle part = new Particle();
                    part.setParticleWithMass(evntBank.getFloat("mass", loop),
                            evntBank.getByte("charge", loop),
                            evntBank.getFloat("px", loop),
                            evntBank.getFloat("py", loop),
                            evntBank.getFloat("pz", loop),
                            evntBank.getFloat("vx", loop),
                            evntBank.getFloat("vy", loop),
                            evntBank.getFloat("vz", loop)
                    );
                    if(status>0) physEvent.addParticle(part);
                }
            }
        }
        return physEvent;
    }
    
    private PhysicsEvent  getPhysicsEventClas12(EvioDataEvent event){
        PhysicsEvent physEvent = new PhysicsEvent();
        physEvent.setBeam(this.beamEnergy);
        if(event.hasBank("EVENTHB::particle")){
            EvioDataBank evntBank = (EvioDataBank) event.getBank("EVENTHB::particle");
            int nrows = evntBank.rows();
            for(int loop = 0; loop < nrows; loop++){
                physEvent.addParticle(new Particle(
                        evntBank.getInt("pid", loop),
                        evntBank.getFloat("px", loop),
                        evntBank.getFloat("py", loop),
                        evntBank.getFloat("pz", loop),
                        evntBank.getFloat("vx", loop),
                        evntBank.getFloat("vy", loop),
                        evntBank.getFloat("vz", loop)
                ));                
            }
        }
        return physEvent;
    }
}
