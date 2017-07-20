package loaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import engineTester.Initialize;
import entities.MultiModeledEntity;
import terrains.Terrain;
import toolbox.MyPaths;

public final class MapLoader {

	private static String mapdata = "map";

	private static List < MultiModeledEntity > entityLists = new ArrayList < MultiModeledEntity > ();

	public static List < MultiModeledEntity > Load() {

		mapdata = MyPaths.makeSavePath(mapdata);

		try {

			InputStream in = MapLoader.class.getResourceAsStream(mapdata);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			Vector3f position = new Vector3f(0, 0, 0);
			float rotX = 0;
			float rotY = 0;
			float rotZ = 0;
			Vector3f scale = new Vector3f(1, 1, 1);
			float textureAtlasID = 1;
			boolean transparency = false;
			float reflectivity = 0.3f;
			float shineDamper = 5;
			boolean fakeLighting = false;
			Float frustumFactor = 425f;
			int number_of_parts = 1;
			boolean b_model = false;

			while ((strLine = br.readLine()) != null) {

				String[] currentLine = strLine.split(" ");

				if (strLine.startsWith("x:")) {
					position.x = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("y:")) {
					position.y = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("z:")) {
					position.z = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("rotX:")) {
					rotX = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("rotY:")) {
					rotY = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("rotZ:")) {
					rotZ = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("scaleX:")) {
					scale.x = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("scaleY:")) {
					scale.y = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("scaleZ:")) {
					scale.z = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("TexID:")) {
					textureAtlasID = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("Transparency:")) {
					transparency = Boolean.valueOf(currentLine[1]);
				} else if (strLine.startsWith("Fake_Lighting:")) {
					fakeLighting = Boolean.valueOf(currentLine[1]);
				} else if (strLine.startsWith("Reflectivity:")) {
					reflectivity = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("Shine_Damper:")) {
					shineDamper = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("Frustum: ")) {
					frustumFactor = Float.valueOf(currentLine[1]);
				} else if (strLine.startsWith("Parts: ")) {
					number_of_parts = Integer.valueOf(currentLine[1]);
				} else if (strLine.startsWith("B_Model: ")) {
					b_model = Boolean.valueOf(currentLine[1]);
				} else if (strLine.startsWith("Tag: ")) {

					MultiModeledEntity ent = Initialize.loadMultiModeledEntity(currentLine[1], position, rotX, rotY, rotZ, scale, (int) textureAtlasID, transparency, shineDamper, reflectivity, fakeLighting, frustumFactor.intValue(), number_of_parts, b_model);
					ent.setID((int) textureAtlasID);

					entityLists.add(ent);

					position = new Vector3f(0, 0, 0);
					rotX = 0;
					rotY = 0;
					rotZ = 0;
					scale = new Vector3f(1, 1, 1);
					textureAtlasID = 1;
					transparency = false;
					reflectivity = 0.3f;
					shineDamper = 5;
					fakeLighting = false;
					frustumFactor = 425f;
					number_of_parts = 1;
					b_model = false;
				}
			}

			in .close();

		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return entityLists;
	}

	public static Terrain loadTerrain(String a, String b, String c, String d, String e, String f) {

		return Initialize.loadTerrain(0, -1, a, b, c, d, e, f);
	}
}