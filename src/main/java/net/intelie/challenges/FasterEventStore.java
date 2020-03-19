package net.intelie.challenges;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author Joao Pedro Coutinho
 *
 */
public class FasterEventStore implements EventStore{
	
	/**
	 * A faster implementation for EventStore. Instead of store a skip list of Event (type-timestamp) in the hash table,
	 * we just store a skip list of Long (timestamps).
	 * 
	 * Thus we can have a smaller memory imprint and a slightly better performance as we don't need a custom comparator.
	 *
	 */

	ConcurrentHashMap<String, ConcurrentSkipListSet<Long>> events = new ConcurrentHashMap<>();
	
	@Override
	public void insert(Event event) {
		
		ConcurrentSkipListSet<Long> listType;
		String type = event.type();
		Long timestamp = new Long (event.timestamp());
		
		if (exists(type)) { //Tests if the specified object is a key in this table.
			listType = events.get(type);
			listType.add(timestamp);
		}
		else {
			listType = new ConcurrentSkipListSet<>();
			listType.add(timestamp);
			
			events.put(type, listType);
		}
	}

	@Override
	public void removeAll(String type) {	
		if (!exists(type)) {
			return;		
		}
		
		events.get(type).clear();
		events.remove(type);
	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		Iterator<Long> iterator = events.get(type).subSet(startTime, endTime).iterator();
		
		return new FasterEventIterator(type, iterator);	
	}

	private boolean exists(String type) {
		if (events.containsKey(type)) {
			return true;
		}
		
		return false;
	}
}
