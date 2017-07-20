package entities;


import org.lwjgl.util.vector.Vector3f;

public class Particle{

	private Vector3f position, scale, color;
	protected Vector3f velocity;
	private float rotY, gravity;
	private int lifeTime;
	
	public Particle(Vector3f position, Vector3f scale,
			
			Vector3f velocity, int life_length, float gravity, Vector3f color) {
		
		this.position = position; 
		this.scale = scale;
		this.velocity = velocity;
		this.gravity = gravity;
		this.color = color;
		this.lifeTime = 0;
	}
	
	public void update(float yaw){
		
		velocity.y += gravity;
		position.y += velocity.y;
		lifeTime++;
		position.x += velocity.x;
		position.z += velocity.z;
		rotY = -yaw;
	}
	
	public int getLifeTime(){
		return lifeTime;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getScale() {
		return scale;
	}

	public float getRotY() {
		return rotY;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public float getGravity() {
		return gravity;
	}

	public Vector3f getColor() {
		return color;
	}
	

}
