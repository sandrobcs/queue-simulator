package main;
import java.util.List;
import main.core.Queue;
import main.core.Scheduler;
import main.core.Simulator;
import main.model.Interval;

public class Main {
    public static void main(String[] args) {
        int count = 100000;

        // Fila 1 - G/G/1/∞, chegadas 2..4, atendimento 1..2
        // 20% → fila 2 (índice 1), 80% → fila 3 (índice 2), exterior = fallback
        Queue queue1 = new Queue(1, Queue.INFINITE, new Interval(2, 4), new Interval(1, 2), new double[]{0.0, 0.2, 0.8});

        // Fila 2 - G/G/2/5, atendimento 4..6
        // 30% → fila 1 (índice 0), 50% → fila 2 (índice 1), 20% → exterior = fallback
        Queue queue2 = new Queue(2, 5, null, new Interval(4, 6), new double[]{0.3, 0.5});

        // Fila 3 - G/G/2/10, atendimento 5..15
        // 70% → fila 3 (índice 2), 30% → exterior = fallback
        Queue queue3 = new Queue(2, 10, null, new Interval(5, 15), new double[]{0.0, 0.0, 0.7});

        Scheduler scheduler = new Scheduler();
        Simulator simulator = new Simulator(List.of(queue1, queue2, queue3), scheduler);
        simulator.simulate(count, 2.0);
    }
}