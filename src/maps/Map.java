package maps;

import java.util.ArrayList;
import java.util.List;

import loaders.LightLoader;
import loaders.MapLoader;
import loaders.ZombieLoader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import components.Statics;
import engineTester.Initialize;
import entities.Camera;
import entities.Gun;
import entities.Light;
import entities.MultiModeledEntity;
import entities.ParticleEmitter;
import entities.Player;
import entities.Zombie;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import toolbox.GameMath;
import toolbox.MousePicker;
import water.WaterTile;

public final class Map {
	
	private Terrain terrain;
	private List<List<Vector2f>> collisions;
	private List<MultiModeledEntity> entities;
	private List<ParticleEmitter> emitters;
	private List<Zombie> zombies;
	private List<WaterTile> waterTiles;
	private List<Light> lights;
	private MasterRenderer masterRenderer;
	private Camera camera;
	private MousePicker picker;
	private Gun gun;
	private Player player;
	
	private int spawnCounter = 0;
	
	public Map(MasterRenderer masterRenderer, Camera camera, Player player, MousePicker picker){
		
		collisions = Initialize.loadCollisions();
		entities = MapLoader.Load();
		terrain = MapLoader.loadTerrain("grass", "yellow", "asphalt", "concrete", "blendmap2", "heightmap5");
		emitters = new ArrayList<ParticleEmitter>();
		zombies = new ArrayList<Zombie>();
		waterTiles = new ArrayList<WaterTile>();
		lights = LightLoader.Load();
		Initialize.initializeZombies();
		this.masterRenderer = masterRenderer;
		this.camera = camera;
		this.player = player;
		this.picker = picker;
	}
	
	public void loadZombies(){
		zombies.clear();
		emitters.clear();
		
		for(int i  = 0; i < Statics.initialZombieCount; i++){
			zombies.add(Initialize.newZombie(i));
			emitters.add(new ParticleEmitter(null, "BLOOD"));
		}
	}
	
	public void render(Vector3f position){
		
		masterRenderer.processTerrain(terrain);
		
		for(Zombie zombie : zombies){
			
			Vector3f zPosition = zombie.getPosition();
			
			if (GameMath.inFrustum(zPosition, camera)){
				
				zombie.setInFrustum(true);
				
				int modelLOD = 0;
				
				float distanceSquared = (position.x - zPosition.x) * (position.x - zPosition.x) +
						(position.y - zPosition.y) * (position.y - zPosition.y);
				
				if (zombie.getbModelDistance() * zombie.getbModelDistance() < distanceSquared){
					modelLOD = 1;
				}
				
				masterRenderer.processMultiModeledEntity(zombie, modelLOD);
			}else {
				zombie.setInFrustum(false);
			}
		}
		
		for (MultiModeledEntity entity : entities){
			masterRenderer.processMultiModeledEntity(entity, 0);
		}
		
		for (WaterTile water : waterTiles){
			masterRenderer.processWater(water);
		}
	}
	
	public void update(){
		
		picker.update();
		
		spawnCounter++;
			
		if (spawnCounter == Statics.SPAWN_RATE){
			spawnCounter = 0;
			int type = (int) (System.currentTimeMillis() % ZombieLoader.NUMBER_OF_TYPES);
			zombies.add(Initialize.newZombie(type));
			emitters.add(new ParticleEmitter(null, "BLOOD"));
		}
			
		for(int i = 0; i < emitters.size(); i++){
			emitters.get(i).update(camera.getYaw(), zombies.get(i).isBleeding(), masterRenderer);
			zombies.get(i).updateZombie(terrain, gun, picker, emitters.get(i), player);
		}
	}
	
	public void setSelectedEntity(String tag, int ID){
		
		for(MultiModeledEntity entity : entities){
			if (entity.getTag().equals(tag) && entity.getID() == ID){
				entity.setIsSelected(true);
			}else {
				entity.setIsSelected(false);
			}
		}
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setGun(Gun gun){
		this.gun = gun;
	}
	
	public Terrain getTerrain() {
		return terrain;
	}

	public List<List<Vector2f>> getCollisions() {
		return collisions;
	}
	
	public List<Light> getLights() {
		return lights;
	}
	
	public List<MultiModeledEntity> getEntities(){
		return entities;
	}
}