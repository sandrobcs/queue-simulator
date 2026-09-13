package core;
import java.util.HashMap;
import model.Event;
import model.Interval;

public class Queue {
    private int queueStatus;
    private final HashMap<Integer, Double> statusTimes;
    
    // Servers
    private final int c;

    // Queue capacity
    private final int K;

    Interval arrivalInterval;
    Interval departureInterval;

    public Queue(int c, int K, Interval arrivalInterval, Interval departureInterval) {
        this.queueStatus = 0;
        this.statusTimes = new HashMap<>();

        this.c = c;
        this.K = K;
        this.arrivalInterval = arrivalInterval;
        this.departureInterval = departureInterval;
    }

    public int getStatus() {
        return queueStatus;
    }

    public void addStatus() {
        this.queueStatus ++;
    }

    public void removeStatus() {
        this.queueStatus --;
    }

    public int getCapacity() {
        return K;
    }

    public int getServers() {
        return c;
    }

    public Interval getArrivalInterval() {
        return arrivalInterval;
    }

    public Interval getDepartureInterval() {
        return departureInterval;
    }

    public void countTime(Event event, Double globalTime) {
        statusTimes.put(queueStatus, statusTimes.get(queueStatus) + event.getTime() - globalTime);
    }

    public void populateStatusTimes() {
        for (int i = 0; i <= K; i++) {
            statusTimes.put(i, 0.0);
        }
    }

    public double getStatusTime(int key){
        return statusTimes.get(key);
    }

}
