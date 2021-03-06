
package water;
 
import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;
import toolbox.GameMath;
import toolbox.MyPaths;
import entities.Camera;
 
public class WaterShader extends ShaderProgram {
 
    private final static String VERTEX_FILE = MyPaths.makeShaderPath("waterVertex");
    private final static String FRAGMENT_FILE = MyPaths.makeShaderPath("waterFragment");
 
    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;
    private int location_dudv;
    private int location_moveFactor;
    private int location_cameraPosition;
 
    public WaterShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }
 
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_modelMatrix = getUniformLocation("modelMatrix");
        location_reflectionTexture = getUniformLocation("reflectionTexture");
        location_refractionTexture = getUniformLocation("refractionTexture");
        location_dudv = getUniformLocation("dudv");
        location_moveFactor = getUniformLocation("moveFactor");
        location_cameraPosition = getUniformLocation("cameraPosition");
    }
 
    public void connectTextureUnits(){
    	super.loadInt(location_reflectionTexture, 0);
    	super.loadInt(location_refractionTexture, 1);
    	super.loadInt(location_dudv, 2);
    }
    
    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }
     
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = GameMath.createViewMatrix(camera);
        loadMatrix(location_viewMatrix, viewMatrix);
        super.loadVector(location_cameraPosition, camera.getPosition());
    }
 
    public void loadModelMatrix(Matrix4f modelMatrix){
        loadMatrix(location_modelMatrix, modelMatrix);
    }
 
    public void loadMoveFactor(float moveFactor){
    	loadFloat(location_moveFactor, moveFactor);
    }

}
 