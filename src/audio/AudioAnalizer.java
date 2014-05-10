package audio;


import java.io.InputStream;

//import serial.Listener;
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
	
	/**
	 * Empty functioned class to initiate Minim with
	 * @author millerds
	 */
	class MinimInit {
		public String sketchPath(String f) {
			return "";
		}
		public InputStream createInput(String f) {
			return null;
		}
	}

	private final int bufferWidth = 2000;

	Thread running;
	Minim minim;
	AudioInput in;
	double[] avgData;
	float[] levels;
	float[] waveForm;

	int bandFound;

	public AudioAnalizer() {
		minim = new Minim(new MinimInit());
		in = minim.getLineIn(Minim.STEREO, bufferWidth);
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
	public double getFrequency(int avgLength) {
		avgData = new double[avgLength];
		levels = new float[avgLength];
		long t = System.currentTimeMillis();
		for (int i = 0; i < avgLength; i++) {
			avgData[i] = makeFouriest(in.left.toArray());
			levels[i] = in.left.level();
			// delay?
		}
		main.Main.print("Time taken: " + Long.toString(System.currentTimeMillis() - t));

		return getAvg(avgData);
	}
	
	private double makeFouriest(float[] data) {
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
