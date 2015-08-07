/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.fitter;

import org.root.base.IDataSet;
import org.root.func.Function1D;

/**
 *
 * @author gavalian
 */
public class DataSetFitterFunction {
    private IDataSet dataSet = null;
    private Function1D  function;
    
    public DataSetFitterFunction(IDataSet ds, Function1D f1d){
        this.dataSet = ds;
        this.function = f1d;
    }
    
    
}
