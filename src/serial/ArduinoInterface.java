package serial;

public class ArduinoInterface implements Runnable{
	//blah
	//messages to receive
	private final byte IN_POSITION = 105;
	//messages to send
	private final byte PLUCK_STRING = -62, FIRE_PISTON = 21, CONTINUE = 113;
	
	Serial serial;
	Listener<SerialEvent> alert;
	
	public ArduinoInterface(Listener<SerialEvent> alert) {
		this.alert = alert;
		serial = new Serial(9600);
	}
	
	/**
	 * Instructs the arduino to pluck the string, to maintain strong enough
	 * amplitude
	 */
	public void pluckString(){
		serial.write(PLUCK_STRING);
	}
	
	/**
	 * Instructs the arduino to continue moving the cloth, indicating if an
	 * object was found or not with objecFound
	 * @param objectFound Whether or not an object was found
	 */
	public void continueMoving(boolean objectFound){
		if(objectFound){
			serial.write(FIRE_PISTON);
		}else{
			serial.write(CONTINUE);
		}
	}
	
	Thread running;
	@Override
	/**
	 * Monitors the serial line, listening for a notification
	 * from the arduino that a box is in position
	 */
	public void run() {
		if(serial.read() == IN_POSITION){
			//initiate box evaluation
			pluckString();
			alert.Activate(new SerialEvent(true));
		}
	}
	
	public void start() {
		running = new Thread(this);
		running.start();
	}
	
}
