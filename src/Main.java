import java.util.HashMap;
import java.util.PriorityQueue;

public class Main {
    private static PriorityQueue<Event> events = new PriorityQueue<>();
    private static HashMap<Integer, Double> times = new HashMap<>();

    private static double currentTime = 0;
    private static int queue = 0;

    // Tamanho máximo da fila
    private static int K = 5;

    // Número de servidores na fila
    private static int c = 2;

    // Intervalo de chegada e saida da fila
    static int arivalL = 2;
    static int arrivalU = 5;
    static int departureL = 3;
    static int departurU = 5;

    public static double rnd(int a, int b) {
        return a + ((b - a) * RandomNumberGenerator.NextRandom());
    }

    public static Event NextEvent() {
        Event event = events.poll();
        return event;
    }

    public static void ARRIVAL(Event event) {
        times.put(queue, times.get(queue) + event.getTime() - currentTime);
        currentTime = event.getTime();

        if (queue < K) {
            queue++;
            if (queue <= c) {
                events.add(new Event(currentTime + rnd(departureL, departurU), EventType.DEPARTURE));
            }
        }

        events.add(new Event(currentTime + rnd(arivalL, arrivalU), EventType.ARRIVAL));
    }

    public static void DEPARTURE(Event event) {
        times.put(queue, times.get(queue) + event.getTime() - currentTime);
        currentTime = event.getTime();

        queue--;
        if (queue >= c) {
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

            if (event.getType() == EventType.ARRIVAL) {
                ARRIVAL(event);
            } else if (event.getType() == EventType.DEPARTURE) {
                DEPARTURE(event);
            }
            count--;
        }

        System.out.println("\n--- Simulation Results ---");
        System.out.printf("Total simulated time: %.2f%n", currentTime);
        System.out.println("Customers | Time (%)");

        for (int i = 0; i < K + 1; i++) {
            double percentage = (times.get(i) / currentTime) * 100;
            System.out.printf("%9d | %6.2f%%%n", i, percentage);
        }
    }
}
