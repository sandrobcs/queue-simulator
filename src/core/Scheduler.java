package core;
import java.util.PriorityQueue;

import model.Event;
import model.EventType;
import util.RandomNumberGenerator;

public class Scheduler {
    private static PriorityQueue<Event> events;
    private static RandomNumberGenerator rnd;

    public Scheduler() {
        Scheduler.events = new PriorityQueue<>();
        Scheduler.rnd = new RandomNumberGenerator();
    }

    public Event nextEvent() {
        Event event = events.poll();
        return event;
    }

    public void addFirstEvent(int time) {
        events.add(new Event(time, EventType.ARRIVAL));
    }

    public void addArrival(Queue queue) {
        events.add(new Event(queue.getGlobalTime() + rnd.randomInRange(queue.getArrivalInterval()), EventType.ARRIVAL));
    }

    public void addDeparture(Queue queue) {
        events.add(new Event(queue.getGlobalTime() + rnd.randomInRange(queue.getDepartureInterval()), EventType.DEPARTURE));
    }
}