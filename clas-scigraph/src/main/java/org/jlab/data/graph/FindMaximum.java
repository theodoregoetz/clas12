package org.jlab.data.graph;

import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FindMaximum
{
    int n_max;
    int[] max_index = new int[10];
    double[] data_array;
    
    int n_density = 650;
    double[] x_density = new double[n_density];
    double delta;

    List<ArrayList<Double>> clust_list;

    public static void main( String[] args )
    {
	System.out.println("Kuku");

	double[] arr_to_test = {2, 2, 2, 3, 3, 2, 3, 5, 2, 3, 2, 1,2, 2, 1, 1, 3, 2, 1, 1, 2, 2, 3, 30, 60, 120, 250, 240, 247, 120, 120, 50, 15, 2, 2, 1, 4, 1, 3, 4, 1, 2
				, 3, 2, 4, 2, 6, 21, 51, 61, 71, 92, 98, 90, 250, 450, 70, 50, 45, 30, 11, 9, 7, 6, 2, 1, 4, 3, 3, 4, 1, 2, 15};
	
	//int ind = nearInclusive(arr_to_test, 12.5);

	FindMaximum a = new FindMaximum();
	a.Analyze(arr_to_test);
    }

    public void Analyze( double[] data )
    {
	int length = data.length;
	
	n_max = 0;
	double[] cumSum = getCumulative_Sum(data);

	//Seems OK	
// 	for( int i = 0; i < cumSum.length; i++ )
// 	    {
// 		System.out.println( cumSum[i]);
// 	    }
	
	//System.out.println("ind = " + ind + "\n");

	x_density[0] = 0;
	
	delta = 0;
	for( int i = 1; i < n_density; i++ )
	    {
		double yy = (double)i*1./((double)n_density);             // a point in the Y axis
		int ind = find_Index(cumSum, yy);
		
		//          Seems ok
		//System.out.println( "yy = " + yy + "    ind = " + ind + "\n" );
		
		x_density[i] = find_interpol_x(cumSum, ind, yy);
		//System.out.println( "x_density[" + i + "] = " + x_density[i] + "  ind = " + ind + "\n" );
		delta = delta + x_density[i] - x_density[i - 1];    // It should be always positive, therefore I didn't take the absalute value
		//System.out.println("Delta i = " + (x_density[i] - x_density[i - 1]) );
	    }
	delta = delta/(double)n_density;
	

	//============ Clustering should start from here =============
	
	clust_list = new ArrayList<ArrayList<Double>>();
	ArrayList<Double> cur_clust = new ArrayList<Double>();
	boolean keep_cluster = true;
	for( int i = 1; i < n_density; i++ )
	    {
		//System.out.println("i = " + i + " x_density = " + x_density[i]);
		if( keep_cluster == false )
		    {
			cur_clust.clear();
		    }
		//System.out.println("delta = " + delta + "\n");
			
		if( (x_density[i] - x_density[i-1]) < 0.2*delta )
		    {
			cur_clust.add((Double) x_density[i]);
			keep_cluster = true;
		    }
		else
		    {
			//System.out.println( "x[i] = " + x_density[i] +  "   x[i-1] = " + x_density[i-1] );
			keep_cluster = false; // This means that this point is already far from the previous, and should be put in another cluster
			//System.out.println("Size = " + cur_clust.size());
			if( cur_clust.size() > 0 )
			    {
				ArrayList<Double> tmp = new ArrayList<Double>(cur_clust);
				//tmp = cur_clust;
				System.out.println("adding to cluster cur_clust_size() = " + cur_clust.size());
				clust_list.add( (ArrayList<Double>) tmp );
			    }
		    }
	    }
	System.out.println("clust_lsut.size = " + clust_list.size() + "\n");

	if( clust_list.size() > 0 )
	    {
		for( int i = 0; i < clust_list.size(); i++ )
		    {
			ArrayList<Double> new_clust = clust_list.get(i);
			System.out.println("clust size = " + new_clust.size());
			double summ = 0;
			for( int j = 0; j < new_clust.size(); j++ )
			    {
				//System.out.println("cur_clust.get = " + new_clust.get(j)  );
				summ = summ + new_clust.get(j);
			    }
			summ = summ/((double)new_clust.size());
			
			System.out.println( "Center of " + i + "-th peak is " + summ);
		    }
	    }
	
	//int n_clusters = 
    }
    
    double[] getCumulative_Sum( double[] a)
    {
	double[] cumSum = new double[a.length];
	cumSum[0] = a[0];

	for( int i = 1; i < a.length; i++ )
	    {
		cumSum[i] = cumSum[i-1] + a[i];
	    }
	
	for( int i = 0; i < a.length; i++ )
	    {
		cumSum[i] = cumSum[i]/cumSum[a.length - 1];
	    }

	return cumSum;
    }

    
    // should find tyhe maximum index of an arry that is lower from the "val"
    // Note This Algorthm is valid when you have an incrasing array
    int find_Index(double[] a, double val) 
    {
	int ind = -10;
	if( val < a[0] )
	    {
		ind = -1;
	    }
	else if (val > a[a.length - 1])
	    {
		ind = a.length - 1;
	    }
	else
	    {
		for( int i = 0; i < a.length; i++)
		    {
			if( a[i] <= val && a[i+1] > val )
			    {
				ind = i;
				break;
			    }
		    }
	    }
	return ind;
    }
    
    double find_interpol_x( double[] a, int i, double val )
    {
	double interpol_x = (double)i + (val-a[i])/( a[i+1] - a[i] );
	//System.out.println( "i = " + i + "   val = " + val + "   a[i] =  " + a[i] + "   a[i+1] =  " + a[i+1] );
	return interpol_x;
    }
    
}