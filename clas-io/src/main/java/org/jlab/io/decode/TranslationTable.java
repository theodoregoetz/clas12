/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.io.decode;

import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public class TranslationTable {
    private final TreeMap<Integer,CreateTranslationEntry>  entryStore = 
            new TreeMap<Integer,CreateTranslationEntry>();
    
    public TranslationTable(){
        
    }
    
    public void addEntry(String entryLine){
        CreateTranslationEntry entry = new CreateTranslationEntry("empty");
        entry.parse(entryLine);
        if(entryStore.containsKey(entry.getHashCreate())==true){
            System.err.println("[Translation Map] (ERROR) ==> "
            + " Duplicate entry detected. Not added.\n" + "\t" + entryLine);
        } else {
            entryStore.put(entry.getHashCreate(), entry);
        }
    }
    
    public void parse(String entries){
        
    }
    
    
}
