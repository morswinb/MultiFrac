package multifracture.tools;

public class PrintArray {
	
	public static void printDouble(double[] Y){
		
		System.out.println("=== "+Y);
		for(int i=0;i<Y.length;i++){
			System.out.println(Y[i]);
		}
		System.out.println("===");
	}

}
