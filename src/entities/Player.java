package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import components.CollisionDetection;
import renderEngine.DisplayManager;
import terrains.Terrain;
import toolbox.GameMath;
import toolbox.MousePicker;

public class Player {

	private static final float TARGET_FPS = DisplayManager.TARGET_FPS;
	private static final float RUN_SPEED = 0.3f;
	private static final float SPRINT_MODIFIER = 1.4f;
	private static final float GRAVITY = -0.055f;
	private static final float SCOPE_SLOWDOWN = 0.6f;
	private static final float CAM_HEIGHT = 6.4f;
	private static final float JUMP_POWER = 0.7f;
	
	private Camera camera;
	private Terrain terrain;
	private MousePicker picker;
	private Gun gun;
	
	private boolean inAir;
	private int health = 64;
	private int score;
	private Vector3f position = new Vector3f(0,0,-100);
	private Vector3f projectedPosition;
	private float terainHeight, speed, upwardsSpeed;
	private Vector2f directionVector = new Vector2f(0,0);
	

	public Player(Camera camera, Terrain terrain, MousePicker picker, Gun gun) {
		this.camera = camera;
		this.terrain = terrain;
		this.picker = picker;
		this.gun = gun;
	}
	
	public void update(float tick){
		
		move(gun.mobility, picker.getCurrentRay(), tick);
		reactToCollisions();
		position = new Vector3f(projectedPosition.x, projectedPosition.y, projectedPosition.z);
		jump(tick);
		updateCamera(tick);
		picker.update();
	}
	
	private void updateCamera(float tick) {
		camera.update(tick);
		camera.setPosition(new Vector3f(position.x, position.y + CAM_HEIGHT, position.z));
	}
	
	private void reactToCollisions(){
		
		int index = CollisionDetection.detectCollisions(projectedPosition);
		
		if(index == -1)
			return;
		
		directionVector = CollisionDetection.collisionDirection(position.x, position.z, projectedPosition.x, projectedPosition.z, index);

		float dotProd = GameMath.dotProd(directionVector, new Vector2f(projectedPosition.x - position.x, projectedPosition.z - position.z));
		
		projectedPosition.x = position.x + speed * directionVector.x * dotProd;
		projectedPosition.z = position.z + speed * directionVector.y * dotProd;
	}
	
	public void move(float mobility, Vector3f cameraRay, float tick){
		
		projectedPosition = new Vector3f(position.x, position.y, position.z);
		terainHeight = terrain.getHeightOfTerrain(position.x, position.z);
		
		speed = RUN_SPEED * mobility * TARGET_FPS * tick;
		speed = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? speed * SPRINT_MODIFIER : speed;
		speed = Mouse.isButtonDown(1) ? speed * SCOPE_SLOWDOWN : speed;

		directionVector.x = 0;
		directionVector.y = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			directionVector.x += cameraRay.x;
			directionVector.y += cameraRay.z;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			directionVector.x -= cameraRay.x;
			directionVector.y -= cameraRay.z;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			directionVector.x -= cameraRay.z;
			directionVector.y += cameraRay.x;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			directionVector.x += cameraRay.z;
			directionVector.y -= cameraRay.x;
		}
		
		projectedPosition.x += speed * directionVector.x;
		projectedPosition.z += speed * directionVector.y;
		
		if (directionVector.lengthSquared() > 0)
			gun.bob();
	}

	
	private void jump(float tick) {
		
		if (position.y <= terainHeight) {
			upwardsSpeed = 0;
			inAir = false;
			position.y = terainHeight;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			if (!inAir) {
				upwardsSpeed = JUMP_POWER;
				inAir = true;
			}
		}
			
		upwardsSpeed += GRAVITY * TARGET_FPS * tick;
		position.y += upwardsSpeed * TARGET_FPS * tick;
	}

	public void decreaseHealth() {
		health--;
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public void setGun(Gun gun) {
		this.gun = gun;
	}
	
	public Gun getGun() {
		return gun;
	}
}
