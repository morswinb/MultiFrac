package multifracture.graphics;

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

public abstract class DrawingScheme implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5759776015719152635L;
	Model m;
	Renderer r;
	
	DrawingScheme(Model m,Renderer r){
		this.m=m;
		this.r=r;
	}
	
	abstract void paintEadge(Graphics2D g, Edge e);
	
	abstract void paintJoint(Graphics2D g, Vertex j);
	
	abstract void paintCrack(Graphics g,Crack c);
	
	abstract void paintPipe(Graphics2D g,Pipe p);
		
	abstract void paintConcreatePipe(Graphics2D g,ConcreatePipe p);
	
	abstract void paintBackgroundPipe(Graphics2D g,BackgroundPipe p);
	
	//abstract void paintClosedCrack(Graphics g,ClosedCrack cc);

	abstract void paintVertex(Graphics2D gBB, Vertex vertex);
}
