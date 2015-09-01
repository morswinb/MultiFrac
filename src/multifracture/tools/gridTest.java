package multifracture.tools;

import java.io.Serializable;

import multifracture.grids.Grid1D;
import multifracture.grids.QuadraticGrid1D;

public class gridTest implements Serializable{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Grid r=new ReguralGrid(10,0);
		
		
		Grid1D q=new QuadraticGrid1D(10,0.001);
		
		
		for(int i=0;i<10;i++)
			System.out.println(q.x[i]);
	}

}
