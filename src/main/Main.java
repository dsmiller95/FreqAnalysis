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
	 * The band at which a container with an object will be higher than
	 * and a container without an object will be lower than
	 * 
	 * This number WILL CHANGE if the sample size of the Audio analyzer changes
	 * It appears that it vary directly, meaning if sample size is doubled then
	 * band will double
	 */
	static int centerThreshold = 112;
	
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
		int sampleSize = 50;
		int samples = 10;
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
			print("Most common: " + common + " : " + getAvg(tmp));
			System.arraycopy(tmp, 0, allData, i * sampleSize, sampleSize);
		}
		common = getMostCommon(allData);
		print("Final Most Common: " + common + " : " + getAvg(allData));
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
		ArrayList<Integer> bukkits = new ArrayList<Integer>();
		//map acts as a frequency map
		int[] map = new int[500];//much bigger than needed; fuck it
		
		for(int i : evaluation){
			map[i/2]++;
		}
		
		int maxVal = map[0], maxIndex = 0;
		for(int i = 1; i < map.length; i++){
			if(map[i] > maxVal){
				maxVal = map[i];
				maxIndex = i;
			}
		}
		return maxIndex*2;
	}
	
	private static double getAvg(int[] dat) {
		double sum = 0;
		for (int i = 0; i < dat.length; i++) {
			sum += dat[i];
		}
		return sum / dat.length;
	}
}
