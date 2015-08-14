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
    Integer     getDataSize(int axis);
    Double      getData(int x, int y);
    Double      getDataX(int index);
    Double      getDataY(int index);
    Double      getErrorX(int index);
    Double      getErrorY(int index);
    Attributes  getAttributes();
    
}
