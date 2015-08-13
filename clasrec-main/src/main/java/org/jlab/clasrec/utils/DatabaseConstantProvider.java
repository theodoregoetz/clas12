/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.clasrec.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.jlab.ccdb.Assignment;
import org.jlab.ccdb.CcdbPackage;
import org.jlab.ccdb.JDBCProvider;
import org.jlab.ccdb.TypeTable;
import org.jlab.ccdb.TypeTableColumn;
import org.jlab.geom.base.ConstantProvider;


/**
 *
 * @author gavalian
 */
public class DatabaseConstantProvider implements ConstantProvider {
    private HashMap<String,String[]> constantContainer = new HashMap<String,String[]>();
    private boolean PRINT_ALL = true;

    private JDBCProvider provider;
    public DatabaseConstantProvider(String address){
        this.initialize(address);
    }

    private void initialize(String address){
        provider = CcdbPackage.createProvider(address);
        provider.connect();

        //Assignment asgmt = provider.getData("/test/test_vars/test_table");
    }

    public void loadTable(String table_name){
        Assignment asgmt = provider.getData(table_name);
        int ncolumns = asgmt.getColumnCount();
        TypeTable  table = asgmt.getTypeTable();
        Vector<TypeTableColumn> typecolumn = asgmt.getTypeTable().getColumns();
        for(int loop = 0; loop < ncolumns; loop++){
            //System.out.println("Reading column number " + loop
            //+ "  " + typecolumn.elementAt(loop).getCellType()
            //+ "  " + typecolumn.elementAt(loop).getName());
            Vector<String> row = asgmt.getColumnValuesString(loop);
            String[] values = new String[row.size()];
            for(int el = 0; el < row.size(); el++){
                values[el] = row.elementAt(el);
                //for(String cell: row){
                //System.out.print(cell + " ");
            }
            StringBuilder str = new StringBuilder();
            str.append(table_name);
            str.append("/");
            str.append(typecolumn.elementAt(loop).getName());
            constantContainer.put(str.toString(), values);
            //System.out.println(); //next line after a row
        }
    }

    @Override
    public boolean hasConstant(String string) {
        return constantContainer.containsKey(string);
    }

    @Override
    public int length(String string) {
        if(this.hasConstant(string)) return constantContainer.get(string).length;
        return 0;
    }

    @Override
    public double getDouble(String string, int i) {
        if(this.hasConstant(string)==true && i < this.length(string)){
            return Double.parseDouble(constantContainer.get(string)[i]);
        } else {

        }
        return 0.0;
    }

    @Override
    public int getInteger(String string, int i) {
        if(this.hasConstant(string)==true && i < this.length(string)){
            return Integer.parseInt(constantContainer.get(string)[i]);
        } else {

        }
        return 0;
    }

    @Override
    public String toString(){
        System.err.println("Database Constat Provider: ");
        StringBuilder str = new StringBuilder();
        if(PRINT_ALL==true){
            for(Map.Entry<String,String[]> entry : constantContainer.entrySet()){
                str.append(String.format("%24s : %d\n", entry.getKey(),entry.getValue().length));
                for(int loop = 0; loop < entry.getValue().length; loop++){
                    //str.append("\n");
                    str.append(String.format("%18s ", entry.getValue()[loop]));
                }
                str.append("\n");
            }
        } else {
            for(Map.Entry<String,String[]> entry : constantContainer.entrySet()){
                str.append(String.format("%24s : %d\n", entry.getKey(),entry.getValue().length));
            }
        }
        return str.toString();
    }

    public void setDefaultDate(Date date) {
        provider.setDefaultDate(date);
    }
    public void setDefaultVariation(Sring variation) {
        provider.setDefaultVariation(variation);
    }
    public void setDefaultRun(int run)) {
        provider.setDefaultRun(run);
    }
}
