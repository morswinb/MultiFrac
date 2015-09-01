package multifracture.geometry;

import java.io.Serializable;
import java.util.Arrays;

import multifracture.Model;
import multifracture.Variables;
import multifracture.tools.SelfSimilar;

public class Pipe extends Edge implements Serializable{

	private static final long serialVersionUID = 2527094956196541910L;
	
	public Pipe(Model m,Vertex j1,Vertex j2){
		super(m,j1,j2);
	}
	
	public Pipe(Model m,Pipe p,Vertex j1,Vertex j2){
		super(m,p,j1,j2);
	}
	
	@Override
	protected void innerConstructor() {
		this.w=new double[grid1D.N];
		double ww=SelfSimilar.w(Variables.initial_self_similar_t,0);	
		Arrays.fill(w, ww);
		for(int i=0;i<pf.length;i++){
			pf[i]=ww*k;
		}
		
	}
	

	public void updateGridPoints(){
		for(int i=0;i<grid1D.x.length;i++){
			gridPoints2D[i].x=vertex1.x+grid1D.x[i]*Lx*L;
			gridPoints2D[i].y=vertex1.y+grid1D.x[i]*Ly*L;
		}
	}
		
	
	@Override
	public void assemblyY(double[] Y) {	
		int k=odeNstartIndex;
		for(int i=0;i<N;i++){
			Y[k++]=w[i];
		}		
	}

	@Override
	public void setY(double[] Y) {
		int k=0;
		for(int i=odeNstartIndex;i<odeNstartIndex+N;i++){
			w[k++]=Y[i];
		}
		
		w[0]=(vertex1.getPressure()-this.sigma_0-m.sigma_l[odeNstartIndex][0])/this.k;
		w[N-1]=(vertex2.getPressure()-this.sigma_0-m.sigma_l[odeNstartIndex+N-1][0])/this.k;
		
		/*for(int i=0;i<pf.length;i++){
			try{
				pf[i]=getFluidPressureAtXi(i);
			}
			catch(Exception e){
				e.printStackTrace();
			}
					
		}*/
	}
	
	@Override
	public double[] getQiAnddQi(Vertex j,int tN){
		
		double x1,x2;
		double pf1,pf2;
		x1=grid1D.x[1];
		x2=grid1D.x[2];		
		double sigma_l=0;
		double q_direction=1;
		
		//left
		if(j==vertex1){
			x1=grid1D.x[1];
			x2=grid1D.x[2];
			sigma_l=m.sigma_l[tN][odeNstartIndex];	
			pf1=this.k*m.Y_work[tN][odeNstartIndex+1]+this.sigma_0+m.sigma_l[tN][odeNstartIndex+1];
			pf2=this.k*m.Y_work[tN][odeNstartIndex+2]+this.sigma_0+m.sigma_l[tN][odeNstartIndex+2];
		}
		//right
		else{
	        x1=grid1D.x[grid1D.x.length-2]-1;
	        x2=grid1D.x[grid1D.x.length-3]-1;
	        //sigma_l=this.elasticity.getSigma(this.grid.N-1,tN);	
	        sigma_l=m.sigma_l[tN][odeNstartIndex+N-1];
	        pf1=this.k*m.Y_work[tN][odeNstartIndex+N-2]+this.sigma_0+m.sigma_l[tN][odeNstartIndex+N-2]; //this.elasticity.getSigma(this.grid.N-2,tN);
			pf2=this.k*m.Y_work[tN][odeNstartIndex+N-3]+this.sigma_0+m.sigma_l[tN][odeNstartIndex+N-3]; //this.elasticity.getSigma(this.grid.N-3,tN);
	        q_direction=-1;
		}
				
		//quadratic version
		double II=x2*(x2-x1);
		double alpha=-1/x1-(x2-x1)/II;
		double beta=pf1*(1/x1+x2/II)-pf2*x1/II;
		
		//linnear version
		//double alpha=-1/x1;
		//double beta=pf1/x1;
		
		double J=j.getPressure(tN);
		double p=J-this.sigma_0-sigma_l;
		double p2=p*p;
		double p3=p2*p;
		
	//	System.out.println("crack.java q "+1/M/L/k/k/k*w3*(alpha*J+beta));
		return new double [] {q_direction/M/L/k/k/k*p3*(alpha*J+beta),q_direction/M/L/k/k/k*(3*p2*(alpha*J+beta)+p3*alpha)};
		//return new double [] {1/M/L/k/k/k*(alpha*J*J*J*J+beta*J*J*J),1/M/L/k/k/k*(4*alpha*J*J*J+3*beta*J*J)};
	}
	
	//@Override
	public void dY(double t,int i_global,int tN){
		ode.dY(t, i_global, tN);
	}
		
	//@Override
	public double getFlowRate(double x) {
		int i=Math.abs(Arrays.binarySearch(grid1D.x,x));
		double x1=grid1D.x[i-1];
		double x2=grid1D.x[i];
		double x3=grid1D.x[i+1];
		double w1=w[i-1];
		double w2=w[i];
		double w3=w[i+1];
		double dw=((w3-w2)/(x3-x2)+(w2-w1)/(x2-x1))/2;
		
		return-this.k/this.M/this.L*w2*w2*w2*dw;
	}

	/*@Override
	public double getFluidPressure(double x) {
		int i=Math.abs(Arrays.binarySearch(grid1D.x,x));
		return this.k*w[i]+this.sigma_0+m.sigma_l[odeNstartIndex+i][0];
	}*/
	
	
	/*
	@Override
	public double getW(int local_index, int tN) {
				
		if(local_index==0 || local_index==this.odeN-1){
			if(local_index==0){
				return (vertex1.getPressure(tN)-sigma_0-elasticity.getSigma(0, tN))/k;
			}
			else{
				return (vertex2.getPressure(tN)-sigma_0-elasticity.getSigma(local_index, tN))/k;
			}
		}
		else
			return m.[this.odeNstartIndex+local_index];
	}*/

	@Override
	public boolean transparent() {
		return false;
	}


	@Override
	public double getFluidPressureAtXi(int i, int tN) {
		
		return this.k*m.Y_work[tN][odeNstartIndex+i]+this.sigma_0+m.sigma_l[tN][odeNstartIndex+i];
	}
}
