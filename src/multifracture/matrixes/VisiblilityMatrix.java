package multifracture.matrixes;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

import multifracture.Model;
import multifracture.Variables;
import multifracture.geometry.ConcreatePipe;
import multifracture.geometry.Crack;
import multifracture.geometry.Edge;
import multifracture.geometry.GridPoint2D;
import multifracture.geometry.Junction;
import multifracture.geometry.Pipe;
import multifracture.geometry.Vertex;
import multifracture.tools.LineIntersection;

public class VisiblilityMatrix implements Serializable{

	
	//1 full visiblility, 0 no visibility, in between partial visibility
	public double[][] visiblilty;
	
	
	//simplified array that shows if eadges are visible to each other
	public boolean[][] eadge_visibility;
	
	
	//calculation of overall visibility
	public VisiblilityMatrix(Model m){
		
		visiblilty=new double[m.grid_points.length][m.grid_points.length];
		eadge_visibility=new boolean[m.edges.size()][m.edges.size()];
						
		
		IntStream.range(0, m.grid_points.length).forEach(
				i -> resolve_for_point(m,i)
		);
		
		
		//Sloppy correction for some edges that are visible only in one direction, this forces symmetric matrix output
		IntStream.range(0, m.grid_points.length).forEach( i -> {
					for(int j=i+1;j<visiblilty.length;j++){
						if(visiblilty[i][j]==1 || visiblilty[j][i]==1){
							visiblilty[i][j]=1;
							visiblilty[j][i]=1;
							
							eadge_visibility[m.grid_points[i].parent.index][m.grid_points[j].parent.index]=true;
							eadge_visibility[m.grid_points[j].parent.index][m.grid_points[i].parent.index]=true;
						}
					}
				}
		);

		
	}
	
	/**
	 * 
	 * @param m 
	 * @param i - index of the grid point visibility algorithm is called on
	 */
	private void resolve_for_point(Model m,int i){
		
		//sort vertexes by angle and find the change in angle
		Vertex[] sorted_vertexs=(Vertex[]) m.vertices.toArray();
		double[] angleStep=new double[sorted_vertexs.length];
		
		GridPoint2D source_point=m.grid_points[i];
		
		LineAngleComperator angleComp=new LineAngleComperator(source_point,sorted_vertexs[0]);
		Arrays.sort(sorted_vertexs,angleComp);	
		
		System.out.println("====="+source_point.parent.vertex1+"\t"+source_point.parent.vertex2);
		for(int k=0;k<sorted_vertexs.length;k++){
			System.out.println(Integer.toHexString(System.identityHashCode(sorted_vertexs[k])));
		}
		System.out.println("=====");
		
		HashMap<Vertex,Integer> sort_order=new HashMap<Vertex,Integer>();
		for(int j=0;j<sorted_vertexs.length;j++){
			sort_order.put(sorted_vertexs[j], j);
			angleStep[j]=angleComp.angleDifference(sorted_vertexs[j],sorted_vertexs[(j+1)%sorted_vertexs.length]);	
		}
		
		
		//last angle is negative so flip it
		angleStep[sorted_vertexs.length-1]=2*Math.PI+angleStep[sorted_vertexs.length-1];
		
		ArrayList<Edge> eadges=(ArrayList<Edge>) m.edges.clone();
		eadges.remove(source_point.parent); // do not count the edge that is the same as parent edge
		EadgeProjectionView epv=new EadgeProjectionView(sorted_vertexs);
		
		Line2D.Double[] rays=new Line2D.Double[sorted_vertexs.length];
		for(int j=0;j<sorted_vertexs.length;j++)
			rays[j]=new Line2D.Double(sorted_vertexs[j],source_point);
		
		
		for(int j=0;j<eadges.size();j++){
			
			Edge e=eadges.get(j);

			if(!e.transparent()){

				//what if sort order mixed ? - probably fixed
				int e1=sort_order.get(e.vertex1);
				int e2=sort_order.get(e.vertex2);
				if(e1>e2){
					int swap=e1;
					e1=e2;
					e2=swap;
				}
		
				double dangle1=0;
				double dangle2=0;
				
				for(int k=e1;k<e2;k++){
					dangle1+=angleStep[k];
				}
					
				for(int k=e2;k<e1+angleStep.length;k++){
					dangle2+=angleStep[k%angleStep.length];
				}
					
				if(dangle1<dangle2){
					while(e1<e2){						
						epv.addProjection(new EadgeProjection(e,source_point,rays[e1],rays[e1+1]),e1);
						e1++;
					}
				}
				else{
					while(e2<(e1+sorted_vertexs.length)){
						epv.addProjection(new EadgeProjection(e,source_point,rays[e2%sorted_vertexs.length],rays[(e2+1)%sorted_vertexs.length]),e2%sorted_vertexs.length);
						e2++;
					}
				}
			}
		}
		
		for(int kk=0;kk<epv.p.length;kk++){
			if(epv.p[kk]!=null){
				
				if(angleStep[kk]>1e-3){	//again ignore small angle change 
				
					int[] projected_indexes=epv.p[kk].getProjectedIndexes();
					GridPoint2D[] epgrid=epv.p[kk].e.gridPoints2D;
					//System.out.println("eadge "+close_eadge.index+" projection from "+epv.p[kk].e.index+" projected indexes "+projected_indexes[0]+" "+projected_indexes[1]);
					
					if(projected_indexes!=null){
						for(int h=projected_indexes[0];h<projected_indexes[1];h++){
							//point visibility
							visiblilty[i][epgrid[h].global_index]=1;
												
							Edge se=m.grid_points[epgrid[h].global_index].parent;
							
							//eadge visibility
							eadge_visibility[m.grid_points[i].parent.index][se.index]=true;
														
							//applies visibility of L to all crack points
							if(se.getClass()==Crack.class){
								visiblilty[i][se.odeNstartIndex+se.odeN-1]=1;
							}
							
						}
					}
				}
			}		
		}
		
		
	}
	
