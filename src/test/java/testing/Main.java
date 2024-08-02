package testing;

import ru.antihack3r.eventsystem.bus.EventBus;
import ru.antihack3r.eventsystem.bus.IEventBus;

public class Main {
    
    public static void main(String[] args) {
        IEventBus buss = new EventBus();
        
        long millis = System.currentTimeMillis();
        buss.subscribe(new Handler2());
        System.out.println("Registered a class with 2 handlers in " + (System.currentTimeMillis() - millis) + " ms");
        
        for (int i = 0; i < 10; i += 1) {
            millis = System.currentTimeMillis();
            buss.post(new Event());
            System.out.println("Posted in " + (System.currentTimeMillis() - millis) + " ms");
        }
        
		buss.clear();
    }
    
}