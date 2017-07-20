package entities;


import models.TexturedModel;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.GameMath;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	
	private float rotX, rotY, rotZ;
	private Vector3f scale;
	private String tag;
	
	private boolean isSelected, isStatic;
	
	private Matrix4f matrix;
	
	private int ID, animID = -1, animCounter;

	public Entity (TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, String tag, boolean isStatic){
		
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.tag = tag;
		if (isStatic){
			this.matrix = GameMath.createTransformationMatrix(position, rotX, rotY, rotZ, scale);
		}
		this.isStatic = isStatic;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public void increasePosition(float dx, float dy, float dz){
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
		
	}
	
	public void increaseRotation(float dx, float dy, float dz){
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	public void increaseScale(float dx, float dy, float dz){
		this.scale.x+=dx;
		this.scale.y+=dy;
		this.scale.z+=dz;
		
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	public void setIsSelected(boolean isSelected){
		this.isSelected = isSelected;
	}
	
	public boolean getIsSelected(){
		return this.isSelected;
	}

	public Matrix4f getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix4f matrix) {
		this.matrix = matrix;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public int getAnimCounter() {
		return animCounter;
	}

	public void setAnimCounter(int animCounter) {
		this.animCounter = animCounter;
	}

	public void setAnimID(int animID) {
		this.animID = animID;
	}
	
	public int getAnimID() {
		return animID;
	}
	
	
	
}
