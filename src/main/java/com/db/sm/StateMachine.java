package com.db.sm;

import java.util.function.Function;

import com.db.sm.tl.CallHandler;
import com.db.sm.tl.StateMachineContext;
import com.db.sm.tl.TimedEvent;


enum StateMachine  {

    // State	Event 1				            Event 2				            Error
    State1	    (StateMachine::state1Event1, 	StateMachine::state1Event2, 	StateMachine::state1Rollback),
    State2	    (StateMachine::state2Event1, 	StateMachine::state2Event2, 	StateMachine::state2Rollback),
    Finished	(StateMachine::noOp, 		    StateMachine::noOp, 		    StateMachine::noOp);
    
    StateMachine(
    	final Function<StateMachineContext<Event>, StateMachine> handler1, 
    	final Function<StateMachineContext<Event>, StateMachine> handler2, 
    	final Function<StateMachineContext<Event>, StateMachine> errorHandler) {
        this.handler1 = handler1;
        this.handler2 = handler2;
        this.errorHandler = errorHandler;
    }
    
    private Function<StateMachineContext<Event>, StateMachine> handler1;
    private Function<StateMachineContext<Event>, StateMachine> handler2;
    private Function<StateMachineContext<Event>, StateMachine> errorHandler;
    

    private static  StateMachine state1Event1(final StateMachineContext<Event> smc) {
        System.out.println("State1 Event 1 : returning State1");
        return StateMachine.State1;
    }

    private static StateMachine state1Event2(final StateMachineContext<Event> smc) {
        System.out.println("State1 Event 2 : returning State2");
        return StateMachine.State2;
    }

    private static StateMachine state2Event1(final StateMachineContext<Event> smc) {
        System.out.println("State2 Event 1 : returning State1");
        return StateMachine.State1;
        
    }

    private static StateMachine state2Event2(final StateMachineContext<Event> smc) {
        System.out.println("State2 Event 2 : returning State2");
        return StateMachine.State2;
    }

    private static StateMachine state1Rollback(final StateMachineContext<Event> smc) {
        System.out.println("State1 Error : returning Finished");
        return StateMachine.Finished;
    }

    private static StateMachine state2Rollback(final StateMachineContext<Event> smc) {
        System.out.println("State2 Error : returning State1");
        smc.addEvent(Event.Error);
        return StateMachine.State1;
    }

    private static StateMachine noOp(final StateMachineContext<Event> smc) {
        System.out.println("Finished No Op : returning Finished");
        return StateMachine.Finished;
    }

    private static class EventHandler implements CallHandler<Event> {

	private final StateMachine sm;
	
	EventHandler(StateMachine sm) {
	    this.sm = sm;
	}
	
	@Override
	public CallHandler<Event> handleEvent(Event event, StateMachineContext<Event> smc) {
	    switch (event) {
    	    	case Event1 : return new EventHandler(sm.handler1.apply(smc));
    	    	case Event2 : return new EventHandler(sm.handler2.apply(smc));
    	    	default : return new EventHandler(sm.errorHandler.apply(smc));
    	    }
	}
    }

    public static StateMachineContext<Event> initialise() {
	return new StateMachineContext<Event>(new EventHandler(StateMachine.State1));
    }
}
