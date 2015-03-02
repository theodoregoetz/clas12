/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.base;

import java.util.TreeMap;

/**
 *
 * @author gavalian
 */
public interface EvioWritableTree {
    
    String getName();
    TreeMap<Integer,Object>  toTreeMap();
    void                     fromTreeMap(TreeMap<Integer,Object> map);
}
