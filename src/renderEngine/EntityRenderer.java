package renderEngine;

import java.util.List;
import java.util.Map;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.GameMath;
import entities.Entity;

public class EntityRenderer {
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	protected void render(Map<TexturedModel, List<Entity>> entities, boolean wireframe){
		
		for(TexturedModel model:entities.keySet()){
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch){
				
				prepareInstance(entity, model.getPartID());
				
				int renderType = GL11.GL_TRIANGLES;
				
				if (wireframe){
					renderType = GL11.GL_LINE_LOOP;
				}
				
				GL11.glDrawElements(renderType, Math.round(model.getRawModel().getVertexCount()), GL11.GL_UNSIGNED_INT,0);
				
			}
			unBindTexturedModel();
		}
		
	}
	
	protected void makeNewProjectionMatrix(Matrix4f projectionMatrix){
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	private void prepareTexturedModel(TexturedModel model){
		
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		if (texture.isHasTransparency()){
			MasterRenderer.disableCulling();
		}
		if (texture.getReflectivity() > 0){
			shader.loadUseSpecular(true);
		}else{
			shader.loadUseSpecular(false);
		}
		shader.loadUseFakeLighting(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShine_damper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	private void unBindTexturedModel(){
		
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
	}
	
	private void prepareInstance(Entity entity, int i){
		
		Matrix4f transformationMatrix = entity.getMatrix();
		
		if (!entity.isStatic()){
			
			transformationMatrix = GameMath.createTransformationMatrix(entity.getPosition(),
					entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		}
		
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
