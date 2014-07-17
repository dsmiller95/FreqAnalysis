package main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import serial.*;
import ui.Visualization;
import audio.*;

public class Main extends JFrame{
	
	public class ReCalibrate implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			findThreshold();
		}
	}
	
	public class TestString implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(findObject()){
				//success case
			}else{
				//fail case
			}
		}
	}
	
	
	public static final boolean debug = true;
	public static final boolean GUI_DEMO = true;
	
	private static ArduinoInterface inter;
	private static AudioAnalizer analizer;
	private static Main gui;
	
	static final double levelThreshold = 0.003;
	static final int sampleSize = 50;
	
	private JButton calibrate, test;
	private Visualization visualization;
	
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
		Thread running;
		String threadName = "Analizer";
		public void Activate(SerialEvent element) {
			new Thread(this, threadName).start();
		}
		@Override
		public void run() {
			inter.continueMoving(findObject());
		}
	}
	
	public static void main(String[] args){
		try {
			analizer = new AudioAnalizer();
			if(GUI_DEMO){
				gui = new Main();
				inter = new ArduinoStub(new ArduinoListener());
			}else{
				inter = new ArduinoComm(new ArduinoListener());
				centerThreshold = findThreshold();
			}
			inter.init();
		} catch (InstantiationException e) {
			System.out.println("could not open a serial port, aborting");
		}
	}
	
	public Main() {
		this.setTitle("Frequency Scale");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setLayout(new FlowLayout());
		
		JPanel buttons = new JPanel(new GridLayout(0, 1));
		
		calibrate = new JButton("Calibrate");
		calibrate.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		calibrate.addActionListener(new ReCalibrate());
		buttons.add(calibrate);
		
		test = new JButton("Test");
		test.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		test.addActionListener(new TestString());
		buttons.add(test);
		
		visualization = new Visualization();
		visualization.setPreferredSize(new Dimension(500, 500));
		visualization.init();
		
		
		this.add(buttons);
		this.add(visualization);
		
		this.pack();
		this.setSize(1420, 1080);
		this.setVisible(true);
	}
	
	/**
	 * Uses audio analysis to detect an object in the aperature
	 * @return true if object found, false otherwise
	 */
	public static boolean findObject(){
		int samples = 5;
		int[] tmp = new int[sampleSize];
		long lastPluck = 0;
		int copyIndex = 0;
		
		int[] allData = new int[sampleSize * samples];
		int common;
		
		for(int i = 0; i < samples; i++){
			double level = analizer.getLevel(true);
			
			if(gui != null){
				gui.visualization.giveThreshold(centerThreshold);
			}
			
			if(level < levelThreshold){
				if(lastPluck + 500 < System.currentTimeMillis()){
					inter.pluckString();
					lastPluck = System.currentTimeMillis();
				}
			}
			analizer.getSamples(tmp, centerThreshold, 50, gui.visualization);
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
	
	/**
	 * Finds the middle band between the two test values, calibrating
	 * @return Center band
	 */
	private static int findThreshold(){
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
				analizer.getSamples(tmp, centerThreshold, 50, null);
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
			allData = new int[sampleSize * samples];
			for(int i = 0; i < samples; i++){
				double level = analizer.getLevel(true);
				if(level < levelThreshold){
					if(lastPluck + 500 < System.currentTimeMillis()){
						inter.pluckString();
						lastPluck = System.currentTimeMillis();
					}
				}
				analizer.getSamples(tmp, centerThreshold, 50, null);
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
