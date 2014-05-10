package main;

import serial.*;

public class Main {
	
	static ArduinoInterface inter;
	
	
	public static class ArduinoListener implements Listener<SerialEvent>{
		public void Activate(SerialEvent element) {
			//Activate proper serial events
		}
	}
	
	public static void main(String[] args){
		try {
			inter = new ArduinoInterface(new ArduinoListener());
			inter.start();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
