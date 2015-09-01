package multifracture.grids;

import java.io.Serializable;

import multifracture.Variables;

public class EvenQuadraticGrid1D extends Grid1D implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7690838357655376804L;
	public EvenQuadraticGrid1D(int N, double esp) {
		
		super(N, esp);
	}
	
	public static void main(String[] args){
		
		int nn=40;
		Grid1D g=new EvenQuadraticGrid1D(nn,0);
		
		for(int i=0;i<nn;i++)
			System.out.println(g.x[i]);
		
	}

	@Override
	protected void inner_remesh() {
		
		double y=Variables.even_grid_y;

		
		//even case
		if(N % 2==0){
			
			double[] xx=new double[N/2];
	        xx[0]=0;
	        for (int i=1;i<N/2;i++)
	            xx[i]=1-Math.pow(1-(1-Math.pow(0,(1/y)))*i/(N/2-1),y);
				
			for(int i=0;i<N/2;i++){
				x[i+N/2]=0.5+xx[i]*0.5;
			}
			
			for(int i=0;i<N/2;i++){
				x[i]=(1-xx[N/2-i-1])*0.5;
			}
			
			
			double dx=x[N/2+2]-x[N/2-3];
			x[N/2-2]=x[N/2-3]+dx/5;
			x[N/2-1]=x[N/2-3]+2*dx/5;
			x[N/2]=x[N/2-3]+3*dx/5;
			x[N/2+1]=x[N/2-3]+4*dx/5;
		}
		
		//odd case
		else{
			
			
			double[] xx=new double[N/2+1];
	        xx[0]=0;
	        for (int i=1;i<N/2+1;i++)
	            xx[i]=1-Math.pow(1-(1-Math.pow(0,(1/y)))*i/(N/2+1-1),y);
						
			for(int i=0;i<N/2+1;i++){
				x[i]=(1-xx[N/2-i])*0.5;
			}
			
			for(int i=0;i<N/2+1;i++){
				x[i+N/2]=0.5+xx[i]*0.5;
			}
		}
		
	}
}


