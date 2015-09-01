package multifracture.ODESolvers;

import java.io.Serializable;

import edu.rit.util.Arrays;
import multifracture.Model;
import multifracture.Variables;
import multifracture.graphics.MultiCrackRenderer;


public class LSODE_JNI_port extends Solver implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3967104191327876799L;
	//double[] w;
	//double[] p;

	static {
		System.loadLibrary("lsode");
	}
	
	private native int lsoda_jni(int n, double t_0,double t_end,double abstol,double retol,double[] Y0,double [] Y, double [] dY,double[] Ys);	

	
	public LSODE_JNI_port(Model m, MultiCrackRenderer r) {
		super(m, r);
		//p=m.get_y();
	}

	public void advance(double timeStep) {
		//System.out.println(m.Y[m.Y.length-1]);
		
		//m.get_y();
		//m.timer.tic();
		lsoda_jni(m.Y.length,m.t,m.t+timeStep,Variables.absTol,Variables.relTol,m.Y,m.Y_work[0],m.dY[0],m.Y);
		//System.out.println(m.dY[m.Y.length-1][m.Y.length-1]);
		
		m.update(m.t+timeStep);
	}
	
	private void dt(double t){	
		m.ODE(t);
	}
}
