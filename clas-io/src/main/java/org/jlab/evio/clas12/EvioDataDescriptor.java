/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.evio.clas12;

import java.util.ArrayList;
import java.util.HashMap;
import org.jlab.data.io.DataDescriptor;
import org.jlab.utils.TablePrintout;

/**
 *
 * @author gavalian
 */
public class EvioDataDescriptor implements DataDescriptor {
    private String descriptorName = "UNDEF";
    private Integer descriptorContainerTag = 0;
    private Integer descriptorContainerNum = 0;
    private ArrayList<String> entryNames = new ArrayList<String>();
    private HashMap<String,EvioDataDescriptorEntry> descriptorEntries = 
            new HashMap<String,EvioDataDescriptorEntry>();
    private HashMap<String,String>  descriptorProperties = 
            new HashMap<String,String>();
    
    public EvioDataDescriptor(String name, String parenttag, String containertag){
        this.descriptorName = name;
        descriptorProperties.put("parent_tag", parenttag);
        descriptorProperties.put("container_tag", containertag);
    }
    
    @Override
    public void init(String s) {
        descriptorEntries.clear();
        String[] tokens = s.split(":");
        descriptorName  = tokens[0];
        descriptorContainerTag = Integer.parseInt(tokens[1]);
        descriptorContainerNum = Integer.parseInt(tokens[2]);
        ArrayList<String> nnn = new ArrayList<String>();
        for(int loop = 3 ; loop < tokens.length; loop+=4){
            nnn.add(tokens[loop]);
            descriptorEntries.put(tokens[loop], 
                    new EvioDataDescriptorEntry("generic",tokens[loop],
                            Integer.parseInt(tokens[loop+1]),
                            Integer.parseInt(tokens[loop+2]),
                            tokens[loop+3]
                    ));
        }
        //entryNames = new String[nnn.size()];
        //for(int loop = 0; loop < nnn.size(); loop++){
        //    entryNames[loop] = nnn.get(loop);
        //}
        //entryNames = (String[]) nnn.toArray();
    }
    
    public void addEntry(String section,String name, Integer tag, Integer num, String type){
        descriptorEntries.put(name, 
                new EvioDataDescriptorEntry(section,name,
                        tag,
                        num,
                        type
                ));        
        entryNames.add(name);
    }
    
    @Override
    public String[] getEntryList() {
        //return entryNames;
        String[] entries = new String[descriptorEntries.size()];
        for(int loop = 0; loop < entryNames.size(); loop++){
            entries[loop] = entryNames.get(loop);
        }
        return entries;
    }

    @Override
    public String getName() {
        return descriptorName;
    }

    @Override
    public int getProperty(String property_name, String entry_name) {
        int ret = -1;
        if(descriptorEntries.containsKey(entry_name)==false){
            System.err.println("[EvioDataDescriptor] ERROR : getProperty requested for "
            + " unknown filed " + entry_name);
            return ret;
        }
        
        if(property_name.equals("tag")==true){
            return descriptorEntries.get(entry_name).tag;
        }
        
        if(property_name.equals("num")==true){
            return descriptorEntries.get(entry_name).num;
        }
        
        if(property_name.equals("type")==true){
            return descriptorEntries.get(entry_name).type.id();
        }
        
        return ret;
    }

    @Override
    public int getProperty(String property_name) {
        if(property_name.equals("tag")==true){
            return descriptorContainerTag;
        }
        if(property_name.equals("num")==true){
            return descriptorContainerNum;
        }
        return 0;
    }

    @Override
    public void show() {
        System.out.println("\n\n>>> BANK name = " + this.getName() +
                " tag = " + this.getPropertyString("parent_tag") );
        String[] entry_names = this.getEntryList();
        TablePrintout table = new TablePrintout("Column:Tag:Number:Type","24:8:8:12");
        for(String item : entry_names){
            String[] tdata = new String[4];
            tdata[0] = item;
            tdata[1] = descriptorEntries.get(item).tag.toString();
            tdata[2] = descriptorEntries.get(item).num.toString();;
            tdata[3] = descriptorEntries.get(item).type.stringName();
            table.addData(tdata);
        }
        table.show();
    }
    
    @Override
    public String getXML() {
        StringBuilder str = new StringBuilder();
        
        return str.toString();
    }

    @Override
    public void setPropertyString(String name, String value) {
        if(descriptorProperties.containsKey(name)==true){
            descriptorProperties.remove(name);
        } 
        descriptorProperties.put(name, value);
    }

    @Override
    public String getPropertyString(String property_name) {
        if(descriptorProperties.containsKey(property_name)==true){
            return descriptorProperties.get(property_name);
        }
        return null;
    }
    
}
