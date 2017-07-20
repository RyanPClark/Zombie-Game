package textures;

public class ModelTexture {

	private int textureID;
	
	private float reflectivity = 0;
	private float shine_damper = 1;
	
	private boolean hasTransparency;
	private boolean useFakeLighting;
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}
	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}
	public boolean isHasTransparency() {
		return hasTransparency;
	}
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}
	public float getReflectivity() {
		return reflectivity;
	}
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	public float getShine_damper() {
		return shine_damper;
	}
	public void setShine_damper(float shine_damper) {
		this.shine_damper = shine_damper;
	}
	public ModelTexture(int id){
		this.textureID = id;
	}public int getID(){
		return this.textureID;
	}
	
}
