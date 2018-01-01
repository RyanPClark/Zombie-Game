package entities;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import components.Statics;
import renderEngine.MasterRenderer;
import sound.Sound;
import toolbox.GameMath;


public class Gun extends MultiModeledEntity{
	
	/**
	 * AK-47, SCAR-H, M9s, LMG,
	 * shotgun, MP5, sniper, M4A1,
	 * uzi, M6034, AK-74U, marvel rifle,
	 * AW50, railgun, CZ805, famas,
	 * flintlock, L85A2, LSAT, M14,
	 * M39EMR, M98B, minigun, MK16
	 */
	
	private Vector3f gunPosition;
	private float speed, zoomAmount, bobMult, deltaAmmo;
	private int recoilCounter, semiautoCounter, reloadingCounter, bobCounter;
	private boolean cocked, reloading, gunForward;
	
	public float ammo, mobility, recoilAmount, power;
	public int ROF, weaponID, maxAmmo, gunSoundID;
	public boolean automatic, semiauto, shooting;
	public float[] offsets, movingValues;

	public Gun(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, Vector3f scale, int weapon, String tag) {
		super(model, position, rotX, rotY, rotZ, scale, tag, false, 1, false);
		
		weaponID = weapon;
	}
	
	private void startReload(){
		if (Keyboard.isKeyDown(Keyboard.KEY_E)){
			deltaAmmo = (maxAmmo - ammo)/(float)Statics.reloadTime;
			cocked = true;
			semiautoCounter = 0;
			reloading = true;
		}
	}
	
	private void reload(){
		if(reloading){
			if(reloadingCounter == Statics.reloadSoundTime){
				Sound.playSound(Statics.reloadSoundID);
			}
			if (reloadingCounter < Statics.reloadTime){
				ammo += deltaAmmo;
				reloadingCounter++;
			}else {
				reloading = false;
				reloadingCounter = 0;
			}
		}
	}
	
	private void shoot(){
		
		if(Mouse.isButtonDown(0) && recoilCounter == ROF && cocked){
			
			if(ammo > 0 && !reloading){
				ammo --;
				recoilCounter = 0;
				Sound.playSound(gunSoundID);
				shooting = true;
				if(!automatic){
					cocked = false;
				}
				if(semiauto){
					semiautoCounter++;
					if (semiautoCounter == Statics.semiautoShots){
						cocked = false;
						semiautoCounter = 0;
					}
				}
			}
		}else {
			shooting = false;
		}
		
	}
	
	private void notifyGunOut(){
		if (ammo <= 0){
			cocked = false;
			if(Mouse.isButtonDown(0) && recoilCounter == ROF){
				Sound.playSound(Statics.gunOutSoundID);
			}
		}
	}
	
	private void recoil(){
			
		if (ROF > Statics.maxROFrecoil){
			speed = Statics.recoilModifier * recoilAmount * Math.abs(recoilCounter - Statics.maxROFrecoil/2);
			if(recoilCounter > Statics.maxROFrecoil){
				speed = 0;
			}
		}else {
			speed = Statics.recoilModifier * recoilAmount * Math.abs(recoilCounter - ROF/2);
		}
			
		gunPosition.y -= speed;
		gunPosition.x -= speed/Statics.recoilSideHook;
		
		if(recoilCounter < ROF){
			recoilCounter++;
		}
	}
	
	private void cock(){
		if(!Mouse.isButtonDown(0)){
			cocked = true;
			semiautoCounter = 0;
		}
	}
	
	public void update(Camera camera, MasterRenderer masterRenderer, boolean starting){
		
		moveWithCamera(camera);
		gunPosition = getPosition();
		
		if(!starting){
			
			startReload();
			reload();
			notifyGunOut();
			shoot();
			recoil();
			cock();
			bob();
			scope(masterRenderer);
		}
		
		gunPosition = GameMath.moveGunFromPlayer(gunPosition, movingValues[0],
				camera.getYaw()+movingValues[1]-zoomAmount/movingValues[2] + bobMult * bobCounter/Statics.bobSpeed);
	}
	
	private void bob(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_S) || 
				Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_A)){
			
			if (gunForward){
				bobCounter++;
				if (bobCounter >= Statics.bobAmount){
					gunForward = false;
				}
			} else {
				bobCounter--;
				if (bobCounter <= -Statics.bobAmount){
					gunForward = true;
				}
			}
		}
		bobMult = 1;
		if (!Mouse.isButtonDown(1)){
			bobMult = 2.5f;
		}
	} 
	
	private void scope(MasterRenderer masterRenderer){
		
		if(weaponID != 6 && weaponID != 12){
			if(Mouse.isButtonDown(1) && zoomAmount < Statics.normalZoomAmount){
				zoomAmount += Statics.normalZoomSpeed;
			}else if (!Mouse.isButtonDown(1) && zoomAmount > 0){
				zoomAmount -= Statics.normalZoomSpeed;
			}
		}else {
			float increase = 1;
			if (weaponID == 12){
				increase = 1.5f;
			}
			if(Mouse.isButtonDown(1) && zoomAmount < Statics.sniperZoomAmount){
				zoomAmount += Statics.sniperZoomSpeed*increase;
				masterRenderer.setZoomAmount(zoomAmount);
			}else if (!Mouse.isButtonDown(1) && zoomAmount > 0){
				zoomAmount -= Statics.sniperZoomSpeed*increase;
				masterRenderer.setZoomAmount(zoomAmount);
			}
			
		}
	}
	
	private void moveWithCamera(Camera camera){
		
		setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y+offsets[0],
				camera.getPosition().z));
		
		setRotZ(camera.getPitch()+offsets[1]);
		setRotY(-camera.getYaw()+Statics.rotYShift);
	}
	
	public void setSpecificData(Vector3f scale, boolean transparency, int ROF, boolean automatic, int maxAmmo, float recoilAmount, float power, float[] offsets, float mobility,
			int gunSoundID, float[] movingValues, boolean semiauto){
	
		this.setScale(scale); this.getModel().getTexture().setHasTransparency(transparency);
		this.ROF = ROF; this.automatic = automatic; this.maxAmmo = maxAmmo;
		this.recoilAmount = recoilAmount; this.power = power; this.offsets = offsets;
		this.mobility = mobility; this.gunSoundID = (int) gunSoundID;
		this.movingValues = movingValues; this.semiauto = semiauto; this.ammo = maxAmmo;
	}
	
}
