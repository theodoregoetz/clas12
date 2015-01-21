package org.jlab.data.graph;

/**
 * Specifies the characteristics to create DataPoints object
 * 
 * @author Erin Kirby
 * @version 061714
 */
public class DataPoints {
    private String dataName = "";
    private String xaxisTitle = "";
    private String yaxisTitle = "";
    
    double[] xpoint;
    double[] ypoint;
    double[] xerror;
    double[] yerror;
    
    
    public DataPoints(String name, String xtitle, String ytitle){
        dataName   = name;
        xaxisTitle = xtitle;
        yaxisTitle = ytitle;
    }
    /**
     * Constructor that initializes an array to story 1 x and 1 y point as well
     * as their x and y errors.
     */
    public DataPoints() {
    	set(1);
    }
    
    public String getName(){ return dataName;}
    public String getXTitle(){ return xaxisTitle;}
    public String getYTitle(){ return yaxisTitle;}
    /**
     * Initializes an array to store the specified number 
     * of x and y points as well as their errors
     * 
     * @param size		the number of data points
     */
    public final void set(int size) {
    	xpoint = new double[size];
    	ypoint = new double[size];
    	xerror = new double[size];
    	yerror = new double[size];
    }
    
    /**
     * Sets the specified x and y point at the specified index
     * 
     * @param index		what index to set the point to, in array indexing format (0 <= index < array size)
     * @param x			the point's x coordinate
     * @param y			the point's y coordinate
     */
    public void setPoint(int index, double x, double y) {
        if((index >= 0) && (index < xpoint.length) && (index<ypoint.length)) {
            xpoint[index] = x;
            ypoint[index] = y;
        }
    }
    
    /**
     * Sets the specified x and y error coordinate to the specified index
     * @param index			the index to set the error to, in array indexing format (0 <= index < array size)
     * @param xe			the x coordinate of the error
     * @param ye			the y coordinate of the error
     */
    public void setPointError(int index, double xe, double ye) {
        if((index >= 0) && (index < xerror.length) && (index < yerror.length)) {
            xerror[index] = xe;
            yerror[index] = ye;
        }
    }
    
    /**
     * Returns the x coordinate at that index
     * 
     * @param index			the index to retrieve the x coordinate from, in array indexing format (0 <= index < array size)
     * @return				the x coordinate at that index
     */
    public double getX(int index) {
        if((index >= 0) && (index < xpoint.length)) {
        	return xpoint[index];
        }
        return 0.0;
    }
    
    /**
     * Returns the y coordinate at that index
     * 
     * @param index			the index to retrieve the y coordinate from, in array indexing format (0 <= index < array size)
     * @return				the y coordinate at that index
     */
    public double getY(int index) {
        if((index >= 0) && (index < ypoint.length)) {
        	return ypoint[index];
        }
        return 0.0;
    }
    
    /**
     * 
     * @return			The number of data points
     */
    public int getN() {
        return xpoint.length;
    }
    
    private double[] getArrayCopy(double[] array){
        double[] acopy = new double[array.length];
        for(int loop = 0; loop < array.length;loop++){
            acopy[loop] = array[loop];
        }
        return acopy;
    }
    
    public double[] getXVector()
    {
        return this.getArrayCopy(xpoint);
    }
    
    public double[] getYVector()
    {
        return this.getArrayCopy(ypoint);
    }
    
    public double[] getEXVector()
    {
        return this.getArrayCopy(xerror);
    }
    
    public double[] getEYVector()
    {
        return this.getArrayCopy(yerror);
    }
    /**
     * Returns the x coordinate of the error
     * 
     * @param index			The index to retrieve the x error from, in array indexing format (0 <= index < array size)
     * @return				The x coordinate of the error
     */
    public double getEX(int index) {
        if((index >= 0) && (index < xerror.length)) {
        	return xerror[index];
        }
        return 0.0;
    }
    
    /**
     * Returns the y coordinate of the error
     * 
     * @param index			The index to retrieve the y error from, in array indexing format (0 <= index < array size)
     * @return				The y coordinate of the error
     */
    public double getEY(int index) {
        if((index >= 0) && (index < yerror.length)) {
        	return yerror[index];
        }
        return 0.0;
    }
    
    /**
     * Sets the arrays as the data points to the object
     * 
     * @param xp		The array of x coordinates
     * @param yp		The array of y coordinates
     * @param xe		The array of x error coordinates
     * @param ye		The array of y error coordinates
     */
    public void setData(double[] xp, double[] yp, double[] xe, double[] ye) {
        xpoint = xp;
        ypoint = yp;
        xerror = xe;
        yerror = ye;
    }
}
