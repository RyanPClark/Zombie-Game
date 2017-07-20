package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	private Vector3f position, color, attenuation;
	private float intensity;
	
	public Light(Vector3f position, Vector3f color, float intensity, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.intensity = intensity;
		this.setAttenuation(attenuation);
	}
	
	public Light(Vector3f position, Vector3f color, float intensity) {
		this.position = position;
		this.color = color;
		this.intensity = intensity;
		this.attenuation = new Vector3f(1,0,0);
	}

	public float getIntensity() {
		return intensity;
	}
	
	public void setIntensity(float intensity){
		this.intensity = intensity;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Vector3f attenuation) {
		this.attenuation = attenuation;
	}
	
}
