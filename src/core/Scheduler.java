package core;
import java.util.PriorityQueue;
import model.Event;
import model.EventType;
import util.RandomNumberGenerator;

public class Scheduler {
    private PriorityQueue<Event> events;
    private RandomNumberGenerator rnd;

    public Scheduler() {
        this.events = new PriorityQueue<>();
        this.rnd = new RandomNumberGenerator();
    }

    public Event nextEvent() {
        Event event = events.poll();
        return event;
    }

    public void addFirstEvent(int time) {
        events.add(new Event(time, EventType.ARRIVAL));
    }

    public void addArrival(Queue queue, double globalTime) {
        events.add(new Event(globalTime + rnd.randomInRange(queue.getArrivalInterval()), EventType.ARRIVAL));
    }

    public void addDeparture(Queue queue, double globalTime) {
        events.add(new Event(globalTime + rnd.randomInRange(queue.getDepartureInterval()), EventType.DEPARTURE));
    }
}