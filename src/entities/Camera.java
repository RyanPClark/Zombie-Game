package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;
import toolbox.GameMath;

public class Camera {
	
	private static final float TARGET_FPS = DisplayManager.TARGET_FPS;
	private static final int FRUSTUM_ANGLE = 35;
	private static final int ROT_CHECK_FRQ = 4;
	private static final float MAX_PITCH = 50;
	private static final float MIN_PITCH = -7.5f;
	private static final float LOOK_SPEED = 0.05f;
	
	private Vector4f rightPlane = new Vector4f(0,0,0,-1), leftPlane = new Vector4f(0,0,0,-1);
	private Vector3f center = new Vector3f(0,0,0);
	
	private Vector3f position = new Vector3f(0,0,0);
	private int upi;

	private float pitch, roll, yaw = 180;
	
	
	public void updateFrustum (){
		
		center = GameMath.moveSomethingBehindPlayer(position, yaw, 5);
		
		rightPlane.x = -(float) Math.cos(Math.toRadians(yaw + FRUSTUM_ANGLE));
		rightPlane.z = -(float) Math.sin(Math.toRadians(yaw + FRUSTUM_ANGLE));
		
		leftPlane.x = (float) Math.cos(Math.toRadians(yaw - FRUSTUM_ANGLE));
		leftPlane.z = (float) Math.sin(Math.toRadians(yaw - FRUSTUM_ANGLE));
	}
	
	private void rotate(float tick){

		upi++;

		int centerX = Display.getWidth()/2;
		int centerY = Display.getHeight()/2;
			
		if (upi % ROT_CHECK_FRQ == 0)
		{
			Mouse.setCursorPosition(centerX, centerY);
		}
		else {
			yaw += LOOK_SPEED * TARGET_FPS * tick * ((float)Mouse.getX() -centerX);
			pitch -= LOOK_SPEED * TARGET_FPS * tick * ((float)Mouse.getY() - centerY);
		}
		
		yaw = yaw < 0 ? yaw + 360 : yaw;
		yaw %= 360;
		
		pitch = pitch > MAX_PITCH ? MAX_PITCH : pitch;
		pitch = pitch < MIN_PITCH ? MIN_PITCH : pitch;
	}
	
	public void update(float tick){
		
		rotate(tick);
		updateFrustum();
	}
	
	public void increaseRotation(float dx, float dy, float dz){
		yaw += dx;
		pitch += dy;
		roll += dz;
	}
	
	public Vector4f getRightPlane() {
		return rightPlane;
	}

	public Vector4f getLeftPlane() {
		return leftPlane;
	}

	public Vector3f getCenter() {
		return center;
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

	public void setPosition(Vector3f position) {
		this.position = position;
	}
}
