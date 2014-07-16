package serial;


import java.applet.Applet;

import javax.swing.JFrame;
import javax.swing.JLabel;

import processing.core.PApplet;

/**
 * Simulates interaction with the arduino
 * @author millerds
 *
 */
public class ArduinoStub extends PApplet implements ArduinoInterface {
	
	public class StubHolder extends JFrame{
		public StubHolder(Applet stub){
			this.setTitle("Arduino Stub");
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.add(stub);
			this.add(new JLabel("Blagh"));
			this.pack();
			this.setSize(500, 500);
			this.setVisible(true);
		}
	}
	
	private Listener<SerialEvent> alert;
	private long lastPlucked = millis(), lastPiston = 0;
	private boolean pistonFire = false;
	
	public ArduinoStub(Listener<SerialEvent> alert) {
		this.alert = alert;
		main.Main.print("Opening arduino stub");
		StubHolder hold = new StubHolder(this);
	}
	
	public void setup(){
		main.Main.print("setting up");
		size(500, 500);
		background(255);
	}
	
	int clr = 0;
	public void draw(){
		background(clr);
		clr = (clr + 1)%255;
		main.Main.print("Drawing");
		/*if(lastPlucked + 1000 > millis()){
			fill(0, 0, 255 * ((millis() - lastPlucked) / 900));
			rect(50, 50, 100, 50);
			stroke(0);
			text("Plucked", 55, 55);
		}
		if(lastPiston + 1000 > millis()){
			if(pistonFire){
				fill(255 * ((millis() - lastPlucked) / 900), 0, 0);
			}else{
				fill(0, 255 * ((millis() - lastPlucked) / 900), 0);
			}
			rect(200, 50, 100, 50);
			stroke(0);
			text("Piston", 55, 55);
		}*/
	}
	
	@Override
	public void pluckString() {
		lastPlucked = millis();
	}
	@Override
	public void continueMoving(boolean objectFound) {
		lastPiston = millis();
		pistonFire = objectFound;
	}
}
