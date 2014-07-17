package serial;
/**
 * Listenes for an event, 
 * @author millerds
 *
 * @param <E>
 */
public interface Listener<E> extends Runnable{
	public void Activate(E element);
}
