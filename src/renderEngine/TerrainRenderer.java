package renderEngine;

import java.util.List;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.TerrainShader;
import terrains.Terrain;
import textures.TerrainTexturePack;

public class TerrainRenderer {
	
	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix){
		
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	protected void render(List<Terrain> terrains, boolean wireframe) {
		for (Terrain terrain:terrains){
			
			int vertexCount = terrain.getModel().getVertexCount();
					
			prepareTerrain(terrain);
					
			if(!wireframe){
				GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount,
						GL11.GL_UNSIGNED_INT, 0);
			}else {
				GL11.glDrawElements(GL11.GL_LINE_LOOP, vertexCount,
						GL11.GL_UNSIGNED_INT, 0);
			}
					
			unBindTexturedModel();
				
		}
	}

	protected void makeNewProjectionMatrix(Matrix4f projectionMatrix){
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	private void prepareTerrain(Terrain terrain){
		
		RawModel rawModel = terrain.getModel();
		
		GL30.glBindVertexArray(rawModel.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		
		shader.loadTransformationMatrix(terrain.getMatrix());
	}
	
	private void bindTextures(Terrain terrain){
		TerrainTexturePack texturePack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundSampler().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrSampler().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgSampler().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbSampler().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	}
	
	private void unBindTexturedModel(){
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
	}
	
	
	
	
	
}
