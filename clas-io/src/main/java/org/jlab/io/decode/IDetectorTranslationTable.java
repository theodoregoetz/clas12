/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.io.decode;

/**
 *
 * @author gavalian
 */
public interface IDetectorTranslationTable {
    String   getName();
    void     setName(String name);
    Integer  getSector(int crate, int slot, int channel);
    Integer  getLayer(int crate, int slot, int channel);
    Integer  getComponent(int crate, int slot, int channel);
}
