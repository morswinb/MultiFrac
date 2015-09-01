package multifracture.geometry;



import java.io.Serializable;

import multifracture.Model;
import multifracture.Variables;
import multifracture.tools.SelfSimilar;


public class Crack extends Edge implements Serializable{

	
	private static final long serialVersionUID = -6622180054439956236L;

	//tip and asymptotic variables
	private double x1,x2,A1,A2,B1,B2;
	public double p1,p2;
	
	public Crack(Model m, Vertex j1,Vertex j2) {
		super(m,j1,j2);
	}
	
	public Crack(Model m,Crack c, Vertex j1,Vertex j2) {
		super(m,c,j1,j2);
	}
	
	@Override
	protected void innerConstructor() {
		
		p1=Variables.asymptotics_powers[0];
		p2=Variables.asymptotics_powers[1];
		x1=1-grid1D.x[N-2];
		x2=1-grid1D.x[N-3];
		
		A1=Math.pow(Math.pow(x2,p1)-Math.pow(x2,p2)/Math.pow(x1,(p2-p1)),-1);  
		A2=-Math.pow((x2/x1),p2)*A1;            
		B1=-A1/Math.pow(x1,(p2-p1));
		B2=1/Math.pow(x1,p2)-A2/Math.pow(x1,(p2-p1));
		
		
		w=SelfSimilar.w(Variables.initial_self_similar_t,grid1D.x);
		this.pf=w.clone();
		for(int i=0;i<pf.length;i++){
			pf[i]*=k;
		}
		
	}
	

	
	//@Override
	//public GridPoint[] getGridPoints() {
	public void updateGridPoints(){
				
		for(int i=0;i<grid1D.x.length;i++){
			gridPoints2D[i].x=vertex1.x+grid1D.x[i]*Lx*L;
			gridPoints2D[i].y=vertex1.y+grid1D.x[i]*Ly*L;
		}
		
		gridPoints2D[grid1D.x.length].x=vertex2.x;
		gridPoints2D[grid1D.x.length].y=vertex2.y;

	}


	@Override
	public double getL(int tN) {
		return Math.sqrt(m.Y_work[tN][odeNstartIndex+N-1]);
	}
	
	
	
	/*
	public double getSingleVal(double xx){
		this.interpolate(w);
		double[] AB=find_fixed_AB_tip_asymptotics();
		//System.out.println("crack single val !!!");
		//System.out.println(AB[0]*Math.pow(1-xx,p1)+AB[1]*Math.pow(1-xx,p2));
		//System.out.println(cs.val(xx));
		//System.out.println(cs.val(xx)+AB[0]*Math.pow(1-xx,p1)+AB[1]*Math.pow(1-xx,p2));
		return cs.val(xx)+AB[0]*Math.pow(1-xx,p1)+AB[1]*Math.pow(1-xx,p2);
		
	}*/
	
	/*
	@Override
	public double getVolume(double x1,double x2) {
		
		//System.out.println(getTrapezoidalVolume());
		///System.exit(1);
		//return 2*getTrapezoidalVolume();
		
        this.interpolate(w);
        
        double[] AB=find_fixed_AB_tip_asymptotics();
        
        //System.out.println("int "+cs.integral(x1,x2));
        
        return (cs.integral(x1,x2)+AB[0]/(p1+1)*(Math.pow(1-x1,p1+1)-Math.pow(1-x2,p1+1))
        						 +AB[1]/(p2+1)*(Math.pow(1-x1,p2+1)-Math.pow(1-x2,p2+1)))*L;
        //return (AB[0]/(p1+1)*(Math.pow(1-x1,p1+1)-Math.pow(1-x2,p1+1))+AB[1]/(p2+1)*(Math.pow(1-x1,p2+1)-Math.pow(1-x2,p2+1)))*L;
	}
	*/
	
	/**
	 * gets volume corresponding to different w data
	 * @param x1
	 * @param x2
	 * @param w_new
	 * @return
	 */
	
	/*
	public double getVolume(double x1,double x2,double[] w_new) {
        this.interpolate(w_new);
        double[] AB=find_fixed_AB_tip_asymptotics();
        
        return (cs.integral(x1,x2)+AB[0]/(p1+1)*(Math.pow(1-x1,p1+1)-Math.pow(1-x2,p1+1))
        						 +AB[1]/(p2+1)*(Math.pow(1-x1,p2+1)-Math.pow(1-x2,p2+1)))*L;
	}
	*/
	
	/**
	 * inacurate volumes left for testing in the future
	 */
	
	/*
	private void interpolate(double[] ww){
		
		double[] AB=find_fixed_AB_tip_asymptotics();		        
        double[] f=new double[grid1D.N+1];
        for(int i=0;i<grid1D.N;i++){
        	f[i]=w[i]-AB[0]*Math.pow((1-grid1D.x[i]),p1)-AB[1]*Math.pow((1-grid1D.x[i]),p2);
        }
        f[grid1D.N]=0;
        
        double [] xx=Arrays.copyOf(grid1D.x,grid1D.N+1);
        xx[grid1D.N]=1;
                
        cs=Spline.interpolate(xx,f);		
	}
	*/
	
