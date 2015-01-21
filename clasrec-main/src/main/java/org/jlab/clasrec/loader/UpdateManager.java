/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.clasrec.loader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.clas.tools.utils.ResourcesUtils;
import org.jlab.clas.tools.utils.StringUtils;
import org.jlab.utils.CLASResources;

/**
 *
 * @author gavalian
 */
public class UpdateManager {
    public static void downloadURL(String url, String file, String directory){
        
        System.err.println("------> ");
        System.err.println("[UpdateManager] url         : " + url);
        System.err.println("[UpdateManager] copy file   : " + file);
        System.err.println("[UpdateManager] destination : " + directory);
        
        Long startTime = System.currentTimeMillis();
        try {
            String urlFile = url + "/" + file;
            String outputFile = directory + "/" + file;
            URL website = new URL(urlFile);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (MalformedURLException ex) {
            //Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(StringUtils.getStringRed("[UpdateManager] failure     : file not found." ));
            return;
        }
        
        Long endTime = System.currentTimeMillis();
        Integer timeElapsed = (int) (endTime - startTime);
        String summary = String.format("[UpdateManager] success     :  time = %6.2f sec", (double) timeElapsed/1000.0);
        System.err.println(StringUtils.getStringGreen(summary));
    }
    
    public static void updatePlugins(){
        String[] plugins = new String[]{
            "clasrec-ftof-1.0-SNAPSHOT.jar",
            "clasrec-ec-1.0-SNAPSHOT.jar",
            "clasrec-eb-1.0-SNAPSHOT.jar",
            "clasrec-dc-1.0-SNAPSHOT.jar"
        };
        
        String pluginsURL = "https://userweb.jlab.org/~gavalian/software/plugins";
        String pluginsDirectory = ResourcesUtils.getResourceDir("CLAS12DIR", "lib/plugins");
        
        for(int loop = 0; loop < plugins.length; loop++){
            UpdateManager.downloadURL(pluginsURL, plugins[loop], pluginsDirectory);
        }        
    }
    
    public static void main(String[] args){
        
        String updateType = args[0];
        System.err.println("\n\n********************************************");
        if(updateType.compareTo("coat")==0){
            String clas12maven = "https://clasweb.jlab.org/clas12maven/org/jlab/coat/coat-libs/1.0-SNAPSHOT";
            String version     = "1.0-SNAPSHOT";
            String jarLib      = "coat-libs";
            String destination = ResourcesUtils.getResourceDir("CLAS12DIR", "lib/clas");
            
            if(destination==null){
                System.err.println("[UpdateManager] ERROR getting destination directory from "
                + " environment CLAS12DIR...");
                return;
            }
            String jarFileName = jarLib + "-" + version + ".jar";
            UpdateManager.downloadURL(clas12maven, jarFileName, destination);
            System.err.println("********************************************");
            System.err.println("\n\nDone......\n");
        }
        
        if(updateType.compareTo("plugins")==0){
            UpdateManager.updatePlugins();
            System.err.println("\n\nDone......\n");
        }
    }
}
