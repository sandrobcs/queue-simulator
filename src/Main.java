import core.Queue;
import core.Scheduler;
import core.Simulator;
import java.util.List;
import model.Interval;

public class Main {
    public static void main(String[] args) {
        int count = 100000;

        Interval arrival = new Interval(3, 5);
        Interval departure = new Interval(4, 5);
        Queue queue = new Queue(1, 5, arrival, departure);
        Scheduler scheduler = new Scheduler();

        Simulator simulator = new Simulator(List.of(queue), scheduler);
        simulator.simulate(count, 3);
    }
}