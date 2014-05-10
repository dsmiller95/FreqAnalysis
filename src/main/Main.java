package main;

import serial.*;
import audio.*;

public class Main {
	public static final boolean debug = true;
	
	
	static ArduinoInterface inter;
	static AudioAnalizer analizer;
	
	static final double levelThreshold = 0.15;
	
	/**
	 * The frequency at which a container with an object will be higher than
	 * and a container without an object will be lower than
	 */
	static final double centerThreshold = 100;
	
	public static class ArduinoListener implements Listener<SerialEvent>{
		public void Activate(SerialEvent element) {
			inter.continueMoving(findObject());
		}
	}
	
	public static void main(String[] args){
		try {
			inter = new ArduinoInterface(new ArduinoListener());
			inter.start();
			analizer = new AudioAnalizer();
		} catch (InstantiationException e) {
			System.out.println("could not open a serial port, aborting");
		}
	}
	
	/**
	 * Uses audio analysis to detect an object in the aperature
	 * @return true if object found, false otherwise
	 */
	public static boolean findObject(){
		int avgSize = 100;
		int samples = 10;
		double avg = 0;
		double tmp;
		
		
		for(int i = 0; i < samples || true; i++){
			if(analizer.getLevel(true) < levelThreshold){
				inter.pluckString();
				try{
					Thread.currentThread().wait(500);
				}catch(Exception e){}
			}
			tmp = analizer.getFrequency(avgSize);
			avg += tmp;
			print("Frequency: " + Double.toString(tmp));
		}
		avg /= samples;
		
		return (avg < centerThreshold);
	}
	
	public static void print(String s){
		if(debug)
			System.out.println(s);
	}
}
