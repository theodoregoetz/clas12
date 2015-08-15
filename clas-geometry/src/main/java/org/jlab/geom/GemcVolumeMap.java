package org.jlab.geom;

import java.util.*;

public class GemcVolumeMap extends LinkedHashMap<String,GemcVolume> {

    /**
     * human-readable string
     **/
    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        for (Map.Entry<String,GemcVolume> vol : this.entrySet()) {
            msg.append(vol.getKey()+":\n");
            msg.append(vol.getValue().toString().replaceAll("(?m)^", "    "));
        }
        return msg.toString();
    }

    /**
     * text format expected by GEMC
     **/
    public String toPaddedString(String sep) {
        List<Integer> pads = null;
        for (Map.Entry<String,GemcVolume> vol : this.entrySet()) {
            List<Integer> w = vol.getValue().getWidths();
            if (pads == null) {
                pads = w;
            } else {
                for (int i=0; i<pads.size(); i++) {
                    if (pads.get(i) < w.get(i)) {
                        pads.set(i,w.get(i));
                    }
                }
            }
        }
        StringBuilder msg = new StringBuilder();
        for (Map.Entry<String,GemcVolume> vol : this.entrySet()) {
            msg.append(vol.getValue().toPaddedString(pads,sep)+"\n");
        }
        return msg.toString();
    }

    /**
     * text format expected by GEMC using | as separator
     **/
    public String toPaddedString() {
        return this.toPaddedString("|");
    }
}
