package multifracture.geometry;

import java.io.Serializable;

import multifracture.Model;
import multifracture.Variables;
import multifracture.ODE.EdgeODE;
import multifracture.ODE.ODEfactory;
import multifracture.elasticity.Elasticity;
import multifracture.grids.Grid1D;
//import multifracture.matrixes.Elasticity;
import multifracture.tools.Spline;

public abstract class Edge implements Serializable {

	//model to witch this PKN eadge beleongs to
	public Model m;
	
	//Physical properties for M and k (do zamiany)
	public double k=Variables.k;
	public double M=Variables.M;
	
	
	//backstress influence sigma_0
	public double sigma_0;
	
	//public Flow flow;
	//public LeakOff leakOff; //leak off disabled for now
	//public Elasticity elasticity;
	
	public double L;
	public double ox,oy,Lx,Ly,Nx,Ny;
	
	public int N; // number of ODEs to be computed from this edge
	public Grid1D grid1D;	
	public GridPoint2D[] gridPoints2D;	//actual location of grid points
	
	public double[] w;	//width for display values
	public double[] pf;	//fluid pressure used for display value
		
	public Vertex vertex1,vertex2;	//end points of this segment
	
	public int odeNstartIndex;		//start index in whole system vector
	public int index; //index of this edge, the maximum value is the number of edges
	
	private Spline interpolatingSpline;
	
	protected Edge(Model m,Vertex j1,Vertex j2){
		//create grid and find number of ODEs
		if(this.getClass()==Crack.class){
			this.N=Variables.N_per_crack;
			this.grid1D=Variables.getCrackGrid();
		}
		else if(this.getClass()==Pipe.class){
			this.N=Variables.N_per_pipe;
			this.grid1D=Variables.getPipeGrid();
		}
		commonInerConstructor(m,j1,j2);
	}
	
	protected Edge(Model m,Edge e,Vertex j1,Vertex j2){

		this.N=e.N;
		this.grid1D=e.grid1D;
		
		commonInerConstructor(m,j1,j2);
		
		this.w=e.w.clone();
		this.pf=e.pf.clone();
		
	}
	
	private void commonInerConstructor(Model m,Vertex j1,Vertex j2){
		
		this.L=j1.distance(j2);		
		this.m=m;
		this.vertex1=j1;
		this.vertex2=j2;		
		
		w=new double[N];
		pf=new double[N];
		
		//set origin location
		this.ox=vertex1.x;
		this.oy=vertex1.y;
		
		//set propagation direction vector
		this.Lx=vertex2.x-vertex1.x;
		this.Ly=vertex2.y-vertex1.y;
		double mag=Math.sqrt(Lx*Lx+Ly*Ly);		
		this.Lx=Lx/mag;
		this.Ly=Ly/mag;
		
		//set normal vector
		this.Nx=-Ly;
		this.Ny=Lx;
		
		//find backstress value
		this.sigma_0=Math.abs(this.Nx*Variables.sigma_x)+Math.abs(this.Ny*Variables.sigma_y);
		
		gridPoints2D=new GridPoint2D[this.grid1D.N];		
		for(int i=0;i<grid1D.x.length;i++)
			gridPoints2D[i]=new GridPoint2D(m,this,grid1D.x[i],L,i);
		
		this.innerConstructor();
		
		//assign connections
		m.add(this);
		m.add(vertex1);
		m.add(vertex2);
		
		vertex1.add(this);
		vertex2.add(this);
		
		ODEfactory.setODE(this);
	}
	
	protected abstract void innerConstructor();

	
	protected EdgeODE ode;
	
	public void setODE(EdgeODE ode){
		this.ode=ode;
	}
	
	public double getL(int tN){
		return this.L;
	}
	
	
	//returns flow rate value at point 0 to 1 of this eadge
	//public abstract double getFlowRate(double x);

	//compute i element derivative to dY vector
	public abstract void dY(double t,int i_global,int tN);
	
