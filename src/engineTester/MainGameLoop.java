package engineTester;

import loaders.GunLoader;
import maps.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import components.Frustum;
import entities.Camera;
import entities.Gun;
import enums.Mode;
import enums.State;
import guis.GuiMaster;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import sound.Sound;
import textures.CurseClass;
import toolbox.MousePicker;

public final class MainGameLoop {
	
	private static Camera camera;
	private static MasterRenderer masterRenderer;
	private static Loader loader;
	private static Gun gun;
	private static Mode mode = Mode.PLAYER;
	private static State state = State.CHOOSING;
	private static int weaponID = (int)(System.currentTimeMillis()%24);
	private static Map map; 
	private static boolean wireframe, flying;
	private static float fps;
	private static MousePicker picker;
		
	public static void main(String[] args) {
		
		init();
		
		while(!Display.isCloseRequested()){
			
			checkGuiInteractions();
			render();
			fps = 1000/DisplayManager.getDelta();
			
			if(state == State.CHOOSING)
				startLogic();
			
			else if(state == State.PLAYING)
				gameLogic();
			
		}
		cleanUp();
	}
	
	private static void displayFPS(){
		if (System.currentTimeMillis() % 10 == 0)
			DisplayManager.setTitle((int)fps + "");
	}
	
	private static void init(){
		DisplayManager.createDisplay();
		loader = new Loader();
		GuiMaster.init(loader);
		GuiMaster.RenderSplash();
		DisplayManager.updateDisplay();
		masterRenderer = new MasterRenderer(loader);
		Sound.init();
		camera = new Camera();
		Frustum.update(camera.getPosition(), camera.getYaw());
		Initialize.init(loader);
		GuiMaster.loadRest(loader);
		picker = new MousePicker(camera, masterRenderer.getProjectionMatrix(), map);
		GunLoader.init();
		map = new Map(masterRenderer, camera, mode, picker, fps);
	}
	
	private static void render(){
		map.render(camera.getPosition());
		masterRenderer.render(map.getLights(), camera, wireframe, mode);
		GuiMaster.update(state, gun, camera.getScore());
		if (gun != null)
			masterRenderer.processMultiModeledEntity(gun, 0);
		
		if (Display.wasResized())
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
		DisplayManager.updateDisplay();
	}
	
	private static void startLogic(){
		
		state = GuiMaster.startState();
		int oldWeaponID = weaponID;
		weaponID = GuiMaster.getWeaponID(weaponID);
		Frustum.update(camera.getPosition(), camera.getYaw());
		if(oldWeaponID != weaponID){
			gun = Initialize.loadGun(weaponID);
			map.setGun(gun);
		}
		
		if (gun != null){
			gun.update(camera, masterRenderer, true);
			gun.ammo = gun.maxAmmo;
		}
		
		if(state != State.CHOOSING){
			deltaCursor();
			map.loadZombies();
			camera.setScore(0);
			if (gun == null){
				gun = Initialize.loadGun(weaponID);
				map.setGun(gun);
			}
			
			picker.setOffset((int)gun.offsets[3]);
			masterRenderer.setZoomAmount(0);
		}
		camera.increaseRotation(-0.1f, 0, 0);
	}
	
	private static void gameLogic(){

		if(Keyboard.isKeyDown(Keyboard.KEY_K))
			camera.setHealth(0);
		
		escapeChecker();
		map.update(mode);
		camera.update(map.getTerrain(), gun.mobility, flying, mode, picker.getCurrentRay(), map.getCollisions(), fps);
		gun.update(camera, masterRenderer, false);
		
		if(camera.getHealth() <= 0){
			state = State.CHOOSING;
			camera.setHealth(64);
			Sound.playSound(10);
			Sound.playSound(11);
			deltaCursor();
		}
		displayFPS();
	}
	
	private static void escapeChecker(){
		if (state != State.PAUSED && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			state = State.PAUSED;
			deltaCursor();
		}
	}
	
	private static void deltaCursor(){
		if(state != State.PAUSED)
			Sound.resumeMusic();
		else
			Sound.pauseMusic();
		
		DisplayManager.resetTitle();
		CurseClass.toggle();
	}
	
	private static void cleanUp(){
		
		GuiMaster.cleanUp();
		masterRenderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
		Sound.cleanUp();
		System.exit(0);
	}
	
	private static void checkGuiInteractions(){
		if(state == State.PAUSED){
			if(GuiMaster.savedAndQuit()){
				cleanUp();
			}
			state = GuiMaster.pauseState();
			if(state != State.PAUSED)
				deltaCursor();
		}
	}
}