	private class EadgeProjection implements Serializable{

		public Edge e;
		GridPoint2D source_point;
		double distance_to_source;
		
		Point2D.Double intersect1;
		Point2D.Double intersect2;
		
		//This class represents a slice of Eadge as viewed form a Grid Point
		public EadgeProjection(Edge e,GridPoint2D source_point,Line2D.Double ray1,Line2D.Double ray2){
			this.e=e;
			this.source_point=source_point;
			
			Line2D.Double edgeLine=new Line2D.Double(e.vertex1,e.vertex2);
			
			intersect1=LineIntersection.getIntersect2(edgeLine, ray1);
			intersect2=LineIntersection.getIntersect2(edgeLine, ray2);
			
			//System.out.println("==============");
			//System.out.println("Eadge "+e);
			//System.out.println("vertex1 "+e.vertex1);
			//System.out.println("vertex2 "+e.vertex2);
			//System.out.println("intersect1 "+intersect1);
			//System.out.println("intersect2 "+intersect2);
			//System.out.println("dist 1 "+source_point.distance(e.vertex1)+" dist 2 "+source_point.distance(e.vertex2));
			
			//System.out.println("ray1 "+ray1.getP1()+" "+ray1.getP2());
			//System.out.println("ray2 "+ray2.getP1()+" "+ray2.getP2());
			//System.out.println("==============");
			
			
			//segment parallel to ray ignore by setting large distance
			if(intersect1==null || intersect2 ==null){
				distance_to_source=1e10;
			}
			
			//segments directly connected to point, ignore by setting large discance
			else if(source_point.distance(e.vertex1)<1e-6 || source_point.distance(e.vertex2)<1e-6){
				distance_to_source=1e10;
			}
			else{
				distance_to_source=(intersect1.distance(source_point)+intersect2.distance(source_point))/2;
			}

		}
		
