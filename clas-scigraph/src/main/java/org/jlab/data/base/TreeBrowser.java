/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.data.base;

import javax.swing.tree.DefaultMutableTreeNode;
import org.jlab.data.graph.DataSet;

/**
 *
 * @author gavalian
 */
public interface TreeBrowser {
    
    void open(String filename);
    DefaultMutableTreeNode  getTree();
    DataSet                 getData(String selection);
    DataSet                 getData(String selection, String condition);
    int                     getDataType(String selection);
}
