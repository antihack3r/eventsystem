package ru.antihack3r.eventsystem.listeners;

public interface IListener {

	void call(Object event);
	
	int getPriority();
	
	boolean shouldReceiveCancelled();
	
	Class<?> getTargetEventType();

}
