package components;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public final class Frustum {

	private static final int angle = Statics.FRUSTUM_ANGLE;
	
	private static Vector4f rightPlane = new Vector4f(0,0,0,-1), leftPlane = new Vector4f(0,0,0,-1);
	private static Vector3f center = new Vector3f(0,0,0);
	
	public static void update(Vector3f position, float rotation){
		
		center = AttackPlayer.moveSomethingBehindPlayer(position, rotation, 5);
		
		rotation = -rotation-90;
		
		rightPlane.x = (float) Math.sin(Math.toRadians(rotation - angle));
		rightPlane.z = (float) Math.cos(Math.toRadians(rotation - angle));
		
		leftPlane.x = -(float) Math.sin(Math.toRadians(rotation + angle));
		leftPlane.z = -(float) Math.cos(Math.toRadians(rotation + angle));
	}

	public static Vector4f getRightPlane() {
		return rightPlane;
	}

	public static Vector4f getLeftPlane() {
		return leftPlane;
	}

	public static Vector3f getCenter(){
		return center;
	}
	
	
}
