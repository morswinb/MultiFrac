package multifracture.graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.VolatileImage;
import java.io.Serializable;

import javax.swing.JFrame;

import multifracture.Model;
import multifracture.Variables;
import multifracture.geometry.GridPoint2D;

public class Renderer extends Component implements KeyListener,MultiCrackRenderer,Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6252579040223660016L;
	VolatileImage backBuffer = null;
    private Model m;
    
    int w=1420;
    int h=900;
    int frame=0;
    int paint_invoke=0;
    renderingThread thread;
    JFrame f;
    
    double scale=7;
    double max_fluid_pressure=0;
    double max_width=0;
    
    int mx=w/2;
    int my=h/2;
    
    DrawingScheme ds;
    
    public Renderer (Model m) {
    	this.m=m;
    	f = new JFrame();
    	f.setSize(w,h);
    	f.add(this);
    	f.setVisible(true);
    	f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	f.addKeyListener(this);
    	
		thread=new renderingThread(this);
		thread.start();
		
		ds=new PressureColorView(m,this);
		//ds=new WidthColorView(m,this);
		//ds=new StructureLogicView(m,this);
    }
    
    public Renderer (Model m,JFrame f) {
    	this.m=m;
    	this.f=f;
    	//f.setSize(w,h);
    	this.setSize(1420, 900);
    	this.setLocation(180,0);
    	this.setPreferredSize(new Dimension(1420, 900));
    	f.add(this);
    	f.addKeyListener(this);
    	
		thread=new renderingThread(this);
		thread.start();
		
		ds=new PressureColorView(m,this);
		//ds=new WidthColorView(m,this);
		//ds=new StructureLogicView(m,this);
    }
    
    
    void createBackBuffer() {
    	
    	if (backBuffer != null) {
    		backBuffer.flush();
    		backBuffer = null;
    	}
    	
    	backBuffer = createVolatileImage(getWidth(), getHeight());
    	
    	
			//System.out.println("creating backbuffer");
    }

    public void paint(Graphics g) {
    	
    	try{
    		if(!backBuffer.contentsLost())
        		g.drawImage(backBuffer, 0, 0, this);
    	}
    	catch(Exception e){
    		//e.printStackTrace();
    	}
    	//System.out.println("paint invoke "+(++paint_invoke));	
    }
    
    private class renderingThread extends Thread{
    	
    	Renderer r;
    	renderingThread(Renderer r){
    		this.r=r;
    	}
    	   	
    	public void run(){	
    		
    		int frame=0;
    		int last_fps = 0;
    		long t_frame=System.currentTimeMillis();
    		while(true)
    		{  		

    			try{
    				if (backBuffer == null) {
    					createBackBuffer();
    				}
    				do {
    					int valCode = backBuffer.validate(getGraphicsConfiguration());
    					if (valCode == VolatileImage.IMAGE_RESTORED) {
    						
    					}
    					else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE){
    						createBackBuffer();
    					}
    					
    					Graphics2D gBB = (Graphics2D) backBuffer.getGraphics();
    					gBB.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    					
    					gBB.setColor(Color.white);
    					gBB.fillRect(0, 0, getWidth(), getHeight());
		    			   
    					
    					if(m!=null){
    					
	    					getMaxWidth();
	    					getMaxFluidPressure();
	    						
	    					gBB.setStroke(new BasicStroke(1));
	    					gBB.setColor(Color.black);
	    					paintBackStress(gBB);
	    					
	    					gBB.setStroke(new BasicStroke((int)(scale/2+1)));
	    					
	    					for(int i=0;i<m.edges.size();i++){
	    						ds.paintEadge(gBB,m.edges.get(i));
	    					}
	    					
	    					for(int i=0;i<m.vertices.size();i++){
	    						//ds.paintVertex(gBB,m.vertices.get(i));
	    					}
	    					
    					}
    					else{
    						gBB.setColor(Color.black);
    						gBB.drawString("load a multifracturing model", 300, 300);
    					}
    					
    					gBB.setColor(Color.black);
						gBB.drawString(last_fps+" fps", 190,20);
    					
	    			    
						gBB.setColor(Color.blue);
						gBB.setStroke(new BasicStroke((float) 1));
						
						/*
	    			    if(Variables.Elatsicity!=0){
		    			    try{
		    			    			    			    	
			    			    //przeniesc w lepsze miejsce
			    			    double [][] visib=m.visibilityMatrix.visiblilty;
			    			    int NN=visib[0].length;
			    			    GridPoint2D[] points=m.grid_points;
			    			    
			    			    for(int i=0;i<0;i+=Variables.elasticity_draw_skip){
			    			    	for(int j=0;j<NN;j++){
			    			    		//if(visib[i][j]==1){
			    			    		if(visib[i][j]!=0){
			    			    		
			    			    			Point2D.Double p1=points[i];
			    			    			Point2D.Double p2=points[j];
			    			    			
			    			    			if(points[i].norm_L>0 && points[j].norm_L>0){
			    			    			
				    			    			int x1=(int) Math.round(p1.x*r.scale)+r.mx;
				    			    			int y1=-(int)Math.round(p1.y*r.scale)+r.my;	
				    			    			int x2=(int) Math.round(p2.x*r.scale)+r.mx;
				    			    			int y2=-(int)Math.round(p2.y*r.scale)+r.my;
				    			    			
				    			    			gBB.drawLine(x1, y1, x2, y2);
			    			    			}
			    			    		}
			    			    	}
			    			    	
			    			    }
		    			    
		    			    }
		    			    catch(Exception e){
		    			    	e.printStackTrace();
		    			    }
	    			    }
	    			    */
						
						
    					
    					// copy from the back buffer to the screen 			    
    					r.repaint();
    				} 
    				while (backBuffer.contentsLost());
    			}
    			
    			catch(Exception e){
    				e.printStackTrace();
    			}
    			
    			frame++;
    			long t_now=System.currentTimeMillis();
    			if(t_now-t_frame>1000){
    				last_fps=frame;
    				frame=0;
    				t_frame=t_now;
    			}
    		
	    		try {
	    				if(last_fps<55){
	    					Thread.sleep(20);
	    				}
	    				else{
	    					Thread.sleep(50);
	    				}
					} catch (Exception e) {
						e.printStackTrace();
    			}
    		}
    	} 	
    }
    
    

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void paintBackStress(Graphics g) {
		
		/**
		
		int arrowx=30;
		int arrowy=30;
		
		double[] smax=m.var.sigma_x_direction;
		double smaxv=m.var.sigma_x;
		drawArrow(g,arrowx,arrowy,arrowx+(int)(smax[0]*10*smaxv),arrowy+(int)(smax[1]*10*smaxv),4);
		
		double[] smin=m.var.sigma_y_direction;
		double sminv=m.var.sigma_y;
		drawArrow(g,arrowx,arrowy,arrowx+(int)(smin[0]*10*sminv),arrowy+(int)(smin[1]*10*sminv),4);
		
		g.drawString("simga_o",10, 15);
		
		*/
		
	}
	
    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2,int ARR_SIZE) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }

	@Override
	public void keyPressed(KeyEvent e) {
		
		//page up pressed
		if(e.getKeyCode()==33){
			this.scale=scale*1.25;
		}
		//page down pressed
		if(e.getKeyCode()==34){
			this.scale=scale*0.8;
		}
		
		if(e.getKeyCode()==38){
			this.my=my+50;
		}
		if(e.getKeyCode()==40){
			this.my=my-50;
		}
		if(e.getKeyCode()==37){
			this.mx=mx+50;
		}
		if(e.getKeyCode()==39){
			this.mx=mx-50;
		}		
	}

	
	private void getMaxWidth(){
		
		try{
		
			double max_p=0;
		    for(int i=0;i<m.edges.size();i++){
		    	double[] a=m.edges.get(i).w;
		    	if(m.edges.get(i)!=null && a!=null)
		    	if(m.edges.get(i).N>0) for(int j=0;j<a.length;j++)
		    		max_p=Math.max(max_p,a[j]);
		    }
		 			    		    
		    max_width=max_p;
		    
		}
		catch(Exception e){
			max_width=1;
		    e.printStackTrace();

		}
	}
	
	private void getMaxFluidPressure(){
		
		try{
		
			double max_p=0;
		    for(int i=0;i<m.edges.size();i++){
		    	double[] pf=m.edges.get(i).pf;
		    	if(m.edges.get(i)!=null && pf!=null)
		    	if(m.edges.get(i).N>0) for(int j=0;j<pf.length;j++)
		    		max_p=Math.max(max_p,pf[j]);
		    }
		 			    		    
		    max_fluid_pressure=max_p;
		    
		}
		catch(Exception e){
			max_fluid_pressure=1;
		    e.printStackTrace();

		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setModel(Model m) {
		this.m=m;
		
		double x=0,y=0;
		for(int i=0;i<m.vertices.size();i++){
			x+=m.vertices.get(i).x;
			y+=m.vertices.get(i).y;
		}
			
		//System.out.println(f);
		mx=(int) (x/m.vertices.size())+this.getWidth()/2;
		my=(int) (y/m.vertices.size())+this.getHeight()/2;
		//System.out.println(x);
	}
}




