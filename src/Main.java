import java.util.HashMap;
import java.util.PriorityQueue;

public class Main {
    private static PriorityQueue<Event> events = new PriorityQueue<>();
    private static HashMap<Integer, Double> times = new HashMap<>();

    private static double currentTime = 0;
    private static int queue = 0;
    private static int K = 2;

    // Intervalo de chegada e saida da fila 
    static int arivalL = 2;
    static int arrivalU = 3;
    static int departureL = 2;
    static int departurU = 4;
    

    public static double rnd(int a, int b) {
        return a + ((b-a)* RandomNumberGenerator.NextRandom());  
    }

    public static Event NextEvent() {
        Event event = events.poll();
        return event;
    }

    public static void ARRIVAL(Event event) {
        times.put(queue , times.get(queue) + event.getTime() - currentTime);
        currentTime = event.getTime();

        if(queue < K) {
            queue++;
            if(queue <= 1) {
                events.add(new Event(currentTime + rnd(departureL, departurU), EventType.DEPARTURE));
            }
        }

        events.add(new Event(currentTime + rnd(arivalL, arrivalU), EventType.ARRIVAL));
    }

    public static void DEPARTURE(Event event) {
        times.put(queue , times.get(queue) + event.getTime() - currentTime);
        currentTime = event.getTime();

        queue --;
        if(queue >= 1) {
            events.add(new Event(currentTime + rnd(departureL, departurU), EventType.DEPARTURE));
        }
    }


    public static void main(String[] args) {
        int count = 100000;

        events.add(new Event(2, EventType.ARRIVAL));

        for (int i = 0; i <= K; i++) {
            times.put(i, 0.0);
        }

        while (count > 0) {
            Event event = NextEvent();

            if(event.getType() == EventType.ARRIVAL){
                ARRIVAL(event);
            } else if (event.getType() == EventType.DEPARTURE) {
                DEPARTURE(event);
            }
            count--;
        }

        for(int i=0; i<K+1; i++) {
            System.out.println(i + ": " + (times.get(i)/currentTime)*100 + "%");
        }
    }
}
