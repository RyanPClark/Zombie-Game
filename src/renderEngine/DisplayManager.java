package renderEngine;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.ImageIOImageData;

import components.Statics;

import toolbox.MyPaths;

public class DisplayManager {
	
	private static int ANTI_ALIASING_LEVEL = Statics.ANTI_ALIASING_LEVEL;

	private static long lastFrameTime;
	private static float delta;
	
	private static final String DISPLAY_TITLE = "Alpha 0.8.7";
	
	public static void createDisplay(){
		
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			
			Display.setDisplayMode(new DisplayMode(Statics.WIDTH,Statics.HEIGHT));
			
			if(ANTI_ALIASING_LEVEL == 1){
				Display.create(new PixelFormat(), attribs); 
			}else {
				Display.create(new PixelFormat().withSamples(ANTI_ALIASING_LEVEL).withStencilBits(1), attribs); 
			}
			
			Display.setResizable(true);
			lastFrameTime = getCurrentTime();
			try {
				Display.setIcon(new ByteBuffer[] {
				        new ImageIOImageData().imageToByteBuffer(ImageIO.read(DisplayManager.class.getResourceAsStream(MyPaths.makeTexturePath("Misc/gameIcon"))), false, false, null),
				        new ImageIOImageData().imageToByteBuffer(ImageIO.read(DisplayManager.class.getResourceAsStream(MyPaths.makeTexturePath("Misc/gameIcon"))), false, false, null)
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, Statics.WIDTH, Statics.HEIGHT);
		
	}
	
	public static void updateDisplay(){
		
		Display.sync(Statics.FPS_CAP);
		Display.update();
		
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime);
		lastFrameTime = currentFrameTime;
	}
	
	public static void closeDisplay(){
		Display.destroy();
	}
	
	public static void setTitle(String title){
		Display.setTitle(title);
	}public static void resetTitle(){
		Display.setTitle(DISPLAY_TITLE);
	}
	private static long getCurrentTime(){
		return 1000*Sys.getTime()/Sys.getTimerResolution();
	}
	
	public static float getDelta(){
		return delta;
	}
	public static void setTitleDelta(){
		
		Display.setTitle(""+Math.round(1000f/delta));
	}
}
