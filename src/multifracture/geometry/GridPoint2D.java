package multifracture.geometry;

import java.awt.geom.Point2D;
import java.io.Serializable;

import multifracture.Model;

public class GridPoint2D extends Point2D.Double implements Serializable{

	public Edge parent;
	private Model m;
	
	public double Lx;	//propagation x component direction
	public double Ly;	//propagation y component direction

	public double ox;
	public double oy;
	
	public int global_index;	//this element index in system matrix
	public int local_index; 	//this element index in parent edge
	
	public double xx; //grid point position for this point;
	private double xLx;
	private double xLy;
	
	
	public double norm_L; //length the area around this grid point as a share of  0 to 1 x interval
	
	public GridPoint2D(Model m,Edge eadge,double xx,double norm_L,int local_index) {
		this.parent=eadge;
		this.norm_L=norm_L;
		this.m=m;
		//this.index=index;
		this.local_index=local_index;
		this.global_index=eadge.odeNstartIndex+local_index;
		Lx=parent.Lx;
		Ly=parent.Ly;
		this.xx=xx;
		this.ox=parent.vertex1.x;
		this.oy=parent.vertex1.y;
		this.xLx=xx*Lx;
		this.xLy=xx*Ly;
	}
	
	//public double getw(int tN){
	//	return parent.getW(local_index,tN);
	//}
	
	public double getSigma_l(int tN){
		return m.sigma_l[tN][global_index];
	}
	
	public double distance(int tN,double x,double y){
		
		double L=parent.getL(tN);
		double dx=(parent.vertex1.x+xLx*L)-x;
		double dy=(parent.vertex1.y+xLy*L)-y;
		
		return Math.sqrt(dx*dx+dy*dy);	
	}
	
	public double getX(int tN){
		return parent.vertex1.x+xx*Lx*parent.getL(tN);
	}
	
	public double getY(int tN){
		return parent.vertex1.y+xx*Ly*parent.getL(tN);
	}
}
