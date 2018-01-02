package guis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import components.Statics;
import entities.Gun;
import enums.State;
import renderEngine.Loader;
import sound.Sound;
import toolbox.MyPaths;

public final class GuiMaster {
	
	private static GUIRenderer guiRenderer;
	private static boolean[] pauseSkips = {true, false, false, true};
	private static boolean[] startSkips = {true, false, false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false, false, false, false};
	
	private static List<GuiTexture> sniperGuis, numbers, startGuis, pausedGuis;
	
	private static GuiTexture sniperGui, bullet, soundIconGui, splash, gui, gui2, gui3, gui4, gui5, randomGui, highLightTexture;
	
	public static void init(){
		
		guiRenderer = new GUIRenderer();
		splash = new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/splashscreen")), new Vector2f(0f,0f), new Vector2f(1f,1f));
	}
	
	public static void RenderSplash(){
		guiRenderer.Render(splash);
	}
	
	public static void loadRest(){
		
		pausedGuis = new ArrayList<GuiTexture>();
		gui = new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/pausescreen")), new Vector2f(0f,0f), new Vector2f(1f,1f));
		gui3 = new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/resumeGame")), new Vector2f(0.0f,0.3f), new Vector2f(0.40f,0.20f));
		gui4 = new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/saveAndQuit")), new Vector2f(0.0f,-0.3f), new Vector2f(0.40f,0.20f));
		
		pausedGuis.add(gui);
		pausedGuis.add(gui3);
		pausedGuis.add(gui4);
		
		bullet = new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/bulletIcon")), new Vector2f(-0.95f,0.9f), new Vector2f(0.02f,0.06f));
		
		startGuis = new ArrayList<GuiTexture>();
		
		gui2 = new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/startscreen")), new Vector2f(0,0), new Vector2f(1f,1f));
		gui5 = new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/begin")), new Vector2f(-0.6f,0.075f), new Vector2f(0.30f,0.2f));
		randomGui = new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/random")), new Vector2f(-0.6f,-0.225f), new Vector2f(0.3f,0.08f));
		soundIconGui = new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/soundIcon")), new Vector2f(-0.85f,-0.85f), new Vector2f(0.03f,0.04f));
		
		sniperGuis = new ArrayList<GuiTexture>();
		sniperGui = new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Gun/sniper_scope")), new Vector2f(0.15f,0.1f), new Vector2f(1.5f,1.5f));
		
		startGuis.add(gui2);startGuis.add(gui5);
		
		for (int i = 0; i < 24; i++){
			float px = 0.0f; float py = 0.875f - 0.25f*i; if(i>7){px = 0.4f; py += 2;}if(i>15){px = 0.8f; py += 2;}
			startGuis.add(new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/gunGui"+(i+1))), new Vector2f(px,py), Statics.gunScale));
		}
		
		startGuis.add(soundIconGui);
		startGuis.add(randomGui);
		
		highLightTexture  = new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/highlight")), new Vector2f(0f,0f), new Vector2f(1f,1f));
		
		sniperGuis.add(sniperGui);
		
		numbers = new ArrayList<GuiTexture>();
		
		for(int i = 0; i < 10; i++){
			numbers.add(new GuiTexture(Loader.loadTexture(MyPaths.makeTexturePath("Guis/"+i)), Statics.numberPosition, Statics.numberScale));
		}
	}
	
	public static State pauseState(){
		if(GuiInteraction.isClicked(gui3)){
			return State.PLAYING;
		}else {
			return State.PAUSED;
		}
	}
	
	public static State startState(){
		
		if(GuiInteraction.isClicked(gui5)){
			return State.PLAYING;
		}else {
			return State.CHOOSING;
		}
	}
	
	public static boolean savedAndQuit(){
		if(GuiInteraction.isClicked(gui4)){
			return true;
		}else {
			return false;
		}
	}
	
	private static boolean musicOn(boolean currentState){
		
		if(GuiInteraction.isClicked(soundIconGui)){
			
			for(int i = 0; i < Statics.clickDelay; i++){}
			return !currentState;
		}else {
			return currentState;
		}
	}
	
	public static int getWeaponID(int weaponID){
		
		for (int i = 0; i < 24; i++){
			if(GuiInteraction.isClicked(startGuis.get(i+2))){
				return i;
			}
		}
		if(GuiInteraction.isClicked(randomGui)){
			Random r = new Random();
			return r.nextInt(24);
		}
		
		return weaponID;
	}
	
	public static void update(State state, Gun gun, int score){
		
		if(state == State.PLAYING){
			renderBullets((int)gun.ammo);
			if((gun.weaponID == 6 && Mouse.isButtonDown(1))||(gun.weaponID == 12 && Mouse.isButtonDown(1))){
				guiRenderer.Render(sniperGuis);
			}
			renderScore(score, new Vector2f(0,0));
		}
		if(state == State.PAUSED){
			guiRenderer.Render(pausedGuis);
			renderBullets((int)gun.ammo);
			GuiInteraction.frameGuis(pausedGuis, highLightTexture, guiRenderer, pauseSkips);
			Sound.setMusicOn(musicOn(Sound.getMusicOn()));
			
		}else if(state == State.CHOOSING){
			guiRenderer.Render(startGuis);
			GuiInteraction.frameGuis(startGuis, highLightTexture, guiRenderer, startSkips);
			renderScore(score, new Vector2f(-1.6f,0));
			Sound.setMusicOn(musicOn(Sound.getMusicOn()));
			if (Sound.getMusicOn()){
				Sound.setMusicOn();
			}else {
				Sound.setMusicOff();
			}
		}
		
		
	}
	public static void cleanUp(){
		guiRenderer.cleanUp();
	}
	public static void renderBullets(int ammo){
		
		guiRenderer.renderBullets(bullet, ammo);
	}
	
	private static void renderScore(int score, Vector2f offsets){
		
		String score_str = "" + score;
		int length = score_str.length();
		
		for(int i = 0; i < length; i++){
			
			String subString = score_str.substring(i, i+1);
			Integer letter = Integer.parseInt(subString);
			GuiTexture tex = numbers.get(letter);
			tex.setPosition(new Vector2f(tex.getPosition().x + i/Statics.distanceBetweenNumbers + offsets.x, tex.getPosition().y+offsets.y));
			guiRenderer.Render(tex);
			tex.setPosition(new Vector2f(tex.getPosition().x - i/Statics.distanceBetweenNumbers - offsets.x, tex.getPosition().y-offsets.y));
			
		}
		
	}
	
	
}