	public double[] tipAsymptotics(int tN){
		//A=A1*w[grid.N-2]+A2*w[grid.N-1];
        //B=B1*w[grid.N-2]+B2*w[grid.N-1];
		double A=A1*m.Y_work[tN][odeNstartIndex+N-3]+A2*m.Y_work[tN][odeNstartIndex+N-2];
		double B=B1*m.Y_work[tN][odeNstartIndex+N-3]+B2*m.Y_work[tN][odeNstartIndex+N-2];
        return new double[]{A,B};
	}
	
	public double[] tipAsymptotics(){
		//double A=A1*m.Ys[startIndex+odeN-3]+A2*m.Ys[startIndex+odeN-2];
		//double B=B1*m.Ys[startIndex+odeN-3]+B2*m.Ys[startIndex+odeN-2];
		double A=A1*w[N-3]+A2*w[N-2];
		double B=B1*w[N-3]+B2*w[N-2];
		return new double[]{A,B};
	}
	
	@Override
	public double[] getQiAnddQi(Vertex j,int tN){
		
		double x1,x2;
		double pf1,pf2;
		x1=grid1D.x[1];
		x2=grid1D.x[2];
		double L=Math.pow(m.Y_work[tN][odeNstartIndex+N-1],.5);
		
		double sigma_l=m.sigma_l[tN][odeNstartIndex];
		pf1=this.k*m.Y_work[tN][odeNstartIndex+1]+this.sigma_0+m.sigma_l[tN][odeNstartIndex+1];
		pf2=this.k*m.Y_work[tN][odeNstartIndex+2]+this.sigma_0+m.sigma_l[tN][odeNstartIndex+2]; 
				
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
		
		//System.out.println(" J "+J+" pf1 "+pf1+" pf2 "+pf2);
		//System.out.println(" w1 "+w+" w2 "+m.Y[tN][startIndex+1]+" w3 "+m.Y[tN][startIndex+2]);
		
		
		//System.out.println("crack.java q "+1/M/L/k/k/k*w3*(alpha*J+beta));
		
		return new double [] {1/M/L/k/k/k*p3*(alpha*J+beta),1/M/L/k/k/k*(3*p2*(alpha*J+beta)+p3*alpha)};
		//return new double [] {1/M/L/k/k/k*(alpha*J*J*J*J+beta*J*J*J),1/M/L/k/k/k*(4*alpha*J*J*J+3*beta*J*J)};
	}

	
	public void assemblyY(double[] Y) {
			
		int k=odeNstartIndex;
		for(int i=0;i<N-1;i++){
			Y[k++]=w[i];
		}
		
		Y[odeNstartIndex+N-1]=L*L;
			
	}
	
	
	public void setY(double[] Y){
		
		//PrintArray.printDouble(w);
		
		
				
		int k=0;
		for(int i=odeNstartIndex;i<odeNstartIndex+N-1;i++){
			w[k++]=Y[i];
		}
					
		//w[0]=vertex1.getFixedPressure()/k;
		L=Math.pow(m.Y[odeNstartIndex+N-1],.5);
		this.vertex2.x=ox+Lx*L;
		this.vertex2.y=oy+Ly*L;
		
		//System.out.println("vertex 1 fluid pressure "+vertex1.getFixedFluidPressure());
		w[0]=(vertex1.getPressure()-this.sigma_0-m.sigma_l[odeNstartIndex][0])/this.k;
		
		this.updateGridPoints();
		
		
		//System.out.println("w0 "+w[0]+" w1 "+w[1]);
		//for(int i=0;i<pf.length;i++){
		//	pf[i]=getFluidPressure(this.grid1D.x[i]);
		//}
	}

	@Override
	public void dY(double t,int i_global,int tN){

		int i_local=i_global-odeNstartIndex;
		if(i_local==N-1){
			//length calculation
			double[] AB=tipAsymptotics(tN);
			double w_0=AB[0];
			m.dY[tN][i_global]=2.0*k/M/3.0*w_0*w_0*w_0;
		}
		
		else{
			this.ode.dY(t,i_global, tN);	
		}	
	}

	/*
	@Override
	public double getFlowRate(double x) {
		int i=Math.abs(Arrays.binarySearch(grid1D.x,x));
		double x1=grid1D.x[i-1];
		double x2=grid1D.x[i];
		double x3=grid1D.x[i+1];
		double w1=w[i-1];
		double w2=w[i];
		double w3=w[i+1];
		
		double L=this.vertex1.distance(this.vertex2);
		double dw=((w3-w2)/(x3-x2)+(w2-w1)/(x2-x1))/2;
		
		return-this.k/this.M/L*w2*w2*w2*dw;
	}
	*/
	
	@Override
	public double getFluidPressureAtXi(int i, int tN) {
		
		if(i==this.N-1) // width at crack tip is zero, so return only sigma terms
			return this.sigma_0+m.sigma_l[tN][odeNstartIndex+i];
		else
			return this.k*m.Y_work[tN][odeNstartIndex+i]+this.sigma_0+m.sigma_l[tN][odeNstartIndex+i];
	}
	
	/*
	@Override
	public double getW(int local_index, int tN) {
		if(local_index==0){
			//System.out.println("local index 0");
			System.out.println("w "+vertex1.getPressure(tN)/k);
			return (vertex1.getPressure(tN)-sigma_0-elasticity.getSigma(0, tN))/k;
		}
		else
			return m.Y[tN][this.startIndex+local_index];
	}
	*/

	@Override
	public boolean transparent() {
		// TODO Auto-generated method stub
		return false;
	}
}
