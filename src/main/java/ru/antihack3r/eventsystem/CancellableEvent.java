package ru.antihack3r.eventsystem;

/**
 * If you want to make your event cancellable, extend this class.
 * Once one of the event listeners cancels your event, the event listeners that were supposed to handle this event
 * won't receive it.
 * @see EventListener#receiveCancelled()
 */
public class CancellableEvent {
	
	/**
	 * Whether this event is cancelled.
	 */
	private boolean cancelled = false;
	
	/**
	 * Sets whether this event is cancelled.
	 * @param cancelled {@code true} to cancel the event, {@code false} otherwise.
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	/**
	 * Cancels this event.
	 */
	public void cancel() {
		this.setCancelled(true);
	}
	
	/**
	 * Gets whether this event is cancelled.
	 * @return {@code true} if this event is cancelled, {@code false} otherwise.
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}
	
}
