package main;

import main.core.Scheduler;
import main.core.Simulator;
import main.util.Config;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java -jar queue-simulator.jar <config.yml>");
            return;
        }

        Config config = new Config(args[0]);
        Scheduler scheduler = new Scheduler();
        Simulator simulator = config.buildSimulator(scheduler);
        simulator.simulate(config.getCount(), config.getFirstArrival());
    }
}