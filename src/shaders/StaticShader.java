package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import toolbox.GameMath;
import toolbox.MyPaths;
import entities.Camera;
import entities.Light;

public class StaticShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = MyPaths.makeShaderPath("vertexShader");
	private static final String FRAGMENT_FILE = MyPaths.makeShaderPath("fragmentShader");
	
	private static final int MAX_LIGHTS = 3;
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightColor[];
	private int location_lightPosition[];
	private int location_attenuation[];
	private int location_shine_damper;
	private int location_reflectivity;
	private int location_intensity[];
	private int location_useFakeLighting;
	private int location_plane;
	private int location_right_plane;
	private int location_left_plane;
	private int location_camera_position;
	private int location_useSpecular;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadLight(List<Light> lights){
		for(int i = 0; i < MAX_LIGHTS; i++){
			if (i<lights.size()){
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColor[i], lights.get(i).getColor());
				super.loadFloat(location_intensity[i], lights.get(i).getIntensity());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			}else {
				super.loadVector(location_lightPosition[i], new Vector3f(0,0,0));
				super.loadVector(location_lightColor[i], new Vector3f(0,0,0));
				super.loadFloat(location_intensity[i], 0);
				super.loadVector(location_attenuation[i], new Vector3f(1,0,0));
			}
		}
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shine_damper = super.getUniformLocation("shine_damper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_useSpecular = super.getUniformLocation("useSpecular");
		location_plane = super.getUniformLocation("plane");
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_intensity = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		
		location_right_plane = super.getUniformLocation("rightPlane");
		location_left_plane = super.getUniformLocation("leftPlane");
		location_camera_position = super.getUniformLocation("cameraPosition");
		
		for(int i = 0; i<MAX_LIGHTS; i++){
			location_lightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
			location_lightColor[i] = super.getUniformLocation("lightColor["+i+"]");
			location_intensity[i] = super.getUniformLocation("intensity["+i+"]");
			location_attenuation[i] = super.getUniformLocation("attenuation["+i+"]");
		}
	}
	
	public void loadClipPlanes(Vector4f plane, Vector4f rightPlane, Vector4f leftPlane){
		super.loadVector4f(location_plane, plane);
		super.loadVector4f(location_right_plane, rightPlane);
		super.loadVector4f(location_left_plane, leftPlane);
	}
	
	public void loadCameraPosition(Vector3f position){
		super.loadVector(location_camera_position, position);
	}
	
	public void loadUseSpecular(boolean useSpecular){
		super.loadBoolean(location_useSpecular, useSpecular);
	}
	
	public void loadUseFakeLighting(boolean useFakeLighting){
		super.loadBoolean(location_useFakeLighting, useFakeLighting);
	}
	
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shine_damper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
		
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = GameMath.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

}
