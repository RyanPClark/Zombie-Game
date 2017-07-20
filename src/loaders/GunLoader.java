package loaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.util.vector.Vector3f;

import toolbox.MyPaths;
import entities.Gun;

public final class GunLoader {
	
	private static String gunData = "Guns/";
	
	public static void init(){
		
		gunData = MyPaths.makeSavePath(gunData);
		gunData = gunData.substring(0, gunData.length() - 5);
	}
	
	public static Gun Load(Gun gun, int ID){
		
		try{
			  InputStream in = GunLoader.class.getResourceAsStream(gunData + ID + ".data");			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  
			  String strLine;
			  
			  Vector3f scale = new Vector3f(1,1,1);
			  boolean transparency = false;
			  int ROF = 8;
			  boolean automatic = true;
			  int maxAmmo = 20;
			  float recoilAmount = 0.1f;
			  float power = 1.5f;
			  float[] offsets = new float[4];
			  float mobility = 0;
			  int gunSoundID = 0;
			  float[] movingValues = new float[3];
			  boolean semiauto = false;
			  
			  while ((strLine = br.readLine()) != null)   {
				  
				 String[] currentLine = strLine.split(" ");
				  
				 if       (strLine.startsWith("scaleX:")){
					 scale.x = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("scaleY:")){
					 scale.y = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("scaleZ:")){
					 scale.z = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Transparency:")){
					 transparency = Boolean.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("ROF:")){
					 ROF = Integer.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Automatic:")){
					 automatic = Boolean.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("MaxAmmo:")){
					 maxAmmo = Integer.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("RecoilAmount:")){
					 recoilAmount = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Power:")){
					 power = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Offsets0:")){
					 offsets[0] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Offsets1:")){
					 offsets[1] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Offsets2:")){
					 offsets[2] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Offsets3:")){
					 offsets[3] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Mobility:")){
					 mobility = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("GunSoundID:")){
					 gunSoundID = Integer.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("Semiauto:")){
					 semiauto = Boolean.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("MovingValues0:")){
					 movingValues[0] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("MovingValues1:")){
					 movingValues[1] = Float.valueOf(currentLine[1]);
				 }else if (strLine.startsWith("MovingValues2:")){
					 movingValues[2] = Float.valueOf(currentLine[1]);
				 }
				 
			  }
			  
			  gun.setSpecificData(scale, transparency, (short)ROF, automatic, (short)maxAmmo, recoilAmount, power, offsets, mobility, gunSoundID, movingValues, semiauto);
			  
			  in.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return gun;
		
	}
}
