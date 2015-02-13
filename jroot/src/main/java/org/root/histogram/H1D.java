package org.root.histogram;

import java.util.TreeMap;
import org.root.base.EvioWritableTree;
import org.root.data.DataSetXY;
import org.root.data.StatNumber;
import org.root.fitter.DataFitter;
import org.root.func.F1D;


/**
 * Defines the class to create a basic 1D Histogram
 * 
 * @author Erin Kirby
 * @version 062614
 */
public class H1D implements EvioWritableTree {

    Axis  xAxis;
    Axis  yAxis;
    double[]   histogramData;
    double[]   histogramDataError;
    String     histTitle = "";
    String     histXTitle = "";
    String     histYTitle = "";
    String     histName  = "";
    
    /**
     * The default constructor, which creates a Histogram1D object with the Name "default", 
     * the Title "default", no Axis titles, and sets the minimum xAxis value to 0, the maximum x
     *  value to 1, and creates 1 bin. 
     */
    public H1D() {
    	setName("default");
    	setTitle("default");
    	set(1,0.0,1.0);
    	initDataStore(1);
    }
    
    /**
     * Creates a Histogram1D object using the specified name, axis titles, number of bins, 
     * and minimum and maximum x axis values.
     * 
     * @param hName		the desired name of the 1-D Histogram
     * @param xTitle	the desired x-axis title
     * @param yTitle	the desired y-axis title
     * @param bins		the desired number of bins
     * @param xMin		the desired minimum value on the x axis
     * @param xMax		the desired maximum value on the x axis
     */
    public H1D(String hName, String xTitle, String yTitle, int bins, double xMin, double xMax) {    	
    	setName(hName);
    	set(bins, xMin, xMax);
    	initDataStore(bins);
        setXTitle(xTitle);
        setYTitle(yTitle);
    }
    
    /**
     * Creates a 1-D Histogram with the specified name, minimum and maximum x-axis values,
     * and the bin heights
     * 
     * @param name			the desired name of the histogram
     * @param xMin			the desired minimum x-axis value
     * @param xMax			the desired maximum x-axis value
     * @param binHeights	a double array of the heights of the bins
     */
    public H1D(String name, double xMin, double xMax, double[] binHeights) {
    	setName(name);
    	set(binHeights.length, xMin, xMax);
    	for (int i = 0; i < binHeights.length; i++) {
    		histogramData[i] = binHeights[i];
    	}
    }
    
    /**
     * Creates a 1-D Histogram with the specified name, number of bins, and minimum and maximum
     * x-axis values
     * 
     * @param name		The desired name of the histogram
     * @param bins		The desired number of bins
     * @param xMin		The desired minimum x-axis value
     * @param xMax		The desired maximum x-axis value
     */
    public H1D(String name, int bins, double xMin, double xMax) {
    	setName(name);
    	set(bins, xMin, xMax);
    }
    
    /**
     * Creates a 1-D Histogram with the specified name, title, number of bins, and minimum
     * and maximum x-axis values
     * 
     * @param name		The desired name of the histogram
     * @param title		The desired title of the histogram
     * @param bins		The desired number of bins
     * @param xMin		The desired minimum x-axis value
     * @param xMax		The desired maximum x-axis value
     */
    public H1D(String name, String title, int bins, double xMin, double xMax) {
    	setName(name);
    	setTitle(title);
    	set(bins, xMin, xMax);
    }
    
    /**
     * Creates a new histogram using the name, title, minimum, and maximum.
     * 
     * @param name		The desired name of the histogram
     * @param title		The desired title of the histogram
     * @param xMin		The desired minimum x-axis value
     * @param xMax		The desired maximum x-axis value
     */
    public H1D(String name, String title, double xMin, double xMax) {
    	setName(name);
    	setTitle(title);
    	set((int)(xMax - xMin), xMin, xMax);
    }
    
    /**
     * Sets the x-axis title to the specified parameter
     * @param xTitle		The desired title of the x-axis
     */
    public final void setXTitle(String xTitle) {
    	this.getXaxis().setTitle(xTitle);
    }
    
