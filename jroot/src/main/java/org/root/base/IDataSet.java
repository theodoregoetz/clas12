/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.base;

import org.root.attr.Attributes;

/**
 *
 * @author gavalian
 */

public interface IDataSet {

    DataRegion  getDataRegion(); 
    Integer     getDataSize();
    Double      getDataX(int index);
    Double      getDataY(int index);
    Attributes  getAttributes();
    
}
