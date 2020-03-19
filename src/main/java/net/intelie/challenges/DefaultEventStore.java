package net.intelie.challenges;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import java.util.Comparator;


/**
 * @author Joao Pedro Coutinho
 *
 */
public class DefaultEventStore implements EventStore{
	
	/**
	 * As we need a fast data structure to store key-value items, a hash table is one of the first as it provides expected constant-time performance O(1)
	 * for most operations like add(), remove() and contains().
	 *
	 * We also know that our items will be searched by it's time stamp, so we need to keep the items sorted. In this case we
	 * can use a skip list, since it provides average performance O(log n) for most operations.
	 * 
	 * Java provides two concurrent structures that attends our needs: ConcurrentHashMap, a thread-safe version of hash table and 
	 * ConcurrentSkipListSet, a thread-safe version of skip list.
	 * 
	 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html">ConcurrentHashMap</a>
	 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentSkipListSet.html">ConcurrentSkipListSet</a>
	 */
	
	ConcurrentHashMap<String, ConcurrentSkipListSet<Event>> events = new ConcurrentHashMap<>();
	Comparator<Event> byTimestamp = Comparator.comparing(Event::timestamp);
	
	@Override
	public void insert(Event event) {
		ConcurrentSkipListSet<Event> listType;
		String type = event.type();
		
		if (exists(type)) { //Tests if the specified object is a key in this table.
			listType = events.get(type);
			listType.add(event);
		}
		else {
			listType = new ConcurrentSkipListSet<Event>(byTimestamp);
			listType.add(event);
			
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
		
		ConcurrentSkipListSet<Event> listQuery = (ConcurrentSkipListSet<Event>) events.get(type).subSet(
					new Event(type, startTime), new Event(type, endTime));
	
		return new DefaultEventIterator(listQuery);	
	}

	private boolean exists(String type) {
		if (events.containsKey(type)) {
			return true;
		}
		
		return false;
	}
}
