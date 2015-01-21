package org.jlab.data.histogram;

import java.util.TreeMap;
import org.jlab.data.base.EvioWritableTree;
import org.jlab.data.base.MultiIndex;

/**
 * Specifies the definition for creating and modifying 3-D Histograms.
 * 
 * @author Erin Kirby
 * @version 061714
 */
public class H3D implements EvioWritableTree {
	
	private String hName = "basic3D";
	private Axis xAxis = new Axis();
	private Axis yAxis = new Axis();
	private Axis zAxis = new Axis();
	private double[] hBuffer = new double[1];
	private MultiIndex offset = new MultiIndex();
	
	/**
	 * Creates an empty 3-D Histogram with the specified name.
	 * 
	 * @param name		The name of the 3-D Histogram
	 */
	public H3D(String name) {
		hName = name;
	}
	
	/**
	 * Creates a 3-D Histogram with the specified parameters.
	 * 
	 * @param name			The name of the histogram
	 * @param bx			The number of x bins
	 * @param xMin			The minimum x value
	 * @param xMax			The maximum x value
	 * @param by			The number of y bins
	 * @param yMin			The minimum y value
	 * @param yMax			The maximum y value
	 * @param bz			The number of z bins
	 * @param zMin			The minimum z value
	 * @param zMax			The maximum z value
	 */
	public H3D(String name, int bx, double xMin, double xMax, int by,
			double yMin, double yMax, int bz, double zMin, double zMax) {
		hName = name;
		this.set(bx, xMin, xMax, by, yMin, yMax, bz, zMin, zMax);
	}
	
	/**
	 * Sets the values to the 3-D histogram object
	 * 
	 * @param bx			The number of x bins
	 * @param xMin			The minimum x value
	 * @param xMax			The maximum x value
	 * @param by			The number of y bins
	 * @param yMin			The minimum y value
	 * @param yMax			The maximum y value
	 * @param bz			The number of z bins
	 * @param zMin			The minimum z value
	 * @param zMax			The maximum z value
	 */
	public final void set(int bx, double xMin, double xMax, 
			int by, double yMin, double yMax, int bz, double zMin, double zMax) {
		xAxis.set(bx, xMin, xMax);
		yAxis.set(by, yMin, yMax);
		zAxis.set(bz, zMin, zMax);
		offset = new MultiIndex(bx, by, bz);
		int buff = offset.getArraySize();
		hBuffer = new double[buff];
	}
	
	/**
	 * Gets the name of the 3-D Histogram
	 * 
	 * @return		the name of the 3-D Histogram
	 */
	public String getName() {
		return hName;
	}
	
	/**
	 * Gets the x-axis of the histogram
	 * 
	 * @return		the x-axis of this histogram
	 */
	public Axis getXAxis() {
		return xAxis;
	}
	
	/**
	 * Gets the y-axis of the histogram
	 * 
	 * @return		the y-axis of this histogram
	 */
	public Axis getYAxis() {
		return yAxis;
	}
	
	/**
	 * Gets the z-axis of the histogram
	 * 
	 * @return		the z-axis of the histogram
	 */
	public Axis getZAxis() {
		return zAxis;
	}
	
