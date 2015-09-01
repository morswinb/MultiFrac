package multifracture.ODE;

import java.io.Serializable;

import multifracture.geometry.Pipe;

public class PipeOdeU extends EdgeODE implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5959850461354022653L;

	public PipeOdeU(Pipe p){
		super(p);
	}
	
	@Override
	public void dY(double t, int i_global, int tN) {
		
		double L=e.getL(tN);
		double[] Y=e.m.Y_work[tN];
		double[] dY=e.m.dY[tN];
		int i_local=i_global-e.odeNstartIndex;
		
		//w1 index i-1
		//w2 index i
		//w3 index i+1
		double w1,w2,w3=0;
		if(i_local==0 || i_local==N-1){
			return;
		}
		if(i_local==1){
			w1=(e.vertex1.getPressure(tN)-e.sigma_0-e.m.sigma_l[tN][i_global-1])/e.k;
			w3=Y[i_global+1];
		}
		else if(i_local==N-2){
			w1=Y[i_global-1];
			w3=(e.vertex2.getPressure(tN)-e.sigma_0-e.m.sigma_l[tN][i_global+1])/e.k;
		}
		else{
			w1=Y[i_global-1];
			w3=Y[i_global+1];
		}
		w2=Y[i_global];	
		
 		double dUdx,d2Udx2;
		
		double U1=w1*w1*w1;
		double U2=w2*w2*w2;
		double U3=w3*w3*w3;
		
		dUdx=FirstDiff(U1,U2,U3,i_local-1);
		d2Udx2=SecondDiff(U1,U2,U3,i_local-1);
		   
    	double flow_part=e.k/3/e.M/L/L*(dUdx*dUdx+3*U2*d2Udx2);
    	flow_part=flow_part/3/w2/w2;
    	
		//double dwdx,d2wdx2;
		
		
		//dwdx=FirstDiff(w1,w2,w3,i-1);
		//d2wdx2=SecondDiff(w1,w2,w3,i-1);
		   
        //flow_part=p.k/p.M/(L*L)*(3*w2*w2*dwdx*dwdx+w2*w2*w2*d2wdx2);
        
        double sig1=e.m.sigma_l[tN][i_global-1];
        double sig2=e.m.sigma_l[tN][i_global];
        double sig3=e.m.sigma_l[tN][i_global+1];
        
        double dsigma=FirstDiff(sig1,sig2,sig3,i_local-1);
        double ddsigma=SecondDiff(sig1,sig2,sig3,i_local-1);
        
       // double elastic_part=1/p.M/L/L*(3*w2*w2*dwdx*dsigma+w2*w2*w2*ddsigma);
        double elastic_part=1/e.M/L/L*(dUdx*dsigma+U2*ddsigma);
        
        //System.out.println("crack flow i "+i_global+"\t"+elastic_part+"\t"+flow_part);
        //System.out.println("crack flow i "+i_global+"\t"+(flow_part+elastic_part));
        dY[i_global]=flow_part+elastic_part;
	}	
}
