package com.db.sm.tl;

public interface CallHandler<T> {
    CallHandler<T>  handleEvent(T event, StateMachineContext<T> tlc);
}
