package multifracture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import multifracture.elasticity.Elasticity;
import multifracture.geometry.Crack;
import multifracture.geometry.GridPoint2D;
import multifracture.geometry.Edge;
import multifracture.geometry.Pipe;
import multifracture.geometry.Vertex;
import multifracture.matrixes.DiscretizationMatrix;
import multifracture.matrixes.JointMatrix;
import multifracture.matrixes.VisiblilityMatrix;
import multifracture.tools.NanoTimer;
import multifracture.tools.PrintArray;

public class Model implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4436866170207644851L;

	
	public double Volume; //fluid volume in this model
	public double Q0;//total pumped in volume in this model
	public double Ql;//total leaked off fluid volume
	public double V0;//initial volume at original t=0
	public double bal_abs;//absolute fluid balance
	public double bal_rel;//relative
	
	public VisiblilityMatrix visibilityMatrix;
	public DiscretizationMatrix discretizationMatrix;
	public JointMatrix jointMatrix;
	public boolean[][] jacabianPatternMatrix;

	//public ArrayList<Edge> edges;
	//public ArrayList<Vertex> vertices;
	
	//public Elasticity elasticity;
	
	public int odeN;

	public ArrayList<Vertex>	vertices=new ArrayList<>();
	public ArrayList<Edge> 		edges=new ArrayList<>();
	
	public GridPoint2D[] grid_points;
	
	public static final NanoTimer timer=new NanoTimer();
	
	public double t; // global time reference point
						//value of time at which this model is currently at
	
	public double[]		Y; //solution and initial value vector
	
	public double[][]	Y_work; //work value vector
	public double[][]	dY;		//derevative vector
	public double[][]	sigma_l;	//calculated stress vector
	
	public int ode_calls;

	public Model(){			
		t=0;
		odeN=0;
	}

	
	
	
	//copy constructor trzeba zrobic jeszcze raz
	public Model(Model m){
		
		//this.timer=m.timer;
		//this.intersect=new ModelIntersections(this);
		this.t=m.t;
		
		this.Q0=m.Q0;
		this.Ql=m.Ql;
		this.V0=m.V0;
		
		//N_ODE=0;
		//collisions_history=new ColisionHistory();
		
		HashMap<Vertex,Vertex> vertexMap=new HashMap<Vertex,Vertex>();
		
		
		for(int i=0;i<m.vertices.size();i++){
			Vertex v_old=m.vertices.get(i);
			Vertex v_new=new Vertex(this,v_old);
			vertexMap.put(v_old,v_new);
			this.add(v_new);
		}
		
		
		for(int i=0;i<m.edges.size();i++){
			Edge e_old=m.edges.get(i);
			Vertex v1_old=e_old.vertex1;
			Vertex v2_old=e_old.vertex2;
			Vertex v1_new=vertexMap.get(v1_old);
			Vertex v2_new=vertexMap.get(v2_old);
			//
			if(e_old.getClass()==Crack.class)
				this.add(new Crack(this,(Crack)e_old, v1_new, v2_new));
			
			if(e_old.getClass()==Pipe.class)
				this.add(new Pipe(this,(Pipe)e_old, v1_new, v2_new));
		}
		updateSize();
		
		this.t=m.t;
	}	
	
	
	
	private void updateSize(){
		
		odeN=0;
		for(int i=0;i<edges.size();i++){
			int n=edges.get(i).N;
			edges.get(i).setStartIndex(odeN);
			odeN+=n;	
		}
		
		Y=new double[odeN];
		Y_work=new double[odeN][odeN];
		dY=new double[odeN][odeN];
		sigma_l=new double[odeN][odeN];
		grid_points=new GridPoint2D[odeN];
		
		for(int i=0;i<odeN;i++){
			Y_work[i]=new double[odeN];
			dY[i]=new double[odeN];
			sigma_l[i]=new double[odeN];
		}
		
		
		//jacabianPatternMatrix=new boolean[odeN][odeN];

		
		// numeracja indeksow
		//int k=0;
		for(int i=0;i<vertices.size();i++){
			vertices.get(i).setMaxtN(odeN);
			vertices.get(i).index=i;
		}
		
		int kk=0;
		for(int i=0;i<edges.size();i++){
			GridPoint2D[] this_edge_grid_points=edges.get(i).gridPoints2D;
			edges.get(i).index=i; // index !!!!	
			//if(edges.get(i).gridPoints2D!=null){	
				for(int j=0;j<this_edge_grid_points.length;j++){
					grid_points[kk++]=this_edge_grid_points[j];
					grid_points[kk-1].global_index=kk-1;
				}
			//}
		}

		for(Edge e:edges){
			e.assemblyY(Y);
		}
				
		if(odeN>0)
			Y_work[0]=Y.clone();

				
	}
	
	public void add(Vertex v){
		if(!vertices.contains(v))
			vertices.add(v);
		//updateSize();
	}
		
	public void add(Edge e){
		if(!edges.contains(e)){
			edges.add(e);
			add(e.vertex1);
			add(e.vertex2);
		}
		updateSize();
	}
	
	public void remove(Edge e){
		edges.remove(e);
		e.vertex1.remove(e);
		e.vertex2.remove(e);
		if(e.vertex1.eadges.size()==0)
			remove(e.vertex1);
		if(e.vertex2.eadges.size()==0)
			remove(e.vertex2);
		updateSize();
	}
	
	public void remove(Vertex v){
		this.vertices.remove(v);
		updateSize();
	}
		
	public void doJPatern(){
		
		this.discretizationMatrix=new DiscretizationMatrix(this);
		this.jointMatrix=new JointMatrix(this);
		
		IntStream.range(0,odeN).parallel().forEach((tn) -> JpatternAddUp(tn));
		
		//paraller
		//for(int tN=0;tN<N_ODE;tN++){
		//	futures.add(executor.submit(new JPatternWorkThread(this,tN)));		
		//}		
		//for (Future<?> future : futures){
		//	try{future.get();}
		//	catch (Exception e){e.printStackTrace();}
		//}
	}
	
	private void JpatternAddUp(int tN){
		
		System.out.println(" unimplemented JpatternAddUp");
		
		/*
		
		double N=odeN;
		boolean[] disc=discretizationMatrix.disc[tN];
		boolean[] join=jointMatrix.joi[tN];
		boolean[] pat=jacabianPatternMatrix[tN];
		
		if(Variables.Elatsicity==0){
			for(int j=0;j<N;j++){
				if(disc[j] || join[j]){
					pat[j]=true;
				}
				else{
					pat[j]=false;
				}
			}
		}
		else{
			double[] vis=visibilityMatrix.visiblilty[tN];
			for(int j=0;j<N;j++){
				if(disc[j] || join[j] || vis[j]>0){
					pat[j]=true;
				}
				else{
					pat[j]=false;
				}
			}
		}
		*/
		
	}
	
	public void doVisibility(){

		System.out.println(" unimplemented doVisibility ");
		
		/*
		
		for(int i=0;i<edges.size();i++)
				edges.get(i).updateGridPoints();
		this.visibilityMatrix=new VisiblilityMatrix(this);
		doJPatern();
		*/
		
	}
	
	
	public double[][] JacobSimple(double t){
		
		System.out.println("disabled method JacobSimple");
		return null;
		
		/*
		
		double delta=Variables.jacobianDelta;
		
		for(int i=1;i<N_ODE;i++){
			Y[i]=Arrays.copyOf(Y[0],Y[0].length);
		}
		
		
		for(int i=0;i<N_ODE;i++){
			Y[i][i]-=delta;
			double[] dY1=Arrays.copyOf(ODEtN(t,i),N_ODE);
			Y[i][i]+=2*delta;
			double[] dY2=Arrays.copyOf(ODEtN(t,i),N_ODE);
		
			for(int j=0;j<N_ODE;j++)
				dY[i][j]=(dY2[j]-dY1[j])/(delta);
			//	dY[i*N_model+j]=(dY2[j]-dY1[j])/delta;	
		}
		return dY;
		
		*/
		
		//return dY.clone();
		//ArrayDeepCopy.deepCopy(dY);
	}
	
	
	public double[][] Jacob(double t){
		
		System.out.println("disabled method Jacob");
		
		/*
		
		//System.out.println("jacobian used");
		
		for(int tN=1;tN<N_ODE;tN++){
			Y[tN]=Arrays.copyOf(Y[0],N_ODE);
		}
		double[] dY0=ODEtN(t,0).clone();
		
		futures.clear();
		for(int tN=0;tN<N_ODE;tN++){		
			futures.add(executor.submit(new JacobiWorkerThread(this,t,tN,dY0)));	
		}
		
		for (Future<?> future : futures){
			try{
				future.get();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		*/
		
		return dY;
		
	}
	
	
	public void calculateSigma(int tN){

		//edges.parallelStream().forEach((e)->e.elasticity.findSigma(tN));
	}
	
	private double[] ODEtN(double t,int tN){
						
		//doElasticity();
		//this.timer.tic();
		
		//find_sigma_l(tN);
		
		//timer.tic();
		//for (Eadge e : eadges){
		//	e.elasticity.findSigma(tN);
		//}
		//timer.toc();
		
		//timer.tic();
		//hail to Java 1.8 lambda expressions
		//edges.parallelStream().forEach((e)->e.elasticity.findSigma(tN));
		//calculateSigma(tN);
		//timer.toc();
		
		
		//paraller
		/*for (Eadge e : eadges){
			futures.add(executor.submit(new ElasticityWorkerThread(e,tN)));
		}
		for (Future<?> future : futures){
			try{
				future.get();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}*/
		//this.timer.toc();
				
		//serial
		//this.timer.tic();
		
		findJunctionPressure(t,tN);

		
		
		//findJunctionPressureParaller(t,tN);
		//this.timer.toc();
		
		/*
		 * //serial
		 * for(int i=0;i<eadges.size();i++){			
		 * int c=eadges.get(i).startIndex;
		 * for(int j=0;j<eadges.get(i).ode_N;j++){		
		 * eadges.get(i).setdY(t,c++,0);
		 * }
		 * }*/
				
		/**/
			
		
		//paraller
		//hail to Java 1.8 lambda expressions		
		//edges.parallelStream().forEach((e)->{
		//	for(int j=e.odeNstartIndex;j<e.odeNstartIndex+e.odeN;j++)
		//		e.dY(t,j,tN);
		//});
		
		
		//serial
		for(int j=0;j<this.edges.size();j++){
			Edge e=edges.get(j);
			for(int i=e.odeNstartIndex;i<e.odeNstartIndex+e.N;i++)
				e.dY(t,i,tN);
		}
		
		
		return dY[tN];	
	}
	
	public double[] ODE(double t){
				
		ode_calls++;
		ODEtN(t,0);
		return dY[0];
	}

	
	public void findJunctionPressure(double t,int tN){
		
		ArrayList<Vertex> junctions=(ArrayList<Vertex>) this.vertices.clone();
		
		for(Vertex j:vertices)
			j.aproximateInitialFluidPressure(t, tN);
		
		
		for(int j=0;j<50;j++){
			
			for(int i=0;i<junctions.size();i++){
				double tol=junctions.get(i).evaluateFluidPressure(t, tN);			
				
				
				//quick fix ?? ignore this loop because cc pipes will produce no change (if connected together for the very fist iterations ???)
				if(tol<1e-12){
					junctions.remove(i);
					i--;
					if(junctions.size()==0)
						j=50;
				}
			}
		}
		
	}
	
	/** obsolete method
	public void findJunctionPressureParaller(double t,int tN){

		//for(int i=0;i<vertexes.size();i++)
		//	vertexes.get(i).evaluateCrackPipePressure(t,tN);
		
		ArrayList<Joint> junctions=(ArrayList<Joint>) this.junctions.clone();
		double[] tol=new double[junctions.size()];
		
		for(int i=0;i<junctions.size();i++)
			junctions.get(i).aproximateInitialFluidPressure(t, tN);
		
		for(int i=0;i<20;i++){
		
			
			int k=0;
			for (Joint j : junctions){
				futures.add(executor.submit(new JunctionPressureThread(k++,tol,j,t,tN)));
			}
			for (Future<?> future : futures){
				try{
					future.get();
				}
				catch (Exception e){
					e.printStackTrace();
				}
			}
			double total_tol=0;
			for(int kk=0;kk<k;kk++){
				total_tol+=tol[kk];
			}
			if(total_tol<1e-8){
				break;
			}
		}
	}	
		
		//for(int j=0;j<50;j++){
		
			//double max_tol=0;
			
			//for(int i=0;i<junctions.size();i++){
				//double tol=junctions.get(i).evaluateFluidPressure(t, tN);
				//if(tol<1e-12){
				//	junctions.remove(i);
				//	i--;
				//	if(junctions.size()==0)
				//		j=50;
				//}
			//}
			
			//if(max_tol<1e-12){ // break once good tolerance reached
			//	break;
			//}
		//}
	*/
	
	
	public void update(double t){
		
		findJunctionPressure(t,0);

		for(Edge e:edges){
			e.setY(Y);
		}
		
		for(Edge e:edges){
			e.assemblyY(Y);
		}
		
		//System.out.println(" flow at update "+eadges.get(0).getFlowRate(0.05));

	}
	
	
	public double getTotalVolume(){
		double V=0;
		//for(int i=0;i<cracks.size();i++)
			//V+=cracks.get(i).getTrapezoidalVolume();
			//V+=cracks.get(i).getSplineVolume();
		//	V+=cracks.get(i).getVolume(0,1);
		//for(int i=0;i<pipes.size();i++)
			//V+=pipes.get(i).getTrapezoidalVolume();
		//	V+=pipes.get(i).getSplineVolume();
		for(int i=0;i<edges.size();i++)
			V+=edges.get(i).getVolume(0,1);
		
		return V;	
	}
	
	
	public void PrintEvents(){
		//events.printEventsReport();
	}
	
}


