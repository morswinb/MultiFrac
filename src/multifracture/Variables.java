package multifracture;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import multifracture.grids.EvenQuadraticGrid1D;
import multifracture.grids.Grid1D;
import multifracture.grids.ModifiedQuadraticGrid1D;
import multifracture.grids.QuadraticGrid1D;
import multifracture.grids.ReguralGrid1D;
import multifracture.tools.Nomalize;
import multifracture.tools.SelfSimilar;

public class Variables implements Serializable{

	
	/**
	 * 
	 */
	public static double t_start=0;
	public static double t_end=100;
	
	private static final long serialVersionUID = -3938769307406994658L;
	//these are initial parameters, might change
	public static int N_per_pipe=21;	//keep odd
	public static int N_per_crack=20;	//keep even
	//public final static double epsilon=0.001;	
	public static double epsilon=1e-3;	//epsilon for crack tip
	//public static double p_epsilon=0.01;//epsilon for pressure at junction
	public static int elasticity_draw_skip=1;
	
	public static double even_grid_y=1.5;
	public static double mod_quad_grid_y1=2.25;
	public static double mod_quad_grid_y2=1.5;
	
	public static double remodel_power=1.5;
	//public static final double g=1;
	//public static final int elasticity_aprox_N=10;
	
	public static double geometry_check_t=1;
	public static double geometry_check_multiplier=2;
	
	public static double colision_acceptance_threshold=.5;
	
	public static boolean fast_jacobian=true;
	public static boolean fast_elasticity=true;
	public static int threadN=7;
	
	public static double jacobianDelta = 1e-2;
	public static boolean CustomJacobian = false;
	
	public static final double initial_self_similar_t=1;
	
	public static double k;
	public static double M;
	public static final double R=1; // pipe radious
	//public static final double q0=2/60.0;
	public static final double q0=.5;
	
	public static double poissons_ration=0.2;
	public static double youngs_modulus=1e10;
	public static double dynamic_viscosity=1;
	public static double h=10;
	
	static{		
		//M=12*dynamic_viscosity;
		//k=2/Math.PI/h*youngs_modulus/(1-poissons_ration*poissons_ration);
		k=4;
		M=1;
	}
	
	public static double g= 0.1413;
	//public static double g= 0.1413/2; //zostawmy tak zeby sobie nie psuch humoru mo¿e inne g siê potem da...
	
	public static double[] sigma_x_direction={1,0};
	public static double[] sigma_y_direction={0,1};
	
	public static double sigma_x=0;
	public static double sigma_y=0;
	
	//0 w
	//1 U
	public static int PipeFlow=0;
	
	//0 w
	//1 U
	//2 w_asymptotics
	public static int CrackFlow=2;
	
	//0 off 
	//1 on
	public static int Elatsicity=1;
	
	
	//0 zero leak off
	//1 Carter leak off
	//2 pressure times carter
	public static int LeakOff=0;
	
	
	public static double[] asymptotics_powers;
	static{
		if(LeakOff==0)
			asymptotics_powers=new double[]{1.0/3.0,4.0/3.0};
		else if(LeakOff==1)
			asymptotics_powers=new double[]{1.0/3.0,5.0/6.0};
		else if(LeakOff==2)
			asymptotics_powers=new double[]{1.0/3.0,1.0/2.0};
	}
	
	
	// Regular Quadratic EvenQuadratic
	//public static Class<?> PipeGrid=EvenQuadraticGrid1D.class;
	public static Class<?> PipeGrid=ReguralGrid1D.class;
	
	// Regular Quadratic
	//public static Class<?> CrackGrid=QuadraticGrid.class;
	public static Class<?> CrackGrid=ModifiedQuadraticGrid1D.class;
	//public static Class<?> CrackGrid=ReguralGrid1D.class;
	
		
	public static double min_transfer_volume;
	public static double max_transfer_volume;
	
	
	public static double sigma_rel_tol=1e-3;
	public static double sigma_abs_tol=1e-6;
	
	public static double relTol=1e-3;
	public static double absTol=1e-6;
	
	
	//grid options
	
	public static Grid1D getPipeGrid(){
		Grid1D g = null;
		Object[] args=new Object[2];
		args[0]=N_per_pipe;
		args[1]=0;
		try {
			g=(Grid1D) PipeGrid.getConstructors()[0].newInstance(args);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return g;
	}
	
	public static Grid1D getCrackGrid(){
		Grid1D g = null;
		Object[] args=new Object[2];
		args[0]=N_per_crack;
		args[1]=epsilon;
		try {
			g=(Grid1D) CrackGrid.getConstructors()[0].newInstance(args);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return g;
	}
	
}
