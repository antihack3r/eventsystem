package testing;

import ru.antihack3r.eventsystem.EventListener;
import ru.antihack3r.eventsystem.bus.EventBus;
import ru.antihack3r.eventsystem.bus.IEventBus;

public class Main {
    
    public static class Event1 {}
    public static class Event2 extends Event1 {}
    
    public static void main(String[] args) {
        IEventBus buss = new EventBus();
        
        long millis = System.currentTimeMillis();
        buss.subscribe(Main.class);
        System.out.println("Registered a class with 2 handlers in " + (System.currentTimeMillis() - millis) + " ms");
        
        millis = System.currentTimeMillis();
        buss.post(new Event2());
        System.out.println("Posted in " + (System.currentTimeMillis() - millis) + " ms");
        
		buss.clear();
    }
    
    @EventListener
    public static void ev1(Event1 event) {
        System.out.println("ev1 fired");
    }
    
    @EventListener
    public static void ev2(Event2 event) {
        System.out.println("ev2 fired");
    }
    
}