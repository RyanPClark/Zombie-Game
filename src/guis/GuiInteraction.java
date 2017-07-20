package guis;


import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

	public final class GuiInteraction {
		
		protected static void frameGuis(List<GuiTexture> guis, GuiTexture highLightTexture, GUIRenderer guiRenderer, boolean[] skips){
			
			int i = -1;
			
			for(GuiTexture gui : guis){
				
				i++;
				if(skips != null){
					
					if(!skips[i]){
						
						if(isInArea(gui)){
							highLightTexture.setPosition(gui.getPosition());
							highLightTexture.setscale(gui.getScale());
							guiRenderer.Render(highLightTexture);
						}
					}
				}
			}
		}
		
		private static boolean isInArea(GuiTexture gui){
				
			float width = Display.getWidth();
			float height = Display.getHeight();
				
			int mx = Mouse.getX(); mx -= width/2;
			int my = Mouse.getY(); my -= height/2;
				
			Vector2f position = gui.getPosition();
			Vector2f scale = gui.getScale();

			float left = (0.5f*position.x*width + 0.5f*scale.x*width);
			float top = (0.5f*position.y*height + 0.5f*scale.y*height);
			float right = (0.5f*position.x*width - 0.5f*scale.x*width);
			float down = (0.5f*position.y*height - 0.5f*scale.y*height);
				
				
			if(mx < left && mx > right && my > down && my < top){
				return true;
			}
			
			return false;
		}
		protected static boolean isClicked(GuiTexture gui){
			if(Mouse.isButtonDown(0)){
				if(isInArea(gui)){
					return true;
				}
			}
			return false;
		}
}
