/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.base;

import java.awt.Graphics2D;
import org.root.pad.GraphAxis;

/**
 *
 * @author gavalian
 */
public interface IDrawableDataSeries {
    double getMinX();
    double getMaxX();
    double getMinY();
    double getMaxY();
    void draw(GraphAxis xaxis, GraphAxis yaxis, Graphics2D g2d);
    Object dataObject();
    IDrawableDataSeries fit(String function, String options);
}