    /**
     * Sets the y-axis title to the specified parameter
     * 
     * @param yTitle		The desired title of the y-axis
     */
    public final void setYTitle(String yTitle) {
        this.getYaxis().setTitle(yTitle);
    }
    
    /**
     * The getter for the histogram title.
     * @return Title of the histogram.
     */
    public String getTitle(){
        return this.histTitle;
    }
    /**
     * The getter for the x-axis title.
     * 
     * @return		The title of the x-axis as a string
     */
    public String getXTitle() {
    	return this.getXaxis().getTitle();
    }
    
    /**
     * The getter for the y-axis title.
     * 
     * @return		The title of the y-axis as a string
     */
    public String getYTitle() {
    	return this.getYaxis().getTitle();
    }
    
    /**
     * Sets the specified parameter as the title of the histogram
     * 
     * @param title		The desired title of the histogram
     */
    public final void setTitle(String title) {
    	histTitle = title;
    }
    
    /**
     * Sets the specified parameter as the name of the histogram
     * 
     * @param name		The desired name of the histogram
     */
    public final void setName(String name) {
    	histName = name;
    }
    
    /**
     * Returns the title of the histogram
     * 
     * @return		the title of the histogram as a string
     */
    public String title() {
    	return histTitle;
    }
    
    /**
     * Returns the name of the histogram
     * 
     * @return		the name of the histogram as a string
     */
    public String name() {
    	return histName;
    }
    
    public int getEntries(){
        int entries = 0;
        for(int loop = 0; loop < this.histogramData.length; loop++){
            entries += (int) this.histogramData[loop];
        }
        return entries;
    }
    /**
     * Calculates the mean of the data in the histogram
     * 
     * @return		the mean of the histogram data as a double
     */
    public double getMean() {
        double mean  = 0.0;
        double summ  = 0.0;
        int    count = 0; 
        for(int i = 0; i < this.getAxis().getNBins(); i++){
            double bincontent =  this.getBinContent(i);
            //System.err.println(" bin count = " + count + " content summ = " + bincontent);
            if(bincontent!=0){
                summ  += this.getAxis().getBinCenter(i)*this.getBinContent(i);
                count += this.getBinContent(i);
            }
        }
        
        if(count!=0){
            mean = summ/count;
        }
        
        return mean;
    }
    
    public String[] getStatText(){
        String[] lines = new String[4];
        lines[0] = this.histName;
        lines[1] = String.format("%-10s %9d", "Entries",this.getEntries());
        lines[2] = String.format("%-10s %9.2f", "Mean",this.getMean());
        lines[3] = String.format("%-10s %9.2f", "RMS",this.getRMS());        
        //lines[1] =;
        return lines;
    }
    /**
     * Calculates the root mean square of the histogram data
     * 
     * @return		the root mean square of the histogram data
     */
    public double getRMS() {
        double mean = this.getMean();
        double rms = 0.0;
        double summ  = 0.0;
        int    count = 0; 
        for(int i = 0; i < this.getAxis().getNBins(); i++){
            int bincontent = (int) this.getBinContent(i);
            if(bincontent!=0){
                double variance = this.getAxis().getBinCenter(i) - mean;
                summ  += variance*variance*this.getBinContent(i);
                count += (int) this.getBinContent(i);
            }
        }
        if(count!=0) {
            rms = summ/count;
            return Math.sqrt(rms);
        }
        return rms;
    }
    
    /**
     * Sets the specified number of bins, min and max to the x axis
     * and creates a standard Y axis with a min value of 0 and a max value
     * of 0. Additionally, sets up the axes to store data.
     * 
     * @param bins		the desired number of bins.
     * @param min		the desired minimum x value
     * @param max		the desired maximum y value
     */
    public final void set(int bins, double min, double max) {
    	xAxis = new Axis(bins, min, max);
    	yAxis = new Axis();
    	initDataStore(bins);
    }
    
