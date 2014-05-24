package main;

import java.util.Scanner;

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
	static int centerThreshold = 410;
	
	public static class ArduinoListener implements Listener<SerialEvent>{
		public void Activate(SerialEvent element) {
			inter.continueMoving(findObject());
		}
	}
	
	public static void main(String[] args){
		try {
			analizer = new AudioAnalizer();
			inter = new ArduinoInterface(new ArduinoListener());
			centerThreshold = findThreshold();
			inter.start();
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
		int samples = 5;
		int[] tmp = new int[sampleSize];
		long lastPluck = 0;
		int copyIndex = 0;
		
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
			sampleSize = analizer.getSamples(tmp, centerThreshold, 50, false);
			common = getMostCommon(tmp, sampleSize);
			print("Most common: " + common + " : " + getAvg(tmp));
			if(common + 50 < centerThreshold || common - 50 > centerThreshold){
				//throw out the data
				i--;
				print("Bad data: " + common);
			}else{
				System.arraycopy(tmp, 0, allData, copyIndex, sampleSize);
				copyIndex += sampleSize;
			}
		}
		common = getMostCommon(allData, copyIndex - sampleSize);
		print("Final Most Common: " + common + " : " + getAvg(allData));
		return (common > centerThreshold);
	}
	
	private static int findThreshold(){
		int sampleSize = 50;
		int samples = 5;
		int[] tmp = new int[sampleSize];
		long lastPluck = 0;
		int copyIndex = 0;
		
		int[] allData = new int[sampleSize * samples];
		int common;

		Scanner in = new Scanner(System.in);
		int lower, higher;
		print("Press enter to confirm lower limit callibration");
		in.next();
		//lower limit
		while(true){
			copyIndex = 0;
			sampleSize = 50;
			allData = new int[sampleSize * samples];
			for(int i = 0; i < samples; i++){
				double level = analizer.getLevel(true);
				if(level < levelThreshold){
					if(lastPluck + 500 < System.currentTimeMillis()){
						print("plucking");
						inter.pluckString();
						lastPluck = System.currentTimeMillis();
					}
				}
				sampleSize = analizer.getSamples(tmp, centerThreshold, 50, false);
				common = getMostCommon(tmp, sampleSize);
				print("Most common: " + common + " : " + getAvg(tmp));
				System.arraycopy(tmp, 0, allData, copyIndex, sampleSize);
				copyIndex += sampleSize;
			}
			common = getMostCommon(allData, copyIndex - sampleSize);
			print("Final Most Common: " + common + " : " + getAvg(allData));
			lower = common;
			
			System.out.println("Retry calibration? (y/n)");
			char res = in.next().toLowerCase().charAt(0);
			if(res == 'n'){
				break;
			}
		}
		
		//higher limit
		while(true){
			copyIndex = 0;
			sampleSize = 50;
			allData = new int[sampleSize * samples];
			for(int i = 0; i < samples; i++){
				double level = analizer.getLevel(true);
				if(level < levelThreshold){
					if(lastPluck + 500 < System.currentTimeMillis()){
						inter.pluckString();
						lastPluck = System.currentTimeMillis();
					}
				}
				sampleSize = analizer.getSamples(tmp, centerThreshold, 50, false);
				common = getMostCommon(tmp, sampleSize);
				print("Most common: " + common + " : " + getAvg(tmp));
				System.arraycopy(tmp, 0, allData, copyIndex, sampleSize);
				copyIndex += sampleSize;
			}
			common = getMostCommon(allData, copyIndex - sampleSize);
			print("Final Most Common: " + common + " : " + getAvg(allData));
			higher = common;
			
			System.out.println("Retry calibration? (y/n)");
			char res = in.next().toLowerCase().charAt(0);
			if(res == 'n'){
				break;
			}
		}
		in.close();
		
		inter.continueMoving(false);
		
		int thres = (lower + higher)/2;
		print("Threshold: " + thres);
		return thres;
	}
	
	public static void print(String s){
		if(debug)
			System.out.println(s);
	}
	
	private static int getMostCommon(int[] evaluation, int length){
		//map acts as a frequency map
		int[] map = new int[10000];//much bigger than needed; fuck it
		
		for(int i = 0; i < length; i++){
			map[evaluation[i]]++;
		}
		
		int maxVal = map[0] + map[1], maxIndex = (map[0] > map[1]) ? 0 : 1;
		for(int i = 1; i < map.length - 1; i++){
			if(map[i] + map[i + 1]> maxVal){
				maxVal = map[i] + map[i + 1];
				maxIndex = (map[i] > map[i + 1]) ? i : (i + 1);
			}
		}
		return maxIndex;
	}
	
	private static double getAvg(int[] dat) {
		double sum = 0;
		for (int i = 0; i < dat.length; i++) {
			sum += dat[i];
		}
		return sum / dat.length;
	}
}
