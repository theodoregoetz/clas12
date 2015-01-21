/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clas.tools.utils;

/**
 *
 * @author gavalian
 */
public class ResourcesUtils {
    
    private String clas12evnDir = "CLAS12DIR";
    
    public static String getResourceDir(String evnName, String relativePath){
        String envDir = System.getenv(evnName);
        if(envDir==null) return null;
        StringBuilder str = new StringBuilder();
        str.append(envDir);
        if(envDir.charAt(envDir.length()-1) != '/') str.append("/");
        str.append(relativePath);
        return str.toString();
    }
    

    
}
