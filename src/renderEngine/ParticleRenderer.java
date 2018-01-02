package renderEngine;

import entities.MultiParticle;
import entities.Particle;

import java.util.List;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.ParticleShader;
import toolbox.GameMath;

public class ParticleRenderer {

	
	private final RawModel quad;
	private ParticleShader shader;

	public ParticleRenderer(ParticleShader shader, Matrix4f projectionMatrix){
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		this.shader = shader;
		quad = Loader.loadToVAO(positions, 2);
		this.shader.start();
		this.shader.loadProjectionMatrix(projectionMatrix);
		this.shader.stop();
	}
	
	protected void makeNewProjectionMatrix(Matrix4f projectionMatrix){
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	
	protected void Render(List<MultiParticle> guis, boolean blood, float yaw){
		
		GL30.glBindVertexArray(quad.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		
		if (blood && guis.size() > 0){
			shader.loadColor(guis.get(0).getColor());
		}
		
		for(MultiParticle gui : guis){
			
			for(int i = 0; i < gui.getSize(); i++){
				
				Matrix4f matrix = GameMath.createTransformationMatrix(gui.getPosition()[i], yaw, gui.getScale()[i]);
				shader.loadTransformation(matrix);
				if (!blood){
					shader.loadColor(gui.getColor());
				}
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
		}
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	protected void SingleParticleRender(List<Particle> guis, boolean blood, float yaw){
		
		GL30.glBindVertexArray(quad.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		
		for(Particle gui : guis){
			
			Matrix4f matrix = GameMath.createTransformationMatrix(gui.getPosition(), yaw, gui.getScale());
			shader.loadTransformation(matrix);
			shader.loadColor(gui.getColor());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
