package multifracture.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

import multifracture.Model;
import multifracture.geometry.BackgroundPipe;
import multifracture.geometry.ConcreatePipe;
import multifracture.geometry.Crack;
import multifracture.geometry.Edge;
import multifracture.geometry.Pipe;
import multifracture.geometry.Vertex;

public class PressureColorView extends DrawingScheme implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6845376259417119503L;
	Color[] colors=byrColorScale();
	
	PressureColorView(Model m,Renderer r){
		super(m,r);
	}
	
	void paintJoint(Graphics2D g, Vertex j) {
		//int x1=(int) Math.round(j.x*r.scale)+r.mx;
		//int y1=-(int)Math.round(j.y*r.scale)+r.my;
		//g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.8f));
		//g.drawOval((int)Math.round(x1-r.scale/3.0),(int)Math.round(y1-r.scale/3.0),(int) Math.round(r.scale/1.5),(int) Math.round(r.scale/1.5));
	}	
	
	
	void paintVertex(Graphics2D g, Vertex v){
		g.setStroke(new BasicStroke((float) 4));
		g.setColor(Color.BLACK);
		int x1=(int) Math.rint(v.x*r.scale)+r.mx;
		int y1=-(int)Math.rint(v.y*r.scale)+r.my;
		
		g.drawString(Integer.toHexString(System.identityHashCode(v)), x1+10, y1-10);
		g.fillOval(x1-5, y1-5, 10, 10);
	}
	
	@Override
	void paintEadge(Graphics2D g, Edge e) {
		
				
		if(e.getClass()==ConcreatePipe.class){
			g.setStroke(new BasicStroke((float) 4));
			this.paintConcreatePipe(g,(ConcreatePipe) e);
		}
		
		//else if(e.getClass()==ClosedCrack.class){
		//	g.setStroke(new BasicStroke((float) 1));
		//	this.paintClosedCrack(g, (ClosedCrack) e);
		//}
		
		else{
			g.setStroke(new BasicStroke((float) 4));
			double[] x=e.grid1D.x;
			double[] pf=e.pf;
			double L=e.vertex1.distance(e.vertex2)*r.scale;
						
			int x1=(int) Math.rint(e.vertex1.x*r.scale)+r.mx;
			int y1=-(int)Math.rint(e.vertex1.y*r.scale)+r.my;
			int x2=(int) Math.rint(e.vertex2.x*r.scale)+r.mx;
			int y2=-(int)Math.rint(e.vertex2.y*r.scale)+r.my;
			
			//System.out.println("======");
			//System.out.println(x1);
			//System.out.println(x2);
			//System.out.println(y1);
			//System.out.println(y2);
			//System.out.println("======");
			
			double dx=(x2-x1+0.0);
			double dy=(y2-y1+0.0);		
			double mag=1/Math.sqrt(dx*dx+dy*dy);
			dx=dx*mag;
			dy=dy*mag;
			
			
			//for(int i=0;i<pf.length;i++)
			//	System.out.println("drawing "+pf[i]);
			//System.out.println("======");
				
			try{
				for(int i=1;i<x.length;i++){
					g.setColor(colors[((int)((pf[i-1]+pf[i])/2/r.max_fluid_pressure*(colors.length-2)))]);
					g.drawLine((int)Math.rint(x1+x[i-1]*L*dx),(int)Math.rint(y1+x[i-1]*L*dy),(int)Math.rint(x1+x[i]*L*dx),(int)Math.rint(y1+x[i]*L*dy));
				}
			}
			catch(Exception ee){
				//ee.printStackTrace();
			}
		}	
	}
		
	void paintConcreatePipe(Graphics2D g,ConcreatePipe p){
		//double L=p.L*r.scale;
		int x1=(int) Math.rint(p.vertex1.x*r.scale)+r.mx;
		int y1=-(int)Math.rint(p.vertex1.y*r.scale)+r.my;
		int x2=(int) Math.rint(p.vertex2.x*r.scale)+r.mx;
		int y2=-(int)Math.rint(p.vertex2.y*r.scale)+r.my;
	    g.setColor(Color.gray);
	    g.drawLine(x1,y1,x2,y2);
	}
	
	@Override
	void paintBackgroundPipe(Graphics2D g, BackgroundPipe p) {
		//double L=p.L*r.scale;
		int x1=(int) Math.rint(p.end1.x*r.scale)+r.mx;
		int y1=-(int)Math.rint(p.end1.y*r.scale)+r.my;
		int x2=(int) Math.rint(p.end2.x*r.scale)+r.mx;
		int y2=-(int)Math.rint(p.end2.y*r.scale)+r.my;
	    g.setColor(Color.gray);
	    g.drawLine(x1,y1,x2,y2);
	}
	
	/*
	void paintClosedCrack(Graphics g,ClosedCrack cc){
		int x1=(int) Math.rint(cc.vertex1.x*r.scale)+r.mx;
		int y1=-(int)Math.rint(cc.vertex1.y*r.scale)+r.my;
		int x2=(int)Math.rint(cc.vertex2.x*r.scale)+r.mx;
		int y2=-(int)Math.rint(cc.vertex2.y*r.scale)+r.my;
		g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		g.drawLine(x1,y1,x2,y2);
	}
	*/
		
	private static Color[] byrColorScale() {
		int[][] rgb = {
	            {0, 0, 0},
	            {0, 0, 159},
	            {0, 0, 191},
	            {0, 0, 223},
	            {0, 0, 255},
	            {0, 32, 255},
	            {0, 64, 255},
	            {0, 96, 255},
	            {0, 128, 255},
	            {0, 159, 255},
	            {0, 191, 255},
	            {0, 223, 255},
	            {0, 255, 255},
	            {32, 255, 223},
	            {64, 255, 191},
	            {96, 255, 159},
	            {128, 255, 128},
	            {159, 255, 96},
	            {191, 255, 64},
	            {223, 255, 32},
	            {255, 255, 0},
	            {255, 223, 0},
	            {255, 191, 0},
	            {255, 159, 0},
	            {255, 128, 0},
	            {255, 96, 0},
	            {255, 64, 0},
	            {255, 32, 0},
	            {255, 0, 0},
	            {223, 0, 0},
	            {191, 0, 0},
	            {191, 0, 0},
	     };
		return getColorScale(rgb);
	}
	
	private static Color[] getColorScale(int[][] rgb) {
		if (rgb == null)
			return null;
		Color[] clr = new Color[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			float[] hsb =  Color.RGBtoHSB(rgb[i][0], rgb[i][1], rgb[i][2], null);
			clr[i] = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
			}
		return clr;
	}

	@Override
	void paintCrack(Graphics g, Crack c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void paintPipe(Graphics2D g, Pipe p) {
		// TODO Auto-generated method stub
		
	}

}
