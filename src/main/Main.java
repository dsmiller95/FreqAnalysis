package main;

import Audio.*;
import serial.*;

public class Main {
	
	static ArduinoInterface inter;
	static AudioMonitor audio;
	
	public static class ArduinoListener implements Listener<SerialEvent>{
		public void Activate(SerialEvent e) {
			//Activate proper serial events
			
		}
	}
	
	public static void main(String[] args){
		inter = new ArduinoInterface(new ArduinoListener());
		audio = new AudioMonitor();
		inter.start();
	}
}
