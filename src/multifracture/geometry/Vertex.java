package multifracture.geometry;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

import multifracture.Model;
import multifracture.pumpinrate.PumpInRate;

public class Vertex extends Point2D.Double implements Serializable{
	
	public ArrayList<Edge> eadges;
	public int index;
	public boolean iterable; //indicates if this vertex should be subjected to iterations when computing pressure
	public Model m;
	public double pressure[];
	public double fixed_pressure;
	public PumpInRate pr;
	
	public Vertex(Model m,double x, double y){
		super(x,y);
		eadges=new ArrayList<Edge>();
		this.m=m;
		m.add(this);
	}
	
	public Vertex(Model m,double x, double y, PumpInRate pr){
		super(x,y);
		eadges=new ArrayList<Edge>();
		this.m=m;
		this.pr=pr;
		m.add(this);
	}
	
	public Vertex(Model m,Vertex v){
		super(v.x,v.y);
		eadges=(ArrayList<Edge>) v.eadges.clone();
		this.m=m;
		this.pr=v.pr;
		m.add(this);
	}
	
	
	public void add(Edge e){
		eadges.add(e);
	}
	
	public void remove(Edge e){
		eadges.remove(e);
	}
	
	public void setMaxtN(int tN){
		this.pressure=new double[tN];
	}
	
	
	public void aproximateInitialFluidPressure(double t,int tN){
		
		double initial=0;
		int cpipes=0;
		for(Edge e:eadges){
			if(e.vertex1==this)
				initial+=e.getFluidPressureAtXi(0,tN);
			else
				initial+=e.getFluidPressureAtXi(e.N-1,tN);
			if(e.getClass()==ConcreatePipe.class)
				cpipes++;
		}
		if(eadges.size()-cpipes==0)
			this.pressure[tN]=10; //desperacja
		else
			this.pressure[tN]=initial/(eadges.size()-cpipes);
	}
	
	
	//evaluates fluid pressure value a a vertex
	public double evaluateFluidPressure(double t,int tN){
		double tol=0;		
		double Q=0;
		double dQ=0;

		double [] qq=null;
		//sum flows from connected edges
		for(int i=0;i<eadges.size();i++){
			qq=eadges.get(i).getQiAnddQi(this, tN);
			Q+=qq[0];
			dQ+=qq[1];
		}
		
		//add pumping rate
		if(pr!=null)
			Q+=pr.getQ0(t);
		
		pressure[tN]-=Q/dQ;
		tol=Math.abs(Q);	
		//pressure[tN]=J1;
		//System.out.println("pressure "+pressure[tN]);
		if(java.lang.Double.isNaN(pressure[tN])){ // accounts for a rare bug when if all elements are of 0 width the result pressure is a NaN
			pressure[tN]=0;
			//System.out.println("pressure "+pressure);
		}
		
		return tol;
	}
	
	public double getPressure(int tN){
		return pressure[tN];
	}
	
	public double getPressure(){
		return pressure[0];
	}
	

}
