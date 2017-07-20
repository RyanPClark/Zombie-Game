package shaders;
 
import org.lwjgl.util.vector.Matrix4f;
 



import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import shaders.ShaderProgram;
import toolbox.GameMath;
import toolbox.MyPaths;
 
public class ParticleShader extends ShaderProgram{
     
    private static final String VERTEX_FILE = MyPaths.makeShaderPath("particleVertexShader");
    private static final String FRAGMENT_FILE = MyPaths.makeShaderPath("particleFragmentShader");
    
    private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_color;
 
    public ParticleShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadColor(Vector3f color){
    	super.loadVector(location_color, color);
    }
    
    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }
    
    public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = GameMath.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
 
    @Override
    protected void getAllUniformLocations() {
    	location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_color = super.getUniformLocation("particleColor");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

     
     
 
}
