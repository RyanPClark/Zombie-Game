package maps;

import java.util.ArrayList;
import java.util.List;

import loaders.LightLoader;
import loaders.MapLoader;
import loaders.ZombieLoader;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import components.Frustum;
import components.Statics;
import engineTester.Initialize;
import entities.Camera;
import entities.Gun;
import entities.Light;
import entities.MultiModeledEntity;
import entities.ParticleEmitter;
import entities.Zombie;
import enums.Mode;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import toolbox.GameMath;
import toolbox.MousePicker;
import toolbox.MyInput;
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
	private Float fps;
	
	private int spawnCounter = 0;
	
	public Map(MasterRenderer masterRenderer, Camera camera, Mode mode, MousePicker picker, Float fps){
		
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
		this.picker = picker;
		this.fps = fps;
	}
	
	public void loadZombies(){
		zombies.clear();
		emitters.clear();
		
		for(int i  = 0; i < Statics.initialZombieCount; i++){
			zombies.add(Initialize.newZombie(i));
			emitters.add(new ParticleEmitter(null, "BLOOD"));
		}
	}
	
	private boolean inFrustum(Vector3f position){
		
		Vector4f a = new Vector4f(position.x - Frustum.getCenter().x, position.y - Frustum.getCenter().y,
				position.z - Frustum.getCenter().z, 0);
		
		float amount = GameMath.dotProd(a, Frustum.getLeftPlane());
		float amount0 = GameMath.dotProd(a, Frustum.getRightPlane());
		
		return (amount > 0 || amount0 > 0);
	}
	
	public void render(Vector3f position){
		
		masterRenderer.processTerrain(terrain);
		
		for(Zombie zombie : zombies){
			
			Vector3f zPosition = zombie.getPosition();
			
			if (inFrustum(zPosition)){
				
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
	
	public void update(Mode mode){
		
		fps = 1000/DisplayManager.getDelta();
		picker.update(mode);
		
		if(mode == Mode.PLAYER){
			
			spawnCounter++;
			
			if (spawnCounter == Statics.SPAWN_RATE){
				spawnCounter = 0;
				int type = (int) (System.currentTimeMillis() % ZombieLoader.NUMBER_OF_TYPES);
				zombies.add(Initialize.newZombie(type));
				emitters.add(new ParticleEmitter(null, "BLOOD"));
			}
			
			for(int i = 0; i < emitters.size(); i++){
				emitters.get(i).update(camera.getYaw(), zombies.get(i).isBleeding(), masterRenderer);
				zombies.get(i).updateZombie(terrain, gun, picker, emitters.get(i), camera, fps);
			}
		}
		
		else {
			
			Vector3f position = camera.getPosition();
			
			addCollision(position);
			interactWithMapEditor(position);
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
	
	private void addCollision(Vector3f position){
		
		if (MyInput.keyboardClicked(Keyboard.KEY_1)){
			collisions.add(new ArrayList<Vector2f>());
		}else if (MyInput.keyboardClicked(Keyboard.KEY_2)){
			collisions.get(collisions.size()-1).add(new Vector2f(position.x, position.z));
		}
	}
	
	private void interactWithMapEditor(Vector3f cam_position){
		
		for(MultiModeledEntity entity : entities){
			if(entity.getIsSelected()){
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && MyInput.keyboardClicked(Keyboard.KEY_V)){
							
					entity.setIsSelected(false);
							
					Vector3f position = new Vector3f(entity.getPosition().x, entity.getPosition().y, entity.getPosition().z);
					Vector3f scale = new Vector3f(entity.getScale().x, entity.getScale().y, entity.getScale().z);
								
					MultiModeledEntity newEntity = new MultiModeledEntity(entity.getModel(), position, entity.getRotX(),
							entity.getRotY(), entity.getRotZ(), scale, entity.getTag(), false, 1, true);
					newEntity.setIsSelected(true);
					newEntity.setID(entity.getID()+1);
					entities.add(newEntity);
					break;
				}
				else if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)){
					entities.remove(entity);
					break;
				}
			}
		}
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