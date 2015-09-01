package multifracture.tools;

import java.io.Serializable;

public class Nomalize implements Serializable{

	public static double norX(double x,double y){
		double mag=Math.sqrt(x*x+y*y);		
		return x=x/mag;
	}
	
	public static double norY(double x,double y){
		double mag=Math.sqrt(x*x+y*y);		
		return y=y/mag;
	}

	public static void to_unit_vector(double[] xy){
		double mag=Math.sqrt(xy[0]*xy[0]+xy[1]*xy[1]);
		xy[0]/=mag;
		xy[1]/=mag;
			
	}
	
}
