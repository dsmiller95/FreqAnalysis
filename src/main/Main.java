package main;

public class Main {

	
	public static void main(String[] args){
		Listener L = new AudioListener();
		AudioMonitor m = new AudioMonitor(L);
		m.start();
	}
}
