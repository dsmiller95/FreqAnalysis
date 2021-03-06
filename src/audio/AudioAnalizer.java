package audio;



import ui.Visualization;
import edu.emory.mathcs.jtransforms.fft.*;
import ddf.minim.*;

/**
 * Class to listen for certain changes in the audio feed, when the change is
 * detected, it will call the ActionListener's actionPerformed event
 * 
 * @author millerds
 * 
 */
public class AudioAnalizer {
	

	public static final int BUFFER_WIDTH = 4000;
	//private final float conversionIGuess = 3035.518f;

	Thread running;
	Minim minim;
	AudioInput in;
	//int[] avgData;
	float[] levels;
	float[] waveForm;

	int bandFound;

	public AudioAnalizer() {
		minim = new Minim(new MinimInit());
		in = minim.getLineIn(Minim.STEREO, BUFFER_WIDTH);
		levels = new float[0];
	}
	
	/**
	 * Returns the level of audio.
	 * @param whether or not to pull recently collected data for a short average
	 * @return level of audio
	 */
	public double getLevel(boolean useRecentData){
		if(useRecentData && levels.length > 15){
			double sum = 0;
			for(int i = levels.length - 15; i < levels.length; i++){
				sum += levels[i];
			}
			return sum / 15;
		}
		return in.left.level();
	}
	
	/**
	 * returns the current frequency according to an average measured on the
	 * audio input. Takes significant amount of time, samples in.left
	 * avgSampleSize times and returns average
	 * @param store
	 * @return the length of the data sample collected.
	 */
	public void getSamples(int[] store, Visualization vis) {
		levels = new float[store.length];
		long t = System.currentTimeMillis();
		int dat;
		for (int i = 0; i < store.length; i++) {
			
			dat = makeFouriest(in.left.toArray(), vis);
			store[i] = dat;
			levels[i] = in.left.level();
			if(vis != null) vis.giveLevel(levels[i]);
			while(t + 20 > System.currentTimeMillis());
			t = System.currentTimeMillis();
		}
	}
	
	private int makeFouriest(float[] data, Visualization vis) {
		if(vis != null) vis.giveWaveForm(data);
		FloatFFT_1D fourier = new FloatFFT_1D(data.length);
		fourier.realForward(data);
		for (int i = 0; i < data.length; i++)
			data[i] = Math.abs(data[i]);
		float max = data[0];
		int maxIndex = 1;
		for (int i = 1; i < data.length; i++) {
			if (data[i] > max) {
				max = data[i];
				maxIndex = i;
			}
		}
		if(vis != null) vis.giveFouriest(data, maxIndex);
		return maxIndex;
	}

}
