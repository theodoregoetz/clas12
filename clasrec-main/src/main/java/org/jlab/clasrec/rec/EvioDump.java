/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.rec;

import java.util.ArrayList;
import org.jlab.clas.tools.utils.CommandLineTools;
import org.jlab.evio.clas12.EvioDataBank;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioSource;

/**
 *
 * @author gavalian
 */
public class EvioDump {
    
    public static void main(String[] args){
                
        CommandLineTools  parser = new CommandLineTools();
        
        parser.addRequired("-i");
        
        parser.addDescription("-i", "input file name");
        parser.addDescription("-b", "bank name to display (eg. -b PCAL::dgtz )");

        
        parser.setMultiOption("-b");
        parser.parse(args);
        
        if(parser.isComplete()==false){
            System.err.println(parser.usageString());
            System.exit(0);
        }
        
        String inputFile = "";
        if(parser.hasOption("-i")==true){
            inputFile = parser.asString("-i");
        }
        
        EvioSource reader = new EvioSource();
        reader.open(inputFile);
        ArrayList<String>  banks = parser.getConfigItems();
        
        int icounter = 0;
        while(reader.hasEvent()==true){
            EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
            icounter++;
            System.out.println("*********************** EVENT # " + icounter 
                    + "***********************");
            for(String bankName : banks){
                if(event.hasBank(bankName)==true){
                    EvioDataBank bank = (EvioDataBank) event.getBank(bankName);
                    bank.show();
                }
            }
        }        
        
    }
}
