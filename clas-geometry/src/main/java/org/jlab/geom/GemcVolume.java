package org.jlab.geom;

import java.util.*;
import java.lang.UnsupportedOperationException;

public class GemcVolume extends LinkedHashMap<String,String> {

    /**
     * defines the keys and order thereof for this map
     **/
    public GemcVolume() {
        // order matters here!
        super.put("mother", "root");
        super.put("description", "");
        super.put("pos", "0*cm 0*cm 0*cm");
        super.put("rotation", "ordered: zxy 0*deg 0*deg 0*deg");
        super.put("color", "808080");
        super.put("type", "GemcTrap");
        super.put("dimensions", "0*cm 0*deg 0*deg 0*cm 0*cm 0*cm 0*deg 0*cm 0*cm 0*cm 0*deg");
        super.put("material", "Air");
        super.put("mfield", "no");
        super.put("ncopy", "1");
        super.put("pMany", "1");
        super.put("exist", "1");
        super.put("visible", "1");
        super.put("style", "0");
        super.put("sensitivity", "no");
        super.put("hit_type", "");
        super.put("identifiers", "");
    }

    /**
     * this prevents new keys from being added outside the constructor
     **/
    @Override
    public String put(String K, String V) {
        if (this.containsKey(K)) {
            return super.put(K,V);
        } else {
            throw new UnsupportedOperationException("Unknown key: "+K);
        }
    }

    /**
     * human-readable format
     **/
    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        for (Map.Entry<String,String> e : this.entrySet()) {
            msg.append(e.getKey()+": "+e.getValue()+"\n");
        }
        return msg.toString();
    }

    /**
     * text format expected by GEMC
     **/
    public String toPaddedString(List<Integer> pads, String sep) {
        StringBuilder msg = new StringBuilder();
        int i = 0;
        Iterator<Map.Entry<String, String>> itr = this.entrySet().iterator();
        while (itr.hasNext()) {
            if (msg.length() > 0) {
                msg.append(" "+sep+" ");
            }
            msg.append(String.format("%1$-" + pads.get(i) + "s", itr.next().getValue()));
            i += 1;
        }
        return msg.toString();
    }

    /**
     * returns the length of the strings stored in this map
     **/
    public List<Integer> getWidths() {
        List<Integer> w = new ArrayList<Integer>();
        for (Map.Entry<String,String> e : this.entrySet()) {
            w.add(e.getValue().length());
        }
        return w;
    }
}
