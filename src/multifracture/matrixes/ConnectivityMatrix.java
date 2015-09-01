package multifracture.matrixes;

import java.io.Serializable;

import multifracture.Model;
import multifracture.geometry.ConcreatePipe;
import multifracture.geometry.Edge;
import multifracture.geometry.Vertex;

public class ConnectivityMatrix implements Serializable{
	
	
	//matrix that shows connectivity between vertexes if eadges are present
	short[][] conn;
	
	//0 no connection
	//1 PKN like connection
	//-1 solid concrete pipe connection (transparent to elasticity problems)

	
	public ConnectivityMatrix(Model model) {
		
		Vertex[] ver=(Vertex[]) model.vertices.toArray();
		
		conn=new short[ver.length][ver.length];
		
		
		for(int i=0;i<ver.length;i++){
			Vertex v1=ver[i];
			for(int j=i+1;j<ver.length;j++){
				Vertex v2=ver[j];
				
				for(int k=0;k<v1.eadges.size();k++){
					Edge e1=v1.eadges.get(k);
					for(int m=0;m<v2.eadges.size();m++){
						Edge e2=v2.eadges.get(m);
						if(e1==e2){
							if(e1.getClass()==ConcreatePipe.class){
								conn[i][j]=-1;
								conn[j][i]=-1;	
							}
							else{
								conn[i][j]=1;
								conn[j][i]=1;	
							}					
						}
					}
				}
			}
		}
		
	}
	
	//debug print
	public void print(){
		for(int i=0;i<conn.length;i++){
			for(int j=0;j<conn.length;j++){
				System.out.print(conn[i][j]+" ");
			}
			System.out.println();
		}
	}

}
