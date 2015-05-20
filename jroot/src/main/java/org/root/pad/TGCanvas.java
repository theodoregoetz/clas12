/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.pad;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import org.root.base.IDataSet;

/**
 *
 * @author gavalian
 */
public class TGCanvas extends JFrame {
    private EmbeddedCanvas embCanvas = null;
    public TGCanvas(){
        super();
        embCanvas = new EmbeddedCanvas(600,600);
        this.add(embCanvas,BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }
    
    public void cd(int pad){
        this.embCanvas.cd(pad);
    }
    
    public void draw(IDataSet ds){
        this.embCanvas.draw(ds);
    }
}
