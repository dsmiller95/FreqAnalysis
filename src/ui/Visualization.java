package ui;
import processing.core.*;

/**
 * Used to show the user/demo what the computer sees
 * @author millerds
 * 
 * TODO:
 * copy existing Fourier visualization
 * Aggregate into Main
 * Modify necessary methods in AudioAnalizer to accept this as an argument, passing needed data in
 */
public class Visualization extends PApplet{

	public Visualization() {
		// TODO Auto-generated constructor stub
	}
	
	public void setup(){
		size(1270, 1030);
		background(255);
	}
	
	
	int color = 0;
	public void draw(){
		background(color);
		color = (color + 1) % 255;
	}
	
	public void giveLevel(float level){
		
	}
	public void giveThreshold(int thresh){
		
	}
	public void giveWaveForm(float[] form){
		
	}
	public void giveFouriest(float[] fouriest){
		
	}
	
	public void drawData(float[] dat, int len) {
	  PVector lastPoint = new PVector(0, height/2);
	  for (int i = 0; i < len; i++) {
	    line(lastPoint.x, lastPoint.y, i, height/2 - dat[i] * 100);
	    //line(i, height/2, i, height/2 - dat[i] * 40);
	    lastPoint.set(i, height/2 - dat[i] * 100);
	  }
	}
}
