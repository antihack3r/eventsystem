package ru.antihack3r.eventsystem.bus;

import ru.antihack3r.eventsystem.listeners.IListener;

import java.util.List;

public interface IEventBus {
	
	boolean isListening(Class<?> eventType);
	
	boolean post(Object event);
	
	void subscribe(Object instance);
	
	void subscribe(Class<?> clasz);
	
	void subscribe(List<IListener> listeners);
	
	void subscribe(IListener listener);
	
	void unsubscribe(Object instance);
	
	void unsubscribe(Class<?> clasz);
	
	void unsubscribe(List<IListener> listeners);
	
	void unsubscribe(IListener listener);
	
	void clear();
	
}
