package entities;

import java.util.List;
import java.util.Random;

import models.TexturedModel;
import renderEngine.DisplayManager;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import sound.Sound;
import terrains.Terrain;
import toolbox.GameMath;
import toolbox.MousePicker;
import components.CollisionDetection;

public class Zombie extends MultiModeledEntity{
	
	private static final float TARGET_FPS = DisplayManager.TARGET_FPS;
	private Vector3f position, scale, lastPosition;
	private float rotation, rotX, speed, health, maxSpeed, maxHealth;
	private int deathCounter, bleedingCounter, attackCounter, bModelDistance;
	private boolean isBleeding, inFrustum, isRising;
	private Vector2f directionVector;
	private ParticleEmitter emitter;
	
	private static final float PLAYER_HIT_DIST = 2.4f;
	private static final int BITE_SOUND_ID = 11;
	private static final int BITE_COUNT_AMT = 30;
	private static final int BLEEDING_TIME = 8;
	
	private static final float SPAWN_HEIGHT = -4.5f;
	private static final float RISE_SPEED = 10;
	private static final float SCALE_MODIFIER = 2;
	private static final int DEATH_COUNT_TIME = 45;
	private static final int SPAWN_RAD = 100;
	private static final int ROT_DEATH_SPEED = 2;
	private static final int SCALE_VARIANCE = 4;
	
	private boolean checkIfKilledPlayer(float bx, float bz){
		
		if(bx > position.x - PLAYER_HIT_DIST && bx < position.x + PLAYER_HIT_DIST && bz < position.z +
				PLAYER_HIT_DIST && bz > position.z - PLAYER_HIT_DIST){
			attackCounter++;
			if(attackCounter == BITE_COUNT_AMT){
				attackCounter = 0;
				Sound.playSound(BITE_SOUND_ID);
			}
			return true;
		}
		return false;
	}
	
	public void updateZombie(Terrain terrain, MousePicker p, Player player){

		emitter.setEmitting(isBleeding);
		emitter.update();
		lastPosition = new Vector3f(position.x, position.y, position.z);
		speed = maxSpeed * TARGET_FPS * DisplayManager.getTick();
		
		if(isBleeding){
			bleedingCounter++;
			if(bleedingCounter == BLEEDING_TIME){
				bleedingCounter = 0;
				isBleeding = false;
			}
		}
		
		if(health <= 0) {
			die(player);
			return;
		}

		float px = player.getPosition().x;
		float pz = player.getPosition().z;
		checkIfKilledPlayer(px,pz);
		
		if(inFrustum) {
			rotation = GameMath.lookAtPlayer(px, pz, position, rotation);
			position.y = terrain.getHeightOfTerrain(position.x, position.z);
		}
		
		if (!isRising){
				
			if (inPlayerHitBox(px, pz))
				player.decreaseHealth();
			else {
				
				position = GameMath.attack(position, speed, rotation);	
				reactToCollision();
			}
		}	
		else {
			deathCounter ++;
			position.y += SPAWN_HEIGHT;
			position.y += deathCounter/RISE_SPEED;
			
			if (deathCounter >= DEATH_COUNT_TIME){
				isRising = false;
			}
		}
		
		setRotY(rotation);
		setRotX(rotX);
		setPosition(position);
	}

	private boolean inPlayerHitBox(float px, float pz) {
		return (px > position.x - PLAYER_HIT_DIST && px < position.x + PLAYER_HIT_DIST && pz < position.z + PLAYER_HIT_DIST && pz > position.z - PLAYER_HIT_DIST);
	}
	
	public Zombie(List<List<TexturedModel>> models, Vector3f position, boolean b_model, int b_model_distance, float health, float speed) {
		
		super(models, position, 0, 40, 0, new Vector3f(2,2,2), "zombie", b_model, 1, false);
		this.position = position;
		this.scale = new Vector3f(2,2,2);
		this.bModelDistance = b_model_distance;
		this.maxHealth = health;
		this.health = health;
		this.maxSpeed = speed;
		
		respawn(0, 0);
	}
	
	private void respawn(float px, float pz){
		
		Random random = new Random();
		isRising = true;
		health = maxHealth;
		position.y = -1000;
		rotX = 0;
		
		while(true){
			
			position.x = random.nextInt(SPAWN_RAD)-SPAWN_RAD/2 + px;
			position.z = random.nextInt(SPAWN_RAD)-SPAWN_RAD/2 + pz;
			if (CollisionDetection.detectCollisions(position) == -1)
				break;
		}
		
		scale.x = random.nextFloat()/SCALE_VARIANCE+1-(1/(2*SCALE_VARIANCE));
		scale.x *= SCALE_MODIFIER;
		scale.y = random.nextFloat()/SCALE_VARIANCE+1-(1/(2*SCALE_VARIANCE));
		scale.y *= SCALE_MODIFIER;
		scale.z = scale.x;
	}
	
	private void die(Player player){
		
		deathCounter ++;
		rotX += ROT_DEATH_SPEED;
		
		if(deathCounter == DEATH_COUNT_TIME * ROT_DEATH_SPEED){
			
			deathCounter = 0;
			respawn(player.getPosition().x, player.getPosition().z);
			player.setScore(player.getScore()+1);
		}

		setRotY(rotation);
		setRotX(rotX);
		setPosition(position);
	}
	
	private void reactToCollision(){
		
		int index = CollisionDetection.detectCollisions(position);
		
		if(index == -1)
			return;
		
		directionVector = CollisionDetection.collisionDirection(lastPosition.x, lastPosition.z, position.x, position.z, index);
			
		position = new Vector3f(lastPosition.x, lastPosition.y, lastPosition.z);
		position.x += speed * directionVector.x * 0.667f;
		position.z += speed * directionVector.y * 0.667f;
		
	}
	
	public float distSqrdToEnt(Vector3f p) {
		return (float) (Math.pow((position.x - p.x), 2) + Math.pow((position.z - p.z), 2));
	}
	
	public void getShot(float power) {
		health -= power;
		isBleeding = true;
		
		Random r = new Random();
		emitter.setPosition(new Vector3f(position.x + r.nextFloat() - 0.5f, position.y + r.nextFloat() * 2 + 5, position.z + r.nextFloat()-0.5f));
	}

	public int getbModelDistance() {
		return bModelDistance;
	}
	
	public void setInFrustum(boolean inFrustum){
		this.inFrustum = inFrustum;
	}

	public void setEmitter(ParticleEmitter emitter) {
		this.emitter = emitter;
	}

	public boolean isInFrustum() {
		return inFrustum;
	}
}
