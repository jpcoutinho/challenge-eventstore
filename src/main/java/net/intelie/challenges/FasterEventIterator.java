
package net.intelie.challenges;

import java.util.Iterator;

/**
 * @author Joao Pedro Coutinho
 *
 */
public class FasterEventIterator implements EventIterator {

	private Iterator<Long> iterator;
	private final String type;
	
	private Long current 	= null;
	private boolean control = false;
	
	
	public FasterEventIterator(String type, Iterator<Long> iterator) {
		this.iterator = iterator;
		this.type = type;
	}

	@Override
	public boolean moveNext() {
		
		control = true;
		
		if (iterator.hasNext()) {
			
			current = iterator.next();
			
			return true;
		}
		
		control = false;
		
		return false;
	}

	@Override
	public Event current() throws IllegalStateException {
		
		if (isIllegal()) {
			
			throw new IllegalStateException();
		}
	
		return new Event(type, current.longValue());
	}
		
	@Override
	public void remove() throws IllegalStateException {
		
		if (isIllegal()) {
			
			throw new IllegalStateException();
		}
		
		iterator.remove();
	}
	
	@Override
	public void close() throws Exception {
		iterator = null;
	}
	
	private boolean isIllegal() {
		if (current == null || control == false) {		
			return true;
		}
		
		return false;
	}
}
