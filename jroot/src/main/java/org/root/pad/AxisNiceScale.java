/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.pad;

import java.util.ArrayList;

/**
 *
 * @author gavalian
 */
public class AxisNiceScale {
    
    private double minPoint;
    private double maxPoint;
    private double maxTicks = 10;
    private double tickSpacing;
    private double range;
    private double niceMin;
    private double niceMax;
    private ArrayList<Double> niceCoordinates = new ArrayList<Double>();
    private ArrayList<String> niceCoordinateLabels = new ArrayList<String>();
    /**
     * Instantiates a new instance of the NiceScale class.
     *
     * @param min the minimum data point on the axis
     * @param max the maximum data point on the axis
     */
    public AxisNiceScale(double min, double max) {
        this.minPoint = min;
        this.maxPoint = max;
        calculate();
    }
    
    public void setMinMaxPointsLog(double minPoint, double maxPoint){
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        calculateLog();
    }
    
    private void calculateLog(){
        this.range = Math.floor(Math.log10(this.maxPoint)) - Math.floor(Math.log10(this.minPoint));
        this.tickSpacing = this.range/(this.maxTicks-1);
        int minlog =  (int) Math.floor(Math.log10(this.minPoint));
        int maxlog =  (int) Math.floor(Math.log10(this.maxPoint));
        this.niceCoordinates.clear();
        this.range = maxlog - minlog;
        for(int loop = 0; loop < this.maxTicks;loop++){
            this.niceCoordinates.add(Math.pow(10, minlog+loop));
        }
    }
    /**
     * Calculate and update values for tick spacing and nice
     * minimum and maximum data points on the axis.
     */
    private void calculate() {
        this.range = niceNum(maxPoint - minPoint, false);
        this.tickSpacing = niceNum(range / (maxTicks - 1), true);
        this.niceMin =
                Math.floor(minPoint / tickSpacing) * tickSpacing;
        this.niceMax =
                Math.ceil(maxPoint / tickSpacing) * tickSpacing;
        niceCoordinates.clear();
        this.niceCoordinateLabels.clear();
        for(int loop = 0; loop < maxTicks; loop++){
            double numberX = niceMin + loop*this.tickSpacing;
            if(numberX<=this.maxPoint&&numberX>=this.minPoint)
                niceCoordinates.add(numberX);
        }
        
        int sigfig = this.getSigFig();
        String format = this.getStringFormat(sigfig+1);
        for(int loop = 0; loop < this.niceCoordinates.size();loop++){
            if(sigfig<0){
                this.niceCoordinateLabels.add(String.format("%.0f", 
                        this.niceCoordinates.get(loop)));
            } else {
                this.niceCoordinateLabels.add(String.format(format,
                        this.niceCoordinates.get(loop)));                
            }

        }
        //System.out.println(" SIG FIG = " + this.getSigFig());
    }
    public String getStringFormat(int sig){
        StringBuilder str = new StringBuilder();
        str.append('%');
        str.append('.');
        str.append(sig);
        str.append('f');
        return str.toString();
    }
    public ArrayList<String>  getCoordinatesLabels(){
        return this.niceCoordinateLabels;
    }
    
    public ArrayList<Double>  getCoordinates(){
        return niceCoordinates;
    }
    /**
     * Returns a "nice" number approximately equal to range Rounds
     * the number if round = true Takes the ceiling if round = false.
     *
     * @param range the data range
     * @param round whether to round the result
     * @return a "nice" number to be used for the data range
     */
    private double niceNum(double range, boolean round) {
        double exponent; /** exponent of range */
        double fraction; /** fractional part of range */
        double niceFraction; /** nice, rounded fraction */
        
        exponent = Math.floor(Math.log10(range));
        fraction = range / Math.pow(10, exponent);
        
        if (round) {
            if (fraction < 1.5)
                niceFraction = 1;
            else if (fraction < 3)
                niceFraction = 2;
            else if (fraction < 7)
                niceFraction = 5;
            else
                niceFraction = 10;
        } else {
            if (fraction <= 1)
                niceFraction = 1;
            else if (fraction <= 2)
                niceFraction = 2;
            else if (fraction <= 5)
                niceFraction = 5;
            else
                niceFraction = 10;
        }
        
        return niceFraction * Math.pow(10, exponent);
    }
    
    /**
     * Sets the minimum and maximum data points for the axis.
     *
     * @param minPoint the minimum data point on the axis
     * @param maxPoint the maximum data point on the axis
     */
    public void setMinMaxPoints(double minPoint, double maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        calculate();
    }
    
    /**
     * Sets maximum number of tick marks we're comfortable with
     *
     * @param maxTicks the maximum number of tick marks for the axis
     */
    public void setMaxTicks(double maxTicks) {
        this.maxTicks = maxTicks;
        calculate();
    }
    
    public int getSigFig(){
        double min = niceCoordinates.get(0);;
        double max = niceCoordinates.get(niceCoordinates.size()-1);
        double difference = max-min;
        int   placeOfDifference = (int) Math.floor(Math.log(difference) / Math.log(10));        
        //System.err.println(" AXIS = " + min + " " + max + " " + difference
        //+ "  " + placeOfDifference );
        return (int) - placeOfDifference;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(int loop = 0; loop < maxTicks; loop++)
            str.append(String.format("%12.4f", niceMin + loop*this.tickSpacing ));
        str.append("\n");
        return str.toString();    
    }
    
    public static void main(String[] args){
        AxisNiceScale axis = new AxisNiceScale(0.0,15.0);
        axis.setMaxTicks(8);
        System.err.println(axis);
    }
}
