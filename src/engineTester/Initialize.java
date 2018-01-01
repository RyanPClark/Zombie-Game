package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import loaders.GunLoader;
import loaders.ZombieLoader;
import maps.Reading;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJLoader;
import entities.Gun;
import entities.MultiModeledEntity;
import entities.Zombie;
import renderEngine.Loader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MyPaths;

public final class Initialize {
	
	private static Loader loader;
	private static List<List<List<TexturedModel>>> texturedGunZ = null;
	private static List<List<Vector2f>> collisions;
	
	public static void init(Loader loader){
		Initialize.loader = loader;
	}
	
	private static TexturedModel loadModel(String textureID, String ModelID, float reflectivity, float shine_damper, boolean transparency
			, boolean fakeLighting, int partID){
		
		ModelTexture gunTexture = new ModelTexture(loader.loadTexture(MyPaths.makeTexturePath(textureID)));
		ModelData gunData = OBJLoader.loadOBJ(MyPaths.makeOBJPath(ModelID));
		
		RawModel RawGun = loader.loadToVAO(gunData.getVertices(), gunData.getTextureCoords(), gunData.getNormals(), gunData.getIndices());
		gunTexture.setShine_damper(shine_damper); gunTexture.setReflectivity(reflectivity);
		gunTexture.setHasTransparency(transparency); gunTexture.setUseFakeLighting(fakeLighting);
		TexturedModel texturedGun = new TexturedModel(RawGun,gunTexture,partID);
		
		return texturedGun;
	}
	
	public static Terrain loadTerrain(float x, float z, String _backgroundTexture, String _rTexture, String _gTexture,
			String _bTexture, String _blendMap, String _heightmap){
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/"+_backgroundTexture)));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/"+_rTexture)));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/"+_gTexture)));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/"+_bTexture)));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture(MyPaths.makeTexturePath("Terrain/"+_blendMap)));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		Terrain terrain = new Terrain(x, z, loader, texturePack, blendMap, "Terrain/"+_heightmap);
		
		return terrain;
	}
	
	protected static Gun loadGun(int weaponID){
		
		TexturedModel texturedGun = loadModel("Gun/"+weaponID, "Gun/"+weaponID, 0.3f, 5, false, false, weaponID);
		Gun gun = new Gun(texturedGun, new Vector3f(0,0,0), 0, 0, 0, new Vector3f(0,0,0), weaponID, "gun");
		gun = GunLoader.Load(gun, weaponID);
		
		return gun;
	}
	
	public static MultiModeledEntity loadMultiModeledEntity(String name, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale,
			int TextureAtlasID, boolean transparency, float shineDamper, float reflectivity,
			boolean fakeLighting, int frustumFactor, int number_of_parts, boolean b_model){
		
		List<TexturedModel> list = new ArrayList<TexturedModel>();
		List<TexturedModel> b_list = new ArrayList<TexturedModel>();
		
		for(int i = 0; i < number_of_parts; i++){
			TexturedModel texturedGun = loadModel("Static/"+ name + (i+1), "Static/" + name + (i+1), reflectivity, shineDamper, transparency, fakeLighting, (i+1));
			list.add(texturedGun);
			if(b_model){
				texturedGun = loadModel("Static/"+ name + (i+1), "Static/" + name + (i+1) + "B", reflectivity, shineDamper, transparency, fakeLighting, (i+1));
				b_list.add(texturedGun);
			}
		}
		
		List<List<TexturedModel>> masterList = new ArrayList<List<TexturedModel>>();
		masterList.add(list);
		masterList.add(b_list);
		
		return new MultiModeledEntity(masterList, position, rotX, rotY, rotZ, scale, name, b_model, number_of_parts, true);
	}
	
	public static List<List<Vector2f>> loadCollisions() {
		
		collisions = Reading.readCollisions("collisions");
		return collisions;
	}
	public static MultiModeledEntity newMultiModeledEntity(String name, Vector3f position, int parts, boolean b_model){
		
		return loadMultiModeledEntity(name, position, 0, 0, 0, new Vector3f(1,1,1), 1, false, 1, 1, false, 425, parts, b_model);
	}
	
	public static Zombie newZombie (int type){
		
		String name = ZombieLoader.zombieTypes[type];
		int number_of_models = ZombieLoader.number_of_parts[type];
		
		if (texturedGunZ.get(type) == null){
			
			List<TexturedModel> aList= new ArrayList<TexturedModel>();
			List<TexturedModel> bList= new ArrayList<TexturedModel>();
			List<List<TexturedModel>> bigList = new ArrayList<List<TexturedModel>>();
			bigList.add(aList);
			bigList.add(bList);
			texturedGunZ.add(type, bigList);
			
			for(int i = 0; i < number_of_models; i++){
				
				ModelData gunData = OBJLoader.loadOBJ(MyPaths.makeOBJPath("Mobs/" +name + "/"+name+(i+1)));
				RawModel RawGun = loader.loadToVAO(gunData.getVertices(), gunData.getTextureCoords(), gunData.getNormals(),
						gunData.getIndices());
				ModelTexture zTexture = new ModelTexture(loader.loadTexture(MyPaths.makeTexturePath("Mobs/"+name+(i+1))));
				zTexture.setShine_damper(5);
				zTexture.setReflectivity(0.3f);
				
				texturedGunZ.get(type).get(0).add(new TexturedModel(RawGun, zTexture, i+1));
				
				gunData = OBJLoader.loadOBJ(MyPaths.makeOBJPath("Mobs/"+name+"/"+name+(i+1)+"B"));
				RawGun = loader.loadToVAO(gunData.getVertices(), gunData.getTextureCoords(), gunData.getNormals(),
						gunData.getIndices());
				
				texturedGunZ.get(type).get(1).add(new TexturedModel(RawGun, zTexture, i+1));
			}
		}
		
		Zombie barn = new Zombie(texturedGunZ.get(type), new Vector3f(25, 0, 25),
				collisions, true, ZombieLoader.b_model_distances[type], ZombieLoader.healths[type], ZombieLoader.speeds[type]);
		
		return barn;
	}
	
	public static void initializeZombies(){
		
		if (texturedGunZ == null){
			texturedGunZ = new ArrayList<List<List<TexturedModel>>>();
			ZombieLoader.Load();
			for(int i = 0; i < ZombieLoader.NUMBER_OF_TYPES; i++){
				texturedGunZ.add(null);
			}
		}
		
		for(int i  = 0; i < ZombieLoader.NUMBER_OF_TYPES; i++){
			@SuppressWarnings("unused")
			Zombie zombie = newZombie(i);
		}
	}
}