	/**
	 * Checks if the bins entered are valid and exist in the histogram
	 * 
	 * @param bx		the x-axis bin
	 * @param by		the y-axis bin
	 * @param bz		the z-axis bin
	 * @return			A boolean indicating if the bin combination is valid
	 */
	private boolean isValidBins(int bx, int by, int bz) {
		if ((bx >= 0 && bx <= xAxis.getNBins()) && (by >= 0 && by <= yAxis.getNBins())
				&& (bz >= 0 && bz <= zAxis.getNBins())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the bin content at that coordinate
	 * @param bx		the x-axis bin
	 * @param by		the y-axis bin
	 * @param bz		the z-axis bin
	 * @return			The content stored at that bin
	 */
	public double getBinContent(int bx, int by, int bz) {
		if (this.isValidBins(bx,  by,  bz)) {
			int buff = offset.getArrayIndex(bx, by, bz);
			return hBuffer[buff];
		}
		return 0.0;
	}
	
	/**
	 * Sets the specified value to that bin
	 * @param bx		the x-axis bin
	 * @param by		the y-axis bin
	 * @param bz		the z-axis bin
	 * @param w			The value to set to that bin
	 */
	public void setBinContent(int bx, int by, int bz, double w) {
		if (this.isValidBins(bx, by, bz)) {
			int buff = offset.getArrayIndex(bx, by, bz);
			hBuffer[buff] = w;
		}
	}
	
	/**
	 * Finds the bin with that value and increments it by 1.0
	 * @param x		the x value
	 * @param y		the y value
	 * @param z		the z value
	 */
	public void fill (double x, double y, double z) {
		int bin = this.findBin(x, y, z);
		if (bin >= 0) {
			this.addBinContent(bin);
		}
	}
	
	/**
	 * Finds the bins with those values and increments it by the specified weight
	 * 
	 * @param x		the x-axis value
	 * @param y		the y-axis value
	 * @param z		the z-axis value
	 * @param w		the weight to increment the bin by
	 */
	public void fill(double x, double y, double z, double w) {
		int bin = this.findBin(x, y, z);
		if (bin >= 0) {
			this.addBinContent(bin, w);
		}
	}
	
	
	private void addBinContent(int bin) {
		hBuffer[bin] = hBuffer[bin] + 1.0;
	}
	
	
	private void addBinContent(int bin, double w) {
		hBuffer[bin] = hBuffer[bin] + w;
	}
	
	/**
	 * Finds the bin with those values
	 * 
	 * @param x		the x-axis value
	 * @param y		the y-axis value
	 * @param z		the z-axis value
	 * @return		the bin with those values
	 */
	public int findBin(double x, double y, double z) {
		int bx = xAxis.getBin(x);
		int by = yAxis.getBin(y);
		int bz = zAxis.getBin(z);
		if (this.isValidBins(bx, by, bz)) {
			return (offset.getArrayIndex(bx, by, bz));
		}
		return -1;
	}
	
	/**
	 * Creates a 3-D array with the bin content
	 * 
	 * @return		a 3-D array with the bin content
	 */
	public double[][][] getContentBuffer() {
		double[][][] buff = new double[xAxis.getNBins()+1][yAxis.getNBins()+1][zAxis.getNBins()+1];
		for (int zloop = 0; zloop <= zAxis.getNBins(); zloop++) {
			for (int yloop = 0; yloop <= yAxis.getNBins(); yloop++) {
				for (int xloop = 0; xloop <= xAxis.getNBins(); xloop++) {
					buff[xloop][yloop][zloop] = this.getBinContent(xloop, yloop, zloop);
				}
			}
		}
		return buff;
	}
	
	/**
	 * Creates a 3-D array with the bin error content 
	 * (specifically all the errors are 0.0 right now)
	 * 
	 * @return		A 3-D array with 0.0 for every element
	 */
	public double[][][] getErrorBuffer() {
		double[][][] buff = new double[xAxis.getNBins()+1][yAxis.getNBins()+1][zAxis.getNBins()+1];
		for (int zloop = 0; zloop <= zAxis.getNBins(); zloop++) {
			for (int yloop = 0; yloop <= yAxis.getNBins(); yloop++) {
				for (int xloop = 0; xloop <= xAxis.getNBins(); xloop++) {
					buff[xloop][yloop][zloop] = 0.0;
				}
			}
		}
		return buff;
	}
	
	/**
	 * Creates a 3-D Histogram of the indicated region
	 * 
	 * @param name			The name of the new 3-D Histogram region
	 * @param bx_start		The beginning x bin of the region
	 * @param bx_end		The ending x bin of the region
	 * @param by_start		The beginning y bin of the region
	 * @param by_end		The ending x bin of the region
	 * @param bz_start		The beginning z bin of the region
	 * @param bz_end		The ending z bin of the region
	 * @return				A new 3-D Histogram of just the specified region
	 */
	public H3D getRegion(String name, int bx_start, int bx_end,
			int by_start, int by_end, int bz_start, int bz_end) {
		double xBinWidth = xAxis.getBinWidth(bx_start);
		double newXMin = xAxis.min() + (xBinWidth * bx_start);
		double newXMax = xAxis.min() + (xBinWidth * bx_end);

		double yBinWidth = yAxis.getBinWidth(by_start);
		double newYMin = yAxis.min() + (yBinWidth * by_start);
		double newYMax = yAxis.min() + (yBinWidth * by_end);
		
		double zBinWidth = zAxis.getBinWidth(bz_start);
		double newZMin = zAxis.min() + (zBinWidth * bz_start);
		double newZMax = zAxis.min() + (zBinWidth * bz_end);
		H3D regHist = new H3D(name, bx_end - bx_start, newXMin, newXMax,
				by_end - by_start, newYMin, newYMax, 
				bz_end - bz_start, newZMin, newZMax);
		
		double content = 0.0;
		for (int z = bz_start; z < bz_end; z++) {
			for (int y = by_start; y < by_end; y++) {
				for (int x = bx_start; x < bx_end; x++) {
					content = this.getBinContent(x, y, z);
					regHist.setBinContent(x, y, z, content);
				}
			}
		}
		return regHist;
	}
	
	/**
	 * Adds up all the bins on the z-axis and creates a projection of the 3-D Histogram
	 * onto the X-Y axis
	 * 
	 * @return		a 2-D projection of the 3-D Histogram on the X-Y axis
	 */
	public H2D projectionXY() {
		String name = "X-Y Projection";
		int xNum = xAxis.getNBins() + 1;
		double xMin = xAxis.min();
		double xMax = xAxis.max();
		int yNum = yAxis.getNBins() + 1;
		double yMin = yAxis.min();
		double yMax = yAxis.max();
		H2D projXY = new H2D(name, xNum, xMin, xMax, yNum, yMin, yMax);
		double height = 0.0;
		for (int x = 0; x <= xAxis.getNBins(); x++) {
			height = 0.0;
			for (int y = 0; y <= yAxis.getNBins(); y++) {
				height = 0.0;
				for (int z = 0; z<= zAxis.getNBins(); z++) {
					height += this.getBinContent(x, y, z);
				}
				projXY.setBinContent(x, y, height);
			}
		}
		return projXY;
	}
	
	/**
	 * Adds up all the bins on the y-axis and creates a projection of the 3-D Histogram
	 * onto the X-Z axis
	 * 
	 * @return		a 2-D projection of the 3-D Histogram on the X-Z axis
	 */
	public H2D projectionXZ() {
		String name = "X-Z Projection";
		int xNum = xAxis.getNBins() + 1;
		double xMin = xAxis.min();
		double xMax = xAxis.max();
		int zNum = zAxis.getNBins() + 1;
		double zMin = zAxis.min();
		double zMax = zAxis.max();
		H2D projXZ = new H2D(name, xNum, xMin, xMax, zNum, zMin, zMax);
		double height = 0.0;
		for (int x = 0; x <= xAxis.getNBins(); x++) {
			height = 0.0;
			for (int z = 0; z <= zAxis.getNBins(); z++) {
				height = 0.0;
				for (int y = 0; y<= yAxis.getNBins(); y++) {
					height += this.getBinContent(x, y, z);
				}
				projXZ.setBinContent(x, z, height);
			}
		}
		return projXZ;
	}
	
	/**
	 * Adds up all the bins on the x-axis and creates a projection of the 3-D Histogram
	 * onto the Y-Z axis
	 * 
	 * @return		a 2-D projection of the 3-D Histogram on the Y-Z axis
	 */
	public H2D projectionYZ() {
		String name = "Y-Z Projection";
		int yNum = yAxis.getNBins() + 1;
		double yMin = yAxis.min();
		double yMax = yAxis.max();
		int zNum = zAxis.getNBins() + 1;
		double zMin = zAxis.min();
		double zMax = zAxis.max();
		H2D projYZ = new H2D(name, yNum, yMin, yMax, zNum, zMin, zMax);
		double height = 0.0;
		for (int y = 0; y <= yAxis.getNBins(); y++) {
			height = 0.0;
			for (int z = 0; z <= zAxis.getNBins(); z++) {
				height = 0.0;
				for (int x = 0; x <= xAxis.getNBins(); x++) {
					height += this.getBinContent(x, y, z);
				}
				projYZ.setBinContent(y, z, height);
			}
		}
		return projYZ;
	}
	
	/**
	 * Creates a slice on the X-Y axis of the specified x bin
	 * 
	 * @param zBin		the bin on the z-axis to slice on
	 * @return			a 2-D Histogram of the X-Y axis on the entered z-axis bin
	 */
	public H2D sliceXY(int zBin) {
		String name = "X-Y Slice of " + zBin + " Z Bin";
		int xNum = xAxis.getNBins();
		double xMin = xAxis.min();
		double xMax = xAxis.max();
		int yNum = yAxis.getNBins();
		double yMin = yAxis.min();
		double yMax = yAxis.max();
		H2D xy = new H2D(name, xNum, xMin, xMax, yNum, yMin, yMax);
		
		for (int x = 0; x < xNum; x++) {
			for (int y = 0; y < yNum; y++) {
				xy.setBinContent(x, y, this.getBinContent(x, y, zBin));
			}
		}
		
		return xy;
	}
	
	/**
	 * Creates a slice on the X-Z axis of the specified x bin
	 * 
	 * @param yBin		the bin on the y-axis to slice on
	 * @return			a 2-D Histogram of the X-Z axis on the entered y-axis bin
	 */
	public H2D sliceXZ(int yBin) {
		String name = "X-Z Slice of " + yBin + " Y Bin";
		int xNum = xAxis.getNBins();
		double xMin = xAxis.min();
		double xMax = xAxis.max();
		int zNum = zAxis.getNBins();
		double zMin = zAxis.min();
		double zMax = zAxis.max();
		H2D xz = new H2D(name, xNum, xMin, xMax, zNum, zMin, zMax);
		
		for (int x = 0; x < xNum; x++) {
			for (int z = 0; z < zNum; z++) {
				xz.setBinContent(x, z, this.getBinContent(x, z, yBin));
			}
		}
		
		return xz;
	}
	
	/**
	 * Creates a slice on the Y-Z axis of the specified x bin
	 * 
	 * @param xBin		the bin on the x-axis to slice on
	 * @return			a 2-D Histogram of the Y-Z axis on the entered x-axis bin
	 */
	public H2D sliceYZ(int xBin) {
		if (xBin >= 0 && xBin < xAxis.getNBins()) {
			String name = "Y-Z Slice of " + xBin + " X Bin";
			int yNum = yAxis.getNBins();
			double yMin = yAxis.min();
			double yMax = yAxis.max();
			int zNum = zAxis.getNBins();
			double zMin = zAxis.min();
			double zMax = zAxis.max();
			H2D yz = new H2D(name, yNum, yMin, yMax, zNum, zMin, zMax);
		
			for (int y = 0; y < yNum; y++) {
				for (int z = 0; z < zNum; z++) {
					yz.setBinContent(y, z, this.getBinContent(y, z, xBin));
				}
			}
		
			return yz;
		}
		return new H2D();
	}
	
	public double[] offset() {
		return hBuffer;
	}

    @Override
    public TreeMap<Integer, Object> toTreeMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fromTreeMap(TreeMap<Integer, Object> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
