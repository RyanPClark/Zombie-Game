package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;

public final class GameMath {
	
	private static final Vector3f rotation1 = new Vector3f(1, 0, 0);
	private static final Vector3f rotation2 = new Vector3f(0, 1, 0);
	private static final Vector3f rotation3 = new Vector3f(0, 0, 1);
	
	private static final float PI = 3.14159265f;
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, Vector3f scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate(toRadians(rx), rotation1, matrix, matrix);
		Matrix4f.rotate(toRadians(ry), rotation2, matrix, matrix);
		Matrix4f.rotate(toRadians(rz), rotation3, matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		return matrix;
	}
	public static Matrix4f createTransformationMatrix(Vector3f translation, float ry, Vector3f scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate(toRadians(ry), rotation2, matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		return matrix;
	}
	public static Matrix4f createViewMatrix(Camera camera){
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate(toRadians(camera.getPitch()), rotation1, viewMatrix, viewMatrix);
		Matrix4f.rotate(toRadians(camera.getYaw()), rotation2, viewMatrix, viewMatrix);
		Matrix4f.rotate(toRadians(camera.getRoll()), rotation3, viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		
		return viewMatrix;
	}
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	private static float sign (Vector2f p1, Vector2f p2, Vector2f p3){
		return (p1.x-p3.x)*(p2.y-p3.y)-(p2.x-p3.x)*(p1.y-p3.y);
	}
	public static boolean barryCentric(Vector2f pt, Vector2f v1, Vector2f v2, Vector2f v3, int i){
		
		boolean b1, b2, b3;
		
		b1 = sign(pt, v1, v2) < i;
		b2 = sign(pt, v2, v3) < i;
		b3 = sign(pt, v3, v1) < i;
		
		return ((b1 == b2) && (b2 == b3));
	}
	public static float dotProd(Vector3f a, Vector3f b){
		
		return a.x * b.x + a.y *b.y + a.z * b.z;
	}
	public static float dotProd(Vector4f a, Vector4f b){
		
		return a.x * b.x + a.y *b.y + a.z * b.z + a.w * b.w;
	}
	public static float dotProd(Vector2f a, Vector2f b){
		
		return a.x * b.x + a.y *b.y;
	}
	public static float crossProd(Vector2f a, Vector2f b){
		
		return a.x * b.y - a.y * b.x;
	}
	private static float toRadians(float input){
		
		return PI * input / 180;
	}
}
