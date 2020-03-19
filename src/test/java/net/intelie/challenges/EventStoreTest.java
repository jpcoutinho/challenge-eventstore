package net.intelie.challenges;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author Joao Pedro Coutinho
 *
 */
public class EventStoreTest {
	
	public static DefaultEventStore 			eventsTest;
	public static DefaultEventIterator 			iteratorTest;
	public static Event 						dummyEvent;
	public static ConcurrentSkipListSet<Event> 	eventList;
	
		
	@Test
	public void testInsert() {
		eventsTest = new DefaultEventStore();
		dummyEvent = new Event("typeA", 12L);
		Event dummyEvent2 = new Event("typeA", 6L);
		
		assertFalse(eventsTest.events.containsKey("typeA"));
		
		eventsTest.insert(dummyEvent);
		assertTrue(eventsTest.events.containsKey("typeA"));
		
		//Divide in more tests
		eventList = eventsTest.events.get("typeA");
		assertTrue(eventList.contains(dummyEvent));
		
		assertFalse(eventList.contains(dummyEvent2));
		eventsTest.insert(dummyEvent2);
		assertTrue(eventList.contains(dummyEvent2));
		
		eventsTest.insert(dummyEvent2);
		assertEquals(2, eventList.size());
	}
	
	
	@Test
	public void testRemoveAll() {
		eventsTest = new DefaultEventStore();
		setupEventsTest();
		
		eventsTest.removeAll("typeA");
		assertFalse(eventsTest.events.containsKey("typeA"));
		
		eventsTest.removeAll("typeD");
		assertFalse(eventsTest.events.containsKey("typeA"));
	}
	
	@Test
	public void testQuery() {
		eventsTest = new DefaultEventStore();
		setupEventsTest();
		
		
		iteratorTest = (DefaultEventIterator) eventsTest.query("typeA", 5L, 15L);
	}
	
	
	@Test
	public void testMoveNext() {
		eventsTest = new DefaultEventStore();
		setupEventsTest();
		
		
		iteratorTest = (DefaultEventIterator) eventsTest.query("typeA", 5L, 15L);
		
		assertTrue(iteratorTest.moveNext());
		iteratorTest.moveNext();
		
		assertFalse(iteratorTest.moveNext());
	}
	
	@Test
	public void testCurrent() {
		eventsTest = new DefaultEventStore();
		setupEventsTest();
		
		
		iteratorTest = (DefaultEventIterator) eventsTest.query("typeA", 5L, 15L);
		
		iteratorTest.moveNext();
		iteratorTest.moveNext();
		
		assertTrue(isEventEqual(new Event("typeA", 12L), iteratorTest.current()));
	}
	
	@Test (expected = IllegalStateException.class)
	public void testCurrentException() {
		eventsTest = new DefaultEventStore();
		setupEventsTest();
		
		iteratorTest = (DefaultEventIterator) eventsTest.query("typeA", 5L, 15L);
		
		iteratorTest.current();
		
		//Divide in two tests
		iteratorTest.moveNext();
		iteratorTest.moveNext();
		iteratorTest.moveNext();
		iteratorTest.current();
		
	}
	
	@Test
	public void testRemove() {
		eventsTest = new DefaultEventStore();
		setupEventsTest();
		
		
		iteratorTest = (DefaultEventIterator) eventsTest.query("typeA", 5L, 15L);
		
		iteratorTest.moveNext();
		iteratorTest.moveNext();
		
		iteratorTest.remove();
		
		eventList = eventsTest.events.get("typeA");
		assertFalse(eventList.contains(new Event("typeA", 11L)));
	}
	
	@Test (expected = IllegalStateException.class)
	public void testRemoveException() {
		eventsTest = new DefaultEventStore();
		setupEventsTest();
		
		iteratorTest = (DefaultEventIterator) eventsTest.query("typeA", 5L, 15L);
		
		iteratorTest.remove();
		
		//Divide in two tests
		iteratorTest.moveNext();
		iteratorTest.moveNext();
		iteratorTest.moveNext();
		iteratorTest.remove();
	}
	
	
	private boolean isEventEqual(Event a, Event b) {
		
		if ((a.type() == a.type()) && (a.timestamp() == b.timestamp())) {
			return true;
		}
		
		return false;	
	}
	
	
	private void setupEventsTest() {
		eventsTest.insert(new Event("typeA", 12L));
		eventsTest.insert(new Event("typeA", 6L));
		eventsTest.insert(new Event("typeB", 25L));
		eventsTest.insert(new Event("typeC", 10L));
		eventsTest.insert(new Event("typeC", 5L));
		eventsTest.insert(new Event("typeC", 36L));
	}
	
}
