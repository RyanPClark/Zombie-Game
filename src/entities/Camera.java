package entities;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import components.CollisionDetection;
import components.Statics;
import components.Frustum;
import enums.Mode;
import terrains.Terrain;
import toolbox.GameMath;

public class Camera {
	
	private float terainHeight, speed, upwardsSpeed, pitch, roll, yaw = 180;

	private boolean isInAir;
	
	private Vector3f position = new Vector3f(0, Statics.CAM_HEIGHT, -100);
	private int upi, score, health = 64;
	private Vector3f lastPosition;
	private Vector2f directionVector;
	
	public void move(Terrain terrain, float mobility, Vector3f cameraRay, float fps){
		
		lastPosition = new Vector3f(position.x, position.y, position.z);
		directionVector = new Vector2f(0, 0);
		
		terainHeight = terrain.getHeightOfTerrain(position.x, position.z);
		
		speed = Statics.RUN_SPEED * mobility * (60f /fps);
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			speed *= Statics.sprintModifier;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
			speed /= (2*Statics.sprintModifier);
		}
		
		if (Mouse.isButtonDown(1)){
			speed *= Statics.scopeSlowdown;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			directionVector.x += cameraRay.x;
			directionVector.y += cameraRay.z;
		}if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			directionVector.x -= cameraRay.x;
			directionVector.y -= cameraRay.z;
		}if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			directionVector.x -= cameraRay.z;
			directionVector.y += cameraRay.x;
		}if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			directionVector.x += cameraRay.z;
			directionVector.y -= cameraRay.x;
		}
		
		position.x += speed * directionVector.x;
		position.z += speed * directionVector.y;
	}
	
	private void jump(boolean flying, float fps) {
		
		if (!flying){
			if (position.y <= terainHeight + Statics.CAM_HEIGHT) {
			      upwardsSpeed = 0;
			      isInAir = false;
			      position.y = terainHeight + Statics.CAM_HEIGHT;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				if (!isInAir) {
				      upwardsSpeed = Statics.JUMP_POWER;
				      isInAir = true;
				}
			}
			
			upwardsSpeed += Statics.GRAVITY * (60/fps);
			position.y += upwardsSpeed * (60/fps);
		}
		else {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				position.y -= Statics.flySpeed*upwardsSpeed;
			}else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
				position.y += Statics.flySpeed*upwardsSpeed;
			}
		}
	}
	
	public void rotate(Mode mode, float fps){
		
		int centerX = Display.getWidth()/2;
		int centerY = Display.getHeight()/2;
		
		if(mode == Mode.PLAYER){
			upi++;
			if(upi%Statics.rotationCheckFrequency == 0){
				Mouse.setCursorPosition(centerX, centerY);
			}else {
				yaw += (60/fps)*((float)Mouse.getX() -centerX)/Statics.lookSpeed;
				pitch -= (60/fps)*((float)Mouse.getY() - centerY)/Statics.lookSpeed;
			}
		}else {
			
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
				pitch += Statics.devLookUpSpeed;
			}else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
				pitch -= Statics.devLookUpSpeed;
			}else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				yaw -= Statics.devLookSideSpeed;
			}else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				yaw += Statics.devLookSideSpeed;
			}
		}
		
		yaw %= 360;
		
		if (pitch > Statics.maxPitch){
			pitch = Statics.maxPitch;
		}
		else if (pitch < Statics.minPitch){
			pitch = Statics.minPitch;
		}
	}
	
	public void update(Terrain terrain, float mobility, boolean flying, Mode mode,
			Vector3f cameraRay, List<List<Vector2f>> collisions, float fps){
		
		move(terrain, mobility, cameraRay, fps);
		jump(flying, fps);
		rotate(mode, fps);
		setPosition(position);
		Frustum.update(position, yaw);
		
		if(mode == Mode.PLAYER){
			Vector2f currentCollision = CollisionDetection.detectCollisions(collisions, position);
			if (currentCollision.x == 1){
				reactToCollisions(collisions.get((int) currentCollision.y));
			}
		}
	}
	
	private void reactToCollisions(List<Vector2f> collisions){
		
		directionVector = CollisionDetection.collisionDirection(new Vector2f(lastPosition.x, lastPosition.z),
				new Vector2f(position.x, position.z), collisions);
		
		if (directionVector.length() != 0){
			
			directionVector.normalise();
			
			float dotProd = GameMath.dotProd(directionVector, new Vector2f(position.x - lastPosition.x, position.z - lastPosition.z));
			
			lastPosition.x += dotProd * speed * directionVector.x;
			lastPosition.z += dotProd * speed * directionVector.y;
		}
		
		setPosition(new Vector3f(lastPosition.x, position.y, lastPosition.z));
	}
	
	public void increaseRotation(float dx, float dy, float dz){
		yaw += dx;
		pitch += dy;
		roll += dz;
	}
	
	public void increasePosition(float dx, float dy, float dz){
		position.x += dx;
		position.y += dy;
		position.z += dz;
	}
	
	private void setPosition(Vector3f position) {
		this.position = position;
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
	public void invertPitch(){
		this.pitch = -pitch;
	}
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
}
