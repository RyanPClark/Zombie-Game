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

import components.Frustum;
import components.Statics;
import enums.Mode;
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
	
	private float zoomAmount = 0;
	
	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();
	private ParticleShader particleShader = new ParticleShader();
	private WaterShader waterShader = new WaterShader();
	
	private EntityRenderer renderer;
	private TerrainRenderer terrainRenderer;
	private SkyboxRenderer skyboxRenderer;
	private ParticleRenderer particleRenderer;
	private WaterRenderer waterRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap <TexturedModel, List<Entity>>();
	
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<MultiParticle> bloods = new ArrayList<MultiParticle>();
	private List<Particle> bullets = new ArrayList<Particle>();
	private List<WaterTile> waters = new ArrayList<WaterTile>();
	
	private WaterFrameBuffers fbos;
	
	public MasterRenderer(Loader loader){
		
		enableCulling();
		createProjectionMatrix();
		fbos = new WaterFrameBuffers();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		particleRenderer = new ParticleRenderer(loader, particleShader, projectionMatrix);
		waterRenderer = new WaterRenderer(loader, waterShader, projectionMatrix, fbos);
	}
	
	public Matrix4f getProjectionMatrix() {
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
	
	public void setZoomAmount(float zoom){
		zoomAmount = zoom;
		createProjectionMatrix();
		renderer.makeNewProjectionMatrix(projectionMatrix);
		terrainRenderer.makeNewProjectionMatrix(projectionMatrix);
		particleRenderer.makeNewProjectionMatrix(projectionMatrix);
		waterRenderer.makeNewProjectionMatrix(projectionMatrix);
	}
	
	public void render(List<Light> light, Camera cam, boolean wireframe, Mode mode){
		
		if (waters.size() > 0){
			fbos.bindReflectionFrameBuffer();
			
			float distance = 2*(cam.getPosition().y - Statics.SEA_LEVEL);
			cam.getPosition().y -= distance;
			cam.invertPitch();
			renderScene(light, cam, wireframe, new Vector4f(0, 1, 0, -Statics.SEA_LEVEL), true, mode);
			cam.getPosition().y += distance;
			cam.invertPitch();
			
			fbos.bindRefractionFrameBuffer();
			renderScene(light, cam, wireframe, new Vector4f(0, -1, 0, Statics.SEA_LEVEL), false, mode);
			fbos.unbindCurrentFrameBuffer();
		}
		
		
		renderScene(light, cam, wireframe, new Vector4f(0, -1, 0, Statics.NORMAL_RENDER_CLIP), true, mode);
		
		if (waters.size() > 0){
			waterRenderer.render(waters, cam);
		}
		
		waters.clear();
		terrains.clear();
		entities.clear();
		bloods.clear();
	}
	
	public void renderScene(List<Light> light, Camera cam, boolean wireframe, Vector4f clipPlane, boolean skybox, Mode mode){
		prepare();
		
		Vector4f rightPlane = Frustum.getRightPlane();
		Vector4f leftPlane = Frustum.getLeftPlane();
		Vector3f center = Frustum.getCenter();
		
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
		
		renderer.render(entities, wireframe, mode);
		shader.stop();
	}
	
	public void renderTestScene(Camera cam, List<Light> lights){
		
		prepare();
		
		shader.start();
		shader.loadViewMatrix(cam);
		shader.loadLight(lights);
		
		renderer.render(entities, false, Mode.PLAYER);
		shader.stop();
		
		particleShader.start();
		particleShader.loadViewMatrix(cam);
		particleRenderer.SingleParticleRender(bullets, true, -cam.getYaw());
		particleShader.stop();
		
		entities.clear();
		bullets.clear();
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void processWater(WaterTile waterTile){
		waters.add(waterTile);
	}
	
	public void processBloodParticle(MultiParticle particle){
		bloods.add(particle);
	}
	
	public void processBullet(Particle bullet){
		bullets.add(bullet);
	}
	
	public void processMultiModeledEntity(MultiModeledEntity entity, int modelLOD){
		
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
	
	public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		GL11.glEnable(GL30.GL_CLIP_DISTANCE1);
		GL11.glEnable(GL30.GL_CLIP_DISTANCE2);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
	}
	
	private void createProjectionMatrix(){
		
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
	
	public void cleanUp(){
		fbos.cleanUp();
		shader.cleanUp();
		terrainShader.cleanUp();
		particleShader.cleanUp();
		waterShader.cleanUp();
	}
}
