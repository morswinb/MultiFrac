package tests;

import java.io.Serializable;

import multifracture.Model;
import multifracture.ODESolvers.LSODE_JNI_port;
import multifracture.ODESolvers.Solver;
import multifracture.geometry.Crack;
import multifracture.geometry.Vertex;
import multifracture.graphics.MultiCrackRenderer;
import multifracture.graphics.Renderer;
import multifracture.pumpinrate.ConstantPumpInRate;

public class single_crack implements Serializable {

	public static void main(String[] args) {
		
		
		Model m=new Model();
		//MultiCrackRenderer r=null;
		MultiCrackRenderer r=new Renderer(m);		
		
			
		Vertex A=new Vertex(m,0,0,new ConstantPumpInRate(1));
		Vertex B=new Vertex(m,10,0);
		Crack c1=new Crack(m,A,B);
						
		Solver sol=new LSODE_JNI_port(m,r);
		sol.compute(5);

		
		
	}

}
