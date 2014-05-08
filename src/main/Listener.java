package main;
/**
 * Listenes for an event, 
 * @author millerds
 *
 * @param <E>
 */
public interface Listener<E> {
	public void Activate(E element);
}
