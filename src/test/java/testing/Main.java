package testing;

import ru.antihack3r.eventsystem.CancelableEvent;
import ru.antihack3r.eventsystem.EventHandler;
import ru.antihack3r.eventsystem.EventPriority;
import ru.antihack3r.eventsystem.bus.EventBus;
import ru.antihack3r.eventsystem.bus.IEventBus;

public class Main {
    
    public static class Event1 { long time; public Event1(long time) {this.time = time;} }
    public static class Event2 extends CancelableEvent { long time; public Event2(long time) {this.time = time;} }
    public static class ThrowMe {}
    
    public static void main(String[] args) {
        IEventBus buss = new EventBus();
        
        long millis = System.currentTimeMillis();
        buss.subscribe(Main.class);
        System.out.println("Registered a class with 5 handlers in " + (System.currentTimeMillis() - millis) + " ms");
        
        buss.post(new Event1(System.currentTimeMillis()));
        buss.post(new Event2(System.currentTimeMillis()));
		
		buss.clear();
	    buss.post(new ThrowMe());
    }
    
    @EventHandler
    public static void e1Handler(Event1 event) {
        System.out.println("e1 fired");
        System.out.println("diff: " + (System.currentTimeMillis() - event.time));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public static void e2Handler(Event2 event) {
        System.out.println("e2 fired (cancelling)");
        event.cancel();
        System.out.println("diff: " + (System.currentTimeMillis() - event.time));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST - 1)
    public static void e2Handler1(Event2 event) {
        System.out.println("e2 fired (but it should not have)");
        System.out.println("diff: " + (System.currentTimeMillis() - event.time));
    }
    
    @EventHandler(receiveCancelled = true, priority = EventPriority.HIGHEST - 2)
    public static void e2Handler2(Event2 event) {
        System.out.println("e2 fired (got it cancelled)");
        System.out.println("diff: " + (System.currentTimeMillis() - event.time));
    }
    
    @EventHandler
    public static void throwHandler(ThrowMe event) {
        System.out.println("throw fired");
        throw new NullPointerException();
    }
    
}