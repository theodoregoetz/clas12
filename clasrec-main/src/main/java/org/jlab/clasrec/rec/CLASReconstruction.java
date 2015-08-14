/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clasrec.rec;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.clas.tools.benchmark.Benchmark;
import org.jlab.clas.tools.utils.CommandLineTools;

import org.jlab.clasrec.loader.ClasPluginLoader;
import org.jlab.clasrec.main.DetectorReconstruction;
import org.jlab.clasrec.utils.ServiceConfiguration;
import org.jlab.coda.clara.core.ICService;
import org.jlab.evio.clas12.EvioDataEvent;
import org.jlab.evio.clas12.EvioDataSync;
import org.jlab.evio.clas12.EvioSource;
/**
 *
 * @author gavalian
 */
public class CLASReconstruction {
    
    
    private final ArrayList<DetectorReconstruction>  detectorFactory =
            new ArrayList<DetectorReconstruction>();
    private final ArrayList<String>  detectorFactoryNames = new 
            ArrayList<String>();
    
    private final ClasPluginLoader pluginLoader = new ClasPluginLoader();
    
    private EvioSource  reader               = new EvioSource();
    private Integer     debugLevel           = 0;
    private Integer     maxEventsReconstruct = -1;
    private Integer     skipEvents           = 0;
    private ServiceConfiguration   serviceConfig = new ServiceConfiguration();
    
    public CLASReconstruction(){
        
    }
    
    public final void setDetectors(String detectorList){
        detectorFactoryNames.clear();
        String[] tokens = detectorList.split(":");
        for(String item : tokens){
            this.detectorFactoryNames.add(item);
        }
    }
    
    public void setDebugLevel(int level){
        this.debugLevel = level;
    }
    
    
    
    public void initServiceConfiguration(ArrayList<String> items){
        for(String configItem : items){
            String[] tokens = configItem.split("=");
            if(tokens.length==2){
                String[] systemItem = tokens[0].split("::");
                if(systemItem.length==2){
                    this.serviceConfig.addItem(systemItem[0], 
                            systemItem[1], tokens[1]);
                }
            }
        }
        System.out.println("****************************************");
        this.serviceConfig.show();
        System.out.println("****************************************");
    }
    
    public void addDetector(DetectorReconstruction rec){
        this.detectorFactory.add(rec);
    }
    
