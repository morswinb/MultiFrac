package multifracture.tools;

import java.io.Serializable;

public class NanoTimer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 158119710177152660L;
	private long startTime=System.nanoTime();
	private long ticTime;
	private long runtime;
	
	public NanoTimer(){
		
	}
	
	public void tic(){
		ticTime=System.nanoTime();
	}
	
	public void toc(){
		runtime+=(System.nanoTime()-ticTime);
		ticTime=0;
	}
	
	public double getTimeShare(){
		return ((double) runtime)/(System.nanoTime()-startTime);
	}

	
	
	//public static long startTime=System.nanoTime();
//	public static long timeinODE=0;
	
	
	//long elapsedTime=System.nanoTime()-m.startTime;
	//System.out.println(((double)m.timeinODE)/elapsedTime);
}
