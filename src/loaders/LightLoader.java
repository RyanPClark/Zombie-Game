package loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import toolbox.MyPaths;
import entities.Light;

public final class LightLoader {
	
	private final static String LIGHT_DATA = MyPaths.makeSavePath("light");
	
	public static List<Light> Load(){
		
		List<Light> lights = new ArrayList<Light>();
		
		try{
			InputStream in = LightLoader.class.getResourceAsStream(LIGHT_DATA);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  
			String strLine;
			  
			Vector3f position = new Vector3f(0,0,0);
			Vector3f color = new Vector3f(1,1,1);
			Vector3f attenuation = new Vector3f(1,0,0);
			Float intensity = 1f;
			  
			while ((strLine = br.readLine()) != null)   {
				  
				String[] currentLine = strLine.split(" ");
				  
				if (strLine.startsWith("lightPosX:")){
					position.x = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("lightPosY:")){
					position.y = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("lightPosZ:")){
					position.z = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("lightColorRed:")){
					color.x = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("lightColorGreen:")){
					color.y = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("lightColorBlue:")){
					color.z = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("lightAttenuationX:")){
					attenuation.x = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("lightAttenuationY:")){
					attenuation.y = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("lightAttenuationZ:")){
					attenuation.z = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("lightIntensity:")){
					intensity = Float.valueOf(currentLine[1]);
				}
				else if (strLine.startsWith("light")){
					Light light = new Light(position, color, intensity, attenuation);
					lights.add(light);
					 
					position = new Vector3f(0,0,0);
					color = new Vector3f(1,1,1);
					attenuation = new Vector3f(1, 0, 0);
					intensity = new Float(1);
				}
			}
			  
			in.close();
			  
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		return lights;
	}
}