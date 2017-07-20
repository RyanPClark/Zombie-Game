package entities;

import java.util.ArrayList;
import java.util.List;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class MultiModeledEntity extends Entity{
	
	private List<List<TexturedModel>> models;
	private boolean b_model;
	private int number_of_parts;

	public MultiModeledEntity(List <List<TexturedModel>> model, Vector3f position,
			float rotX, float rotY, float rotZ, Vector3f scale, String tag,
			boolean b_model, int number_of_parts, boolean Static)
	
	
	{
		super(model.get(0).get(0), position, rotX, rotY, rotZ, scale, tag, Static);

		models = model;
		this.number_of_parts = number_of_parts;
		this.b_model = b_model;
	}
	
	public MultiModeledEntity(TexturedModel model, Vector3f position,
			float rotX, float rotY, float rotZ, Vector3f scale, String tag,
			boolean b_model, int number_of_parts, boolean Static)
	
	
	{
		super(model, position, rotX, rotY, rotZ, scale, tag, Static);

		models = new ArrayList<List<TexturedModel>>();
		List<TexturedModel> moodels = new ArrayList<TexturedModel>();
		moodels.add(model);
		models.add(moodels);
		this.number_of_parts = number_of_parts;
		this.b_model = b_model;
	}

	public List<List<TexturedModel>> getModels() {
		return models;
	}

	public void setModels(List<List<TexturedModel>> models) {
		this.models = models;
	}

	public boolean isB_model() {
		return b_model;
	}

	public void setB_model(boolean b_model) {
		this.b_model = b_model;
	}

	public int getNumber_of_parts() {
		return number_of_parts;
	}

	public void setNumber_of_parts(int number_of_parts) {
		this.number_of_parts = number_of_parts;
	}
	
	
	
}
