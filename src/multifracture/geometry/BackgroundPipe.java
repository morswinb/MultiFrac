package multifracture.geometry;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class BackgroundPipe implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5078679672774937151L;
	public Point2D.Double end1; //location of x=0
	public Point2D.Double end2; //location of x=1;
	public double L;
	
	public BackgroundPipe(double x1,double y1,double x2,double y2){
		this.end1=new Point2D.Double(x1,y1);
		this.end2=new Point2D.Double(x2,y2);
		this.L=end1.distance(end2);
	}
		
}

