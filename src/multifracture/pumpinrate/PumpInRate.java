package multifracture.pumpinrate;

import java.io.Serializable;

public abstract class PumpInRate implements Serializable{

	public abstract double getQ0(double t);

	//returns integral form t1 to t2 of q0(t) dt
	public abstract double getTotalQ0Volume(double t1, double t2);
	
}
