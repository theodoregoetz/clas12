/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.io.decode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gavalian
 */
public class TranslationTable {
    
    private final TreeMap<Integer,TranslationTableEntry>  entryStore = 
            new TreeMap<Integer,TranslationTableEntry>();
    
    public TranslationTable(){
        
    }
    
    public void addEntry(String entryLine){
        TranslationTableEntry entry = new TranslationTableEntry("empty");
        entry.parse(entryLine);
        if(entryStore.containsKey(entry.getHashCreate())==true){
            System.err.println("[Translation Map] (ERROR) ==> "
            + " Duplicate entry detected. Not added.\n" + "\t" + entryLine);
        } else {
            entryStore.put(entry.getHashCreate(), entry);
        }
    }
    
    public Set<Integer>  getCreateList(){
        Set<Integer> createKeys = new HashSet<Integer>();
        for(Map.Entry<Integer,TranslationTableEntry> entry : entryStore.entrySet()){
            createKeys.add(entry.getValue().create);
        }
        return createKeys;
    }
    
    public TranslationTableEntry  getEntry(int create, int slot, int channel){
        Integer hash = TranslationTableEntry.getHashCreate(create, slot, channel);
        if(entryStore.containsKey(hash)==true){
            return entryStore.get(hash);
        }
        return null;
    }
    
    public void parse(String entries){
        
    }
    
    public void readFile(String filename){
        BufferedReader in = null;        
        try {
            in = new BufferedReader(new FileReader(filename));
            while (in.ready()) {
                String s = in.readLine();
                if(s.startsWith("#")==false){
                    this.addEntry(s);
                }
            }               
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TranslationTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TranslationTable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(TranslationTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<Integer,TranslationTableEntry> entry : entryStore.entrySet()){
            str.append(String.format("%-12d : %s\n", entry.getKey(),entry.getValue().toString()));
        }
        return str.toString();
    }
}
