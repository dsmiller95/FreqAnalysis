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
	

	private final int bufferWidth = 40000;
	//private final float conversionIGuess = 3035.518f;

	Thread running;
	Minim minim;
	Visualization vis;
	AudioInput in;
	//int[] avgData;
	float[] levels;
	float[] waveForm;

	int bandFound;

	public AudioAnalizer() {
		minim = new Minim(new MinimInit());
		in = minim.getLineIn(Minim.STEREO, bufferWidth);
		levels = new float[0];
	}
	
	public void setVisualization(Visualization vis){
		this.vis = vis;
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
	 * @return the length of the data sample collected. will probably be less than
	 * 	store.length if first-pass is enabled
	 */
	public int getSamples(int[] store, int centerGoal, int tolerance, boolean firstPassFilter) {
		levels = new float[store.length];
		long t = System.currentTimeMillis();
		int dat;
		int index = 0;
		for (int i = 0; i < store.length; i++) {
			dat = makeFouriest(in.left.toArray());
			if(firstPassFilter && (dat + tolerance < centerGoal || dat - tolerance > centerGoal)){
				//i--;
			}else{
				store[index] = dat;
				levels[index] = in.left.level();
				index++;
			}
			while(t + 20 > System.currentTimeMillis());
			t = System.currentTimeMillis();
		}

		return index;
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
		if(vis != null){
			vis.drawData(data, data.length);
			vis.stroke(0, 255, 0);
			vis.line(maxIndex, 0, maxIndex, vis.height);
			vis.stroke(0);
		}
		return maxIndex;
	}

}
