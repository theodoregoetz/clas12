/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.io.decode;

/**
 *
 * @author gavalian
 */
public class TranslationTableEntry {
    
    public Integer sector    = 1;
    public Integer layer     = 1;
    public Integer component = 1;
    public String  detector  = "EC";
    
    public Integer create    = 2;
    public Integer slot      = 1;
    public Integer channel   = 1;
    
    public TranslationTableEntry(String _det_){
        this.detector = _det_;
    }
    
    public TranslationTableEntry(String _det_,
            int _sec_, int _lay_, int _comp_,
            int _cr_ , int _sl_ , int _ch_){
        this.detector = _det_;
        this.setDetector(_sec_, _lay_, _comp_);
        this.setCreate(_cr_, _sl_, _ch_);
    }
    
    public final void setDetector(int s, int l, int c){
        this.sector    = s;
        this.layer     = l;
        this.component = c;
    }
    
    public final void setCreate(int c, int s, int ch){
        this.create  = c;
        this.slot    = s;
        this.channel = ch;
    }
    
    public static Integer getHashCreate(int _create_, int _slot_, int _ch_){
        Integer hash_c = _create_*256*256*256 + _slot_*256*256 + _ch_;
        return hash_c;
    }
    
    public Integer getHashCreate(){
        return TranslationTableEntry.getHashCreate(this.create,this.slot, this.channel);
    }
    
    public void parse(String format){
        String[] tokens = format.split("\\s+");
        if(tokens.length<7){
            System.err.println("[ERROR] error parsing the string : " + format);
            return;
        }
        this.detector  = tokens[0];
        this.sector    = Integer.parseInt(tokens[1]);
        this.layer     = Integer.parseInt(tokens[2]);
        this.component = Integer.parseInt(tokens[3]);
        this.create    = Integer.parseInt(tokens[4]);
        this.slot      = Integer.parseInt(tokens[5]);
        this.channel   = Integer.parseInt(tokens[6]);        
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("%-8s %5d %5d %5d %5d %5d %5d", this.detector,
                this.sector, this.layer, this.component,
                this.create,this.slot,this.channel));
        return str.toString();
    }
}
