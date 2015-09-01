package multifracture.matrixes;

import java.io.Serializable;

import multifracture.Model;
import multifracture.geometry.Crack;
import multifracture.geometry.Edge;
import multifracture.geometry.Pipe;

public class DiscretizationMatrix implements Serializable {

	//1 point connected by discretization, 0 not connected (tridiagonal structure+tip asymptotics and fracture length effects)
	public boolean[][] disc;

	public DiscretizationMatrix(Model m){
		
		//disc=new boolean[m.grid_points.length][m.grid_points.length];
		disc=new boolean[m.odeN][m.odeN];
		
		for(Edge e:m.edges){
			if(e.getClass()==Crack.class){
				doCrack((Crack) e);
			}
			else if(e.getClass()==Pipe.class){
				doPipe((Pipe) e);
			}
		}
	}
	
	private void doCrack(Crack c){
		
		
		int si=c.odeNstartIndex;
		
		disc[1+si][1+si]=true;
		disc[1+si][1+si+1]=true;
		disc[1+si][si+c.odeN-3]=true;
		disc[1+si][si+c.odeN-2]=true;
		disc[1+si][si+c.odeN-1]=true;
		
		for(int i=2;i<c.odeN-1;i++){
			disc[i+si][i+si-1]=true;
			disc[i+si][i+si]=true;
			disc[i+si][i+si+1]=true;
			
			disc[i+si][si+c.odeN-3]=true;
			disc[i+si][si+c.odeN-2]=true;
			disc[i+si][si+c.odeN-1]=true;
		}
		
		disc[si+c.odeN-1][si+c.odeN-3]=true;
		disc[si+c.odeN-1][si+c.odeN-2]=true;
		disc[si+c.odeN-1][si+c.odeN-1]=true;
		
	}
	
	private void doPipe(Pipe p){
		
		int si=p.odeNstartIndex;
		
		disc[1+si][1+si]=true;
		disc[1+si][1+si+1]=true;
		
		for(int i=2;i<p.odeN-2;i++){
			disc[i+si][i+si-1]=true;
			disc[i+si][i+si]=true;
			disc[i+si][i+si+1]=true;
		}
		
		disc[si+p.odeN-2][si+p.odeN-3]=true;
		disc[si+p.odeN-2][si+p.odeN-2]=true;
		
	}
	
	
	
	//debug print
	public void print(){
		for(int i=0;i<disc.length;i++){
			for(int j=0;j<disc.length;j++){
				System.out.print(disc[i][j]+" ");
			}
			System.out.println();
		}
	}
}
