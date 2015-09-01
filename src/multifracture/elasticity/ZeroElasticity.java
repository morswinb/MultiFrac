package multifracture.elasticity;

import java.io.Serializable;

import multifracture.geometry.Crack;
import multifracture.geometry.Edge;
import multifracture.geometry.Pipe;

public class ZeroElasticity implements Elasticity,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1522501769347077048L;
	//double[] zeros;
	
	public ZeroElasticity(){
		
		//zeros=new double[e.grid.N];
	}

	@Override
	public double getSigma(int i, int tN) {
		return 0;
	}

	@Override
	public void findSigma(int tN) {
		
	}
	

}
