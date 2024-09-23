package ru.antihack3r.eventsystem;

/**
 * If you want to make your event cancelable, extend this class.
 * Once one of the event listeners cancels your event, the event listeners that were supposed to handle this event
 * won't receive it.
 * @see EventListener#receiveCanceled()
 */
public class CancelableEvent {
	
	/**
	 * Whether this event is canceled.
	 */
	private boolean canceled = false;
	
	/**
	 * Sets whether this event is canceled.
	 * @param canceled {@code true} to cancel the event, {@code false} otherwise.
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	/**
	 * Cancels this event.
	 */
	public void cancel() {
		this.setCanceled(true);
	}
	
	/**
	 * Gets whether this event is canceled.
	 * @return {@code true} if this event is canceled, {@code false} otherwise.
	 */
	public boolean isCanceled() {
		return this.canceled;
	}
	
}
