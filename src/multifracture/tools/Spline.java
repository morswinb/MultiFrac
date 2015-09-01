package multifracture.tools;

import java.io.Serializable;
import java.util.Arrays;

public class Spline implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1981444918350900302L;
	static int n;
	double[] a;
	double[] b;
	double[] c;
	double[] d;
	double[] x;
	
	public Spline(double[] a,double[] b,double[] c,double[] d,double[] x){
		this.a=a;
		this.b=b;
		this.c=c;
		this.d=d;
		this.x=x;
	}
	
	public double val(double x1){
		
		int i=Arrays.binarySearch(x,x1);
		
		if(i<0)
			i=Math.abs(i)-2;
		if(i>n-2) i=n-2;
		if(i<0) i=0;
			
		double xx=(x1-x[i]);
		return a[i]+xx*(b[i]+xx*(c[i]+xx*d[i]));
	}
	
	public double derevative(double x1){
		int i=Arrays.binarySearch(x,x1);
		
		if(i<0)
			i=Math.abs(i)-2;
		if(i>n-2) i=n-2;
		if(i<0) i=0;
			
		double xx=(x1-x[i]);
		return b[i]+xx*(c[i]+xx*d[i]);
	}
	
	public double integral(double x0,double x1){
		int i1=Arrays.binarySearch(x,x0);
		if(i1<0)
			i1=Math.abs(i1)-2;
		if(i1>n-2) i1=n-2;
		if(i1<0) i1=0;
		
		int i2=Arrays.binarySearch(x,x1);
		if(i2<0)
			i2=Math.abs(i2)-2;
		if(i2>n-2) i2=n-2;
		if(i2<0) i2=0;
		
		if(i1==i2){
			return integ(i2,x1)-integ(i1,x0);
		}
		else{
			double integral=integ(i1,x[i1+1])-integ(i1,x0);
			for(int i=i1+1;i<i2;i++)
				integral+=integ(i,x[i+1]); 
			integral+=integ(i2,x1);
			return integral;
		}
		
	}
	
	private double integ(int i,double x0){
		double xx=(x0-x[i]);
		return xx*(a[i]+xx*(b[i]/2+xx*(c[i]/3+xx*d[i]/4)));
	}
	
	public static Spline interpolate(double[]x, double[]y,double yp0,double yp1){
		
		n=x.length;
		
		double[] a=Arrays.copyOf(y,y.length);
		double[] h=new double[n-1];
		double[] L=new double[n];
		double[] u=new double[n];
		double[] z=new double[n];
		double[] c=new double[n];
		double[] b=new double[n-1];
		double[] d=new double[n-1];
		double[] alpha=new double[n];
		
		//Step 1
		for(int i=0;i<n-1;i++)
		    h[i]=x[i+1]-x[i];
		
		//Step 2
		alpha[0]=3*(a[1]-a[0])/h[0]-3*yp0;
		alpha[n-1]=3*yp1-3*(a[n-1]-a[n-2])/h[n-2];
		
		//Step 3
		for(int i=1;i<n-1;i++)
		    alpha[i]=3/h[i]*(a[i+1]-a[i])-3/h[i-1]*(a[i]-a[i-1]);
		
		//Step 4
		L[0]=2*h[0];
		u[0]=0.5;
		z[0]=alpha[0]/L[0];

		//Step 5
		for(int i=1;i<n-1;i++){
		    L[i]=2*(x[i+1]-x[i-1]-h[i-1]*u[i-1]);
		    u[i]=h[i]/L[i];
		    z[i]=(alpha[i]-h[i-1]*z[i-1])/L[i];
		}
		
		//Step 6
		L[n-1]=h[n-2]*(2-u[n-2]);
		z[n-1]=(alpha[n-1]-h[n-2]*z[n-2])/L[n-1];
		c[n-1]=z[n-1];
	
		//Step 7
		for (int i=n-2;i>=0;i--){
		   c[i]=z[i]-u[i]*c[i+1];
		   b[i]=(a[i+1]-a[i])/h[i]-h[i]*(c[i+1]+2*c[i])/3;
		   d[i]=(c[i+1]-c[i])/(3*h[i]);
		}
		
		a=Arrays.copyOf(a,n-1);
		b=Arrays.copyOf(b,n-1);
		c=Arrays.copyOf(c,n-1);
		d=Arrays.copyOf(d,n-1);
		x=Arrays.copyOf(x,n-1);
		
		return new Spline(a,b,c,d,x);
	}
	
	public static Spline interpolate(double[]x, double[]y){
		
		n=x.length;
		
		double[] h=new double[n-1];
		double[] u=new double[n];
		double[] z=new double[n];
		double[] v=new double[n];
		double[] a=new double[n-1];
		double[] b=new double[n-1];
		double[] c=new double[n-1];
		double[] d=new double[n-1];
		
		
		for(int i=0;i<n-1;i++){
			h[i]=x[i+1]-x[i];
			b[i]=(y[i+1]-y[i])/h[i];
		}
		
		u[1]=2*(h[0]+h[1]);
		v[1]=6*(b[1]-b[0]);
		for (int i=2;i<n-1;i++){
			u[i]=2*(h[i-1]+h[i])-h[i-1]*h[i-1]/u[i-1];
			v[i]=6*(b[i]-b[i-1])-h[i-1]*v[i-1]/u[i-1];
		}
		
		z[n-1]=0;
		
		for(int i=n-2;i>0;i--){
			z[i]=(v[i]-h[i]*z[i+1])/u[i];
		}
		
		z[0]=0; 
		
		for(int i=0;i<n-1;i++){
			a[i]=y[i];
			b[i]=-h[i]/6*z[i+1]-h[i]/3*z[i]+(y[i+1]-y[i])/h[i];
			c[i]=z[i]/2;
			d[i]=(z[i+1]-z[i])/6/h[i];
		}
		
		
		a=Arrays.copyOf(a,n-1);
		b=Arrays.copyOf(b,n-1);
		c=Arrays.copyOf(c,n-1);
		d=Arrays.copyOf(d,n-1);
		x=Arrays.copyOf(x,n-1);
		
		return new Spline(a,b,c,d,x);
	}
	
	public static Spline interpolate_linnear(double[]x, double[]y){
		
		n=x.length;
		
		double[] h=new double[n-1];
		double[] a=new double[n-1];
		double[] b=new double[n-1];
		double[] c=new double[n-1];
		double[] d=new double[n-1];
		
		
		for(int i=0;i<n-1;i++){
			h[i]=x[i+1]-x[i];
			
			a[i]=y[i];
			b[i]=(y[i+1]-y[i])/h[i];
		}

		
		return new Spline(a,b,c,d,x);
	}

	
	public static void main(String[] args) {	
		
		double[] x=new double[]{0,1,2,3,4};
		double[] y=new double[]{Math.exp(x[0]),Math.exp(x[1]),Math.exp(x[2]),Math.exp(x[3]),Math.exp(x[4])};
		double yp0=Math.exp(x[0]);
		double yp1=Math.exp(x[x.length-1]);	
		
		//Spline cs =interpolate(x,y,yp0,yp1);
		Spline cs =interpolate_linnear(x,y);
		for(double i=-.2;i<=4.3;i+=0.1){
			//System.out.println(Math.abs(cs.val(i)-Math.exp(i))/Math.exp(i));
			//System.out.println(Math.exp(i));
			//cs.val(i);
		}
		
		System.out.println(cs.integral(-.2,4.3));
		
	}
	

}
