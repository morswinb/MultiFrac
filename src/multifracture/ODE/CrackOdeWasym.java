package multifracture.ODE;

import java.io.Serializable;

import multifracture.geometry.Crack;

public class CrackOdeWasym extends EdgeODE implements Serializable{
		
	private static final long serialVersionUID = 1L;
	private Crack c;
	
	//precomputed spatial vectors
	//(1-x)^p1, (1-x)^p2, (1-x)^(p1-1), (1-x)^(p2-1) ,(1-x)^(p1-2) ,(1-x)^(p2-2)  
	private final double[] xp1,xp2,xp11,xp21,xp12,xp22;

	public CrackOdeWasym(Crack c){
		super(c);
		this.c=c;
		
		xp1=new double[N];
		xp2=new double[N];
		xp11=new double[N];
		xp21=new double[N];
		xp12=new double[N];
		xp22=new double[N];
		
		for(int i=0;i<N-1;i++){
			xp1[i]=Math.pow(1-x[i],c.p1);
			xp2[i]=Math.pow(1-x[i],c.p2);
			xp11[i]=Math.pow(1-x[i],(c.p1-1));
			xp21[i]=Math.pow(1-x[i],(c.p2-1));
			xp12[i]=Math.pow(1-x[i],(c.p1-2));
			xp22[i]=Math.pow(1-x[i],(c.p2-2));
		}
	}
	
	@Override
	public void dY(double t, int i_global, int tN) {
		
		double L=c.getL(tN);		
		double[] AB=c.tipAsymptotics(tN);
		double[] Y=c.m.Y_work[tN];	
		double[] dY=e.m.dY[tN];
		int i_local=i_global-c.odeNstartIndex;
			
		//w1 index i-1
		//w2 index i
		//w3 index i+1
		double w1,w2,w3=0;
		
		if(i_local==0){
			return;
		}
		if(i_local==1){
			w1=(c.vertex1.getPressure(tN)-e.sigma_0-e.m.sigma_l[tN][i_global-1])/e.k-AB[0]*xp1[0]-AB[1]*xp2[0];
		}
		else{
			w1=Y[i_global-1]-AB[0]*xp1[i_local-1]-AB[1]*xp2[i_local-1];
		}
		
		double dwdx,d2wdx2;
		w2=Y[i_global]-AB[0]*xp1[i_local]-AB[1]*xp2[i_local];
		
		
		if(i_local==N-2){
	        dwdx=-c.p1*AB[0]*xp11[N-2]-c.p2*AB[1]*xp21[N-2];
	        d2wdx2=c.p1*(c.p1-1)*AB[0]*xp12[N-2]+c.p2*(c.p2-1)*AB[1]*xp22[N-2];
		}
		else{
			w3=Y[i_global+1]-AB[0]*xp1[i_local+1]-AB[1]*xp2[i_local+1];
			dwdx=FirstDiff(w1,w2,w3,i_local-1);
			d2wdx2=SecondDiff(w1,w2,w3,i_local-1);
			
			dwdx+=-c.p1*AB[0]*xp11[i_local]-c.p2*AB[1]*xp21[i_local];
			d2wdx2+=c.p1*(c.p1-1)*AB[0]*xp12[i_local]+c.p2*(c.p2-1)*AB[1]*xp22[i_local];
		}
		
        double w_0=AB[0];   
        double w_3=w_0*w_0*w_0;
        double w=Y[i_global];
        double flow_part=c.k/c.M/(L*L)
        		*(1.0/3.0*w_3*x[i_local]*dwdx+w*w*(3*dwdx*dwdx+w*d2wdx2));             
            
        double sig1=c.m.sigma_l[tN][i_global-1];
        double sig2=c.m.sigma_l[tN][i_global];
        double sig3=c.m.sigma_l[tN][i_global+1];       
        double dsigma=FirstDiff(sig1,sig2,sig3,i_local-1);
        double ddsigma=SecondDiff(sig1,sig2,sig3,i_local-1);
        double elastic_part=1/c.M/L/L*(3*w*w*dwdx*dsigma+w*w*w*ddsigma);
        
        dY[i_global]=flow_part+elastic_part;
	}
}
