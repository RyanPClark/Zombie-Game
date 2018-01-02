package loaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import toolbox.MyPaths;
import entities.Gun;

public final class GunLoader {
	
	private static String gunData = "Guns/";
	
	public static void init(){
		
		gunData = MyPaths.makeSavePath(gunData);
		gunData = gunData.substring(0, gunData.length() - 5);
	}
	
	public static void loadGun (Gun gun, int ID){
		
		try{
			  InputStream in = GunLoader.class.getResourceAsStream(gunData + ID + ".data");
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  
			  String strLine;
			  
			  while ((strLine = br.readLine()) != null)   {
				  
				 String[] currentLine = strLine.split(" ");
				 String val = currentLine[1];
				 
				 if       (strLine.startsWith("scale:"))
					 gun.setScale(Float.valueOf(val));
				 else if (strLine.startsWith("ROF:"))
					 gun.ROF = Integer.valueOf(val);
				 else if (strLine.startsWith("Automatic:"))
					 gun.automatic = Boolean.valueOf(val);
				 else if (strLine.startsWith("MaxAmmo:"))
					 gun.maxAmmo = Integer.valueOf(val);
				 else if (strLine.startsWith("RecoilAmount:"))
					 gun.recoilAmount = Float.valueOf(val);
				 else if (strLine.startsWith("Power:"))
					 gun.power = Float.valueOf(val);
				 else if (strLine.startsWith("Offsets0:"))
					 gun.offsets[0] = Float.valueOf(val);
				 else if (strLine.startsWith("Offsets1:"))
					 gun.offsets[1] = Float.valueOf(val);
				 else if (strLine.startsWith("Offsets2:"))
					 gun.offsets[2] = Float.valueOf(val);
				 else if (strLine.startsWith("Offsets3:"))
					 gun.offsets[3] = Float.valueOf(val);
				 else if (strLine.startsWith("Mobility:"))
					 gun.mobility = Float.valueOf(val);
				 else if (strLine.startsWith("GunSoundID:"))
					 gun.gunSoundID = Integer.valueOf(val);
				 else if (strLine.startsWith("Semiauto:"))
					 gun.semiauto = Boolean.valueOf(val);
				 else if (strLine.startsWith("MovingValues0:"))
					 gun.movingValues[0] = Float.valueOf(val);
				 else if (strLine.startsWith("MovingValues1:"))
					 gun.movingValues[1] = Float.valueOf(val);
				 else if (strLine.startsWith("MovingValues2:"))
					 gun.movingValues[2] = Float.valueOf(val);
			  }
			  
			  gun.ammo = gun.maxAmmo;
			  in.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
