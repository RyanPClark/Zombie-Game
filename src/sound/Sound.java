package sound;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import loaders.SoundLoader;
import toolbox.MyPaths;

public final class Sound {
	
	private static Clip[] clip;
	private static AudioInputStream[] sounds;
	private static float volumes[];
	private static String[] soundStrings;
	private static boolean musicOn = true;

	public static void init(){
		sounds = new AudioInputStream[15];
		soundStrings = new String[15];
		clip = new Clip[15];
		volumes = new float[15];
		
		soundStrings = SoundLoader.load();
		volumes = SoundLoader.getVolumes();
		
		for (int i = 0; i < soundStrings.length; i++){
			try {
			    sounds[i]=AudioSystem.getAudioInputStream(
			    		new BufferedInputStream(Sound.class.getResourceAsStream(
			    				MyPaths.makeSoundPath(soundStrings[i]))));
			    clip[i] = AudioSystem.getClip();
			
				if(i == 1){
					clip[i].open(sounds[i]);
					FloatControl gainControl = 
						    (FloatControl) clip[i].getControl(FloatControl.Type.MASTER_GAIN);
						gainControl.setValue(volumes[i]);
				}
				
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
			
		}
		
		clip[1].loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void playSound(int i){
		
		if(!clip[i].isOpen()){
			try {
				clip[i].open(sounds[i]);
				FloatControl gainControl = 
					    (FloatControl) clip[i].getControl(FloatControl.Type.MASTER_GAIN);
					gainControl.setValue(volumes[i]);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (musicOn){
			clip[i].setFramePosition(0);
			clip[i].stop();
		    clip[i].start();
		}
		
	}
	
	public static void pauseMusic(){
		
		for(int i = 0; i < clip.length; i++){
			clip[i].stop();
		}
		
	}
	
	public static void resumeMusic(){
		if (musicOn){
			for(int i = 0; i < clip.length; i++){
				if(clip[i].isOpen()){
					clip[i].start();
				}
			}
		}
		
	}
	
	public static void cleanUp(){
		for(int i = 0; i < clip.length; i++){
			if (clip[i].isOpen()){
				clip[i].close();
			}
		}
	}
	
	public static void scream(){
		
		if (musicOn){
			clip[2].start();
		}
	}
	
	public static void setMusicOn(){
		
		if (clip[1].isOpen()){
			clip[1].start();
		}
		musicOn = true;
	}
	
	public static void setMusicOff(){
		pauseMusic();
		musicOn = false;
	}
	
	public static boolean getMusicOn(){
		return musicOn;
	}

	public static void setMusicOn(boolean _musicOn) {
		musicOn = _musicOn;
	}
}