		public int[] getProjectedIndexes(){

			//co tutaj robi ten null ?
			if(intersect1== null || intersect2==null)
				return null;
			
			double d1=intersect1.distance(e.vertex1);
			double d2=intersect2.distance(e.vertex1);
			
			if(d2<=d1){
				Point2D.Double swap=intersect2;
				intersect2=intersect1;
				intersect1=swap;

			}
			
			d1=intersect1.distance(e.vertex1);
			d2=intersect2.distance(e.vertex1);
			//System.out.println("d1 "+d1+"  d2 "+d2);
			

			int k=0;
			GridPoint2D[] e_grid=e.gridPoints2D;
			while(k<e_grid.length && e_grid[k].distance(e.vertex1)<d1-1e-8) //1e-8 adds a small error margin for calculation of almost overlapping points
				k++;
			
			int i1=k;
			while(k<e_grid.length && e_grid[k].distance(e.vertex1)<d2+1e-8)
				k++;
				
			int i2=k;
			
			//if(i2-i1<3){
			//	return null;
			//}
			//System.out.println(i1+" "+i2+"\t "+e_grid+" length \t"+e_grid.length);
			
			return new int[]{i1,i2};
			
		}
	}
	
	private class EadgeProjectionView {
		
		public EadgeProjection[] p;
		//public ArrayList[] p_a;
		
		//Vertex[] vertexes;
		
		//holds a 360 view from a point
		public EadgeProjectionView(Vertex[] vertexes){
			p=new EadgeProjection[vertexes.length];
			
			//p_a=new ArrayList[vertexes.length];
			//for(int i=0;i<vertexes.length;i++){
			//	p_a[i]=new ArrayList();
			//}
			
			//this.vertexes=vertexes;
		}
		
		
		public void addProjection(EadgeProjection ep,int i){
			
			//p_a[i].add(ep);
			
			if(p[i]==null)
				p[i]=ep;
			else if(p[i].distance_to_source>ep.distance_to_source)
				p[i]=ep;
		}
	}
	
	private class LineAngleComperator implements Serializable,Comparator<Point2D.Double>{

		Point2D.Double source_point,closest_point;
		double ox;
		double oy;
		//double Nx;
		//double Ny;
		double closest_angle;
		
		public LineAngleComperator(Point2D.Double source_point,Point2D.Double closest_point){
			this.source_point=source_point;
			this.closest_point=closest_point;
			ox=source_point.x;
			oy=source_point.y;
			
			double Nx=closest_point.x-source_point.x;
			double Ny=closest_point.y-source_point.y;
			double mag=Math.sqrt(Nx*Nx+Ny*Ny);
			Nx=Nx/mag;
			Ny=Ny/mag;
			
			closest_angle=Math.atan2(Ny,Nx)+Math.PI;
			//System.out.println(closest_angle);
			
		}
		
		@Override
		public int compare(Point2D.Double o1, Point2D.Double o2) {
			
			//compears angles or returns closer
			int ang=-(int) Math.signum(angleDifference(o1,o2));
			if(ang==0){
				double d1=Math.sqrt((ox-o1.x)*(ox-o1.x)+(oy-o1.y)*(oy-o1.y));
				double d2=Math.sqrt((ox-o2.x)*(ox-o2.x)+(oy-o2.y)*(oy-o2.y));
				
				ang=-(int) Math.signum(d1-d2);
				//double d1;
				
			}
			return ang;
		}
		
		
		public double angleDifference(Point2D.Double o1, Point2D.Double o2) {
			
			//finds difference between angles
			double a1=getAngle(o1);
			double a2=getAngle(o2);		
			return getAngle(o1)-getAngle(o2);
		}

		//returns angle between two vectors measured counter in range <0;2*Pi)
		private double getAngle(Point2D.Double p){
			return Math.atan2(p.y-oy,p.x-ox)+Math.PI;
		}
		
	}
	
	//debug print
	public void print(){
		for(int i=0;i<visiblilty.length;i++){
			for(int j=0;j<visiblilty.length;j++){
				System.out.print(visiblilty[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	//debug print
	public void printEadge(){
		for(int i=0;i<eadge_visibility.length;i++){
			for(int j=0;j<eadge_visibility.length;j++){
				if(eadge_visibility[i][j]){
					System.out.print(1+" ");
				}
				else{
					System.out.print(0+" ");
				}
				
			}
			System.out.println();
		}
	}
}


