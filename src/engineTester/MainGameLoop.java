package engineTester;

import loaders.GunLoader;
import maps.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import entities.Camera;
import entities.Gun;
import entities.Player;
import enums.State;
import guis.GuiMaster;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import sound.Sound;
import textures.CurseClass;
import toolbox.MousePicker;

public final class MainGameLoop {
	
	private static Player player;
	private static Camera camera;
	private static Gun gun;
	private static State state = State.CHOOSING;
	private static int weaponID = (int)(System.currentTimeMillis()%24);
	private static boolean wireframe;
	private static float tick;
	private static MousePicker picker;
		
	public static void main(String[] args) {
		
		init();
		
		while(!Display.isCloseRequested()){

			tick = DisplayManager.displayFPS();
			checkGuiInteractions();
			render();
			
			if(state == State.CHOOSING)
				startLogic();
			
			else if(state == State.PLAYING)
				gameLogic();
			
		}
		cleanUp();
	}
	
	private static void init(){
		DisplayManager.createDisplay();
		GuiMaster.init();
		GuiMaster.RenderSplash();
		DisplayManager.updateDisplay();
		GuiMaster.loadRest();
		GunLoader.init();
		Sound.init();
		MasterRenderer.init();
		camera = new Camera();
		picker = new MousePicker(camera, MasterRenderer.getProjectionMatrix());
		Map.init(camera, player, picker);
		player = new Player(camera, Map.getTerrain(), picker, gun);
		Map.setPlayer(player);
	}
	
	private static void render(){
		Map.render(camera.getPosition());
		MasterRenderer.render(Map.getLights(), camera, wireframe);
		GuiMaster.update(state, gun, player.getScore());
		if (gun != null)
			MasterRenderer.processMultiModeledEntity(gun, 0);
		
		if (Display.wasResized())
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
		DisplayManager.updateDisplay();
	}
	
	private static void startLogic(){
		
		state = GuiMaster.startState();
		camera.updateFrustum();
		
		int oldWeaponID = weaponID;
		weaponID = GuiMaster.getWeaponID(weaponID);
		
		if(oldWeaponID != weaponID){
			gun = Initialize.loadGun(weaponID);
			player.setGun(gun);
		}
		
		if (gun != null){
			gun.update(camera, picker, true);
			gun.ammo = gun.maxAmmo;
		}
		
		if(state != State.CHOOSING){
			deltaCursor();
			Map.loadZombies();
			player.setScore(0);
			if (gun == null){
				gun = Initialize.loadGun(weaponID);
				player.setGun(gun);
			}
			
			picker.setOffset((int)gun.offsets[3]);
			MasterRenderer.setZoomAmount(0);
		}
		camera.increaseRotation(-0.1f, 0, 0);
	}
	
	private static void gameLogic(){

		if(Keyboard.isKeyDown(Keyboard.KEY_K))
			player.setHealth(0);
		
		escapeChecker();
		Map.update();
		player.update(tick);
		gun.update(camera, picker, false);
		
		if(player.getHealth() <= 0){
			state = State.CHOOSING;
			player.setHealth(64);
			Sound.playSound(10);
			Sound.playSound(11);
			deltaCursor();
		}
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
		MasterRenderer.cleanUp();
		Loader.cleanUP();
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