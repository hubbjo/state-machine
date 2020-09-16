package com.db.sm.tl;

import java.util.function.Function;

public enum TrafficLightStateMachine {
//  State		Event Handler
    RED			(TrafficLightStateMachine::transitionToRedAmber),
    RED_AMBER	(TrafficLightStateMachine::transitionToGreen),
    GREEN		(TrafficLightStateMachine::transitionToAmber),
    AMBER		(TrafficLightStateMachine::transitionToRed);
    
    final Function<StateMachineContext<TimedEvent>, TrafficLightStateMachine> eventHandler; 
    
    TrafficLightStateMachine(final Function<StateMachineContext<TimedEvent>, TrafficLightStateMachine> eventHandler) {
		this.eventHandler = eventHandler;
    }
    
    public static TrafficLightStateMachine transitionToGreen(StateMachineContext<TimedEvent> tlc) {
		System.out.println("Transitioning to GREEN");
		tlc.setTimedEvent(TimedEvent.Timeout, 2);
		return TrafficLightStateMachine.GREEN;
    }

    public static TrafficLightStateMachine transitionToAmber(StateMachineContext<TimedEvent> tlc) {
		System.out.println("Transitioning to AMBER");
		tlc.setTimedEvent(TimedEvent.Timeout, 2);
		return TrafficLightStateMachine.AMBER;
    }

    public static TrafficLightStateMachine transitionToRed(StateMachineContext<TimedEvent> tlc) {
		System.out.println("Transitioning to RED");
		tlc.setTimedEvent(TimedEvent.Timeout, 2);
		return TrafficLightStateMachine.RED;
    }

    public static TrafficLightStateMachine transitionToRedAmber(StateMachineContext<TimedEvent> tlc) {
		System.out.println("Transitioning to RED_AMBER");
		tlc.setTimedEvent(TimedEvent.Timeout, 2);
		return TrafficLightStateMachine.RED_AMBER;
    }

    private static class TLCallHandler implements CallHandler<TimedEvent> {

		private final TrafficLightStateMachine sm;

		TLCallHandler(TrafficLightStateMachine sm) {
			this.sm = sm;
		}

		@Override
		public CallHandler<TimedEvent> handleEvent(TimedEvent event, StateMachineContext<TimedEvent> tlc) {
			TrafficLightStateMachine newState =  sm.eventHandler.apply(tlc);
			return new TLCallHandler(newState);
		}
    }

    public static StateMachineContext<TimedEvent> initialise() {
		return new StateMachineContext<TimedEvent>(new TLCallHandler(TrafficLightStateMachine.RED));
    }
}
