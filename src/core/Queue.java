package core;

import java.util.HashMap;
import model.Event;
import model.Interval;

public class Queue {
    public static final int INFINITE = Integer.MAX_VALUE;

    private int queueStatus;
    private final HashMap<Integer, Double> statusTimes;
    private int loss;
    private final double[] routing;

    private final int servers;
    private final int capacity;

    Interval arrivalInterval;
    Interval departureInterval;

    public Queue(int servers, int capacity, Interval arrivalInterval, Interval departureInterval, double[] routing) {
        this.queueStatus = 0;
        this.statusTimes = new HashMap<>();
        this.loss = 0;
        this.servers = servers;
        this.capacity = capacity;
        this.arrivalInterval = arrivalInterval;
        this.departureInterval = departureInterval;
        this.routing = routing;
    }

    public int nextQueue(double rnd) {
        double sum = 0.0;
        for (int i = 0; i < routing.length; i++) {
            sum += routing[i];
            if (rnd < sum)
                return i;
        }
        return -1;
    }

    public int getStatus() {
        return queueStatus;
    }

    public void in() {
        this.queueStatus++;
    }

    public void out() {
        this.queueStatus--;
    }

    public int getLoss() {
        return loss;
    }

    public void addLoss() {
        this.loss++;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getServers() {
        return servers;
    }

    public Interval getArrivalInterval() {
        return arrivalInterval;
    }

    public Interval getDepartureInterval() {
        return departureInterval;
    }

    public void countTime(Event event, Double globalTime) {
        statusTimes.merge(queueStatus, event.getTime() - globalTime, Double::sum);
    }

    public void populateStatusTimes() {
        if (capacity == INFINITE)
            return;
        for (int i = 0; i <= capacity; i++) {
            statusTimes.put(i, 0.0);
        }
    }

    public double getStatusTime(int key) {
        return statusTimes.get(key);
    }

    public HashMap<Integer, Double> getStatusTimes() {
        return statusTimes;
    }

}
