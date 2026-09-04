package core;
import model.Event;
import model.EventType;

public class Simulator {
    static Scheduler scheduler;
    static Queue queue;

    public Simulator(Queue queue, Scheduler scheduler) {
        Simulator.queue = queue;
        Simulator.scheduler = scheduler;
    }

    public static void ARRIVAL(Event event) {
        queue.countTime(event);

        if (queue.getStatus() < queue.getCapacity()) {
            queue.addStatus();
            if (queue.getStatus() <= queue.getServers()) {
                scheduler.addDeparture(queue);
            }
        }

        scheduler.addArrival(queue);
    }

    public static void DEPARTURE(Event event) {
        queue.countTime(event);
        queue.removeStatus();
    
        if (queue.getStatus() >= queue.getServers()) {
            scheduler.addDeparture(queue);
        }
    }

    public void simulate(int count) {
        scheduler.addFirstEvent(3);
        queue.populateStatusTimes();

        while (count > 0) {
            Event event = scheduler.nextEvent();

            if (event.getType() == EventType.ARRIVAL) {
                ARRIVAL(event);
            } else if (event.getType() == EventType.DEPARTURE) {
                DEPARTURE(event);
            }
            count--;
        }
        double globalTime = queue.getGlobalTime();

        System.out.println("\n--- Simulation Results ---");
        System.out.printf("Total simulated time: %.2f%n", globalTime);
        System.out.println("Customers | Time (%)");

        for (int i = 0, K = queue.getCapacity(); i < K + 1; i++) {
            double percentage = (queue.getStatusTime(i) / globalTime) * 100;
            System.out.printf("%9d | %6.2f%%%n", i, percentage);
        }
    }
}