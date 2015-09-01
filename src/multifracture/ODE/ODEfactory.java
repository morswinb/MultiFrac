package multifracture.ODE;

import multifracture.geometry.Crack;
import multifracture.geometry.Edge;
import multifracture.geometry.Pipe;

public class ODEfactory {
	
	public static void setODE(Edge e){
		
		if(e.getClass()==Pipe.class){
			//e.setODE(new PipeOdeW((Pipe) e));
			e.setODE(new PipeOdeU((Pipe) e));
		}
		
		if(e.getClass()==Crack.class){
			e.setODE(new CrackOdeW((Crack) e));
			//e.setODE(new CrackOdeWasym((Crack) e));
		}
	}
		
	/*	
		if(Variables.CrackFlow==0)		c.flow=new CrackFlow_w(c);
		//else if(Variables.CrackFlow==1)	c.flow=new CrackFlow_U(c);
		else if(Variables.CrackFlow==2)	c.flow=new CrackFlow_w_asym(c);	
		
		if(Variables.LeakOff==0)		c.leakOff=new ZeroLeakOff(c);
		
		if(Variables.Elatsicity==0)		c.elasticity=new ZeroElasticity(c);
		else if(Variables.Elatsicity==1)c.elasticity=new Elasticity8b(c);
		else if(Variables.Elatsicity==2)c.elasticity=new Elasticity8c(c);
		else if(Variables.Elatsicity==3)c.elasticity=new Elasticity8d(c);
		//else if(Variables.Elatsicity==1)c.elasticity=new Elasticity3Crack(c);
		//else if(Variables.Elatsicity==2)c.elasticity=new Elasticity2(c);
		//else if(Variables.Elatsicity==3)c.elasticity=new Elasticity3(c);
		
		
	}
	
	public static void setOperators(Pipe p){
		
		if(Variables.PipeFlow==0)		p.flow=new PipeFlow_w(p);
		else if(Variables.PipeFlow==1)	p.flow=new PipeFlow_U(p);
		//p.flow=new PipeFlow_U(p);
		
		if(Variables.Elatsicity==0)		p.elasticity=new ZeroElasticity(p);
		else if(Variables.Elatsicity==1)p.elasticity=new Elasticity8b(p);
		else if(Variables.Elatsicity==2)p.elasticity=new Elasticity8c(p);
		else if(Variables.Elatsicity==3)p.elasticity=new Elasticity8d(p);
		
		p.leakOff=new ZeroLeakOff(p);
		
	}*/
}