    public void addDetector(String classname){
         try {
            Class   c = Class.forName(classname);
            if(c.getSuperclass()==DetectorReconstruction.class){
                DetectorReconstruction dr = (DetectorReconstruction) c.newInstance();
                this.addDetector(dr);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CLASReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CLASReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CLASReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addDetectorToFactory(String classname){
        try {
            Class   c = Class.forName(classname);
            if(c.getSuperclass()==DetectorReconstruction.class){
                DetectorReconstruction dr = (DetectorReconstruction) c.newInstance();
                this.detectorFactory.add(dr);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CLASReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CLASReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CLASReconstruction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void init(){
        for(DetectorReconstruction detectorRec : this.detectorFactory ){
            try {
                detectorRec.setDebugLevel(this.debugLevel);
                detectorRec.configure(serviceConfig);
                detectorRec.init();
                System.err.println("[INIT-DETECTORS] ----> detector initialization "
                        + detectorRec.getName() + " ......... ok");
            } catch (Exception e) {
                System.err.println("[INIT-DETECTORS] ----> ERROR initializing detector "
                + detectorRec.getName());
            }
        }
    }
    
    public void initDetectorModules(){
        for(DetectorReconstruction detectorRec : this.detectorFactory ){
            try {
                detectorRec.setDebugLevel(this.debugLevel);
                detectorRec.configure(serviceConfig);
                detectorRec.init();
                System.err.println("[INIT-DETECTORS] ----> detector initialization "
                        + detectorRec.getName() + " ......... ok");
            } catch (Exception e) {
                System.err.println("[INIT-DETECTORS] ----> ERROR initializing detector "
                + detectorRec.getName());
            }
        }
    }
    
    public void initDetectors(){
        this.detectorFactory.clear();
        TreeMap<String,ICService> detectorClasses = pluginLoader.getPluginLoader().getClassMap();
        
        for(String detector : this.detectorFactoryNames){
            if(detectorClasses.containsKey(detector)==true){
                System.err.println("[INIT-DETECTORS] ---> found detector ["+detector+"]");
                detectorFactory.add((DetectorReconstruction) detectorClasses.get(detector));
            } else {
                System.err.println("[INIT-DETECTORS] ---> ERROR : detector ["
                        +detector+"] not found");
            }
        }
        //for(Map.Entry<String,ICService> service : detectorClasses.entrySet()){            
        //}
        
        for(DetectorReconstruction detectorRec : this.detectorFactory ){
            try {
                detectorRec.setDebugLevel(this.debugLevel);
                detectorRec.configure(serviceConfig);
                detectorRec.init();
                System.err.println("[INIT-DETECTORS] ----> detector initialization "
                        + detectorRec.getName() + " ......... ok");
            } catch (Exception e) {
                System.err.println("[INIT-DETECTORS] ----> ERROR initializing detector "
                + detectorRec.getName());
            }
        }
    }
    
    
    public void setSkip(Integer skip){
        this.skipEvents = skip;
    }
    
    public void setMaxEvents(int maxevt){
        this.maxEventsReconstruct = maxevt;
    }
    
    public void initPlugins(){
        System.err.println("[PLUGIN] ----> Initializing Plugins");
        pluginLoader.loadPluginDirectory();
        pluginLoader.show();
    }
    
    public void run(String filename, String output){
        reader.open(filename);
        //this.initDetectors();
        EvioDataSync writer = new EvioDataSync();
        writer.open(output);
        
        Benchmark bench = new Benchmark();
        bench.addTimer("WRITER");
        bench.addTimer("TOTAL");
        
        for(DetectorReconstruction rec : this.detectorFactory){
            bench.addTimer(rec.getName());
        }
        
        Long  processTime = System.currentTimeMillis();
        int iEventCounter = 0;
        while(reader.hasEvent()){
            iEventCounter++;
            bench.resume("TOTAL");
            EvioDataEvent event = (EvioDataEvent) reader.getNextEvent();
            if(iEventCounter >= this.skipEvents){
                for(DetectorReconstruction rec : this.detectorFactory){
                    bench.resume(rec.getName());
                    try {
                        rec.processEvent(event);
                    } catch (Exception e) {
                        System.err.println("[CLAS-REC] -----> ERROR : excpetion thrown by "
                                + rec.getName());                    
                        e.printStackTrace();
                    }
                    bench.pause(rec.getName());
                }            
                bench.resume("WRITER");
                writer.writeEvent(event);
                bench.pause("WRITER");
            }
            
            bench.pause("TOTAL");
            Long currentTime = System.currentTimeMillis();
            if((currentTime-processTime)>10000){
                processTime = currentTime;
                System.err.println(bench.getTimer("TOTAL").toString());
            }
            if(this.maxEventsReconstruct>0&&iEventCounter>=this.maxEventsReconstruct) break;
        }
        writer.close();
        System.err.println("\n\n" + bench.toString());
    }
    
    public static void printUsage(){
        System.err.println("\n\n");
        System.err.println("\t Usage: CLASReconstruction [service list] [input file] [output file]");
        System.err.println("\n\n");
        System.err.println("\t service list : list of services to run (e.g DCHB:DCTB:EC:FTOF:EB)");
        System.err.println("\t input file   : input file name");
        System.err.println("\t output file  : output file name (optional)");
        System.err.println("\n\n");
    }
    
    public static void main(String[] args){
        
        CommandLineTools  cmdParser = new CommandLineTools();
        
        cmdParser.addRequired("-i");
        //cmdParser.addRequired("-s");
        cmdParser.addDescription("-i", "input file name");
        cmdParser.addDescription("-s", "service list to run (e.g. BST:FTOF:EB )");
        cmdParser.addDescription("-o", "output file name");
        cmdParser.addDescription("-n", "number of events to run");
        cmdParser.addDescription("-l", "number of events to skip");
        
        cmdParser.parse(args);
        
        
        Integer nEventsToRun = -1;
        String  outputFileName = "rec_output.evio";
        
        if(cmdParser.isComplete()==false){
            System.err.println(cmdParser.usageString());
            System.exit(0);
        }
        //if(args.length<2){
        //    CLASReconstruction.printUsage();

        //}
        
        String serviceList  = "FMT:DCHB:DCTB:BST:FTCAL:FTHODO:FTMATCH:FTOF:CTOF:EC:EB";
        if(cmdParser.hasOption("-s")){
           serviceList = cmdParser.asString("-s");
        }
        
        String inputFile    = cmdParser.asString("-i");
        String outputFile   = "rec_output.evio";
        
        if(cmdParser.hasOption("-o")==true){
            outputFile = cmdParser.asString("-o");
        }
        
                
        if(cmdParser.hasOption("-n")==true){
            nEventsToRun = cmdParser.asInteger("-n");
        }
        //if(args.length>2) {
        //    outputFile = args[2];
        //}
        
        CLASReconstruction  clasRec = new CLASReconstruction();
        
        clasRec.initServiceConfiguration(cmdParser.getConfigItems());
        clasRec.setMaxEvents(nEventsToRun);
        clasRec.setDetectors(serviceList);
        clasRec.initPlugins();
        
        if(cmdParser.hasOption("-l")==true){
            Integer skip = cmdParser.asInteger("-l");            
            clasRec.setSkip(skip);
        }
        
        clasRec.run(inputFile, outputFile);
        
    }
}
