package guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;

import components.Statics;

import models.RawModel;
import renderEngine.Loader;

public final class GUIRenderer {

	private static final float distanceBetweenBullets = Statics.distanceBetweenBullets;
	
	private final RawModel quad;
	private GuiShader shader;
	
	public GUIRenderer(Loader loader){
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}
		
		
	protected void Render(List<GuiTexture> guis){
			
		initialize();
			
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		for(GuiTexture gui : guis){
				
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			shader.loadTransformation(gui.getMatrix());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
			
		terminate();
	}
		
	protected void Render(GuiTexture gui){

		initialize();
			
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
		
		shader.loadTransformation(gui.getMatrix());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			
		terminate();
	}
		
	protected void renderBullets(GuiTexture bullet, int ammo){
			
		initialize();
			
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, bullet.getTexture());
		
		for(short i = 0; i < ammo; i++){
			
			bullet.setPosition(new Vector2f(bullet.getPosition().x + 1/distanceBetweenBullets, bullet.getPosition().y));
			
			shader.loadTransformation(bullet.getMatrix());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		
		bullet.setPosition(new Vector2f(bullet.getPosition().x - ammo/distanceBetweenBullets, bullet.getPosition().y));
		
		terminate();
	}
	
	private void initialize(){
		
		GL30.glBindVertexArray(quad.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
			
		shader.start();
	}
	
	private void terminate(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
			
		shader.stop();
	}
		
	protected void cleanUp(){
		shader.cleanUp();
	}
}
