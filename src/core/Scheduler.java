package core;
import java.util.PriorityQueue;
import model.Event;
import model.EventType;
import util.RandomNumberGenerator;

public class Scheduler {
    private final PriorityQueue<Event> events;
    private final RandomNumberGenerator rnd;

    public Scheduler() {
        this.events = new PriorityQueue<>();
        this.rnd = new RandomNumberGenerator();
    }

    public Event nextEvent() {
        return events.poll();
    }

    public void addFirstEvent(int time) {
        events.add(new Event(time, EventType.ARRIVAL, 0));
    }

    public void addArrival(Queue queue, double globalTime, int queueIndex) {
        events.add(new Event(globalTime + rnd.randomInRange(queue.getArrivalInterval()), EventType.ARRIVAL, queueIndex));
    }

    public void addPassage(Queue queue, double globalTime, int queueIndex) {
        events.add(new Event(globalTime + rnd.randomInRange(queue.getDepartureInterval()), EventType.PASSAGE, queueIndex));
    }

    public void addDeparture(Queue queue, double globalTime, int queueIndex) {
        events.add(new Event(globalTime + rnd.randomInRange(queue.getDepartureInterval()), EventType.DEPARTURE, queueIndex));
    }
}