package multifracture.grids;

import java.io.Serializable;

public class ReguralGrid1D extends Grid1D implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3735033681590576112L;

	public ReguralGrid1D(int N, double esp) {
		
		super(N, esp);
		
	}

	@Override
	protected void inner_remesh() {
		
		if(eps>0){
			double dx=(1.0-eps)/(N-2);
			for(int i=0;i<N-1;i++)
				x[i]=dx*i;
			x[N-1]=1;
		}
		else{
			double dx=(1.0-eps)/(N-1);
			for(int i=0;i<N;i++){
				x[i]=dx*i;
			}
		}
		
	}
	
	
	public static void main(String[] args){
		
		//Grid g=new ReguralGrid(51,1e-4);
		Grid1D g=new ReguralGrid1D(10,0);
		//System.out.println("epsilon "+g.eps);
		
		for(int i=0;i<g.N;i++){
			System.out.println(g.x[i]);
		}
	}
}
