package core;
import model.Event;
import model.EventType;

public class Simulator {
    private static double globalTime;
    static Scheduler scheduler;
    static Queue queue;

    public Simulator(Queue queue, Scheduler scheduler) {
        Simulator.globalTime = 0;
        Simulator.queue = queue;
        Simulator.scheduler = scheduler;
    }

    public double getGlobalTime() {
        return globalTime;
    }

    public void setGlobalTime(double globalTime) {
        Simulator.globalTime = globalTime;
    }

    public static void ARRIVAL(Event event) {
        Simulator.globalTime = queue.countTime(event, globalTime);

        if (queue.getStatus() < queue.getCapacity()) {
            queue.addStatus();
            if (queue.getStatus() <= queue.getServers()) {
                scheduler.addDeparture(queue, globalTime);
            }
        }

        scheduler.addArrival(queue, globalTime);
    }

    public static void DEPARTURE(Event event) {
        Simulator.globalTime = queue.countTime(event, globalTime);
        queue.removeStatus();
    
        if (queue.getStatus() >= queue.getServers()) {
            scheduler.addDeparture(queue, globalTime);
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

        System.out.println("\n--- Simulation Results ---");
        System.out.printf("Total simulated time: %.2f%n", globalTime);
        System.out.println("Customers | Time (%)");

        for (int i = 0, K = queue.getCapacity(); i < K + 1; i++) {
            double percentage = (queue.getStatusTime(i) / globalTime) * 100;
            System.out.printf("%9d | %6.2f%%%n", i, percentage);
        }
    }
}