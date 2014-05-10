package main;

import serial.*;

public class Main {
	
	public static class ArduinoListener implements Listener<SerialEvent>{
		public void Activate(SerialEvent element) {
			//Activate proper serial events
		}
	}
	
	public static void main(String[] args){
		ArduinoInterface m = new ArduinoInterface(new ArduinoListener());
		m.start();
	}
}
