import core.Queue;
import core.Scheduler;
import core.Simulator;
import java.util.List;
import model.Interval;

public class Main {
    public static void main(String[] args) {
        int count = 100000;

        Queue queue1 = new Queue(2, 3, new Interval(1, 5), new Interval(4, 5));
        Queue queue2 = new Queue(1, 5, null, new Interval(1, 3));

        Scheduler scheduler = new Scheduler();
        Simulator simulator = new Simulator(List.of(queue1, queue2), scheduler);
        simulator.simulate(count, 2.5);
    }
}