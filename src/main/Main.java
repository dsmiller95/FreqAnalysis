package main;

import java.util.ArrayList;
import java.util.Collections;

import serial.*;
import audio.*;

public class Main {
	public static final boolean debug = true;
	
	
	static ArduinoInterface inter;
	static AudioAnalizer analizer;
	
	static final double levelThreshold = 0.003;
	
	/**
	 * The frequency at which a container with an object will be higher than
	 * and a container without an object will be lower than
	 */
	static double centerThreshold = 365;
	
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
			centerThreshold = analizer.convertFrequencyToBand(365);
		} catch (InstantiationException e) {
			System.out.println("could not open a serial port, aborting");
		}
	}
	
	/**
	 * Uses audio analysis to detect an object in the aperature
	 * @return true if object found, false otherwise
	 */
	public static boolean findObject(){
		int sampleSize = 100;
		int samples = 40;
		double avg, finalAvg = 0;
		int[] tmp;
		long lastPluck = 0;
		
		int[] allData = new int[sampleSize * samples];
		int common;
		
		for(int i = 0; i < samples; i++){
			double level = analizer.getLevel(true);
			if(level < levelThreshold){
				if(lastPluck + 500 < System.currentTimeMillis()){
					inter.pluckString();
					lastPluck = System.currentTimeMillis();
				}
			}
			tmp = analizer.getSamples(sampleSize);
			common = getMostCommon(tmp);
			print("Most common: " + analizer.convertBandToFrequency(common));
			System.arraycopy(tmp, 0, allData, i * sampleSize, sampleSize);
		}
		common = getMostCommon(allData);
		print("Final Most Common: " + analizer.convertBandToFrequency(common));
		return (common > centerThreshold);
		
		/*
		avg = 0;
		for(int i = 0; i < samples; i++){
			double level = analizer.getLevel(true);
			if(level < levelThreshold){
				if(lastPluck + 500 < System.currentTimeMillis()){
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
		}*/
	}
	
	public static void print(String s){
		if(debug)
			System.out.println(s);
	}
	
	private static int getMostCommon(int[] evaluation){
		ArrayList<Integer> data = new ArrayList<Integer>();
		for(int i : evaluation){
			data.add(i);
		}
		Collections.sort(data);
		
		int current = -1, len = 0;
		int maxLen = 0, maxVal = 0;
		
		for(Integer i : data){
			if(i + 1 >= current && i - 1 <= current){//small tolerance
				len++;
			}else{
				if(len > maxLen){
					maxLen = len;
					maxVal = current;
				}
				current = i;
				len = 1;
			}
		}
		
		return maxVal;
	}
}