    /**
     * Initializes the double arrays for the histogram data and data errors.
     * 
     * @param size 		the number of data points to store
     */
    final void initDataStore(int size)
    {
        histogramData      = new double[size];
        histogramDataError = new double[size];
    }
    
    public void fit(F1D func){
        this.fit(func, "*");
    }
    
    public void fit(F1D func, String options){
        DataFitter.fit(this.getDataSet(), func);
        if(options.contains("Q")==false){
            func.show();
        }
    }
    /**
     * Increments the bin corresponding to that value by 1
     * 
     * @param value		the value to increment
     */
    public void fill(double value) {
    	incrementBinContent(xAxis.getBin(value));
    }
    
    /**
     * Increments the bin corresponding to that value by that weight
     * 
     * @param value		the value to increment
     * @param weight	the weight to increment by
     */
    public void fill(double value, double weight) {
    	incrementBinContent(xAxis.getBin(value), weight);
    }
    
    /**
     * Normalizes the histogram data to the specified number
     * 
     * @param number		the value to normalize the data to
     */
    public void normalize(double number) {
        for(int i = 0; i < histogramData.length; i++)
        {
            histogramData[i] /= number;
        }
    }
    
    /**
     * Increments the content in the specified bin by one. The bin is specified in array indexing
     * format (to increment the 1st bin, enter 0, the 2nd, enter 1, ... , the nth, enter n-1)
     * 
     * @param bin		the bin to be incremented, specified in array indexing format.
     */
    public void incrementBinContent(int bin) {
    	if (bin >= 0 && bin < histogramData.length) {
    		histogramData[bin] = histogramData[bin] + 1.0;
    		histogramDataError[bin] = Math.sqrt(Math.abs(histogramData[bin]));
    	}
    }
    
    /**
     * Increments the content in the specified bin by the entered weight. The bin is specified in array indexing
     * format (to increment the 1st bin, enter 0, the 2nd, enter 1, ... , the nth, enter n-1)
     * 
     * @param bin		the bin to be incremented, specified in array indexing format.
     * @param weight	the weight to increment by
     */
    public void incrementBinContent(int bin, double weight) {
    	if (bin >= 0 && bin < histogramData.length) {
    		histogramData[bin] = histogramData[bin] + weight;
    		histogramDataError[bin] = Math.sqrt(Math.abs(histogramData[bin]));
    	}
    }
    
    /**
     * Divides the current histogram object by the parameter 1-D histogram. 
     * Requires that both histograms have the same number of bins.
     * 
     * @param hist		the 1-D histogram object to divide the current object by
     */
    public void divide(H1D hist) {
    	if(hist.getAxis().getNBins() !=this.getAxis().getNBins())
        {
            System.out.println("ERROR: inconsistent bins in histograms");
            return;
        }
        
        StatNumber result = new StatNumber();
        StatNumber hdiv   = new StatNumber();
        for(int i = 0; i < this.getAxis().getNBins(); i++)
        {
            result.set(this.getBinContent(i), this.getBinError(i));
            hdiv.set(hist.getBinContent(i), hist.getBinError(i));
            result.divide(hdiv);
            this.setBinContent(i, result.number());
            this.setBinError(i, result.error());
        }
    }
    
    /**
     * Sets the value to the specified bin. The bin is specified in array indexing format 
     * (i.e. to set the value to the 1st bin, enter 0, to the 2nd bin, enter 1, ... , 
     * the nth bin, enter n-1)
     * 
     * @param bin		the bin to enter the value into, specified in array indexing format
     * @param value		the value to store in the specified bin
     */
    public void setBinContent(int bin, double value) {
    	if ((bin >= 0) && (bin < histogramData.length)) {
    		histogramData[bin] = value;
    		histogramDataError[bin] = Math.sqrt(Math.abs(histogramData[bin]));
    	}
    }
    /**
     * returns a copy of the histogram with different name.
     * @param name
     * @return 
     */
    public H1D histClone(String name){
        H1D hclone = new H1D(name, this.histXTitle, this.histYTitle,
        this.xAxis.getNBins(),this.xAxis.min(),this.xAxis.max());
        for(int loop = 0; loop < this.xAxis.getNBins(); loop++){
            hclone.setBinContent(loop, this.getBinContent(loop));
            hclone.setBinError(loop, this.getBinError(loop));
        }
        return hclone;
    }
    /**
     * Sets the bin error to the specified bin. The bin is specified in array indexing format 
     * (i.e. to set the value to the 1st bin, enter 0, to the 2nd bin, enter 1, ... , 
     * the nth bin, enter n-1)
     * 
     * @param bin		the bin to enter the value of the error into, specified in array indexing format
     * @param value		the error to store in the specified bin
     */
    public void setBinError(int bin, double value) {
    	if (bin >= 0 && bin < histogramDataError.length) {
    		histogramDataError[bin] = value;
    	}
    }
    
