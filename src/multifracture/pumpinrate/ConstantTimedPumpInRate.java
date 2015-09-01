package multifracture.pumpinrate;

import java.io.Serializable;

public class ConstantTimedPumpInRate extends PumpInRate implements Serializable{

	double q0=1;
	double tstart,tend;
	
	public ConstantTimedPumpInRate(double q0,double tstart,double tend){
		this.q0=q0;
		this.tstart=tstart;
		this.tend=tend;
	}
	
	@Override
	public double getQ0(double t) {
		//System.out.println(t);

		if(t>tstart && t<tend)
			return q0;
		else
			return 0;
	}


	@Override
	public double getTotalQ0Volume(double t1, double t2) {
		System.out.println("unimplemented method !!!!");
		return Math.max(0,q0*Math.min(t1-tstart,tend-tstart));
	}

}
