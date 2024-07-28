package ru.antihack3r.eventsystem.bus;

import ru.antihack3r.eventsystem.CancelableEvent;
import ru.antihack3r.eventsystem.EventHandler;
import ru.antihack3r.eventsystem.listeners.IListener;
import ru.antihack3r.eventsystem.listeners.Listener;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

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
		return event instanceof CancelableEvent? this.postCancelable(event): this.postNormal(event);
	}
	
	private boolean postNormal(Object event) {
		List<IListener> listeners = this.listenerMap.get(event.getClass());
		
		if (listeners != null)
			for (IListener listener: listeners)
				listener.call(event);
		
		return false;
	}
	
	private boolean postCancelable(Object event) {
		List<IListener> listeners = this.listenerMap.get(event.getClass());
		CancelableEvent _event = (CancelableEvent) event;
		
		if (listeners != null) {
			_event.setCancelled(false);
			
			for (IListener listener: listeners) {
				if (!_event.isCancelled() || listener.shouldReceiveCancelled())
					listener.call(_event);
			}
		}
		
		return _event.isCancelled();
	}
	
	@Override
	public void subscribe(Object instance) {
		this.subscribe(this.getListenersFor(instance.getClass(), instance));
	}
	
	@Override
	public void subscribe(Class<?> clasz) {
		this.subscribe(this.getListenersFor(clasz, null));
	}
	
	@Override
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
		this.unsubscribe(this.getListenersFor(instance.getClass(), instance));
	}
	
	@Override
	public void unsubscribe(Class<?> clasz) {
		this.unsubscribe(this.getListenersFor(clasz, null));
	}
	
	@Override
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
	public void clear() {
		this.listenerMap.clear();
		this.listenerCache.clear();
		this.staticListenerCache.clear();
	}
	
	private List<IListener> getListenersFor(Class<?> clazz, Object instance) {
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
	
	private void makeListenersFor(List<IListener> listeners, Class<?> clazs, Object instance) {
		for (Method method: clazs.getMethods()) {
			if (this.isValid(method)) {
				EventHandler anno = method.getAnnotation(EventHandler.class);
				listeners.add(new Listener(anno.priority(), anno.receiveCancelled(), method, instance));
			}
		}
	}
	
	private boolean isValid(Method method) {
		if (!method.isAnnotationPresent(EventHandler.class)) return false;
		if (method.getReturnType() != void.class) return false;
		if (method.getParameterCount() != 1) return false;
		
		return !method.getParameters()[0].getType().isPrimitive();
	}
	
	private void insert(List<IListener> listeners, IListener listener) {
		int i = 0;
		for (; i < listeners.size(); i++) {
			if (listener.getPriority() > listeners.get(i).getPriority()) break;
		}
		
		listeners.add(i, listener);
	}
	
}
