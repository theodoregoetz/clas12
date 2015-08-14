package org.jlab.geom;

import java.util.*;

public class GemcVolume extends LinkedHashMap<String,String> {

    public GemcVolume() {
        this.put("mother", "root");
        this.put("description", "");
        this.put("pos", "0*cm 0*cm 0*cm");
        this.put("rotation", "ordered: zxy 0*deg 0*deg 0*deg");
        this.put("color", "808080");
        this.put("type", "GemcTrap");
        this.put("dimensions", "0*cm 0*deg 0*deg 0*cm 0*cm 0*cm 0*deg 0*cm 0*cm 0*cm 0*deg");
        this.put("material", "Air");
        this.put("mfield", "no");
        this.put("ncopy", "1");
        this.put("pMany", "1");
        this.put("exist", "1");
        this.put("visible", "1");
        this.put("style", "0");
        this.put("sensitivity", "no");
        this.put("hit_type", "");
        this.put("identifiers", "");
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        for (Map.Entry<String,String> e : this.entrySet()) {
            msg.append(e.getKey()+": "+e.getValue()+"\n");
        }
        return msg.toString();
    }

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

    public List<Integer> getWidths() {
        List<Integer> w = new ArrayList<Integer>();
        for (Map.Entry<String,String> e : this.entrySet()) {
            w.add(e.getValue().length());
        }
        return w;
    }
}
