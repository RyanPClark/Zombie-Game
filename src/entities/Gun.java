package entities;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import components.Statics;
import maps.Map;
import renderEngine.MasterRenderer;
import sound.Sound;
import toolbox.GameMath;
import toolbox.MousePicker;


public class Gun extends MultiModeledEntity{
	
	/**
	 * 00  AK-47
	 * 01  SCAR-H
	 * 02  M9s
	 * 03  LMG
	 * 04  shotgun
	 * 05  MP5
	 * 06  sniper
	 * 07  M4A1
	 * 08  uzi
	 * 09  M6034
	 * 10  AK-74U
	 * 11  marvel rifle,
	 * 12  AW50
	 * 13  railgun
	 * 14  CZ805
	 * 15  famas
	 * 16  flintlock
	 * 17  L85A2
	 * 18  LSAT
	 * 19  M14
	 * 20  M39EMR
	 * 21  M98B
	 * 22  minigun
	 * 23  MK16
	 */

	private static final float Z_BODY_CENTER_OFFSET = 0;
	private static final float Z_XZ_RADIUS = 1;
	private static final float Z_Y_RADIUS = 3;

	private static final float RELOAD_TIME = 75;
	
	private Vector3f gunPosition;
	private float speed, zoomAmount, bobMult, deltaAmmo;
	private int recoilCounter, semiautoCounter, reloadingCounter, bobCounter;
	private boolean cocked, reloading, gunForward;
	
	public float ammo, mobility, recoilAmount, power;
	public int ROF, weaponID, maxAmmo, gunSoundID;
	public boolean automatic, semiauto, shooting;
	public float[] offsets = new float[4];
	public float[] movingValues = new float[3];
	
	private Zombie nearestShotZombie = null;
	private float nearstShotZombieDistSqrd = 0;

	public Gun(TexturedModel model, int weapon, String tag) {
		super(model, new Vector3f(0,0,0), 0, 0, 0, new Vector3f(1,1,1), tag, false, 1, false);
		weaponID = weapon;
	}
	
	private void startReload(){
		if (Keyboard.isKeyDown(Keyboard.KEY_E)){
			deltaAmmo = (maxAmmo - ammo)/RELOAD_TIME;
			cocked = reloading = true;
			semiautoCounter = 0;
		}
	}
	
	private void reload(){
		if(!reloading)
			return;
		if(reloadingCounter == Statics.reloadSoundTime)
			Sound.playSound(Statics.reloadSoundID);
		if (reloadingCounter < RELOAD_TIME){
			ammo += deltaAmmo;
			reloadingCounter++;
		}
		else {
			reloading = false;
			reloadingCounter = 0;
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
	
	public void update(Camera camera, MousePicker picker, boolean starting){
		
		moveWithCamera(camera);
		gunPosition = getPosition();
		
		gunPosition = GameMath.moveGunFromPlayer(gunPosition, movingValues[0],
				camera.getYaw()+movingValues[1]-zoomAmount/movingValues[2] + bobMult * bobCounter/Statics.bobSpeed);
		
		if(starting)
			return;
			
		startReload();
		reload();
		notifyGunOut();
		shoot();
		recoil();
		cock();
		scope();
		checkKilledZombies(picker);
	}
	
	public void checkKilledZombies(MousePicker picker) {
		
		if(!shooting)
			return;
		
		nearstShotZombieDistSqrd = 2000000000;
		nearestShotZombie = null;
		
		for (Zombie zombie : Map.getZombies()) {

			if(!zombie.isInFrustum())
				continue;
			
			Vector3f zpos = zombie.getPosition();
			Vector3f zscale = zombie.getScale();
			
			if(picker.inEllipse(new Vector3f(zpos.x, zpos.y+(offsets[2]+Z_BODY_CENTER_OFFSET), zpos.z),
					Z_XZ_RADIUS*zscale.x, Z_Y_RADIUS*zscale.y, Z_XZ_RADIUS*zscale.z)){
				
				float distSqrd = zombie.distSqrdToEnt(gunPosition);
				if(distSqrd < nearstShotZombieDistSqrd) {
					nearstShotZombieDistSqrd = distSqrd;
					nearestShotZombie = zombie;
				}
			}
		}
		if(nearestShotZombie != null)
			nearestShotZombie.getShot(power);
	}
	
	public void bob(){
		if (gunForward){
			bobCounter++;
			gunForward = (bobCounter <= Statics.bobAmount);
		}
		else {
			bobCounter--;
			gunForward = (bobCounter <= -Statics.bobAmount);
		}
		bobMult = Mouse.isButtonDown(1) ? 1 : 2.5f;
	} 
	
	private void scope(){
		
		if(weaponID != 6 && weaponID != 12){
			if(Mouse.isButtonDown(1) && zoomAmount < Statics.normalZoomAmount)
				zoomAmount += Statics.normalZoomSpeed;
			else if (!Mouse.isButtonDown(1) && zoomAmount > 0)
				zoomAmount -= Statics.normalZoomSpeed;
		}
		else {
			float increase = 1;
			if (weaponID == 12)
				increase = 1.5f;
			if(Mouse.isButtonDown(1) && zoomAmount < Statics.sniperZoomAmount)
				zoomAmount += Statics.sniperZoomSpeed*increase;
			else if (!Mouse.isButtonDown(1) && zoomAmount > 0)
				zoomAmount -= Statics.sniperZoomSpeed*increase;
			MasterRenderer.setZoomAmount(zoomAmount);
		}
	}
	
	private void moveWithCamera(Camera camera){
		
		setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y+offsets[0], camera.getPosition().z));
		
		setRotZ(camera.getPitch()+offsets[1]);
		setRotY(-camera.getYaw()+Statics.rotYShift);
	}
}
