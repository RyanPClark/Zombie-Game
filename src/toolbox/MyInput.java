package toolbox;

import org.lwjgl.input.Keyboard;

public final class MyInput {

	private static boolean key0, key1, key2, keyQ, keyTAB, keyV;
	
	public static boolean keyboardClicked(int KEY){
		
		if (KEY == Keyboard.KEY_0){
			if(!Keyboard.isKeyDown(KEY) && key0){
				key0 = false;
				return true;
			}else if (Keyboard.isKeyDown(KEY)){
				key0 = true;
				return false;
				}else {
				return false;
			}
		}else if (KEY == Keyboard.KEY_1){
			if(!Keyboard.isKeyDown(KEY) && key1){
				key1 = false;
				return true;
			}else if (Keyboard.isKeyDown(KEY)){
				key1 = true;
				return false;
				}else {
				return false;
			}
		}else if (KEY == Keyboard.KEY_2){
			if(!Keyboard.isKeyDown(KEY) && key2){
				key2 = false;
				return true;
			}else if (Keyboard.isKeyDown(KEY)){
				key2 = true;
				return false;
				}else {
				return false;
			}
		}else if (KEY == Keyboard.KEY_TAB){
			if(!Keyboard.isKeyDown(KEY) && keyTAB){
				keyTAB = false;
				return true;
			}else if (Keyboard.isKeyDown(KEY)){
				keyTAB = true;
				return false;
				}else {
				return false;
			}
		}
		else if (KEY == Keyboard.KEY_Q){
			if(!Keyboard.isKeyDown(KEY) && keyQ){
				keyQ = false;
				return true;
			}else if (Keyboard.isKeyDown(KEY)){
				keyQ = true;
				return false;
				}else {
				return false;
			}
		}else if (KEY == Keyboard.KEY_V){
			if(!Keyboard.isKeyDown(KEY) && keyV){
				keyV = false;
				return true;
			}else if (Keyboard.isKeyDown(KEY)){
				keyV = true;
				return false;
				}else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
}
