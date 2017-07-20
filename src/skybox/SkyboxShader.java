package skybox;
 
import org.lwjgl.util.vector.Matrix4f;
 


import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
 
import shaders.ShaderProgram;
import toolbox.GameMath;
import toolbox.MyPaths;
 
public class SkyboxShader extends ShaderProgram{
 
    private static final String VERTEX_FILE = MyPaths.makeShaderPath("skyboxVertexShader");
    private static final String FRAGMENT_FILE = MyPaths.makeShaderPath("skyboxFragmentShader");
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
	private int location_right_plane;
	private int location_left_plane;
	private int location_camera_position;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = GameMath.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        super.loadMatrix(location_viewMatrix, matrix);
    }
    
    public void loadClipPlanes(Vector4f rightPlane, Vector4f leftPlane){
    	
		super.loadVector4f(location_right_plane, rightPlane);
		super.loadVector4f(location_left_plane, leftPlane);
	}
    
	public void loadCameraPosition(Vector3f position){
		super.loadVector(location_camera_position, position);
	}
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_right_plane = super.getUniformLocation("rightPlane");
		location_left_plane = super.getUniformLocation("leftPlane");
		location_camera_position = super.getUniformLocation("cameraPosition");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
 
}
