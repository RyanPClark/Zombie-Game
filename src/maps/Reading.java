package maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import toolbox.MyPaths;

public final class Reading {
	
	public static List<List<Vector2f>> readCollisions(String name){
		
		name = MyPaths.makeSavePath(name);
		
		List<List<Vector2f>> list = new ArrayList<List<Vector2f>>();
			
		try{
			InputStream in = Reading.class.getResourceAsStream(name);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			List<Vector2f> subList = new ArrayList<Vector2f>();
				  
			while ((strLine = br.readLine()) != null)   {
					  
				if (strLine.startsWith("New:")){
						  
					list.add(subList);
					subList = new ArrayList<Vector2f>();
				}
				else if (strLine.startsWith("Vector:")){

					String[] currentLine = strLine.split(" ");
					Vector2f vector = new Vector2f(Float.valueOf(currentLine[1]), Float.valueOf(currentLine[2]));      
					subList.add(vector);
				}
				  
			}	  
			in.close();
				  
		}
		catch (IOException e){
			e.printStackTrace();
		}
			
		return list;
	}
}
