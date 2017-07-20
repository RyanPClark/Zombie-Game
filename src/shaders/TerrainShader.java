package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import toolbox.GameMath;
import toolbox.MyPaths;
import entities.Camera;
import entities.Light;

public class TerrainShader extends ShaderProgram{
	
	private static final int MAX_LIGHTS = 3;
	
	private static final String VERTEX_FILE = MyPaths.makeShaderPath("terrainVertexShader");
	private static final String FRAGMENT_FILE = MyPaths.makeShaderPath("terrainFragmentShader");
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightColor[];
	private int location_lightPosition[];
	private int location_attenuation[];
	private int location_intensity[];
	private int location_backgroundSampler;
	private int location_rSampler;
	private int location_gSampler;
	private int location_bSampler;
	private int location_blendMap;
	private int location_plane;
	private int location_right_plane;
	private int location_left_plane;
	private int location_camera_position;

	public TerrainShader() {
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
		location_backgroundSampler = super.getUniformLocation("backgroundSampler");
		location_rSampler = super.getUniformLocation("rSampler");
		location_gSampler = super.getUniformLocation("gSampler");
		location_bSampler = super.getUniformLocation("bSampler");
		location_blendMap = super.getUniformLocation("blendMapSampler");
		location_plane = super.getUniformLocation("plane");
		location_right_plane = super.getUniformLocation("rightPlane");
		location_left_plane = super.getUniformLocation("leftPlane");
		location_camera_position = super.getUniformLocation("cameraPosition");
		
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_intensity = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		
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
	
	public void connectTextureUnits(){
		super.loadInt(location_backgroundSampler, 0);
		super.loadInt(location_rSampler, 1);
		super.loadInt(location_gSampler, 2);
		super.loadInt(location_bSampler, 3);
		super.loadInt(location_blendMap, 4);
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
