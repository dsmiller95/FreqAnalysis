package serial;

public interface ArduinoInterface extends Runnable{
	public void pluckString();
	public void continueMoving(boolean objectFound);
	public void init();
}
