package main;

import serial.*;
import audio.*;

public class Main {
	public static final boolean debug = true;
	
	
	static ArduinoInterface inter;
	static AudioAnalizer analizer;
	
	static final double levelThreshold = 0.005;
	
	/**
	 * The frequency at which a container with an object will be higher than
	 * and a container without an object will be lower than
	 */
	static final double centerThreshold = 330;
	
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
		int samples = 40;
		double avg, finalAvg = 0;
		double tmp;
		long lastPluck = 0;
		
		for(int j = 0; j < 5; j++){
			avg = 0;
			for(int i = 0; i < samples; i++){
				double level = analizer.getLevel(true);
				if(level < levelThreshold){
					if(lastPluck + 100 < System.currentTimeMillis()){
						inter.pluckString();
						lastPluck = System.currentTimeMillis();
					}
				}
				tmp = analizer.getFrequency(avgSize);
				avg += tmp;
				//print("Frequency: " + Double.toString(analizer.convertBandToFrequency(tmp)));
			}
			avg /= samples;
			avg = analizer.convertBandToFrequency(avg);
			print("Avg: " + avg + " Object? " + (avg > centerThreshold));
			if(avg > centerThreshold + 50 || avg < centerThreshold - 50){
				//throw out the data
				j--;
				print("Rejected Data");
			}else{
				finalAvg += (avg > centerThreshold) ? 1 : 0;
			}
		}
		return ((finalAvg/5.0) >= 0.5);
	}
	
	public static void print(String s){
		if(debug)
			System.out.println(s);
	}
}
