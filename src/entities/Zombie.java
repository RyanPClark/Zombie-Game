package entities;

import java.util.List;
import java.util.Random;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import sound.Sound;
import terrains.Terrain;
import toolbox.MousePicker;
import components.AttackPlayer;
import components.CollisionDetection;
import components.Statics;

public class Zombie extends MultiModeledEntity{
	
	private Vector3f position, scale, lastPosition;
	private float rotation, rotX, speed, health, maxSpeed, maxHealth;
	private int deathCounter, bleedingCounter, attackCounter, bModelDistance;
	private boolean isBleeding, inFrustum, isRising;
	private List<List<Vector2f>> collisions;
	private Vector2f directionVector;
	
	private boolean checkIfKilledPlayer(float bx, float bz){
		
		if(bx > position.x - Statics.playerHitDistance && bx < position.x + Statics.playerHitDistance && bz < position.z +
				Statics.playerHitDistance && bz > position.z - Statics.playerHitDistance){
			attackCounter++;
			if(attackCounter == Statics.biteCounterAmount){
				attackCounter = 0;
				Sound.playSound(Statics.biteSoundID);
			}
			return true;
		}
		return false;
	}
	
	public void updateZombie(Terrain terrain, Gun gun, MousePicker p, ParticleEmitter e, Camera cam, float fps){
		
		lastPosition = new Vector3f(position.x, position.y, position.z);
		
		speed = maxSpeed;
		speed  *= (60/fps);
		
		float px = cam.getPosition().x;
		float pz = cam.getPosition().z;
		
		if(isBleeding){
			bleedingCounter++;
			if(bleedingCounter == Statics.bleedingTime){
				bleedingCounter = 0;
				isBleeding = false;
			}
		}
		
		if (health > 0){
			
			checkIfKilledPlayer(px,pz);
			
			if(inFrustum){
				
				if(gun.shooting){
					if(p.inEllipse(new Vector3f(position.x, position.y+(gun.offsets[2]+Statics.bodyCenterOffset), position.z),
							Statics.xRadius*scale.x, Statics.yRadius*scale.y, Statics.xRadius*scale.z, e)){
						health -= gun.power;
						isBleeding = true;
					}
				}
				
				position.y = terrain.getHeightOfTerrain(position.x, position.z);
			}
			
			if (!isRising){
				
				if(!(px > position.x - Statics.playerHitDistance && px < position.x + Statics.playerHitDistance
						&& pz < position.z + Statics.playerHitDistance && pz > position.z - Statics.playerHitDistance)){
					
					rotation = AttackPlayer.lookAtPlayer(px, pz, position, rotation);
					position = AttackPlayer.attack(position, speed, rotation);
					
					Vector2f currentCollision = CollisionDetection.detectCollisions(collisions, position);
					if (currentCollision.x == 1){
						reactToCollision((int) currentCollision.y);
					}
					
				}else {
					cam.setHealth(cam.getHealth()-1);
				}
			}
			
			else {
				deathCounter ++;
				position.y += Statics.spawnHeight;
				position.y += deathCounter/Statics.risingSpeed;
				
				if (deathCounter >= Statics.deathCounterTime){
					isRising = false;
				}
			}	
		}
		
		else if(health <= 0){
			die(cam);
		}
		
		setRotY(rotation);
		setRotX(rotX);
		setPosition(position);
	}

	public Zombie(List<List<TexturedModel>> models, Vector3f position,
			List<List<Vector2f>> collisions, boolean b_model, int b_model_distance, float health, float speed) {
		
		super(models, position, 0, 40, 0, new Vector3f(2,2,2), "zombie", b_model, 1, false);
		this.position = position;
		this.scale = new Vector3f(2,2,2);
		this.collisions = collisions;
		this.bModelDistance = b_model_distance;
		this.maxHealth = health;
		this.health = health;
		this.maxSpeed = speed;
		
		respawnSharkCode(0, 0);
	}
	
	private void respawnSharkCode(float px, float pz){
		
		Random random = new Random();
		isRising = true;
		health = maxHealth;
		position.y = -1000;
		rotX = 0;
		
		while(true){
			
			position.x = random.nextInt(Statics.spawnRadius)-Statics.spawnRadius/2 + px;
			position.z = random.nextInt(Statics.spawnRadius)-Statics.spawnRadius/2 + pz;
			if (CollisionDetection.detectCollisions(collisions, position).x == 0){
				break;
			}
		}
		
		scale.x = random.nextFloat()/Statics.scaleVariance+1-(1/(2*Statics.scaleVariance));
		scale.x *= Statics.scaleModifier;
		scale.y = random.nextFloat()/Statics.scaleVariance+1-(1/(2*Statics.scaleVariance));
		scale.y *= Statics.scaleModifier;
		scale.z = scale.x;
	}
	
	private void respawnShark(Camera cam){	
		respawnSharkCode(cam.getPosition().x, cam.getPosition().z);
		cam.setScore(cam.getScore()+1);
	}
	private void die(Camera cam){
		
		deathCounter ++;
		rotX += Statics.rotationDeathSpeed;
		
		if(deathCounter == Statics.deathCounterTime*Statics.rotationDeathSpeed){
			
			deathCounter = 0;
			respawnShark(cam);
		}
	}
	
	private void reactToCollision(int index){
		
		directionVector = CollisionDetection.collisionDirection(new Vector2f(lastPosition.x, lastPosition.z),
				new Vector2f(position.x, position.z), collisions.get(index));
		
		if(directionVector.length() != 0){
			directionVector.normalise();
		}
			
		position = new Vector3f(lastPosition.x, lastPosition.y, lastPosition.z);
		position.x += speed * directionVector.x/1.5f;
		position.z += speed * directionVector.y/1.5f;
		
	}

	public boolean isBleeding() {
		return isBleeding;
	}
	public int getbModelDistance() {
		return bModelDistance;
	}
	public void setInFrustum(boolean inFrustum){
		this.inFrustum = inFrustum;
	}
	
}
