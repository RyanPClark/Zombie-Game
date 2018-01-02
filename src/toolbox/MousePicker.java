package toolbox;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;

public final class MousePicker {

	private Vector3f currentRay = new Vector3f();
	private Vector3f realRay = new Vector3f();

	private Matrix4f projectionMatrix, viewMatrix;
	private Camera camera;
	
	private int gunOffset = 150;

	public MousePicker(Camera cam, Matrix4f projection) {
		camera = cam;
		projectionMatrix = projection;
		viewMatrix = GameMath.createViewMatrix(camera);
	}
	
	public void setOffset(int offset){
		gunOffset = offset;
	}

	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public Vector3f getRealRay(){
		return realRay;
	}

	public void update() {
		
		viewMatrix = GameMath.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		realRay = currentRay;
	}

	private Vector3f calculateMouseRay() {
		
		float mouseX = Display.getWidth()/2 + gunOffset;
		float mouseY = Display.getHeight()/2;
		
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}

	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / Display.getWidth() - 1f;
		float y = (2.0f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
	
	//**********************************************************
	
	private float square(float a){
		return a*a;
	}
	
	
	@SuppressWarnings("unused")
	private boolean inSphere(Vector3f center, float radius){
		
		Vector3f orginDifference = new Vector3f(camera.getPosition().x - center.x, camera.getPosition().y - center.y,
				camera.getPosition().z - center.z);
		
		float b = GameMath.dotProd(currentRay, orginDifference);
		float c = GameMath.dotProd(orginDifference, orginDifference) - (radius*radius);
		
		float d = (b*b)-c;
		
		return d >= 0;
	}
	
	public boolean inEllipse(Vector3f center, float c, float g, float k){
		
		/**
		 
		 c is for x
		 g is for y
		 k is for z 
		 
		 */
		
		Vector3f orginDifference = new Vector3f(camera.getPosition().x - center.x, camera.getPosition().y - center.y,
				camera.getPosition().z - center.z);
		
		float a = orginDifference.x;
		float d = orginDifference.y;
		float h = orginDifference.z;
		
		float b = currentRay.x;
		float f = currentRay.y;
		float j = currentRay.z;
		
		float ANSWER;
		
		float PART_1A = 2*a*b*g*g*k*k+2*c*c*d*f*k*k+2*c*c*g*g*h*j;
		PART_1A = square(PART_1A);
		float PART_1B = a*a*g*g*k*k+c*c*d*d*k*k+c*c*g*g*h*h-c*c*g*g*k*k;
		float PART_1C = b*b*g*g*k*k+c*c*f*f*k*k+c*c*g*g*j*j;
		float PART_1 = PART_1A - 4 * PART_1B * PART_1C;
		
		float PART_2A = 2*a*b*g*g*k*k;
		float PART_2B = 2*c*c*d*f*k*k;
		float PART_2C = 2*c*c*g*g*h*j;
		
		ANSWER = PART_1 - PART_2A - PART_2B - PART_2C;
		
		return ANSWER >= 0;
	}

}
