package toolbox;

public final class MyPaths {

	private static final String BASE_PATH = "/res";
		
	public static String makeTexturePath(String input){
		return BASE_PATH + "/Images/"+input+".png";
	}
	
	public static String makeOBJPath(String input){
		
		return BASE_PATH + "/OBJs/"+input+".obj";
	}
	
	public static String makeShaderPath(String input){
		
		return BASE_PATH + "/Shaders/"+input+".txt";
	}
	
	public static String makeSoundPath(String input){
		
		return BASE_PATH + "/Sounds/"+input;
	}
	
	public static String makeSavePath(String input){
		
		return BASE_PATH + "/Save Files/"+input+".data";
	}
}