    /**
     * Returns the content of the specified bin as a double. The bin is defined in array indexing
     * format (i.e. to retrieve the 1st bin's content, enter 0, for the 2nd bin, enter 1, ... , 
     * for the nth bin, enter n-1)
     * 
     * @param bin		The bin to retrieve the content of, specified in array indexing format
     * @return			The content of the bin entered as a parameter
     */
    public double getBinContent(int bin) {
    	if ((bin >= 0) && (bin < histogramData.length)) {
    		return histogramData[bin];
    	}
    	return 0.0;
    }
    
    /**
     *Returns the error of the specified bin as a double. The bin is defined in array indexing
     * format (i.e. to retrieve the 1st bin's error, enter 0, for the 2nd bin, enter 1, ... , 
     * for the nth bin, enter n-1)
     * 
     * @param bin		The bin to retrieve the error of, specified in array indexing format
     * @return			The error of the bin entered as a parameter
     */
    public double getBinError(int bin) {
        if(bin >= 0 && bin < histogramDataError.length) {
            return histogramDataError[bin];
        }
        return 0.0;
    }
    
    public Axis getXaxis(){return this.xAxis;}
    public Axis getYaxis(){ return this.yAxis;}
    /**
     * Retrieves the x-axis as an Axis object
     * 
     * @return the x-axis of the histogram as an Axis object
     */
    public Axis getxAxis() {
        return xAxis;
    }
    
    /**
     * Retrieves the y-axis as an Axis object
     * 
     * @return the y-axis of the histogram as an Axis object
     */
    public Axis getyAxis() {
        return yAxis;
    }
    
    /**
     * Retrieves the x-axis as an Axis object
     * 
     * @return the x-axis of the histogram as an Axis object
     */
    public Axis getAxis() {
        return xAxis;
    }
    
    /**
     * Overrides the toString method of type Object
     * 
     * @return		a formatted string representation of the content in the histogram
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for(int i = 0; i < xAxis.getNBins(); i++) {
            buffer.append(String.format("%12.6f %12.6f %12.6f\n",
                    xAxis.getBinCenter(i),this.getBinContent(i),
                    this.getBinError(i)));
        }
        return buffer.toString();
    }       
    
    public DataSetXY getDataSet(){
        DataSetXY dataset = new DataSetXY(this.getAxis().getBinCenters(),
                this.getData());
        return dataset;
    }
    /**
     * Retrieves a graph of the histogram
     * 
     * @return a DataPoints object of the histogram data
     */
    public GraphErrors getGraph(){
        GraphErrors  graph = new GraphErrors(this.getAxis().getBinCenters(),
                this.getData());
        return graph;
    }
    
    /*
    public DataPoints getGraph() {
        DataPoints graph = new DataPoints(this.histName+"_graph",this.histXTitle,this.histYTitle);
        int npoints = this.getAxis().getNBins();
        
        graph.set(npoints);
        for(int i = 0; i < npoints; i++) {
            graph.setPoint(i, 
                    this.getAxis().getBinCenter(i), 
                    this.getBinContent(i));
        }
        return graph;
    }*/
    
    /**
     * 
     * @return		the data in the histogram
     */
    public double[] getData() {
    	return histogramData;
    }
    
