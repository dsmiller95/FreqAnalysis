package serial;

public class SerialEvent {
	
	private boolean isAvailable;
	public ArduinoInterface source;
	
	public SerialEvent(ArduinoInterface s, boolean avb) {
		isAvailable = avb;
		source = s;
	}
	
	public boolean isAvailable(){
		return isAvailable;
	}
}
