package ui;

import processing.core.*;

/**
 * Used to show the user/demo what the computer sees
 * 
 * @author millerds
 * 
 *         TODO: copy existing Fourier visualization Aggregate into Main Modify
 *         necessary methods in AudioAnalizer to accept this as an argument,
 *         passing needed data in
 */
public class Visualization extends PApplet {

	public Visualization() {
		// TODO Auto-generated constructor stub
	}

	public void setup() {
		size(1270, 1030);
		background(255);
	}

	private float level = 0;
	private int thresh = 0, variance = 0, common = 0;
	private float[] waveForm = new float[1], fouriest = new float[1];
	private float tmp;
	
	private static final int waveFormOffset = 60, fouriestOffset = 150;
	
	public void draw() {
		tmp = width / (float)audio.AudioAnalizer.BUFFER_WIDTH;
		fill(255, 255, 255, 50);
		rect(0, 0, width, height);
		drawData(waveForm, width, height - waveFormOffset, 100);
		drawData(bukkitize(fouriest, width), width, height - fouriestOffset, 100);
		stroke(0, 255, 0);
		line(common * tmp, 0, common * tmp, height);
		stroke(0);

		fill(255, 0, 0);
		rect(width - 50, height - (height * level), width, height);
		stroke(255, 0, 0);
		
		line(thresh * tmp, 0, thresh * tmp, height);
		line((thresh - variance) * tmp, height - fouriestOffset, (thresh + variance) * tmp, height - fouriestOffset);
		line((thresh - variance) * tmp, height - fouriestOffset - 20, (thresh - variance) * tmp, height - fouriestOffset + 20);
		line((thresh + variance) * tmp, height - fouriestOffset - 20, (thresh + variance) * tmp, height - fouriestOffset + 20);
		stroke(0);
	}

	public void giveLevel(float level) {
		this.level = level;
	}

	public void giveThreshold(int thresh, int variance) {
		this.thresh = thresh;
		this.variance = variance;
	}

	public void giveWaveForm(float[] form) {
		waveForm = new float[form.length];
		System.arraycopy(form, 0, waveForm, 0, form.length);
	}

	public void giveFouriest(float[] fouriest, int mostCommon) {
		this.fouriest = new float[fouriest.length];
		System.arraycopy(fouriest, 0, this.fouriest, 0, fouriest.length);
		common = mostCommon;
	}

	public void drawData(float[] dat, int len, int heightOffset, int multiplier) {
		if(len > dat.length) return;
		PVector lastPoint = new PVector(0, heightOffset);
		for (int i = 0; i < len; i++) {
			line(lastPoint.x, lastPoint.y, i, heightOffset - dat[i] * multiplier);
			// line(i, height/2, i, height/2 - dat[i] * 40);
			lastPoint.set(i, heightOffset - dat[i] * multiplier);
		}
	}
	
	/**
	 * Takes an array of length greater than targetLen, and puts the data into
	 * evenly distributed bukkits in a new array of length targetLen
	 * @param in Input data
	 * @param targetLen target array length
	 * @return bukkitized array
	 */
	private float[] bukkitize(float[] in, int targetLen){
		if(targetLen >= in.length) return in;
		float[] res = new float[targetLen];
		float inIndex = 0;
		float bukkitSize = (float)in.length / targetLen;
		float sum;
		int count = 0;
		for(int i = 0; i < targetLen; i++){
			sum = 0;
			count = 0;
			for(int j = (int)inIndex; j < bukkitSize + inIndex && j < in.length; j++){
				sum += in[j];
				count++;
			}
			inIndex += bukkitSize;
			res[i] = sum/count;
		}
		return res;
	}
}