    /**
     * 
     * @return		the data error in the histogram
     */
    public double[] getDataError() {
    	return histogramDataError;
    }
    /**
     * Returns bin number with maximum entries.
     * @return 
     */
    public int getMaximumBin(){
        int bin = 0;
        double max = this.histogramData[0];
        for(int loop = 0; loop < this.histogramData.length; loop++){
            if(this.histogramData[loop]>max){
                max = this.histogramData[loop];
                bin = loop;
            }
        }
        return bin;
    }
    /**
     * Changes the bin widths to vary with a set minimum slope to 
     * be allowed as its own bin.
     * 
     * @param	sensitivity			what percentage of the maximum slope
     * 								all bins have to be at minimum to be 
     * 								considered its own distinct bin
     */
    public void fixBinWidths(double sensitivity) {
    	double maxSlope = Math.abs(histogramData[1] - histogramData[0]);
    	for (int i = 1; i < histogramData.length - 1; i++) {
    		double slope = Math.abs(histogramData[i+1] - histogramData[i]);
    		if (slope > maxSlope) {
    			maxSlope = slope;
    		}
    	}
    	
    	double minSlope = maxSlope * sensitivity; //allows bin slope to
    									  		  //be as little as that 
    											  //magnitude of the max
    	double[] histData = new double[histogramData.length];
    	double[] histMargins = new double[xAxis.axisMargins.length];
    	
    	for (int i = 0; i < histData.length; i++) {
    		histData[i] = -1.0;
    	}
    	
    	histData[0] = histogramData[0];
    	histMargins[0] = xAxis.axisMargins[0];
    	int index = 0;
    	for (int i = 0; i < histogramData.length - 1; i++) {
    		if (Math.abs(histogramData[i+1] - histogramData[i]) < minSlope) {
    			if (histData[index] == -1.0) {
    				histData[index] = 0.0;
    			}
    			histData[index] += histogramData[i+1];
    			histMargins[index+1] = xAxis.axisMargins[i+2];
    		}
    		
    		else {
    			histData[index+1] = histogramData[i+1];
    			histMargins[index+1] = xAxis.axisMargins[i+1];
    			index++;
    		}
    	}
    	
    	set(index + 1, xAxis.min(), xAxis.max());
    	
    	for (int i = 0; i < histogramData.length; i++) {
    		setBinContent(i, histData[i]);
    		xAxis.set(histMargins);
    	}
    	
    	for (int i = 0; i < histogramData.length - 1; i++) {
    		if (Math.abs(histogramData[i+1] - histogramData[i]) < minSlope) {
    			fixBinWidths(sensitivity);
    			break;
    		}
    	}
    }
    
    @Override
    public TreeMap<Integer, Object> toTreeMap() {
        TreeMap<Integer, Object> hcontainer = new TreeMap<Integer, Object>();
        hcontainer.put(1, new int[]{1});        
        hcontainer.put(2, new int[]{this.getxAxis().getNBins()});
        hcontainer.put(3, new double[]{this.getxAxis().min(),this.getxAxis().max()});
        hcontainer.put(4, this.histogramData);
        hcontainer.put(5, this.histogramDataError);
        byte[] nameBytes = this.histName.getBytes();
        hcontainer.put(6, nameBytes);
        return hcontainer;
    }

    @Override
    public void fromTreeMap(TreeMap<Integer,Object> map) {
        if(map.get(1) instanceof int[]){
            if(  ((int[]) map.get(1))[0]==1){
                int    nbins    = ((int[]) map.get(2))[0];
                double binsmin  = ((double[]) map.get(3))[0];
                double binsmax  = ((double[]) map.get(3))[1];
                byte[] name     = (byte[]) map.get(6);
                histName = new String(name);                
                this.set(nbins, binsmin, binsmax);
                double[] binc = (double[]) map.get(4);
                double[] bine = (double[]) map.get(5);
                for(int loop = 0; loop < nbins; loop++){
                    histogramData[loop] = binc[loop];
                    histogramDataError[loop] = bine[loop];
                }
            }
        }
    }
}
