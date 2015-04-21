/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jlab.evio.stream.EvioInputStream;
import org.jlab.evio.stream.EvioOutputStream;
import org.root.group.ITreeViewer;
import org.root.group.TDirectory;
import org.root.histogram.H1D;
import org.root.histogram.H2D;
import org.root.pad.RootCanvas;

/**
 *
 * @author gavalian
 */
public class NTuple implements ITreeViewer {
    
    private ArrayList<NTupleRow>  tupleRows = new ArrayList<NTupleRow>();
    private ArrayList<DataVector> processResults = new ArrayList<DataVector>();
    
    private String[] tupleVariables = null;
    private String   tupleName;
    
    public NTuple(String name, int ncolumns){
        this.tupleName = name;
        char[] letter_a = new char[]{'a'};
        this.tupleVariables = new String[ncolumns];
        for(int loop = 0; loop < ncolumns; loop++){
            this.tupleVariables[loop] = new String(letter_a);
            letter_a[0]++;
        }
    }
    
    public NTuple(String name, String variableList){
        this.tupleName = name;
        this.init(variableList);
    }
    
    private void init(String variableList){
        tupleVariables = variableList.split(":");
    }
    
    public static boolean isTupleFile(String filename){
        EvioInputStream inStream = new EvioInputStream();
        inStream.open(filename);
        TreeMap<Integer,Object> tree = inStream.getObjectTree(1);
        if(tree.get(1) instanceof int[]){
            int[] type = (int[]) tree.get(1);
            if(type[0]==100){
                return true;
            }
        }
        return false;
    }
    
