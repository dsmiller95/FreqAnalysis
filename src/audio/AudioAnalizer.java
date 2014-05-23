package audio;



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
	

	private final int bufferWidth = 1500;
	private final float conversionIGuess = 3035.518f;

	Thread running;
	Minim minim;
	AudioInput in;
	int[] avgData;
	float[] levels;
	float[] waveForm;

	int bandFound;

	public AudioAnalizer() {
		minim = new Minim(new MinimInit());
		in = minim.getLineIn(Minim.STEREO, bufferWidth);
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
	 * @param avgLength The number of elements to take into account when averaging
	 */
	public int[] getSamples(int len) {
		avgData = new int[len];
		levels = new float[len];
		long t = System.currentTimeMillis();
		for (int i = 0; i < len; i++) {
			avgData[i] = makeFouriest(in.left.toArray());
			levels[i] = in.left.level();
			while(t + 50 > System.currentTimeMillis());
			t = System.currentTimeMillis();
		}
		//main.Main.print("Time taken: " + Long.toString(System.currentTimeMillis() - t));

		return avgData;
	}
	
	public double convertFrequencyToBand(double freq){
		return (freq * conversionIGuess / in.sampleRate());
	}
	
	public double convertBandToFrequency(double band){
		return (band * in.sampleRate() / conversionIGuess);
	}
	
	private int makeFouriest(float[] data) {
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
		return maxIndex;
	}

	private double getAvg(double[] dat) {
		double sum = 0;
		for (int i = 0; i < dat.length; i++) {
			sum += dat[i];
		}
		return sum / dat.length;
	}
}
