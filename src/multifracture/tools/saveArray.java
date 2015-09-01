package multifracture.tools;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class saveArray {


	public static void save2DDouble(double[][] a,String file){
		
		System.out.println("saving ");
		
		Writer writer = null;

		StringBuffer text=new StringBuffer();
		
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a[i].length;j++){
				text.append(a[i][j]+" ");
				//System.out.print(a[i][j]+" ");
			}
			text.append("\n");
			//System.out.println();
		}
		
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(file), "utf-8"));
		    writer.write(text.toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
		
	}
	
	public static void save2DBoolean(boolean[][] a,String file){
		
		System.out.println("saving ");
		
		Writer writer = null;

		StringBuffer text=new StringBuffer();
		
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a.length;j++){
				if(a[i][j])
					text.append(1+" ");
				else{
					text.append(0+" ");
				}
			}
			text.append("\n");
		}
		
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(file), "utf-8"));
		    writer.write(text.toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
		
	}

}
