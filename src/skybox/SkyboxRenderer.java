package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import components.Statics;
import entities.Camera;
import renderEngine.Loader;
import models.RawModel;

public class SkyboxRenderer {
	
	private static final float SKY_SIZE = Statics.SKY_SIZE;
	
	private static final float[] VERTICES = {
		
	    -SKY_SIZE,  SKY_SIZE, -SKY_SIZE,
	    -SKY_SIZE, -SKY_SIZE, -SKY_SIZE,
	    SKY_SIZE, -SKY_SIZE, -SKY_SIZE,
	     SKY_SIZE, -SKY_SIZE, -SKY_SIZE,
	     SKY_SIZE,  SKY_SIZE, -SKY_SIZE,
	    -SKY_SIZE,  SKY_SIZE, -SKY_SIZE,

	    -SKY_SIZE, -SKY_SIZE,  SKY_SIZE,
	    -SKY_SIZE, -SKY_SIZE, -SKY_SIZE,
	    -SKY_SIZE,  SKY_SIZE, -SKY_SIZE,
	    -SKY_SIZE,  SKY_SIZE, -SKY_SIZE,
	    -SKY_SIZE,  SKY_SIZE,  SKY_SIZE,
	    -SKY_SIZE, -SKY_SIZE,  SKY_SIZE,

	     SKY_SIZE, -SKY_SIZE, -SKY_SIZE,
	     SKY_SIZE, -SKY_SIZE,  SKY_SIZE,
	     SKY_SIZE,  SKY_SIZE,  SKY_SIZE,
	     SKY_SIZE,  SKY_SIZE,  SKY_SIZE,
	     SKY_SIZE,  SKY_SIZE, -SKY_SIZE,
	     SKY_SIZE, -SKY_SIZE, -SKY_SIZE,

	    -SKY_SIZE, -SKY_SIZE,  SKY_SIZE,
	    -SKY_SIZE,  SKY_SIZE,  SKY_SIZE,
	     SKY_SIZE,  SKY_SIZE,  SKY_SIZE,
	     SKY_SIZE,  SKY_SIZE,  SKY_SIZE,
	     SKY_SIZE, -SKY_SIZE,  SKY_SIZE,
	    -SKY_SIZE, -SKY_SIZE,  SKY_SIZE,
	    
	    -SKY_SIZE,  SKY_SIZE, -SKY_SIZE,
	     SKY_SIZE,  SKY_SIZE, -SKY_SIZE,
	     SKY_SIZE,  SKY_SIZE,  SKY_SIZE,
	     SKY_SIZE,  SKY_SIZE,  SKY_SIZE,
	    -SKY_SIZE,  SKY_SIZE,  SKY_SIZE,
	    -SKY_SIZE,  SKY_SIZE, -SKY_SIZE,
	  
	};
	
	private static String[] TEXTURE_FILES = {"plain sky/plain_sky_back", "plain sky/plain_sky_front", "plain sky/plain_sky_top",
		"plain sky/plain_sky_top", "plain sky/plain_sky_right", "plain sky/plain_sky_left"};
	
	private RawModel cube;
	private int texture;
	private SkyboxShader shader;
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix){
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Camera camera){
		shader.start();
		shader.loadViewMatrix(camera);
		GL30.glBindVertexArray(cube.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	
	
	
	
	
}
