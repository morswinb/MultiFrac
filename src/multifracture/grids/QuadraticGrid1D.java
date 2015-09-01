package multifracture.grids;

import java.io.Serializable;

public class QuadraticGrid1D extends Grid1D implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6646522609016377528L;
	public static double y=2.5;

	public QuadraticGrid1D(int N, double esp) {
		
		super(N, esp);        
	}

	@Override
	protected void inner_remesh() {
		
        x[0]=0;
        for (int i=1;i<N-1;i++)
            x[i]=1-Math.pow(1-(1-Math.pow(eps,(1/y)))*i/(N-2),y);
        x[N-1]=1;
	}
	
	public static void main(String[] args){
		
		Grid1D g=new QuadraticGrid1D(51,1e-4);
		//System.out.println("epsilon "+g.eps);
		
		for(int i=0;i<g.N;i++){
			System.out.println(g.x[i]);
		}
	}
}


