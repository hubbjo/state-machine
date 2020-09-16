package com.db.sm.tl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StateMachineContext <T> {

    private final BlockingQueue<T> eventQueue = new ArrayBlockingQueue<T>(100); 
    private final ScheduledExecutorService scheduler;

    public StateMachineContext(final CallHandler<T> callHandler) {

        // One thread for reading events
        // Second thread for publishing timed events
        scheduler = Executors.newScheduledThreadPool(2);
        scheduler.submit(() -> {
        	CallHandler<T> localCallHandler = callHandler;
        	while(true) {
        	    try {
                	final T event = eventQueue.take();
                	localCallHandler = localCallHandler.handleEvent(event, StateMachineContext.this );
        	    }
        	    catch (InterruptedException e){}
        	}
            });
    }

    public void addEvent(T event) {
        eventQueue.add(event);
    }
    
    void setTimedEvent(final T event, final int delay) {
    	scheduler.schedule(() -> addEvent(event), delay, TimeUnit.SECONDS);
    }
}
