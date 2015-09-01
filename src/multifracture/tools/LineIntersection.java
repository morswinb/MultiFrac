package multifracture.tools;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class LineIntersection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9059067753845039504L;

	
	//intersections only inside interval
	public static Point2D.Double getIntersect(Line2D.Double l1, Line2D.Double l2){
		
		
		//co to kurwa jest ???
	    if (!l1.intersectsLine(l2)){
	    	return null;
	    }
	    
	    double  x1 = l1.getX1(), y1 = l1.getY1(), x2 = l1.getX2(), y2 = l1.getY2(), x3 = l2.getX1(), y3 = l2.getY1(), x4 = l2.getX2(), y4 = l2.getY2();
	    
	    Point2D.Double intersection=new Point2D.Double();
	    
	    intersection.x = det(det(x1, y1, x2, y2), x1 - x2, det(x3, y3, x4, y4), x3 - x4)/det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
	    intersection.y = det(det(x1, y1, x2, y2), y1 - y2, det(x3, y3, x4, y4), y3 - y4)/det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
	    //System.out.println("intersection x "+intersection.x+" y "+intersection.y);
	
	    if(Double.isNaN(intersection.x)|| Double.isNaN(intersection.y)|| Double.isInfinite(intersection.x) || Double.isInfinite(intersection.y))
	    	return null;
	    
	    return intersection;
	}	
	
	private static double det(double a, double b, double c, double d){
	    return a * d - b * c;
	}
	
	
	//allows for intersection outside line intervals
	public static Point2D.Double getIntersect2(Line2D.Double l1, Line2D.Double l2){
		
	    
	    double  x1 = l1.getX1(), y1 = l1.getY1(), x2 = l1.getX2(), y2 = l1.getY2(), x3 = l2.getX1(), y3 = l2.getY1(), x4 = l2.getX2(), y4 = l2.getY2();
	    
	    Point2D.Double intersection=new Point2D.Double();
	    
	    intersection.x = det(det(x1, y1, x2, y2), x1 - x2, det(x3, y3, x4, y4), x3 - x4)/det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
	    intersection.y = det(det(x1, y1, x2, y2), y1 - y2, det(x3, y3, x4, y4), y3 - y4)/det(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
	    //System.out.println("intersection x "+intersection.x+" y "+intersection.y);
	
	    if(Double.isNaN(intersection.x)|| Double.isNaN(intersection.y)|| Double.isInfinite(intersection.x) || Double.isInfinite(intersection.y))
	    	return null;
	    
	    return intersection;
	}
}
