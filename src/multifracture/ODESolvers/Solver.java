package multifracture.ODESolvers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import multifracture.Model;
import multifracture.Variables;
import multifracture.geometry.Edge;
import multifracture.graphics.MultiCrackRenderer;
import multifracture.tools.NanoTimer;

public abstract class Solver implements Serializable{

	/**
	 * 
	 * My double step strategy solver implementation, inner step for fracture growth, outer step for colisions and elasticity matrix calculations
	 * 
	 */
	private static final long serialVersionUID = -8195768036419638744L;
	protected Model m;
	protected Model m1;
	public LinkedList<Model> m_list;

	public MultiCrackRenderer r;
	
	public Solver(Model m,MultiCrackRenderer r){
		this.m=m;
		this.r=r;
		m_list=new LinkedList<Model>();
		m_list.add(m);
		//intersect=new ModelIntersections();
	}
	
	public void compute(double t_end){
		
		//reset volumes
		m.V0=m.getTotalVolume();
		m.Q0=0;
		m.Ql=0;
		
		double t=0;
		double advance_t=Variables.geometry_check_t;
		
		//initial check for intersections ???
		//ModelIntersections intersect=new ModelIntersections(m);
		
		while(t<=t_end){
		
			
			m=new Model(m);
			
			//compute model for next time step
			advance(advance_t);
			t+=advance_t;

			
			m_list.add(m);
			
			if(r!=null){
				r.setModel(m);
			}
			
		}
	}
	
	public void PrintModelHistory(){
		for(Model m:m_list)
			this.printM(m);
	}
	
	private void printM(Model m){
		System.out.println("t="+m.t+"\t\tbalance="+m.bal_rel+"\t\tedges="+m.edges.size()+"\t\tvertexes="+m.vertices.size());
	}
	
	public void PrintModelBalanceAndFractures(){
		for(Model m:m_list)
			this.printB(m);
	}
	
	private void printB(Model m){
		System.out.format("%e %e",m.t,m.bal_rel);
		for(Edge e:m.edges)
			System.out.format(" %e ",e.L);
		System.out.format("\n");
	}
	
	// solver should attempt to update the model by this time step.
	public abstract void advance(double advanceTime);
	
}
