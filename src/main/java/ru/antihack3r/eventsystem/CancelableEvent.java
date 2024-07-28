package ru.antihack3r.eventsystem;

/**
 *
 */
public class CancelableEvent {
	
	private boolean cancelled = false;
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public void cancel() {
		this.setCancelled(true);
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
}
