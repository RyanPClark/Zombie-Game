package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import components.Statics;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.MultiModeledEntity;
import entities.MultiParticle;
import entities.Particle;
import models.TexturedModel;
import shaders.ParticleShader;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MasterRenderer {
	
	private static float zoomAmount = 0;
	
	private static Matrix4f projectionMatrix;

	private static StaticShader shader = new StaticShader();
	private static TerrainShader terrainShader = new TerrainShader();
	private static ParticleShader particleShader = new ParticleShader();
	private static WaterShader waterShader = new WaterShader();
	
	private static EntityRenderer renderer;
	private static TerrainRenderer terrainRenderer;
	private static SkyboxRenderer skyboxRenderer;
	private static ParticleRenderer particleRenderer;
	private static WaterRenderer waterRenderer;
	
	private static Map<TexturedModel, List<Entity>> entities = new HashMap <TexturedModel, List<Entity>>();
	
	private static List<Terrain> terrains = new ArrayList<Terrain>();
	private static List<MultiParticle> bloods = new ArrayList<MultiParticle>();
	private static List<Particle> bullets = new ArrayList<Particle>();
	private static List<WaterTile> waters = new ArrayList<WaterTile>();
	
	private static WaterFrameBuffers fbos;
	
	public static void init() {
		
		enableCulling();
		createProjectionMatrix();
		fbos = new WaterFrameBuffers();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(projectionMatrix);
		particleRenderer = new ParticleRenderer(particleShader, projectionMatrix);
		waterRenderer = new WaterRenderer(waterShader, projectionMatrix, fbos);
	}
	
	public static Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public static void enableCulling(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void setZoomAmount(float zoom){
		zoomAmount = zoom;
		createProjectionMatrix();
		renderer.makeNewProjectionMatrix(projectionMatrix);
		terrainRenderer.makeNewProjectionMatrix(projectionMatrix);
		particleRenderer.makeNewProjectionMatrix(projectionMatrix);
		waterRenderer.makeNewProjectionMatrix(projectionMatrix);
	}
	
	public static void render(List<Light> light, Camera cam, boolean wireframe){
		
		if (waters.size() > 0){
			fbos.bindReflectionFrameBuffer();
			
			float distance = 2*(cam.getPosition().y - Statics.SEA_LEVEL);
			cam.getPosition().y -= distance;
			cam.invertPitch();
			renderScene(light, cam, wireframe, new Vector4f(0, 1, 0, -Statics.SEA_LEVEL), true);
			cam.getPosition().y += distance;
			cam.invertPitch();
			
			fbos.bindRefractionFrameBuffer();
			renderScene(light, cam, wireframe, new Vector4f(0, -1, 0, Statics.SEA_LEVEL), false);
			fbos.unbindCurrentFrameBuffer();
		}
		
		
		renderScene(light, cam, wireframe, new Vector4f(0, -1, 0, Statics.NORMAL_RENDER_CLIP), true);
		
		if (waters.size() > 0){
			waterRenderer.render(waters, cam);
		}
		
		waters.clear();
		terrains.clear();
		entities.clear();
		bloods.clear();
	}
	
	public static void renderScene(List<Light> light, Camera cam, boolean wireframe, Vector4f clipPlane, boolean skybox){
		prepare();
		
		Vector4f rightPlane = cam.getRightPlane();
		Vector4f leftPlane = cam.getLeftPlane();
		Vector3f center = cam.getCenter();
		
		if(skybox){
			skyboxRenderer.render(cam);
		}
		
		terrainShader.start();
		terrainShader.loadClipPlanes(clipPlane, rightPlane, leftPlane);
		terrainShader.loadCameraPosition(center);
		terrainShader.loadLight(light);
		terrainShader.loadViewMatrix(cam);
		terrainRenderer.render(terrains, wireframe);
		terrainShader.stop();
		
		if (clipPlane.w == 100000){
			particleShader.start();
			particleShader.loadViewMatrix(cam);
			particleRenderer.Render(bloods, true, -cam.getYaw());
			particleShader.stop();
		}
		
		shader.start();
		shader.loadClipPlanes(clipPlane, rightPlane, leftPlane);
		shader.loadCameraPosition(center);
		shader.loadLight(light);
		shader.loadViewMatrix(cam);
		
		renderer.render(entities, wireframe);
		shader.stop();
	}
	
	public static void renderTestScene(Camera cam, List<Light> lights){
		
		prepare();
		
		shader.start();
		shader.loadViewMatrix(cam);
		shader.loadLight(lights);
		
		renderer.render(entities, false);
		shader.stop();
		
		particleShader.start();
		particleShader.loadViewMatrix(cam);
		particleRenderer.SingleParticleRender(bullets, true, -cam.getYaw());
		particleShader.stop();
		
		entities.clear();
		bullets.clear();
	}
	
	public static void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public static void processWater(WaterTile waterTile){
		waters.add(waterTile);
	}
	
	public static void processBloodParticle(MultiParticle particle){
		bloods.add(particle);
	}
	
	public static void processBullet(Particle bullet){
		bullets.add(bullet);
	}
	
	public static void processMultiModeledEntity(MultiModeledEntity entity, int modelLOD){
		
		List<TexturedModel> list = entity.getModels().get(modelLOD);
		
		for(TexturedModel model : list){
			
			List<Entity> batch = entities.get(model);
			
			if (batch!=null){
				batch.add(entity);
			}else{
				List<Entity> newBatch = new ArrayList<Entity>();
				newBatch.add(entity);
				entities.put(model, newBatch);
			}
		}
	}
	
	public static void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		GL11.glEnable(GL30.GL_CLIP_DISTANCE1);
		GL11.glEnable(GL30.GL_CLIP_DISTANCE2);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
	}
	
	private static void createProjectionMatrix(){
		
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) (1f/Math.tan(Math.toRadians((Statics.FOV-zoomAmount)/2)))*aspectRatio;
		float x_scale = y_scale/aspectRatio;
		float frustum_length = Statics.FAR_PLANE - Statics.NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((Statics.FAR_PLANE+Statics.NEAR_PLANE)/frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2*Statics.NEAR_PLANE*Statics.FAR_PLANE)/frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	public static void cleanUp(){
		fbos.cleanUp();
		shader.cleanUp();
		terrainShader.cleanUp();
		particleShader.cleanUp();
		waterShader.cleanUp();
	}
}
