/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clasrec.loader;

import org.jlab.clas.tools.utils.FileUtils;
import org.jlab.clas.tools.utils.ResourcesUtils;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;

import org.jlab.coda.clara.core.ICService;

/**
 *
 * @author gavalian
 */
public class ClasPluginLoader {
    
    private final JarPluginLoader pluginLoader = new JarPluginLoader();
    
    public ClasPluginLoader(){
        
    }
        
    public JarPluginLoader getPluginLoader(){ return pluginLoader;}
    
    public void loadPluginDirectory(String dirname,String classname){
        String pluginDirectory = ResourcesUtils.getResourceDir("CLAS12DIR", dirname);
        if(pluginDirectory==null){
            System.err.println("[ERROR] Can not find directory : lib/plugins ");
            return;
        }
        
        List<String> pluginFiles = FileUtils.getFilesInDir(pluginDirectory);
        System.err.println("[JarFileLoader] ----> directory   : " + pluginDirectory);
        System.err.println("[JarFileLoader] ----> files found : " + pluginFiles.size());
        pluginLoader.clear();
        
        for(String jarFile : pluginFiles){
            System.err.println("[JarFileLoader] ----> scanning file : " + jarFile);
            pluginLoader.scan(jarFile,classname);
        }
    }
    
    public void loadPluginDirectory(){
        String pluginDirectory = ResourcesUtils.getResourceDir("CLAS12DIR", "lib/plugins");
        if(pluginDirectory==null){
            System.err.println("[ERROR] Can not find directory : lib/plugins ");
            return;
        }
        
        List<String> pluginFiles = FileUtils.getFilesInDir(pluginDirectory);
        System.err.println("[JarFileLoader] ----> directory   : " + pluginDirectory);
        System.err.println("[JarFileLoader] ----> files found : " + pluginFiles.size());
        pluginLoader.clear();
        
        for(String jarFile : pluginFiles){
            System.err.println("[JarFileLoader] ----> scanning file : " + jarFile);
            pluginLoader.scan(jarFile);
        }
    }
    
    public void loadPluginDirectoryMonitoring(){
        String pluginDirectory = ResourcesUtils.getResourceDir("CLAS12DIR", "lib/plugins");
        if(pluginDirectory==null){
            System.err.println("[ERROR] Can not find directory : lib/plugins ");
            return;
        }
        
        List<String> pluginFiles = FileUtils.getFilesInDir(pluginDirectory);
        System.err.println("[JarFileLoader] ----> directory   : " + pluginDirectory);
        System.err.println("[JarFileLoader] ----> files found : " + pluginFiles.size());
        pluginLoader.clear();
        
        for(String jarFile : pluginFiles){
            System.err.println("[JarFileLoader] ----> scanning file : " + jarFile);
            pluginLoader.scanMonitoring(jarFile);
        }
    }
    
    public void show(){
        TreeMap<String,ICService> detectors = pluginLoader.getClassMap();
        for(Map.Entry<String,ICService> entry : detectors.entrySet()){
            System.err.println(String.format("[PLUGINS] --->  %8s : %s <-", 
                    entry.getKey(),entry.getValue()));
        }
    }
    
}
