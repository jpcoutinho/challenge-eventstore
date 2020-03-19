package net.intelie.challenges;

import java.util.Iterator;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author Joao Pedro Coutinho
 *
 */
public class DefaultEventIterator implements EventIterator {

	private ConcurrentSkipListSet<Event> listQuery;
	private Iterator<Event> iterator;
	
	private Event current = null;
	private boolean control = false;
	
	public DefaultEventIterator(ConcurrentSkipListSet<Event> listQuery) {
		this.listQuery = listQuery;
		iterator = listQuery.iterator();
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
	
		return current;
	}
		
	@Override
	public void remove() throws IllegalStateException {
		
		if (isIllegal()) {
			throw new IllegalStateException();
		}
		
		listQuery.remove(current);
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