    public void readFile(String filename){
        BufferedReader in = null;
        this.tupleRows.clear();
        try {
            in = new BufferedReader(new FileReader(filename));
            while (in.ready()) {
                String s = in.readLine();
                if(s.startsWith("#")==false&&s.length()>4){
                    this.addRow(s);
                }
                //NTupleRow  row = new NTupleRow(s);
                //this.tupleRows.add(row);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NTuple.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NTuple.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(NTuple.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void scan(){
        StringBuilder str = new StringBuilder();
        for(int loop = 0; loop < 87; loop++) str.append("*");
        str.append("\n");
        str.append(String.format("* NTUPLE [%24s]  ENTRIES %14d %25s *\n",
                this.tupleName,this.tupleRows.size()," "));
        for(int loop = 0; loop < 87; loop++) str.append("*");
        str.append("\n");
        str.append(String.format("*   %-12s  * %14s * %14s * %14s * %14s *\n", "variable",
                "min","max","mean","rms"));
        for(int loop = 0; loop < 87; loop++) str.append("*");
        str.append("\n");
        for(int loop = 0; loop < this.tupleVariables.length; loop++){
            DataVector vec = this.getVector(this.tupleVariables[loop]);                    
            str.append(String.format("*   %-12s  * %14e * %14e * %14e * %14e *\n", this.tupleVariables[loop],
                    vec.getMin(),vec.getMax(),vec.getMean(),vec.getRMS()));
        }
        for(int loop = 0; loop < 87; loop++) str.append("*");
        str.append("\n");
        System.out.println(str.toString());
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(NTupleRow row : this.tupleRows){
            str.append(row.toString());
        }
        return str.toString();
    }
    
    public void addRow(double[] rowLine){
        NTupleRow row = new NTupleRow(rowLine);
        this.tupleRows.add(row);
    }
    
    public void addRow(String rowLine){
        NTupleRow row = new NTupleRow(rowLine);
        if(row.columns()!=this.tupleVariables.length){
            System.err.println("ERROR adding line : " + rowLine);
            System.err.println("Missmatch between number of columns and number of variables...");
            return;
        }
        this.tupleRows.add(row);
        //Tuple
    }
    
    public int varibleIndex(String variable){
        for(int loop = 0; loop < this.tupleVariables.length; loop++){
            if(variable.compareTo(this.tupleVariables[loop])==0) return loop;
        }
        return -1;
    }
    
    private void process(String[] variables, NTupleSelector selector){
        this.processResults.clear();
        ArrayList<Integer>  index = new ArrayList<Integer>();
        for(int loop = 0; loop < variables.length; loop++){
            this.processResults.add(new DataVector());
            index.add(this.varibleIndex(variables[loop]));
        }
        
        for(NTupleRow row : this.tupleRows){
            if(selector.isValid(row)==true){
                //vec.add(row.get(index));
                for(int loop = 0 ; loop < index.size();loop++){
                    if(index.get(loop)>=0){
                        this.processResults.get(loop).add(row.get(index.get(loop)));
                    }
                }
            }
        }        
    }
    
    
    public void draw(String var, String selection){
        //if(selection.length()<2) return this.getVector(var);
        String[] tokens = var.split(":");
        NTupleSelector selector = new NTupleSelector(selection,this.tupleVariables);
        this.process(tokens, selector);
        System.err.println(" VECTORS = " +  this.processResults.size() + "  LENGTH = " 
        + this.processResults.get(0).getSize());
    }
    
    public DataVector  getVector(String variable){
        DataVector vec = new DataVector();
        int index = this.varibleIndex(variable);
        if(index>=0){
            for(NTupleRow row : this.tupleRows){
                vec.add(row.get(index));
            }
        }
        return vec;
    }
    
    public H1D histogram1D(String var, String selection, int bins, double min, double max){
        H1D h = new H1D("H_"+var,bins,min,max);
        DataVector  vector  = this.getVector(var, selection);                    
        for(int loop = 0; loop < vector.getSize(); loop++){
            h.fill(vector.getValue(loop));
        }
        return h;
    }
    
    
    
    public H1D histogram1D(String var, String selection, int bins){
        DataVector  vector  = this.getVector(var, selection);
        H1D h = new H1D("H_"+var,bins,vector.getMin(),vector.getMax());
        for(int loop = 0; loop < vector.getSize(); loop++){
            h.fill(vector.getValue(loop));
        }
        return h;        
    }
    
    public H1D histogram1D(String var, String selection){
        return this.histogram1D(var, selection, 100);
    }
    
    
    public DataVector  getVector(String variable,String selection){
        if(selection.length()<2) return this.getVector(variable);
        NTupleSelector selector = new NTupleSelector(selection,this.tupleVariables);
        DataVector vec = new DataVector();
        int index = this.varibleIndex(variable);
        if(index>=0){
            for(NTupleRow row : this.tupleRows){
                if(selector.isValid(row)){
                    vec.add(row.get(index));
                }
            }
        }
        return vec;
    }

    public DefaultMutableTreeNode getTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("tuple");
        for(String item : this.tupleVariables){
            root.add(new DefaultMutableTreeNode(item));
        }
        return root;
    }

    public void draw(String obj, String selection, String options, RootCanvas canvas) {
        if(obj.contains("%")==true){
            String[] tokens = obj.split("%");
            System.out.println("NOT IMPLEMENTED YET to DRAW " + tokens[0] + " vs "
            + tokens[1]);
            DataVector  vecX = null;
            DataVector  vecY = null;
            if(selection.length()>0){
                vecX = this.getVector(tokens[1], selection);
                vecY = this.getVector(tokens[0], selection);
            } else {
                vecX = this.getVector(tokens[1]);
                vecY = this.getVector(tokens[0]);
            }

            H2D H = new H2D(obj,100,vecX.getMin(),vecX.getMax(),100,vecY.getMin(),vecY.getMax());
            H.setTitle(obj);
            H.setXTitle(tokens[1]);
            H.setYTitle(tokens[0]);
            
            for(int loop = 0; loop < vecX.size(); loop++){
                H.fill(vecX.getValue(loop), vecY.getValue(loop));
            }
            canvas.draw(canvas.getCurrentPad(), H);
            canvas.incrementPad();
            
        } else {
            System.out.println("NOT IMPLEMENTED YET to DRAW " + obj);
            int currentpad = canvas.getCurrentPad();
            DataVector  vec = null;
            if(selection.length()>0){
                vec = this.getVector(obj,selection);
            } else {
                vec = this.getVector(obj);//, selection);
            }
            System.out.println("VECTOR SIZE = " + vec.size() + " " + vec.getMin()
                    + " " + vec.getMax());
            H1D H = new H1D(obj,100,vec.getMin(),vec.getMax());
            for(int loop = 0; loop < vec.getSize(); loop++){
                H.fill(vec.getValue(loop));
            }
            canvas.draw(currentpad, H);
            canvas.incrementPad();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> getVariables() {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(this.tupleVariables));
        return list;
    }
    
    public String getTupleFormat(){
        StringBuilder str = new StringBuilder();
        for(String item : this.tupleVariables){
            str.append(item);
            str.append(":");
        }        
        return str.toString();
    }
    
    public void open(String filename){
        EvioInputStream inStream = new EvioInputStream();
        inStream.open(filename);
        TreeMap<Integer,Object> tree = inStream.getObjectTree(1);
        if(tree.get(1) instanceof int[]){
            int[] type = (int[]) tree.get(1);
            if(type[0]==100){
                byte[] formatBytes = (byte[]) tree.get(2);
                String format = new String(formatBytes);
                this.tupleRows.clear();
                this.init(format);
                int nentries = inStream.getEntries();
                for(int loop = 2; loop < nentries; loop++){
                    tree = inStream.getObjectTree(loop);
                    if(tree.get(1) instanceof double[]){
                        double[] array = (double[]) tree.get(1);
                        NTupleRow  row = new NTupleRow(array);
                        this.tupleRows.add(row);
                    }
                }
            }
        }
    }
    
    public void write(String filename){
        EvioOutputStream outStream = new EvioOutputStream(filename);
        String format = this.getTupleFormat();
        TreeMap<Integer,Object> tree = new TreeMap<Integer,Object>();
        byte[] varEntry = format.getBytes();
        int[]  fileType = new int[]{100};
        tree.put(1, fileType);
        tree.put(2, varEntry);
        outStream.writeTree(tree);
        
        tree.clear();
        for(int loop = 0; loop < this.tupleRows.size(); loop++){
            tree.clear();
            NTupleRow row = this.tupleRows.get(loop);
            double[] rowData = new double[row.columns()];
            //float[] rowData = new float[row.columns()];
            for(int nr = 0; nr < row.columns(); nr++){
                rowData[nr] =  row.get(nr);
            }
            tree.put(1, rowData);
            outStream.writeTree(tree);
        }
        outStream.close();
    }
}
