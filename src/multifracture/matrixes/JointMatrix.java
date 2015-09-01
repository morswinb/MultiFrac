package multifracture.matrixes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import multifracture.Model;
import multifracture.geometry.ConcreatePipe;
import multifracture.geometry.Crack;
import multifracture.geometry.Edge;
import multifracture.geometry.Junction;
import multifracture.geometry.Pipe;
import multifracture.geometry.Vertex;

public class JointMatrix implements Serializable{

	//1 points connected by joint flow calculation sham
	//concreate pipes connect whole clusters !!!!
	
	public boolean[][] joi;			//point to point connection
	public boolean[][] joi_edges;	//eadge to eadge connection

	private HashMap<Junction,Boolean> visitFlags;
	
	public JointMatrix(Model m){
		//joi=new boolean[m.grid_points.length][m.grid_points.length];
		joi=new boolean[m.odeN][m.odeN];
		joi_edges=new boolean[m.edges.size()][m.edges.size()];
		
		ArrayList<Junction> joints=new ArrayList<Junction>();
		
		for(Vertex v:m.vertices)
			if(v.getClass()==Junction.class)
				joints.add((Junction) v);
		
		
		Junction[] j=new Junction[joints.size()];
		joints.toArray(j);
		visitFlags=new HashMap<Junction,Boolean>();
		
		for(int i=0;i<j.length;i++){
			if(null==visitFlags.get(j[i])){
				visitFlags.put(j[i], true);
				traverse(j[i]);
			}
		}
		
		//System.out.println("done");
	}
	
	@SuppressWarnings("unchecked")
	private void traverse(Junction start) {
		
		ArrayList<Junction> adjected=new ArrayList<Junction>();
		LinkedList<Junction> new_adjected=new LinkedList<Junction>();
		
		new_adjected.add(start);
		
		while(new_adjected.size()>0){
			Junction e=new_adjected.poll();
			adjected.add(e);
			
			for(int i=0;i<e.eadges.size();i++){
				if(e.eadges.get(i).getClass()==ConcreatePipe.class){
					Junction ee=(Junction) e.eadges.get(i).vertex1;
					if(e==ee){
						ee=(Junction) e.eadges.get(i).vertex2;
					}
					
					if(visitFlags.get(ee)==null){
						new_adjected.add(ee);
						visitFlags.put(ee,true);
					}
				}
			}
		}
		
		for(int i=0;i<adjected.size();i++){
			for(int j=0;j<adjected.size();j++)
					apply(adjected.get(i),adjected.get(j));
		}
		
		//System.out.println(adjected.size());
	}
	
	//marks influence of source to target
	//adjected grid point for pipe source
	//adjected grid point and length for crack source
	//adjected point of source are affected
	public void apply(Junction source,Junction target){
		
		for(Edge e:source.eadges){
			if(e.getClass()!=ConcreatePipe.class){
				for(Edge f:target.eadges){
					if(f.getClass()!=ConcreatePipe.class){
						applyEadge(e,f,source,target);
					}
				}
			}
		}
	}

	private void applyEadge(Edge s,Edge t,Junction sv, Junction tv){
		
		int t1=t.odeNstartIndex+1;
		//int t2=t.startIndex+1;
		if(t.getClass()==Pipe.class && tv==((Pipe) t).vertex2){
			t1=t.odeNstartIndex+t.odeN-2;
			//t2=t.startIndex+t.ode_N-2;
		}
		
		int s1=s.odeNstartIndex+1;
		int s2=s.odeNstartIndex+2;
		
		if(s.getClass()==Pipe.class && sv==((Pipe) s).vertex2){
			s1=s.odeNstartIndex+s.odeN-2;
			s2=s.odeNstartIndex+s.odeN-3;
		}
		
		joi[t1][s1]=true;
		joi[t1][s2]=true;
		
		if(s.getClass()==Crack.class){
			joi[t1][s1+s.odeN-2]=true;
		}
		
		
		joi_edges[s.index][t.index]=true;
		
	}

	
	//debug print
	public void print(){
		for(int i=0;i<joi.length;i++){
			for(int j=0;j<joi.length;j++){
				System.out.print(joi[i][j]+" ");
			}
			System.out.println();
		}
	}
}
