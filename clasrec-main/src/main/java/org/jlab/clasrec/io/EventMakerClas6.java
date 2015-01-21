/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clasrec.io;

import org.jlab.clas.pdg.PDGDatabase;
import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.data.io.DataEvent;
import org.jlab.evio.clas12.EvioDataBank;

/**
 *
 * @author gavalian
 */
public class EventMakerClas6 implements IEventMaker {

    public PhysicsEvent createEvent(DataEvent event) {
        if(event.hasBank("EVENT::particle")==false) return null;
        PhysicsEvent physEvent = new PhysicsEvent();        
        EvioDataBank bank = (EvioDataBank) event.getBank("EVENT::particle");
        int rows = bank.rows();
        for(int loop = 0; loop < rows ; loop++){
            int pid = bank.getInt("pid",loop);
            if(PDGDatabase.isValidId(pid)==true){
            Particle part = new Particle(pid,
                    bank.getFloat("px",loop),
                    bank.getFloat("py",loop),
                    bank.getFloat("pz",loop),
                    bank.getFloat("vx",loop),
                    bank.getFloat("vy",loop),
                    bank.getFloat("vz",loop)
            );
            physEvent.addParticle(part);
            } else {
                Particle part = new Particle();
                int charge = bank.getByte("charge", loop);
                float mass = bank.getFloat("mass", loop);
                part.initParticleWithPidMassSquare(pid, charge, mass, 
                        bank.getFloat("px",loop),
                        bank.getFloat("py",loop),
                        bank.getFloat("pz",loop),
                        bank.getFloat("vx",loop),
                        bank.getFloat("vy",loop),
                        bank.getFloat("vz",loop)
                );
                physEvent.addParticle(part);
            }

        }
        return physEvent;            
    }
    
}
