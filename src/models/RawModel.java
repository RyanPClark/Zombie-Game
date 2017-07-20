package models;

public class RawModel {

	private int vaoID, vertexCount;

	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public int getVaoId(){
		return vaoID;
	}
	
	public int getVertexCount(){
		return vertexCount;
	}
}
