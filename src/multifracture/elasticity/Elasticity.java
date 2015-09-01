package multifracture.elasticity;

public interface Elasticity{
		
	//returns pseudo elasticity component of raynolds equations
	//public double getElasticity(double t,int i,int tN);
	
	//returns backstress	
	public double getSigma(int i, int tN);
	
	public void findSigma(int tN);
	
}
