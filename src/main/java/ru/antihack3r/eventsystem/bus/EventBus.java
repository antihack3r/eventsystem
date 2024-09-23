package ru.antihack3r.eventsystem.bus;

import ru.antihack3r.eventsystem.CancelableEvent;
import ru.antihack3r.eventsystem.EventListener;
import ru.antihack3r.eventsystem.listeners.IListener;
import ru.antihack3r.eventsystem.listeners.Listener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/**
 * Default implementation of {@link IEventBus}.
 */
public class EventBus implements IEventBus {
	
	private final Map<Object, List<IListener>> listenerCache = new ConcurrentHashMap<>();
	private final Map<Class<?>, List<IListener>> staticListenerCache = new ConcurrentHashMap<>();
	
	private final Map<Class<?>, List<IListener>> listenerMap = new ConcurrentHashMap<>();
	
	@Override
	public boolean isListening(Class<?> eventType) {
		List<IListener> listeners = this.listenerMap.get(eventType);
		return listeners != null && !listeners.isEmpty();
	}
	
	@Override
	public boolean post(Object event) {
		return event instanceof CancelableEvent ? this.postCancelable(event): this.postNormal(event);
	}
	
	private boolean postNormal(Object event) {
		List<IListener> listeners = this.getAllListenersFor(event.getClass());
		
		for (IListener listener: listeners)
			listener.call(event);
		
		return false;
	}
	
	private boolean postCancelable(Object event) {
		List<IListener> listeners = this.listenerMap.get(event.getClass());
		CancelableEvent _event = (CancelableEvent) event;
		
		if (listeners != null) {
			_event.setCanceled(false);
			
			for (IListener listener: listeners) {
				if (!_event.isCanceled() || listener.shouldReceiveCanceled())
					listener.call(_event);
			}
		}
		
		return _event.isCanceled();
	}
	
	@Override
	public void subscribe(Object instance) {
		this.subscribe(this.getOrMakeListenersFor(instance.getClass(), instance));
	}
	
	@Override
	public void subscribe(Class<?> clazz) {
		this.subscribe(this.getOrMakeListenersFor(clazz, null));
	}
	
	public void subscribe(List<IListener> listeners) {
		for (IListener listener: listeners)
			this.subscribe(listener);
	}
	
	@Override
	public void subscribe(IListener listener) {
		insert(this.listenerMap.computeIfAbsent(listener.getTargetEventType(),
				clazz -> new CopyOnWriteArrayList<>()), listener);
	}
	
	@Override
	public void unsubscribe(Object instance) {
		this.unsubscribe(this.getOrMakeListenersFor(instance.getClass(), instance));
	}
	
	@Override
	public void unsubscribe(Class<?> clazz) {
		this.unsubscribe(this.getOrMakeListenersFor(clazz, null));
	}
	
	public void unsubscribe(List<IListener> listeners) {
		for (IListener listener: listeners)
			this.unsubscribe(listener);
	}
	
	@Override
	public void unsubscribe(IListener listener) {
		List<IListener> l = this.listenerMap.get(listener.getTargetEventType());
		
		if (l != null) {
			l.remove(listener);
		}
	}
	
	@Override
	public void unsubscribeAll(Class<?> eventType) {
		this.listenerMap.remove(eventType);
	}
	
	@Override
	public void clear() {
		this.listenerMap.clear();
		this.listenerCache.clear();
		this.staticListenerCache.clear();
	}
	
	/**
	 * Gets listeners for each event listener method in the specified class from cache
	 * or makes them and adds them to the cache.
	 * @param clazz class to find listeners in.
	 * @param instance instance of <tt>clazz</tt> if it needs one.
	 * @return new list containing listeners from the specified class.
	 */
	private List<IListener> getOrMakeListenersFor(Class<?> clazz, Object instance) {
		Function<Object, List<IListener>> func = o -> {
			List<IListener> listeners = new CopyOnWriteArrayList<>();
			
			makeListenersFor(listeners, clazz, instance);
			
			return listeners;
		};
		
		if (instance == null) return this.staticListenerCache.computeIfAbsent(clazz, func);
		
		for (Object key: this.listenerCache.keySet())
			if (key == instance)
				return this.listenerCache.get(instance);
		
		List<IListener> listeners = func.apply(instance);
		this.listenerCache.put(instance, listeners);
		return listeners;
	}
	
	/**
	 * Makes listeners for each event listener method in the specified class and adds them to the specified list.
	 * @param listeners list of listeners to add event listeners to.
	 * @param clazz a class to find listeners in.
	 * @param instance instance of <tt>clazz</tt> if it needs one.
	 */
	private void makeListenersFor(List<IListener> listeners, Class<?> clazz, Object instance) {
		for (Method method: clazz.getMethods()) {
			if (this.isValid(method)) {
				EventListener anno = method.getAnnotation(EventListener.class);
				listeners.add(new Listener(anno.priority(), anno.receiveCanceled(), method, instance));
			}
		}
	}
	
	/**
	 * Checks whether the specified method could be an event listener.
	 * @param method method to be checked.
	 * @return <tt>true</tt> if the method could be an event listener, <tt>false</tt> otherwise.
	 */
	private boolean isValid(Method method) {
		if (!method.isAnnotationPresent(EventListener.class)) return false;
		if (method.getReturnType() != void.class) return false;
		if (method.getParameterCount() != 1) return false;
		
		return !method.getParameters()[0].getType().isPrimitive();
	}
	
	/**
	 * Inserts the specified listener into the specified list.
	 * @param listeners list of listeners.
	 * @param listener listener.
	 */
	private void insert(List<IListener> listeners, IListener listener) {
		int i = 0;
		for (; i < listeners.size(); i++) {
			if (listener.getPriority() > listeners.get(i).getPriority()) break;
		}
		
		listeners.add(i, listener);
	}
	
	/**
	 * Gets all listeners for the specified event type.
	 * @param eventType event type.
	 * @return all listeners for the specified event type.
	 */
	private List<IListener> getAllListenersFor(Class<?> eventType) {
		List<IListener> list = new ArrayList<>();
		
		for (Map.Entry<Class<?>, List<IListener>> entry: this.listenerMap.entrySet())
			if (entry.getKey().isAssignableFrom(eventType))
				list.addAll(entry.getValue());
		
		return list;
	}
	
}
