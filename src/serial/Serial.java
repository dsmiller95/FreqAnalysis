package serial;

/**
 * Placeolder class to allow for interaction with serial
 * @author millerds
 *
 */
public class Serial {
	
	int baudRate;
	
	public Serial(int rate) {
		baudRate = rate;
	}
	
	/**
	 * reads the next value off serial
	 * @return
	 */
	public byte read(){
		return 0;
	}
	
	/**
	 * writes b to serial
	 * @param b
	 */
	public void write(byte b){
		return;
	}
}
