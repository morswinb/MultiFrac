package multifracture.grids;

import java.io.Serializable;

import multifracture.Variables;

public class ModifiedQuadraticGrid1D extends Grid1D implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6646522609016377528L;
	public ModifiedQuadraticGrid1D(int N, double esp) {
		
		super(N, esp);        
	}

	@Override
	protected void inner_remesh() {
		
		double y1=Variables.mod_quad_grid_y1; // near tip
		double y2=Variables.mod_quad_grid_y2; // near entrence

		N--;
		
		//even case
		if(N % 2==0){
			
			new Exception("no even case for this grid");
		}
		
		//odd case
		else{
			
			
			double[] xx=new double[N/2+1];
			double[] xxx=new double[N/2+1];
	        xx[0]=0;
	        for (int i=1;i<N/2+1;i++){
	        	
	        	xx[i]=1-Math.pow(1-(1-Math.pow(2*eps,(1/y1)))*i/(N/2),y1);
	        	xxx[i]=1-Math.pow(1-(i+0.0)/(N/2),y2);
	        }
			
			//Grid g=new QuadraticGrid(N/2+1,eps);
			
			for(int i=0;i<N/2+1;i++){
				x[i]=(1-xxx[N/2-i])*0.5;
			}
			
			for(int i=0;i<N/2+1;i++){
				x[i+N/2]=0.5+xx[i]*0.5;
			}
		}
		
		N++;
		x[N-1]=1;
	}
	
	
	public static void main(String[] args){
		
		Grid1D g=new ModifiedQuadraticGrid1D(50,1e-4);
		//System.out.println("epsilon "+g.eps);
		
		for(int i=0;i<g.N;i++){
			System.out.println(g.x[i]);
		}
	}
}


