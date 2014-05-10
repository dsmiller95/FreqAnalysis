package Audio;

import java.io.InputStream;

import edu.emory.mathcs.jtransforms.fft.*;
import ddf.minim.*;

/**
 * Class to listen for certain changes in the audio feed, when the change is
 * detected, it will call the ActionListener's actionPerformed event
 * 
 * @author millerds
 * 
 */
public class AudioMonitor implements Runnable {

	class MinimInit {
		public String sketchPath(String f) {
			return "";
		}

		public InputStream createInput(String f) {
			return null;
		}
	}

	private final int avgSampleSize = 500, bufferWidth = 2000;

	Listener<AudioEvent> alert;
	Thread running;
	Minim minim;
	AudioInput in;
	double[] avgData = new double[avgSampleSize];
	float[] waveForm;

	int bandFound;

	public AudioMonitor(Listener<AudioEvent> alerter) {
		alert = alerter;
		minim = new Minim(new MinimInit());
		in = minim.getLineIn(Minim.STEREO, bufferWidth);
	}

	@Override
	/**
	 * Monitors the audio feed, calling events on alert object
	 * when necessary. This is the method which will be controlling
	 * the flow of the program
	 */
	public void run() {
		
		
	}

	public void start() {
		running = new Thread(this);
		running.start();
	}

	/**
	 * returns the current frequency according to an average measured on the
	 * audio input. Takes significant amount of time, samples in.left
	 * avgSampleSize times and returns average
	 */
	private double getFrequency() {
		for (int i = 0; i < avgSampleSize; i++) {
			avgData[i] = makeFouriest(in.left.toArray());
			// delay?
		}

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
