package multifracture.geometry;

import java.io.Serializable;

import multifracture.Model;
import multifracture.Variables;
import multifracture.grids.ReguralGrid1D;

public class ConcreatePipe extends Edge implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 973350997295798285L;
	public double M=Variables.M;
	public double R=Variables.R;
	
	public ConcreatePipe(Model m,Vertex j1,Vertex j2){
		super(m,j1,j2);
		gridPoints2D=new GridPoint2D[0];
	}
	
	@Override
	public double[] getQiAnddQi(Vertex j,int tN){
		
		double gamma=3*Math.PI*R*R*R*R/2/M/L;
		double J;
		double Jo;
		if(j==vertex1){
			J=vertex1.getPressure(tN);
			Jo=vertex2.getPressure(tN);
		}
		else{
			J=vertex2.getPressure(tN);
			Jo=vertex1.getPressure(tN);
		}
		//System.out.println("J "+J+" Jo "+Jo);
		//System.out.println("Q "+(gamma*(J-Jo)));
        return new double[]{gamma*(J-Jo),gamma};

	}


	@Override
	public double getFluidPressure(double x) {
		return 0; //ugly value to help newton iterations
		//return (vertex1.getFixedPressure()+vertex2.getFixedPressure());
	}


	@Override
	public boolean transparent() {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public double getFluidPressureAtXi(int i) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public double getSingleVal(double xx) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public double getFluidPressureAtXi(int i, int tN) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeY(double[] Y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setY(double[] Y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dY(double t, int i, int tN) {
		// TODO Auto-generated method stub
		
	}
}
