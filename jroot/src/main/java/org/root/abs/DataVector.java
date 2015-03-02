/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.root.abs;
import java.util.ArrayList;

/**
 *
 * @author gavalian
 * @param <T>
 */
public class DataVector<T extends Number> {
    
    private ArrayList<T> datavec         = new ArrayList<T>();
    private Boolean      isVectorOrdered = true;
        
    public void add(T value){
        /*
        if(this.isOrdered()==true){
            if(datavec.size()>0){
                if(value.getClass() == Double.class){
                    Double prev = (Double) datavec.get(datavec.size()-1);
                    Double next = (Double) value;
                    if(prev>next){
                        this.isVectorOrdered = false;
                    }
                }
                
                if(value.getClass() == Float.class){
                    Float prev = (Float) datavec.get(datavec.size()-1);
                    Float next = (Float) value;
                    if(prev>next){
                        this.isVectorOrdered = false;
                    }
                }
                
                if(value.getClass() == Integer.class){
                    Integer prev = (Integer) datavec.get(datavec.size()-1);
                    Integer next = (Integer) value;
                    if(prev>next){
                        this.isVectorOrdered = false;
                    }
                }
                
                if(value.getClass() == Short.class){
                    Short prev = (Short) datavec.get(datavec.size()-1);
                    Short next = (Short) value;
                    if(prev>next){
                        this.isVectorOrdered = false;
                    }
                }                
                //if(prev.){
                //} else {  
                //}
            }
        }*/
        /*
        if(datavec.size()>0){
            if(datavec.get(datavec.size()-1)>value){
                
            }
        }*/
        
        datavec.add(value);
    }
    
    public void clear(){
        datavec.clear();
    }
    
    public double getIntegral(){
        double sum = 0.0;
        for(Number n : this.datavec){
            sum += n.doubleValue();
        }
        //if(T  == Double.class){          
        //        }
        return sum;
    }
    
    public boolean isOrdered(){
        return this.isVectorOrdered;
    }
    
    public void show(){
        //System.out.print(" VEC : " + this.getClass().getGenericInterfaces()[0] + "\n");
        //System.out.println(((ParameterizedType) getClass()
          //                  .getGenericSuperclass()).getActualTypeArguments()[0]);
        for(int loop = 0; loop < datavec.size(); loop++){
            System.out.print("  " + datavec.get(loop).toString());
        }
        System.out.println();
    }
    
    public static void main(String[] args){
        //int ntries = Integer.parseInt(args[0]);
        
        DataVector<Double> vec = new DataVector<Double>();
        /*
        for(int n = 2 ; n < ntries; n++){
            //DataVector<Double> vec = new DataVector<Double>();
            vec.clear();
            for(int loop = 0 ; loop < n; loop++){
                vec.add((loop+4)*0.5);
            }
        }*/
        //vec.clear();
        //vec.show();
        
        vec.add(1.0);
        vec.add(4.0);
        vec.add(3.0);
        vec.show();
        System.out.println("ORDERED = " + vec.isOrdered());
        System.out.println(vec.getIntegral());
    }
}
