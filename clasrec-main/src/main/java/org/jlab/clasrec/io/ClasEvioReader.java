/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clasrec.io;
import java.util.ArrayList;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class ClasEvioReader {
    private final ArrayList<String>  readerFiles  = new ArrayList<String>();
    private final EvioSource         dataReader   = new EvioSource();
    private Integer                  currentFile  = 0;
    private EvioDataEvent            currentEvent = null;
    private IEventMaker              physicsEventMaker = null;
    
    public ClasEvioReader(){
        physicsEventMaker = new EventMakerClas6();
    }
    
    public ClasEvioReader(IEventMaker maker){
        physicsEventMaker = maker;
    }
    
    public void addFile(String file){
        readerFiles.add(file);
    }
    
    public void open(){
        currentFile = 0;
        dataReader.open(readerFiles.get(0));
    }
    /**
     * Reader next buffer into the event, returns true is successful
     * and false if the end of the files have been reached.
     * @return true or false
     */
    public boolean  next(){
        if(dataReader.hasEvent()==true){
            currentEvent = (EvioDataEvent) dataReader.getNextEvent();
            return true;
        }
        
        if(currentFile==readerFiles.size()-1){
            currentEvent = null;
            return false;
        }
        
        dataReader.close();
        currentFile++;
        dataReader.open(readerFiles.get(currentFile));
        currentEvent = (EvioDataEvent) dataReader.getNextEvent();
        
        return true;
    }
    /**
     * returns Physics event from current data event buffer
     * @return 
     */
    public PhysicsEvent getPhysicsEvent(){
        return physicsEventMaker.createEvent(currentEvent);
    }
}
