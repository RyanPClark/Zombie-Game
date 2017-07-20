package textures;

public class TerrainTexturePack {
	
	private TerrainTexture backgroundSampler;
	private TerrainTexture rSampler;
	private TerrainTexture gSampler;
	private TerrainTexture bSampler;
	public TerrainTexturePack(TerrainTexture backgroundSampler,
			TerrainTexture rSampler, TerrainTexture gSampler,
			TerrainTexture bSampler) {
		this.backgroundSampler = backgroundSampler;
		this.rSampler = rSampler;
		this.gSampler = gSampler;
		this.bSampler = bSampler;
	}
	public TerrainTexture getBackgroundSampler() {
		return backgroundSampler;
	}
	public TerrainTexture getrSampler() {
		return rSampler;
	}
	public TerrainTexture getgSampler() {
		return gSampler;
	}
	public TerrainTexture getbSampler() {
		return bSampler;
	}
	
	
	
}
