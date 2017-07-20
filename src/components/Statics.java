package components;

import org.lwjgl.util.vector.Vector2f;

public final class Statics {

	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.001f;
	public static final float FAR_PLANE = 1024;
	public static final float NORMAL_RENDER_CLIP = 100000;
	
	public static final int FRUSTUM_LENGTH = 256;
	public static final int FRUSTUM_CENTER = -90;
	public static final int FRUSTUM_ANGLE = (int) (FOV/2);
	
	public static final float RUN_SPEED = 0.3f;
	public static final float GRAVITY = -0.055f;
	public static final float JUMP_POWER = 0.7f;
	public static final float sprintModifier = 1.4f;
	public static final float scopeSlowdown = 0.6f;
	public static final float maxPitch = 50;
	public static final float minPitch = -7.5f;
	public static final float devLookUpSpeed = 0.5f;
	public static final float devLookSideSpeed = 1f;
	public static final float CAM_HEIGHT = 6.4f;
	public static final int flySpeed = 5;
	public static final int terrainBounds = 125;
	public static final int rotationCheckFrequency = 4;
	public static final int lookSpeed = 20;
	public static final int max_health = 64;
	
	public static final int reloadTime = 75;
	public static final int reloadSoundID = 6;
	public static final int reloadSoundTime = 15;
	public static final int gunOutSoundID = 3;
	public static final int semiautoShots = 4;
	public static final int maxROFrecoil = 8;
	public static final int recoilSideHook = 5;
	public static final int sniperZoomSpeed = 4;
	public static final int sniperZoomAmount = 36;
	public static final int normalZoomSpeed = 4;
	public static final int normalZoomAmount = 36;
	public static final int rotYShift = -100;
	public static final int bobAmount = 18;
	public static final float bobSpeed = 25f;
	public static final float recoilModifier = 0.0008f;
	
	public static final float playerHitDistance = 2.4f;
	public static final float yRadius = 3;
	public static final float xRadius = 1f;
	public static final float bodyCenterOffset = 0;
	public static final float spawnHeight = -4.5f;
	public static final float risingSpeed = 10;
	public static final float scaleModifier = 2;
	public static final int biteSoundID = 11;
	public static final int biteCounterAmount = 30;
	public static final int bleedingTime = 8;
	public static final int deathCounterTime = 45;
	public static final int spawnRadius = 100;
	public static final int rotationDeathSpeed = 2;
	public static final int scaleVariance = 4;
	

	public static final float distanceBetweenBullets = 70f;
	public static final float distanceBetweenNumbers = 12f;
	public static final Vector2f numberPosition = new Vector2f(0.65f, 0.85f);
	public static final Vector2f numberScale = new Vector2f(0.05f, 0.05f);
	public static final Vector2f gunScale = new Vector2f(0.19f, 0.115f);
	public static final int clickDelay = 250000000;
	
	public static final int SPAWN_RATE = 150;
	public static final int SEA_LEVEL = -5;
	public static final int initialZombieCount = 3;
	public static final int WIDTH = 1080;
	public static final int HEIGHT = 720;
	public static final int FPS_CAP = 60;
	
	public static final int ANTI_ALIASING_LEVEL = 1;
	
	public static final int ANISOTROPIC_LEVEL = 1;
	public static final float LOD_BIAS = 0.4f;
	
	public static final float TERRAIN_SIZE = 500;
	public static final float TERRAIN_MAX_HEIGHT = 8;
	public static final float TERRAIN_MAX_PIXEL_COLOR = 256*256*256;
	public static final int TERRAIN_RES = 1;
	
	public static final float SKY_SIZE = 300f;
	
	public static final int WATER_REFLECTION_RES = 2;
}
