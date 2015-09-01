package multifracture.ODE;

import multifracture.geometry.Edge;

public abstract class EdgeODE {

	private double[] D,F,G;
	protected double[] x;
	protected int N;
	protected Edge e;
	
	public EdgeODE(Edge e){
		
		this.e=e;
		x=e.grid1D.x;
		N=e.grid1D.N;
		
		//FD		
		D=new double[N-2];
		F=new double[N-2];
		G=new double[N-2];
		
		double[] dx=new double[N-1];
		for (int i=0;i<N-1;i++){
			dx[i]=x[i+1]-x[i];
		}		
		
		for (int i=0;i<N-2;i++){
			D[i]=dx[i];
			F[i]=dx[i+1];
			G[i]=1/dx[i]+1/dx[i+1];
		}
	}
	
	
	public abstract void dY(double t, int i_global, int tN);
	
	protected double FirstDiff(double y1,double y2,double y3,int i){
		return 1.0/2.0*((y3-y2)/F[i]+(y2-y1)/D[i]);		
	}
	
	protected double SecondDiff(double y1,double y2,double y3,int i){		
		return 1.0/2.0*G[i]*((y3-y2)/F[i]-(y2-y1)/D[i]);
	}
}
