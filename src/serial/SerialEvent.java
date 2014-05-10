package serial;

public class SerialEvent {
	
	private boolean isAvailable;
	
	public SerialEvent(boolean avb) {
		isAvailable = avb;
	}
	
	public boolean isAvailable(){
		return isAvailable;
	}
}
