package multifracture.pumpinrate;

import java.io.Serializable;

public class ConstantPumpInRate extends PumpInRate implements Serializable{

	double q0=1;
	
	public ConstantPumpInRate(double q0){
		this.q0=q0;
	}
	
	@Override
	public double getQ0(double t) {
		return q0;
	}
	
	@Override
	public double getTotalQ0Volume(double t1, double t2) {
		return q0*(t2-t1);
	}


}
