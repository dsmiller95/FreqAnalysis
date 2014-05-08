package main;

/**
 * Holds all data needed to be passed as an alert to the listener
 * Needed alert cases:
 *  - audio intensity fading
 *  - audio frequency changing sufficiently
 * @author millerds
 *
 */
public class AudioEvent {
	
	
	public AudioEvent() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return true if this event was called due to a fading intensity
	 */
	public boolean isIntensityFade(){
		return false;
	}
	/**
	 * @return true if this event was called due to a frequency change
	 */
	public boolean isFrequencyChange(){
		return false;
	}
	/**
	 * @return The magnitude and direction of change in frequency
	 */
	public double getFrequencyChange(){
		return 0.0;
	}
}
