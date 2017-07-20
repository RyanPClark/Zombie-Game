package loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import toolbox.MyPaths;

public final class SoundLoader {
	
	private static String soundData = "sound";
	private static String soundStrings[];
	private static float soundVolumes[];
	
	public static String[] load(){
		
		soundData = MyPaths.makeSavePath(soundData);
		soundStrings = new String[15];
		soundVolumes = new float[15];
		
		try{

			InputStream in = SoundLoader.class.getResourceAsStream(soundData);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  
			String strLine;
			Integer i = 0;
			  
			while ((strLine = br.readLine()) != null)   {
				  
				String[] currentLine = strLine.split(" ");
				  
				soundStrings[i] = currentLine[0];
				soundVolumes[i] = Float.valueOf(currentLine[1]);
				i++;
			}
			  
			in.close();
			  
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		return soundStrings;
	}
	
	public static float[] getVolumes(){
		return soundVolumes;
	}
}
