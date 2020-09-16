package com.db.sm.tl;

public class Main {

    public static void main(final String[] args) throws InterruptedException {
	
	final StateMachineContext<TimedEvent> trafficLightContext = TrafficLightStateMachine.initialise();

	// Generate initial event to kick start the state machine
	trafficLightContext.addEvent(TimedEvent.Timeout);
	Thread.sleep(100000);
    }    
}
