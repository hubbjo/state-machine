package com.db.sm;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class StateMachineContextOld {

    final static Comparator<Event> c = new Comparator<Event>() {

	    @Override
	    public int compare(Event event1, Event event2) {
		if (event1 == Event.Error && event2 == Event.Error) {
		    return 0;
		} else if (event1 == Event.Error) {
		    return -1;
		} else if (event2 == Event.Error) {
		    return 1;
		} else {
		    return 0;
		} 
	    }
	};

    final BlockingQueue<Event> eventQueue = new PriorityBlockingQueue<Event>(10, c); 
    StateMachine currentState = StateMachine.State1;
    
    void addEvent(Event event) {
	eventQueue.add(event);
    }
    
    Event getNextEvent() {
	while (eventQueue.size() == 0) {
	    try {
		Thread.sleep(1000);
	    }
	    catch(Exception e) {}
	}
	return eventQueue.remove();
    }
    
    StateMachine getCurrentState() {
	return currentState;
    }
    
    void setStateMachine(StateMachine sm) {
	this.currentState = sm;
    }
}
