package ru.antihack3r.eventsystem.bus;

import ru.antihack3r.eventsystem.CancelableEvent;
import ru.antihack3r.eventsystem.listeners.IListener;

/**
 * An interface representing an event bus.
 * @see EventBus
 */
public interface IEventBus {
	
	/**
	 * Returns whether at least one event listener is currently registered for the specified event type.
	 * @param eventType event type to check for registered listeners.
	 * @return whether the event is being listened for.
	 */
	boolean isListening(Class<?> eventType);
	
	/**
	 * Posts the specified event to all event listeners that have been subscribed for this event.
	 * @param event event to post.
	 * @return whether the event is canceled, or {@code false} if the event class doesn't extend
	 * {@link CancelableEvent}
	 */
	boolean post(Object event);
	
	/**
	 * Finds all correctly structured methods in the specified class and turns them into event listeners on this bus.
	 * @param instance instance of the class to find methods in.
	 * @see ru.antihack3r.eventsystem.EventListener
	 */
	void subscribe(Object instance);
	
	/**
	 * Finds all correctly structured methods in the specified class and turns them into event listeners on this bus.
	 * @param clazz class to find methods in.
	 * @see ru.antihack3r.eventsystem.EventListener
	 */
	void subscribe(Class<?> clazz);
	
	/**
	 * Subscribes a specified event listener on this bus.
	 * @param listener event listener to subscribe.
	 */
	void subscribe(IListener listener);
	
	/**
	 * Finds all correctly structured methods in the specified class and removes them from the list of
	 * event listeners on this bus.
	 * @param instance instance of the class to find methods in.
	 * @see ru.antihack3r.eventsystem.EventListener
	 */
	void unsubscribe(Object instance);
	
	/**
	 * Finds all correctly structured methods in the specified class and removes them from the list of
	 * event listeners on this bus.
	 * @param clazz class to find methods in.
	 * @see ru.antihack3r.eventsystem.EventListener
	 */
	void unsubscribe(Class<?> clazz);
	
	/**
	 * Unsubscribes a specified event listener on this bus.
	 * @param listener event listener to unsubscribe.
	 */
	void unsubscribe(IListener listener);
	
	/**
	 * Unsubscribes all event listeners that were subscribed to the specified event.
	 * @param eventType type of the event.
	 */
	void unsubscribeAll(Class<?> eventType);
	
	/**
	 * Unsubscribes all event listeners from this bus.
	 */
	void clear();
	
}
