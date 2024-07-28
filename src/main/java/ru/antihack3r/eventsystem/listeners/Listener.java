package ru.antihack3r.eventsystem.listeners;

import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * Default implementation of {@link IListener}.
 */
public class Listener implements IListener {
	
	private final int priority;
	private final boolean receiveCancelled;
	private final Class<?> targetEventType;
	private final Consumer<Object> executor;
	
	public Listener(int priority, boolean receiveCancelled, Method method, Object instance) {
		this.targetEventType = method.getParameters()[0].getType();
		this.priority = priority;
		this.receiveCancelled = receiveCancelled;
		this.executor = event -> {
			try {
				method.invoke(instance, event);
			} catch (Throwable t) {
				throw new RuntimeException(String.format("Method handler %s in class %s had thrown an exception!",
						method.getName(), method.getDeclaringClass()), t.getCause());
			}
		};
	}
	
	@Override
	public void call(Object event) {
		this.executor.accept(event);
	}
	
	@Override
	public int getPriority() {
		return this.priority;
	}
	
	@Override
	public boolean shouldReceiveCancelled() {
		return this.receiveCancelled;
	}
	
	@Override
	public Class<?> getTargetEventType() {
		return this.targetEventType;
	}
	
}
