<<<<<<< HEAD
package com.idk.coin;

import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AlarmSound {
	public static void main(String[] args) {
		for(;;) {
				AlarmSound.playAlert();
			try {
			Thread.sleep(1000);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void beep01() {
		playBeep("beep01.wav");	
	}
	public static void beep02() {
		playBeep("beep02.wav");
	}
	public static void beep04() {
		playBeep("beep04.wav");
	}
	public static void beep05() {
		playBeep("beep05.wav");
	}
	public static void alarm01() {
		playAlarm("Alarm02.wav");
	}
	public static void playSound() {
		playSound("Alarm02.wav");
	}
	public static void playAlert() {
		playDistress("button09.wav");
	}
	public static void playDistress() {
		playDistress("distress.wav");
	}
	
	public static boolean is_alarm = false;
	 //max 6.0206 ~ -80.0
	public static boolean is_beep  = false;public static float   default_dp = -20;
	public static void playAlarm(String filename) {
		if(is_alarm) return ;
		is_alarm = true;
		try {
			
			URL url = AlarmSound.class.getResource(filename);
			File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 double gain = .5D; // number between 0 and 1 (loudest)
			 float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			 gainControl.setValue(default_dp);
			 clip.start();
			 
			 
			 new Thread(new Runnable() {
				 public void run() {
					 try {
						 Thread.sleep(1000*3); 
					 }catch(Exception e) {
						 
					 }
					 clip.stop();
					 is_alarm = false;
					// gainControl.setValue(ovol);
					 
				 }
			 }).start();
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void playBeep(String filename) {
		if(is_beep) return;
		is_beep = true;
		try {
			URL url = AlarmSound.class.getResource(filename);
			File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 double gain = 3; // number between 0 and 1 (loudest)
			 float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			
			 gainControl.setValue(default_dp);
			//System.out.println(gainControl.getMaximum() + " , " + gainControl.getMinimum());
			//System.out.println(gainControl.toString());
			// float ovol  = gainControl.getValue();
			// gainControl.setValue(10.0f);
			 clip.start();
			 
			 
			 new Thread(new Runnable() {
				 public void run() {
					 try {
						 Thread.sleep(1000*1);
					 }catch(Exception e) {
						 
					 }
					 clip.stop();
					 is_beep = false;
					// gainControl.setValue(ovol);
					 
				 }
			 }).start();
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void playSound(String filename) {
		try {
			
			URL url = AlarmSound.class.getResource(filename);
			File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 //double gain = .5D; // number between 0 and 1 (loudest)
			 //float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			 gainControl.setValue(default_dp);
			// float ovol  = gainControl.getValue();
			// gainControl.setValue(10.0f);
			 clip.start();
			 
			 
			/* new Thread(new Runnable() {
				 public void run() {
					 try {
						 Thread.sleep(1000*1); 
					 }catch(Exception e) {
						 
					 }
					 clip.stop();
					// gainControl.setValue(ovol);
					 
				 }
			 }).start();*/
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void playDistress(String filename) {
		///if(is_beep) return;
		//is_beep = true;
		try {
			URL url = AlarmSound.class.getResource(filename);
			File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 double gain = .5D; // number between 0 and 1 (loudest)
			 float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			 gainControl.setValue(default_dp);
			// float ovol  = gainControl.getValue();
			// gainControl.setValue(10.0f);
			 clip.start();
			 
			 
			 new Thread(new Runnable() {
				 public void run() {
					 try {
						 Thread.sleep(1000*2);
					 }catch(Exception e) {
						 
					 }
					 clip.stop();
					 is_beep = false;
					// gainControl.setValue(ovol);
					 
				 }
			 }).start();
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
=======
package com.idk.coin;

import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AlarmSound {
	public static void main(String[] args) {
		for(;;) {
				AlarmSound.playAlert();
			try {
			Thread.sleep(1000);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void beep01() {
		playBeep("beep01.wav");	
	}
	public static void beep02() {
		playBeep("beep02.wav");
	}
	public static void beep04() {
		playBeep("beep04.wav");
	}
	public static void beep05() {
		playBeep("beep05.wav");
	}
	public static void alarm01() {
		playAlarm("Alarm02.wav");
	}
	public static void playSound() {
		playSound("Alarm02.wav");
	}
	public static void playAlert() {
		playDistress("button09.wav");
	}
	public static void playDistress() {
		playDistress("distress.wav");
	}
	
	public static boolean is_alarm = false;
	 //max 6.0206 ~ -80.0
	public static boolean is_beep  = false;public static float   default_dp = -20;
	public static void playAlarm(String filename) {
		if(is_alarm) return ;
		is_alarm = true;
		try {
			
			URL url = AlarmSound.class.getResource(filename);
			File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 double gain = .5D; // number between 0 and 1 (loudest)
			 float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			 gainControl.setValue(default_dp);
			 clip.start();
			 
			 
			 new Thread(new Runnable() {
				 public void run() {
					 try {
						 Thread.sleep(1000*3); 
					 }catch(Exception e) {
						 
					 }
					 clip.stop();
					 is_alarm = false;
					// gainControl.setValue(ovol);
					 
				 }
			 }).start();
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void playBeep(String filename) {
		if(is_beep) return;
		is_beep = true;
		try {
			URL url = AlarmSound.class.getResource(filename);
			File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 double gain = 3; // number between 0 and 1 (loudest)
			 float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			
			 gainControl.setValue(default_dp);
			//System.out.println(gainControl.getMaximum() + " , " + gainControl.getMinimum());
			//System.out.println(gainControl.toString());
			// float ovol  = gainControl.getValue();
			// gainControl.setValue(10.0f);
			 clip.start();
			 
			 
			 new Thread(new Runnable() {
				 public void run() {
					 try {
						 Thread.sleep(1000*1);
					 }catch(Exception e) {
						 
					 }
					 clip.stop();
					 is_beep = false;
					// gainControl.setValue(ovol);
					 
				 }
			 }).start();
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void playSound(String filename) {
		try {
			
			URL url = AlarmSound.class.getResource(filename);
			File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 //double gain = .5D; // number between 0 and 1 (loudest)
			 //float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			 gainControl.setValue(default_dp);
			// float ovol  = gainControl.getValue();
			// gainControl.setValue(10.0f);
			 clip.start();
			 
			 
			/* new Thread(new Runnable() {
				 public void run() {
					 try {
						 Thread.sleep(1000*1); 
					 }catch(Exception e) {
						 
					 }
					 clip.stop();
					// gainControl.setValue(ovol);
					 
				 }
			 }).start();*/
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void playDistress(String filename) {
		///if(is_beep) return;
		//is_beep = true;
		try {
			URL url = AlarmSound.class.getResource(filename);
			File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 double gain = .5D; // number between 0 and 1 (loudest)
			 float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			 gainControl.setValue(default_dp);
			// float ovol  = gainControl.getValue();
			// gainControl.setValue(10.0f);
			 clip.start();
			 
			 
			 new Thread(new Runnable() {
				 public void run() {
					 try {
						 Thread.sleep(1000*2);
					 }catch(Exception e) {
						 
					 }
					 clip.stop();
					 is_beep = false;
					// gainControl.setValue(ovol);
					 
				 }
			 }).start();
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
>>>>>>> refs/remotes/origin/dell-b001
