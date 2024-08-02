package testing;

import ru.antihack3r.eventsystem.EventListener;

public class Handler2 extends Handler1 {
	
	@EventListener
	public void _onEvent(Event event) {
		System.out.println("Event fired on handler 2");
	}
	
}
