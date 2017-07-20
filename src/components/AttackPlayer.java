package components;

import org.lwjgl.util.vector.Vector3f;

public final class AttackPlayer {
	
	private static final float PI = 3.14159265f;
	
	public static Vector3f attack(Vector3f position, float speed, float rotY){
		
		rotY = toRadians(rotY);
		
		position.x += speed * Math.sin(rotY);
		position.z += speed * Math.cos(rotY);
		
		return position;
	}
	
	public static Vector3f moveSomethingBehindPlayer(Vector3f playerPosition, float rotation, float amount){
		
		Vector3f returningPosition = new Vector3f(playerPosition.x, playerPosition.y, playerPosition.z);
		
		rotation = toRadians(rotation+90);
		
		returningPosition.x += amount * Math.cos(rotation);
		returningPosition.z += amount * Math.sin(rotation);
		
		return returningPosition;
	}
	
	public static Vector3f moveGunFromPlayer(Vector3f position, float speed, float rotY){

		rotY = toRadians(-rotY-20);
		
		position.x += speed * Math.sin(rotY);
		position.z += speed * Math.cos(rotY);
		
		return position;
		
	}
	
	public static float lookAtPlayer(float px, float pz, Vector3f position, float rotY){
		
		rotY = toDegrees(Math.atan((position.x-px)/(position.z-pz)));
		
		if(position.z - pz > 0){
			rotY +=180;
		}
		
		return rotY;
	}
	
	private static float toRadians(float input){
		
		return PI * input / 180;
	}
	
	private static float toDegrees(double input){
		
		return (float) (180 * input / PI);
	}
	
}
