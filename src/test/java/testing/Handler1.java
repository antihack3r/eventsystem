package testing;

import ru.antihack3r.eventsystem.EventListener;

public class Handler1 {
	
	@EventListener
	public void onEvent(Event event) {
		System.out.println("Event fired on handler 1");
	}
	
}
