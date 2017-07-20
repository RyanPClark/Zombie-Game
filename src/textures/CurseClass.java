package textures;

import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import toolbox.MyPaths;

public final class CurseClass {
	
	private static void setCursorBlank(String path){
		Texture tex = null;
		try {
			tex = TextureLoader.getTexture("PNG", CurseClass.class.getResourceAsStream(path));
		} catch (Exception e) {}
	    int byteCount = 4; //Set this to 4 for PNG, TGA, etc.
	    int trueWidth = tex.getImageWidth() * byteCount;

	    int[] array = new int[tex.getImageWidth() * tex.getImageHeight()];
	    byte[] data = tex.getTextureData();

	    for(int y = 0; y < tex.getImageHeight(); y++) {
	      for(int x = 0; x < tex.getImageWidth(); x++) {
	        int i = y * trueWidth + x * byteCount; int r = data[i]     & 0xFF;
	        int g = data[i + 1] & 0xFF; int b = data[i + 2] & 0xFF;
	        int a = 0xFF;

	        if(byteCount == 4) {
	          a = data[i + 3] & 0xFF;

	          if(a > 0) {
	            double ap = a / 255.0; //alpha percentage
	            r = (int)Math.round(r * ap); g = (int)Math.round(g * ap);
	            b = (int)Math.round(b * ap); a = 0xFF;
	          }
	        }

	        array[y * tex.getImageWidth() + x] =
	          (a << 24) | (r << 16) | (g <<  8) | (b);
	      }
	    }
	    Cursor cursor = null;
		try {
			cursor = new Cursor(tex.getImageWidth(),tex.getImageHeight(),0,
			tex.getImageHeight() - 1,1,IntBuffer.wrap(array),null);
			Mouse.setNativeCursor(cursor);
		} catch (LWJGLException e) {}
	}public static void reset(){
		try {
			Mouse.setNativeCursor(null);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}public static void toggle(){
		
		if (Mouse.getNativeCursor() == null){
			setCursorBlank(MyPaths.makeTexturePath("Misc/test"));
		}else {
			reset();
		}
	}
}
