package tests;

import java.io.Serializable;

import multifracture.Model;
import multifracture.geometry.Crack;
import multifracture.geometry.Vertex;
import multifracture.geometry.Pipe;
import multifracture.graphics.MultiCrackRenderer;
import multifracture.graphics.Renderer;
import multifracture.tools.PrintArray;

public class visibility_example_for_thesis implements Serializable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Model m=new Model();
		//MultiCrackRenderer r=null;
		MultiCrackRenderer r=new Renderer(m);		
		
			
		Vertex A=new Vertex(m,10,10);
		Vertex B=new Vertex(m,20,20);
		Vertex C=new Vertex(m,30,30);
		Vertex D=new Vertex(m,40,40);
		Vertex E=new Vertex(m,50,50);
		Vertex F=new Vertex(m,40,30);
		Vertex G=new Vertex(m,60,50);
		Vertex H=new Vertex(m,60,40);
		Vertex I=new Vertex(m,30,20);
		Vertex J=new Vertex(m,40,10);
		Vertex K=new Vertex(m,50,10);
		Vertex L=new Vertex(m,40,50);
		Vertex M=new Vertex(m,30,60);
		Vertex N=new Vertex(m,20,40);
		Vertex O=new Vertex(m,10,50);
		Vertex P=new Vertex(m,30,40);
		Vertex Q=new Vertex(m,20,50);
		Vertex S=new Vertex(m,50,30);
		Vertex R=new Vertex(m,50,60);
		Vertex T=new Vertex(m,10,60);
		
		
		new Pipe(m,A,B);
		new Pipe(m,B,C);
		new Pipe(m,C,D);
		new Pipe(m,D,E);
		
		new Pipe(m,C,N);
		new Pipe(m,N,Q);
		new Pipe(m,D,P);
		new Pipe(m,P,Q);
		new Pipe(m,B,I);
		new Pipe(m,I,J);
		new Pipe(m,I,F);
		new Pipe(m,C,F);
		new Pipe(m,L,E);
		new Pipe(m,P,L);
		
		new Crack(m,N,O);
		new Crack(m,J,K);
		new Crack(m,F,S);
		new Crack(m,D,H);
		new Crack(m,E,G);
		new Crack(m,L,M);
		new Crack(m,L,R);
				
		//System.out.println("YYYY");
		//for(int i=0;i<m.Y.length;i++){
		//	System.out.println(m.Y[i]);
		//}
		
		double[] y=m.ODE(1);
		PrintArray.printDouble(y);
		
		//y.clone();
		
	}

}
