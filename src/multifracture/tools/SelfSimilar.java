package multifracture.tools;

import java.io.Serializable;

public class SelfSimilar implements Serializable{
	
	private static double a=0;
	
    public static double kl=4;
    public static double ke=1;
    public static double q0=1;
    public static double qn=1;
    public static double tn=1;
	private static double xn,wn;
	private static double b0=1;
	private static double b1=-1.0/16.0;
	private static double b2=-15.0/224.0*b1;
	private static double b3=-3.0/80.0*b2;
	private static double b4=-11.0/5824.0*b3;
	   
	private static double xi_s;
	
	private static double Y(double x){
		
		xi_s=1.3208446*Math.pow(q0/qn,0.6);
		xn=Math.pow(kl*ke/4.0,1.0/5.0)*Math.pow(qn,3.0/5.0)*Math.pow(tn,4.0/5.0);
		wn=qn*tn/xn;
		
		
		return 0.6*xi_s*xi_s*			// ZAPISAC W PRAWIDLOWEJ POSTACI W PRZYSZLOSCI
				   (b0/1.0*Math.pow(1-x,1.0)
					+b1/2.0*Math.pow(1-x,2.0)
					+b2/3.0*Math.pow(1-x,3.0)
					+b3/4.0*Math.pow(1-x,4.0)
					+b4/5.0*Math.pow(1-x,5.0));   
		
	}
	
	private static double dY(double x){
		xi_s=1.3208446*Math.pow(q0/qn,0.6);
		xn=Math.pow(kl*ke/4.0,1.0/5.0)*Math.pow(qn,3.0/5.0)*Math.pow(tn,4.0/5.0);
		wn=qn*tn/xn;
		
		   return 0.6*xi_s*xi_s*			// ZAPISAC W PRAWIDLOWEJ POSTACI W PRZYSZLOSCI
				   (b0/1.0*Math.pow(1-x,0)
					+2*b1/2.0*Math.pow(1-x,1.0)
					+3*b2/3.0*Math.pow(1-x,2.0)
					+4*b3/4.0*Math.pow(1-x,3.0)
					+5*b4/5.0*Math.pow(1-x,4.0));   
		
	}
	
	public static double q(double t,double x){
		xi_s=1.3208446*Math.pow(q0/qn,0.6);
		xn=Math.pow(kl*ke/4.0,1.0/5.0)*Math.pow(qn,3.0/5.0)*Math.pow(tn,4.0/5.0);
		wn=qn*tn/xn;
		double dwdx=-wn*1.0/3.0*Math.pow(Y(x),-2.0/3.0)*Math.pow(a+t,1.0/5.0)*dY(x);
		double ww=w(t,x);
		return -kl/ke/L(t)*ww*ww*ww*dwdx;
	}
	
	public static double w(double t,double x){
		xi_s=1.3208446*Math.pow(q0/qn,0.6);
		xn=Math.pow(kl*ke/4.0,1.0/5.0)*Math.pow(qn,3.0/5.0)*Math.pow(tn,4.0/5.0);
		wn=qn*tn/xn;
		return wn*Math.pow(Y(x),1.0/3.0)*Math.pow(a+t,1.0/5.0);
	}
	
	public static double[] w(double t,double x[]){
		xi_s=1.3208446*Math.pow(q0/qn,0.6);
		xn=Math.pow(kl*ke/4.0,1.0/5.0)*Math.pow(qn,3.0/5.0)*Math.pow(tn,4.0/5.0);
		wn=qn*tn/xn;
		double[] ww=new double[x.length];
		for(int i=0;i<x.length;i++){
			ww[i]=w(t,x[i]);
		}
		return ww;
	}
	
	public static double L(double t){
		xi_s=1.3208446*Math.pow(q0/qn,0.6);
		xn=Math.pow(kl*ke/4.0,1.0/5.0)*Math.pow(qn,3.0/5.0)*Math.pow(tn,4.0/5.0);
		wn=qn*tn/xn;
		return xi_s*xn*Math.pow(a+t,4.0/5.0);
	}
	
	public static double L_inv(double L){
		xi_s=1.3208446*Math.pow(q0/qn,0.6);
		xn=Math.pow(kl*ke/4.0,1.0/5.0)*Math.pow(qn,3.0/5.0)*Math.pow(tn,4.0/5.0);
		wn=qn*tn/xn;
		return Math.pow(L/xi_s/xn,5.0/4.0)-a;
	}
}


