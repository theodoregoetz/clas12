/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clasrec.io;

import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.data.io.DataEvent;

/**
 *
 * @author gavalian
 */
public interface IEventMaker {
    PhysicsEvent createEvent(DataEvent event);
}
