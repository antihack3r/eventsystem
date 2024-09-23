package ru.antihack3r.eventsystem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method with this and it will become an event listener.
 * The structure is like so:
 * <pre>
 * {@code
 * @EventListener
 * public void onEvent(Event event) {
 *     // ...
 * }
 * }
 * </pre>
 * An event listener method should be {@code public} and {@code void} and should only have one parameter.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventListener {
	
	/**
	 * The higher the number, the sooner the listener will be called.
	 * @return event priority of this listener.
	 * @see EventPriority
	 */
	int priority() default EventPriority.MEDIUM;
	
	/**
	 * If set to {@code true}, the event listener will be called regardless of whether the event is canceled.
	 * @return {@code true} if the event listener should receive all canceled events, {@code false} otherwise.
	 */
	boolean receiveCanceled() default false;
	
}