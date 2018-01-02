package maps;

import java.util.ArrayList;
import java.util.List;

import loaders.LightLoader;
import loaders.MapLoader;
import loaders.ZombieLoader;

import org.lwjgl.util.vector.Vector3f;

import components.CollisionDetection;
import engineTester.Initialize;
import entities.Camera;
import entities.Light;
import entities.MultiModeledEntity;
import entities.Player;
import entities.Zombie;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import toolbox.GameMath;
import toolbox.MousePicker;
import water.WaterTile;

public final class Map {
	
	private static final int INITIAL_ZOMBIE_COUNT = 3;
	private static final int SPAWN_RATE = 150;
	
	private static Terrain terrain;
	private static List<MultiModeledEntity> entities;
	private static List<Zombie> zombies = new ArrayList<Zombie>();
	private static List<WaterTile> waterTiles = new ArrayList<WaterTile>();
	private static List<Light> lights;
	private static Camera camera;
	private static MousePicker picker;
	private static Player player;
	
	private static int spawnCounter = 0;
	
	public static void init (Camera camera, Player player, MousePicker picker){
		
		CollisionDetection.collisions = MapLoader.readCollisions("collisions");
		entities = MapLoader.Load();
		terrain = MapLoader.loadTerrain("grass", "yellow", "asphalt", "concrete", "blendmap2", "heightmap5");
		lights = LightLoader.Load();
		Initialize.initializeZombies();
		Map.camera = camera;
		Map.player = player;
		Map.picker = picker;
	}
	
	public static void loadZombies(){
		zombies.clear();
		for(int i  = 0; i < INITIAL_ZOMBIE_COUNT; i++)
			zombies.add(Initialize.newZombie(i));
	}
	
	public static void render(Vector3f pos){
		
		MasterRenderer.processTerrain(terrain);
		
		for(Zombie zombie : zombies){
			
			Vector3f zPos = zombie.getPosition();
			if(!GameMath.inFrustum(zPos, camera)) {
				zombie.setInFrustum(false);
				continue;
			}
			
			zombie.setInFrustum(true);
				
			float distanceSquared = (pos.x - zPos.x) * (pos.x - zPos.x) + (pos.y - zPos.y) * (pos.y - zPos.y);
			int modelLOD = (zombie.getbModelDistance() * zombie.getbModelDistance() < distanceSquared) ? 1 : 0;
				
			MasterRenderer.processMultiModeledEntity(zombie, modelLOD);
		}
		
		for (MultiModeledEntity entity : entities){
			MasterRenderer.processMultiModeledEntity(entity, 0);
		}
		
		for (WaterTile water : waterTiles){
			MasterRenderer.processWater(water);
		}
	}
	
	public static void update(){
			
		spawnZombies();
			
		for(int i = 0; i < zombies.size(); i++){
			zombies.get(i).updateZombie(terrain, picker, player);
		}
	}
	
	private static void spawnZombies() {
		spawnCounter++;
		if (spawnCounter != SPAWN_RATE)
			return;
		spawnCounter = 0;
		int type = (int) (System.currentTimeMillis() % ZombieLoader.NUMBER_OF_TYPES);
		zombies.add(Initialize.newZombie(type));
	}
	
	public static void setPlayer(Player player) {
		Map.player = player;
	}
	
	public static Terrain getTerrain() {
		return terrain;
	}
	
	public static List<Light> getLights() {
		return lights;
	}
	
	public static List<MultiModeledEntity> getEntities(){
		return entities;
	}

	public static List<Zombie> getZombies() {
		return zombies;
	}
}