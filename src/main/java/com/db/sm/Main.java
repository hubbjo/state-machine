package com.db.sm;

import java.util.Random;

import com.db.sm.tl.StateMachineContext;

public class Main {

    public static void main(String[] args) {
	
	final StateMachineContext<Event> smc = StateMachine.initialise();
	final Random random = new Random(System.currentTimeMillis());
	for (int i = 0; i < 10; i++) {
	    final double rand = random.nextDouble();
	    System.out.println(rand);
	    final Event event =  rand < 0.5 ? Event.Event1 : rand < 0.9 ? Event.Event2 : Event.Error;
	    smc.addEvent(event);
	    try {
			Thread.sleep(1000);
	    }
	    catch (Exception e) {
	    }
	}
    }
}