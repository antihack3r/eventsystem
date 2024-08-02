package ru.antihack3r.eventsystem.listeners;

import ru.antihack3r.eventsystem.EventListener;

/**
 * An interface representing an event listener.
 * @see Listener
 */
public interface IListener {
	
	/**
	 * Invokes the listener.
	 * @param event event object that will be passed to the event handler.
	 */
	void call(Object event);
	
	/**
	 * Gets the priority of this event listener.
	 * @return priority of this event listener, as specified in {@link EventListener#priority()}
	 */
	int getPriority();
	
	/**
	 * @see EventListener#receiveCancelled()
	 */
	boolean shouldReceiveCancelled();
	
	/**
	 * Gets the type of the event that this listener is registered for.
	 * @return type of the event that this listener is registered for.
	 */
	Class<?> getTargetEventType();

}
