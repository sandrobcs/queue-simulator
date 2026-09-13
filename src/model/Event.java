package model;
public class Event implements Comparable<Event> {
    private double time;
    private EventType type;
    private int queueIndex;

    public Event(double time, EventType type, int queueIndex) {
        this.time = time;
        this.type = type;
        this.queueIndex = queueIndex;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public int getQueueIndex() {
        return queueIndex;
    }

    public void setQueueIndex(int queueIndex) {
        this.queueIndex = queueIndex;
    }
}
