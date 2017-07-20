package water;
 
import java.util.List;
 


import models.RawModel;
 


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
 


import renderEngine.Loader;
import toolbox.MyPaths;
import entities.Camera;
 
public class WaterRenderer {
 
	private static final String dudv = MyPaths.makeTexturePath("Terrain/dudv");
	private static final float waveSpeed = 0.00125f;
	
    private RawModel quad;
    private WaterShader shader;
    private WaterFrameBuffers fbos;
    
    private float moveFactor = 0;
    
    private int dudvTexture;
 
    public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        
        dudvTexture = loader.loadTexture(dudv);
        
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
        setUpVAO(loader);
    }
 
    public void render(List<WaterTile> water, Camera camera) {
        prepareRender(camera);
        for (WaterTile tile : water) {
            shader.loadModelMatrix(tile.getMatrix());
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }
        unbind();
    }
     
    private void prepareRender(Camera camera){
        shader.start();
        shader.loadViewMatrix(camera);
        moveFactor += waveSpeed;
        moveFactor %= 1;
        shader.loadMoveFactor(moveFactor);
        GL30.glBindVertexArray(quad.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
    }
    
    public void makeNewProjectionMatrix(Matrix4f projectionMatrix){
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
     
    private void unbind(){
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
 
    private void setUpVAO(Loader loader) {
        // Just x and z vectex positions here, y is set to 0 in v.shader
        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices, 2);
    }
 
}
