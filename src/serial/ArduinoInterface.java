package serial;

import jssc.*;

public class ArduinoInterface implements Runnable{
	//messages to receive
	private final byte IN_POSITION = 105;
	//messages to send
	private final byte PLUCK_STRING = -62, FIRE_PISTON = 21, CONTINUE = 113;
	
	private final int baudRate = 9600;
	
	Listener<SerialEvent> alert;
	SerialPort port;
	boolean open = false;
	
	/**
	 * Opens the first available port for serial communications
	 * @param alert The listener to alert when a messae is intercepted
	 * @throws InstantiationException Throws if no serial port is open
	 */
	public ArduinoInterface(Listener<SerialEvent> alert) throws InstantiationException {
		this.alert = alert;
		
		String[] portNames = SerialPortList.getPortNames();
		System.out.println("Found Ports: ");
		for(int i = 0; i < portNames.length; i++){
			System.out.println(portNames[i]);
		}
		
		if(portNames.length > 0){
			port = new SerialPort(portNames[0]);
			
			try {
				port.openPort();
				port.setParams(baudRate, 8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				open = true;
			} catch (SerialPortException e) {}
		}
		
		if(!open){
			throw new InstantiationException();
		}
	}
	
	/**
	 * Instructs the arduino to pluck the string, to maintain strong enough
	 * amplitude
	 */
	public void pluckString(){
		try {
			port.writeByte(PLUCK_STRING);
		} catch (SerialPortException e) {}
	}
	
	/**
	 * Instructs the arduino to continue moving the cloth, indicating if an
	 * object was found or not with objecFound
	 * @param objectFound Whether or not an object was found
	 */
	public void continueMoving(boolean objectFound){
		try{
			if(objectFound){
				port.writeByte(FIRE_PISTON);
			}else{
				port.writeByte(CONTINUE);
			}
		} catch (SerialPortException e) {}
	}
	
	Thread running;
	byte[] data = new byte[1];
	@Override
	/**
	 * Monitors the serial line, listening for a notification
	 * from the arduino that a box is in position
	 */
	public void run() {
		try {
			data = port.readBytes();
		} catch (SerialPortException e) {}
		if(data != null){
			for(int i = 0; i < data.length; i++){
				if(data[i] == IN_POSITION){
					pluckString();
					alert.Activate(new SerialEvent(true));
				}
			}
		}
	}
	
	public void start() {
		running = new Thread(this);
		running.start();
	}
	
}
