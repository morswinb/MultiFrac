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

public class close_cracks_wyth_angle implements Serializable {

	public static void main(String[] args) {
		
		
		Model m=new Model();
		//MultiCrackRenderer r=null;
		MultiCrackRenderer r=new Renderer(m);		
		
			
		Vertex A=new Vertex(m,0,0,new ConstantPumpInRate(1));
		Vertex B1=new Vertex(m,10,0);
		Vertex B2=new Vertex(m,-10,0);
		Vertex B3=new Vertex(m,2*3.1622,2*3.1622);
		Vertex B4=new Vertex(m,2*-3.1622,2*-3.1622);
		
		Crack c1=new Crack(m,A,B1);
		Crack c2=new Crack(m,A,B2);
		Crack c3=new Crack(m,A,B3);
		Crack c4=new Crack(m,A,B4);
		
		
		//Solver sol=new LSODE_JNI_port(m,r);
		//sol.compute(5);

		
		
	}

}
