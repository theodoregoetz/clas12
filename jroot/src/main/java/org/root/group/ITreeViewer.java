/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.group;

import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.root.pad.RootCanvas;

/**
 *
 * @author gavalian
 */
public interface ITreeViewer { 
    public List<String>  getVariables();
    DefaultMutableTreeNode getTree();
    void  draw(String obj, String selection, String options, RootCanvas canvas);
}
