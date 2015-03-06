/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.rec;

import java.io.Console;
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
    
    public static String waitForEnter() {
        String line = "";
        Console c = System.console();
        if (c != null) {
            // printf-like arguments
            //c.format(message, args);
            c.format("\nPress Enter for Next Event or Bank Name: ");
            line = c.readLine();
        }
        return line;
    }
    
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
        String command = "";
        EvioDataEvent event = null;
        int icounter = 0;
        while(reader.hasEvent()==true){
            
            if(command.length()<4){
                event = (EvioDataEvent) reader.getNextEvent();
                icounter++;
                System.out.println("*********************** EVENT # " + icounter 
                        + "  ***********************");
                event.show();
            }
            command = EvioDump.waitForEnter();
            if(command.length()>4){
                if(event.hasBank(command)==true){
                    EvioDataBank bank = (EvioDataBank) event.getBank(command);
                    bank.show();
                }
            }
            
            if(command.compareTo("q")==0){
                reader.close();
                System.exit(0);
            }
            /*
            for(String bankName : banks){
                if(event.hasBank(bankName)==true){
                    EvioDataBank bank = (EvioDataBank) event.getBank(bankName);
                    bank.show();
                }
            }*/
        }        
        
    }
}
