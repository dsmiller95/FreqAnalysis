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
	private long lastPlucked = 0, lastPiston = 0;
	private boolean pistonFire = true;
	
	public ArduinoStub(Listener<SerialEvent> alert) {
		this.alert = alert;
		StubHolder hold = new StubHolder(this);
	}
	
	public void setup(){
		size(500, 500);
		background(255);
		textSize(25);
		noStroke();
	}
	
	public void draw(){
		background(255);
		float tmp;
		if(lastPlucked + 1000 > millis()){
			tmp = (float) (255 * ((millis() - lastPlucked) / 1000.0));
			fill(tmp, tmp, 255);
			rect(50, 50, 103, 40);
			fill(255);
			text("Plucked", 55, 80);
		}
		if(lastPiston + 1000 > millis()){
			tmp = (float) (255 * ((millis() - lastPiston) / 1000.0));
			if(pistonFire){
				fill(255, tmp, tmp);
			}else{
				fill(tmp, 255, tmp);
			}
			rect(200, 50, 120, 40);
			fill(255);
			text("Continue", 205, 80);
		}
		
		stroke(0);
		if(mousePressed && isMouseOnButton()){
			fill(150, 150, 150);
		}else{
			fill(200, 200, 200);
		}
		rect(50, 150, 170, 40, 10);
		fill(0);
		text("Object in pos", 55, 180);
		noStroke();
	}
	
	public void mousePressed(){
		if(isMouseOnButton()){
			main.Main.print("Object in position signal");
			
			//wait for buffer to fill
			for(long t = System.currentTimeMillis(); t + 750 > System.currentTimeMillis(););
			alert.Activate(new SerialEvent(true));
		}
	}
	
	private boolean isMouseOnButton(){
		return (mouseX > 50 && mouseX < 220 && mouseY > 150 && mouseY < 190);
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