	//sets values to Y vector
	//public abstract void setY();

	//assembles solution vector Y form model objects
	public abstract void assemblyY(double []Y);
	
	//sets values of solution Y to model objects
	public abstract void setY(double []Y);

	
	//returns flow rate value at point 0 to 1 of this eadge
	//public abstract double getFluidPressure(double x);
	
	//returns flow rate value at point 0 to 1 of this eadge
	//public abstract double getFluidPressureAtXi(int i);
	
	//this works on Y[][]
	public abstract double getFluidPressureAtXi(int i, int tN);
	
	//methods used to evaluate joint conditions by newton method
	public abstract double[] getQiAnddQi(Vertex j,int tN);
	
	public void setStartIndex(int start) {
		this.odeNstartIndex=start;
	}
	
	//returns working width at edge grid index
	//public abstract double getW(int local_index,int tN);
	
	public double getVolume(double x1, double x2) {	
		
        interpolate();
		return (interpolatingSpline.integral(x1,x2))*L;
	}
	
	private void interpolate() {
		interpolatingSpline=Spline.interpolate(grid1D.x,w);
		
	}
	
	//returns w(x), where x is any x in(0,1), extrapolation required
	public double getSingleVal(double xx){
		interpolate();
		return interpolatingSpline.val(xx);
	}

	
	
	/*
	public double[] getSigma() {
		System.out.println("geting sigma from "+this);
		
		double[] sig=new double[gridPoints2D.length];
		for(int i=0;i<gridPoints2D.length;i++)
			sig[i]=this.elasticity.getSigma(i,0);
		return sig;		
	}*/

	//if true the edge is transparent to elasticity calculation
	abstract public boolean transparent();
	
	//general print debug method
	public void printALL(){
		System.out.println(this.getClass());
		System.out.println("ode start index "+this.odeNstartIndex);
		System.out.println("L "+this.L+" Lx "+this.Lx+" Ly "+this.Ly);
		System.out.println("Ox "+this.ox+" Oy "+this.oy);
		System.out.println("simga_0 "+this.sigma_0);
		System.out.println(" N "+this.grid1D.N+" grid type "+this.grid1D.getClass());
		//System.out.println("flow rate at 0.1 " +this.getFlowRate(0.1)+" flow rate at 0.9 "+this.getFlowRate(0.9));
		
		System.out.println("w");
		print_w();
		
		System.out.println("pf");
		print_pf();
		
		System.out.println("grid point x");
		print_grid_point_x();
		
		System.out.println("sigma_l");
		print_sigma_l();

		System.out.println("");
	}
	
	public void print_w(){
		for(int i=0;i<this.w.length;i++){
			System.out.println(this.w[i]);
		}
	}
	
	public void print_sigma_l(){
		
		for(int i=0;i<this.gridPoints2D.length;i++){
			System.out.println(this.gridPoints2D[i].getSigma_l(0));
			//System.out.println(this.elasticity.getSigma(i,0));
		}
		//double[] sigma_l=this.getElasticityInfluence();
		//for(int i=0;i<sigma_l.length;i++){
		//	System.out.println(sigma_l[0]);
		//}
	}
	
	public void print_sigma_l_efect(){
		
		//double e[]=	this.getElasticityInfluence();
		//for(int i=0;i<e.length;i++){
		//	System.out.println(e[i]);
		//}
		//double[] sigma_l=this.getElasticityInfluence();
		//for(int i=0;i<sigma_l.length;i++){
		//	System.out.println(sigma_l[0]);
		//}
	}
	
	public void print_pf(){
		for(int i=0;i<this.w.length;i++){
			System.out.println(this.pf[i]);
		}
	}
	
	public void print_grid_point_x(){
		for(int i=0;i<this.gridPoints2D.length;i++){
			System.out.println(this.gridPoints2D[i].xx);
		}
	}

}
