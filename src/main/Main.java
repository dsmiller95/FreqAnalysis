package main;

public class Main {
	
	public static class AudioListener implements Listener<AudioEvent>{
		public void Activate(AudioEvent element) {
			//Activate proper serial events
		}
	}
	
	public static void main(String[] args){
		Listener<AudioEvent> L = new AudioListener();
		AudioMonitor m = new AudioMonitor(L);
		m.start();
	}
}
