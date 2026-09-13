package core;
import java.util.List;
import model.Event;
import model.EventType;

public class Simulator {
    private double globalTime;
    private final Scheduler scheduler;
    private final List<Queue> queues;

    public Simulator(List<Queue> queues, Scheduler scheduler) {
        this.globalTime = 0;
        this.queues = queues;
        this.scheduler = scheduler;
    }

    public double getGlobalTime() {
        return globalTime;
    }

    public void setGlobalTime(double globalTime) {
        this.globalTime = globalTime;
    }

    public void ARRIVAL(Event event) {
        int index = event.getQueueIndex();
        Queue queue = queues.get(index);

        queue.countTime(event, globalTime);
        globalTime = event.getTime();

        if (queue.getStatus() < queue.getCapacity()) {
            queue.in();
            if (queue.getStatus() <= queue.getServers()) {
                if(index == queues.size() - 1) {
                    scheduler.addDeparture(queues.get(0), globalTime, 0);
                } else {
                    scheduler.addPassage(queue, globalTime, index);
                }
            }
        } else {
            queue.addLoss();
        }

        scheduler.addArrival(queues.get(0), globalTime, index);
    }

    public void DEPARTURE(Event event) {
        int index = event.getQueueIndex();
        Queue queue = queues.get(index);

        queue.countTime(event, globalTime);
        globalTime = event.getTime();
        queue.out();

        if (queue.getStatus() >= queue.getServers()) {
            scheduler.addDeparture(queue, globalTime, index);
        }
    }

    public void PASSAGE(Event event) {
        int index = event.getQueueIndex();
        int nextIndex = index + 1;
        Queue queue = queues.get(index);
        Queue next = queues.get(nextIndex);

        queue.countTime(event, globalTime);
        next.countTime(event, globalTime);
        globalTime = event.getTime();

        queue.out();
        if (queue.getStatus() >= queue.getServers()) {
            scheduler.addPassage(queue, globalTime, index);
        }

        if (next.getStatus() < next.getCapacity()) {
            next.in();
            if (next.getStatus() <= next.getServers()) {
                if (nextIndex == queues.size() - 1) {
                    scheduler.addDeparture(next, globalTime, nextIndex);
                } else {
                    scheduler.addPassage(next, globalTime, nextIndex);
                }
            }
        } else {
            next.addLoss();
        }
    }

    

    public void simulate(int count) {
        scheduler.addFirstEvent(3);
        for (Queue queue : queues) {
            queue.populateStatusTimes();
        }

        while (count > 0) {
            Event event = scheduler.nextEvent();

            if (event.getType() == EventType.ARRIVAL) {
                ARRIVAL(event);
            } else if (event.getType() == EventType.DEPARTURE) {
                DEPARTURE(event);
            } else if (event.getType() == EventType.PASSAGE) {
                PASSAGE(event);
            }
            count--;
        }

         System.out.println("\n--- Simulation Results ---");
        System.out.printf("Total simulated time: %.2f%n", globalTime);

        for (int q = 0; q < queues.size(); q++) {
            Queue queue = queues.get(q);
            System.out.printf("%nQueue %d | Losses: %d%n", q + 1, queue.getLoss());
            System.out.println("Customers | Time (%)");

            for (int i = 0; i <= queue.getCapacity(); i++) {
                double percentage = (queue.getStatusTime(i) / globalTime) * 100;
                System.out.printf("%9d | %6.2f%%%n", i, percentage);
            }
        }
    }
}