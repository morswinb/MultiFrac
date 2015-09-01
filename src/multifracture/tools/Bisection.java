package multifracture.tools;

import java.io.Serializable;

public class Bisection implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2803053885068134111L;

	public static double findZero(BisectableFunction f,double a,double b){
		
		final double epsilon = 0.000001;
		double m, y_m, y_a;		
		
		//System.out.println("a "+a+"f(a) "+f.getVal(a));
		//System.out.println("b "+b+"f(b) "+f.getVal(b));
		
		while ( (b-a) > epsilon )
		{
			m = (a+b)/2;           // Mid point
			y_m=f.getVal(m);
			y_a=f.getVal(a);
			
			//y_m = m*m*m + m - 3.0;       // y_m = f(m)
			//y_a = a*a*a + a - 3.0;       // y_a = f(a)
			
			if ( (y_m > 0 && y_a < 0) || (y_m < 0 && y_a > 0) )
			{  // f(a) and f(m) have different signs: move b
				b = m;
			}
			else
			{  // f(a) and f(m) have same signs: move a
				a = m;
			}
			//System.out.println("New interval: [" + a + " .. " + b + "]");
		}
		return (a+b)/2;
		//System.out.println("Approximate solution = " + (a+b)/2 )
	}
	
	public interface BisectableFunction{
		public double getVal(double arg);
	}
}