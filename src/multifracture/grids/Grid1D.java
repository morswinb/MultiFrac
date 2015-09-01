package multifracture.grids;

import java.io.Serializable;

public abstract class Grid1D implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4396483234481288115L;
	public int N;
	public double eps;
	public double [] x;
	
	public Grid1D(int N, double eps){
		this.remesh(N, eps);
	}
	
	
	public void remesh(int N, double eps){
		this.N=N;
		this.eps=eps;
		this.x=new double[N];
		this.inner_remesh();
	}
	
	protected abstract void inner_remesh();
		
	//creates reversed/flipped image of this grid
	public void reverse(){
		
		double[] x_rev=new double[N];
		
		for(int i=0;i<N;i++){
			x_rev[i]=1-x[N-1-i];
		}
		x=x_rev;
		
	}
	
	
}
