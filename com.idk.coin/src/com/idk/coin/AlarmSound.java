package com.idk.coin;

import java.io.File;
import java.net.URL;
import java.util.Hashtable;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AlarmSound {
	public static Hashtable<String,Clip>   table;
	
	public static void main(String[] args) {
		/**
		Method[] methods = AlarmSound.class.getMethods();
		for(;;) {
			for(Method m : methods) {
				String name = m.getName();
				if(name.startsWith("beep") || name.startsWith("play") || name.startsWith("alarm")) {
					System.out.println(name);
					try {
						 m.invoke(AlarmSound.class, null);
						 Thread.sleep(1000* 5);
						}catch(Exception e) {
							
						}
					}
				}
			try {
				 Thread.sleep(1000* 10);
				}catch(Exception e) {
					
				}
		}
		*/
			
		//AlarmSound.playSound("piano.wav");
		//AlarmSound.playSound("wolf.wav");
		for(;;) {
			try {
				Thread.sleep(1000*3);
				AlarmSound.maxVolAlarm();
				Thread.sleep(1000*3);
				AlarmSound.playSound();
				Thread.sleep(1000*5);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static AlarmSound instance;
	
	public AlarmSound() {
		table = new Hashtable<String,Clip>();
	}
	public static AlarmSound getInstance() {
		if(instance == null) {
			instance = new AlarmSound();
		}
		return instance;
	}
	public static void minMaxVolBeep() {
		 playBeep("beep01.wav");	
	}
	public static void minMaxVolBeep2() {
		//순간 거래량 많은경우 2
		 playBeep("beep02.wav");
	}
	public static void minMaxVolBeep4() {
		//순간 거래량 많은경우 2
				playBeep("beep04.wav");
	}
	public static void beep051() {
		 playBeep("beep05.wav");
	}
	public static void maxVolAlarm() {
		//전체 거래량 많은경우
				playAlarm("Alarm02.wav");
	}
	//public static void alarm01() {
		//전체 거래량 많은경우
	//	playAlarm("Alarm02.wav");
	//}
	public static void playSound() {
		//전체 거래량 많은경우
		playSound("Alarm02.wav");
	}
	public static void orderSound() {
		playDistress("button09.wav");
	}
	public static void playAlerts() {
		//주문전송
		playDistress("button09.wav");
	}
	public static void playDistress() {
		playDistress("distress.wav");
	}
	public static void playReset() {
		playDistress("baby.wav");
	}
	
	public void addClip(String key, Clip clip) {
		table.put(key, clip);
	}
	public boolean containsKey(String key) {
		return table.containsKey(key);
	}
	public Clip getClip(String key) {
		if(!table.containsKey(key)) return null;
		//System.out.println("found : " +key);
		return table.get(key);
	}
	public static boolean is_alarm = false;
	 //max 6.0206 ~ -80.0
	public static boolean is_beep  = false;
	public static float   default_dp = -15;
	
	public static void loadAudioFile(String filename) {
		try {
			
			URL url = AlarmSound.class.getResource(filename);
			File file = new File(url.getPath());
			Clip clip =  AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			 getInstance().addClip(filename, clip);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void playAlarm(String filename) {
		if(is_alarm) return ;
		is_alarm = true;
		
		if(!getInstance(). containsKey(filename)) {
			loadAudioFile(filename);
			
			try {
				
				URL url = AlarmSound.class.getResource(filename);
				File file = new File(url.getPath());
				Clip clip =  AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(file));
				
				 //FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				 //double gain = .5D; // number between 0 and 1 (loudest)
				// float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
				// gainControl.setValue(default_dp + 5 );
				 
				 
				 getInstance().addClip(filename, clip);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	
		 new Thread(new Runnable() {
			 public void run() {
				 Clip clip = getInstance().getClip(filename);
				// System.out.println(clip.isActive() + " , " +clip.available() + "," + clip.isOpen()+"," + clip.isRunning() + ", " +  clip.getFramePosition());
				 try {
				 clip.setFramePosition(0);
				 clip.start();	 
				 try {
					 Thread.sleep(1000*3); 
				 }catch(Exception e) {
					 
				 }
				 clip.stop();
				// gainControl.setValue(ovol);
				 }catch(Exception e) {
					 e.printStackTrace();
				 }finally {
					 is_alarm = false;
				 }
				 
			 }
		 }).start();
		
	}
	public synchronized static void playBeep(String filename) {
		if(is_beep) return;
		is_beep = true;
		try {
			if(!getInstance(). containsKey(filename)) {
				loadAudioFile(filename);
			}
			/*
			URL url = AlarmSound.class.getResource(filename);
			 File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 double gain = 3; // number between 0 and 1 (loudest)
			 float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			
			 gainControl.setValue(default_dp);
	 
			 clip.start();*/
			 
			 
			 new Thread(new Runnable() {
				 public void run() {
					 Clip clip = getInstance().getClip(filename);
					 clip.setFramePosition(0);
					 clip.start();
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
			
			/*URL url = AlarmSound.class.getResource(filename);
			File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 gainControl.setValue(default_dp);
			 clip.start();*/
			 
			 if(!getInstance(). containsKey(filename))  loadAudioFile(filename);
					
			 
			 Clip clip = getInstance().getClip(filename);
			 clip.setFramePosition(0);
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
			/*URL url = AlarmSound.class.getResource(filename);
			File file = new File(url.getPath());
			final Clip clip =  AudioSystem.getClip();
			 
			 clip.open(AudioSystem.getAudioInputStream(file));
			 FloatControl gainControl =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			 double gain = .5D; // number between 0 and 1 (loudest)
			 float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			 gainControl.setValue(default_dp);
			 clip.start();*/
			 if(!getInstance(). containsKey(filename))  loadAudioFile(filename);
			 
			 new Thread(new Runnable() {
				 public void run() {
					 Clip clip = getInstance().getClip(filename);
					 clip.setFramePosition(0);
					 clip.start();
					 try {
						 Thread.sleep(1000*2);
					 }catch(Exception e) {
						 
					 }
					 clip.stop();
					 is_beep = false;
					 
				 }
			 }).start();
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
