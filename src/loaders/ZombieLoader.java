package loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import toolbox.MyPaths;

public final class ZombieLoader {

	private static String mapdata = "zombies";
	
	public static String[] zombieTypes;
	public static int[] b_model_distances;
	public static int[] number_of_parts;
	public static float[] healths;
	public static float[] speeds;
	
	public static int NUMBER_OF_TYPES;
	
	public static void Load(){
		
		mapdata = MyPaths.makeSavePath(mapdata);
		
		try{

			InputStream in = ZombieLoader.class.getResourceAsStream(mapdata);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  
			String strLine;
			  
			int parts = 1;
			int b_model_distance = 23;
			float health = 5;
			float speed = 0.18f;
			int i = 0;
			    
			while ((strLine = br.readLine()) != null)   {
				 
				String[] currentLine = strLine.split(" ");
				 
				if(strLine.startsWith("NUMBER_OF_TYPES")){
					NUMBER_OF_TYPES = Integer.valueOf(currentLine[1]);
					zombieTypes = new String[NUMBER_OF_TYPES];
					b_model_distances = new int[NUMBER_OF_TYPES];
					number_of_parts = new int[NUMBER_OF_TYPES];
					healths = new float[NUMBER_OF_TYPES];
					speeds = new float[NUMBER_OF_TYPES];
				}
				else if(strLine.startsWith("Parts:")){
					parts = Integer.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("b_model_distance:")){
					b_model_distance = Integer.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("health:")){
					health = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("speed:")){
					speed = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("Tag: ")){
				
					zombieTypes[i] = currentLine[1];
					number_of_parts[i] = parts;
					b_model_distances[i] = b_model_distance;
					healths[i] = health;
					speeds[i] = speed;
					 
					i++;
					parts = 1;
					b_model_distance = 23;
					health = 5;
					speed = 0.18f;
				}
			}
			  
			in.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}	
	}
}
