package Audio;


/**
 * Class to listen for certain changes in the audio feed, when the change is
 * detected, it will call the ActionListener's actionPerformed event
 * @author millerds
 *
 */
public class AudioMonitor implements Runnable{
	
	Listener<AudioEvent> alert;
	Thread running;
	
	public AudioMonitor(Listener<AudioEvent> alerter) {
		alert = alerter;
	} 
	
	@Override
	/**
	 * Monitors the audio feed, calling events on alert object
	 * when necessary. This is the method which will be controlling
	 * the flow of the program
	 */
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
