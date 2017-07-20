
package water;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.GameMath;
 
public class WaterTile {
     
    private float WATER_TILE_SIZE = 125;
     
    private float x,z, height;
    private Matrix4f matrix;
     
    public WaterTile(float centerX, float centerZ, float height, float TILE_SIZE){
        this.x = centerX;
        this.z = centerZ;
        this.height = height;
        this.WATER_TILE_SIZE = TILE_SIZE;
        this.matrix = GameMath.createTransformationMatrix(
                new Vector3f(x, height, z), 0,
                new Vector3f(WATER_TILE_SIZE, WATER_TILE_SIZE, WATER_TILE_SIZE));
    }
 
    public float getHeight() {
        return height;
    }
 
    public float getX() {
        return x;
    }
 
    public float getZ() {
        return z;
    }

	public float getWATER_TILE_SIZE() {
		return WATER_TILE_SIZE;
	}

	public void setWATER_TILE_SIZE(float wATER_TILE_SIZE) {
		WATER_TILE_SIZE = wATER_TILE_SIZE;
	}

	public Matrix4f getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix4f matrix) {
		this.matrix = matrix;
	}
 
 
 
}