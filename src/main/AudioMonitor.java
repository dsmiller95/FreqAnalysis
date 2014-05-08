package main;


/**
 * Class to listen for certain changes in the audio feed, when the change is
 * detected, it will call the ActionListener's actionPerformed event
 * @author millerds
 *
 */
public class AudioMonitor implements Runnable{
	
	Listener<Boolean> alert;
	Thread running;
	
	public AudioMonitor(Listener<Boolean> alerter) {
		alert = alerter;
	}

	@Override
	public void run() {
		
	}
	
	public void start(){
		running = new Thread(this);
		running.start();
	}
	
	/**
	 * returns the current frequency measured on the audio input
	 */
	private double getFrequency(){
		return 0.0;
	}
	
}
