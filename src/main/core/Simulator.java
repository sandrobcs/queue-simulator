package main.core;

import java.util.List;
import java.util.Map;

import main.model.Event;

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

    private void advanceTime(Event event) {
        // Records the current state time before processing the event.
        for (Queue queue : queues) {
            queue.countTime(event, globalTime);
        }
        globalTime = event.getTime();
    }

    private void scheduleService(Queue queue, int queueIndex) {
        if (queueIndex == queues.size() - 1) {
            scheduler.addDeparture(queue, globalTime, queueIndex);
        } else {
            scheduler.addPassage(queue, globalTime, queueIndex);
        }
    }

    private void scheduleNext(Queue current, int currentIndex) {
        if (currentIndex < 0 || currentIndex >= queues.size()) {
            return;
        }

        int nextIndex = current.nextQueue(scheduler.nextRandom());
        if (nextIndex == -1) {
            return;
        }

        Queue next = queues.get(nextIndex);
        if (next.getStatus() >= next.getCapacity()) {
            next.addLoss();
            return;
        }

        // The customer joins the queue and starts service only if a server is available.
        next.in();
        if (next.getStatus() <= next.getServers()) {
            scheduleService(next, nextIndex);
        }
    }

    public void ARRIVAL(Event event) {
        int index = event.getQueueIndex();
        Queue queue = queues.get(index);

        advanceTime(event);
        if (queue.getStatus() < queue.getCapacity()) {
            queue.in();
            if (queue.getStatus() <= queue.getServers()) {
                scheduleService(queue, index);
            }
        } else {
            queue.addLoss();
        }

        scheduler.addArrival(queues.get(0), globalTime, 0);
    }

    public void PASSAGE(Event event) {
        int index = event.getQueueIndex();
        Queue queue = queues.get(index);

        advanceTime(event);
        queue.out();

        if (queue.getStatus() >= queue.getServers()) {
            scheduleService(queue, index);
        }

        scheduleNext(queue, index);
    }

    public void DEPARTURE(Event event) {
        int index = event.getQueueIndex();
        Queue queue = queues.get(index);

        advanceTime(event);
        queue.out();

        if (queue.getStatus() >= queue.getServers()) {
            scheduler.addDeparture(queue, globalTime, index);
        }

        scheduleNext(queue, index);
    }

    public void simulate(int count, double timeFirstEvent) {
        scheduler.addFirstEvent(timeFirstEvent);
        for (Queue queue : queues) {
            queue.populateStatusTimes();
        }

        while (scheduler.getRandomCount() < count) {
            Event event = scheduler.nextEvent();

            // The priority queue always returns the next event in time.
            switch (event.getType()) {
                case ARRIVAL -> ARRIVAL(event);
                case PASSAGE -> PASSAGE(event);
                case DEPARTURE -> DEPARTURE(event);
            }
        }

        System.out.println("\n--- Simulation Results ---");
        System.out.printf("Total simulated time: %.2f%n", globalTime);

        for (int index = 0; index < queues.size(); index++) {
            Queue queue = queues.get(index);
            System.out.printf("%nQueue %d | Losses: %d%n", index + 1, queue.getLoss());
            System.out.println("Customers |       Time | Time (%)");

            queue.getStatusTimes().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        double time = entry.getValue();
                        double percentage = (time / globalTime) * 100;
                        System.out.printf("%9d | %10.2f | %6.2f%%%n", entry.getKey(), time, percentage);
                    });
        }
    }
}