package ui;
import processing.core.*;

/**
 * Used to show the user/demo what the computer sees
 * @author millerds
 * 
 * TODO:
 * import core processing library
 * copy existing Fourier visualization
 * Aggregate into Main
 * Modify necessary methods in AudioAnalizer to accept this as an argument, passing needed data in
 */
public class Visualization extends PApplet{

	public Visualization() {
		// TODO Auto-generated constructor stub
	}
	
	public void setup(){
		size(1770, 1030);
		background(255);
	}
	
	
	int color = 0;
	public void draw(){
		background(color);
		color = (color + 1) % 255;
	}

}
