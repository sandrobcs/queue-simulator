package main.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import main.core.Queue;
import main.core.Scheduler;
import main.core.Simulator;
import main.model.Interval;

public class Config {

    private int count;
    private double firstArrival;
    private List<Queue> queues;

    public Config(String filepath) throws IOException {
        Yaml yaml = new Yaml();
        Map<String, Object> data;

        try (FileInputStream fis = new FileInputStream(filepath)) {
            data = yaml.load(fis);
        }

        Map<String, Object> simulation = (Map<String, Object>) data.get("simulation");
        this.count = (int) simulation.get("count");
        this.firstArrival = ((Number) simulation.get("firstArrival")).doubleValue();

        List<Map<String, Object>> queuesData = (List<Map<String, Object>>) data.get("queues");
        List<String> queueNames = new ArrayList<>();
        for (Map<String, Object> q : queuesData) {
            queueNames.add((String) q.get("name"));
        }

        List<Map<String, Object>> routingData = (List<Map<String, Object>>) data.get("routing");
        Map<String, double[]> routingMap = new HashMap<>();

        // Converts queue names from YAML into indices used by the simulator.
        for (String name : queueNames) {
            routingMap.put(name, new double[queueNames.size()]);
        }

        if (routingData != null) {
            for (Map<String, Object> route : routingData) {
                String from = (String) route.get("from");
                String to = (String) route.get("to");
                double probability = ((Number) route.get("probability")).doubleValue();
                int toIndex = queueNames.indexOf(to);
                routingMap.get(from)[toIndex] = probability;
            }
        }

        this.queues = new ArrayList<>();
        for (Map<String, Object> q : queuesData) {
            String name = (String) q.get("name");
            int servers = (int) q.get("servers");
            int capacity = q.containsKey("capacity") ? (int) q.get("capacity") : Queue.INFINITE;

            Interval arrivalInterval = null;
            if (q.containsKey("arrivalInterval")) {
                List<Double> arr = (List<Double>) q.get("arrivalInterval");
                arrivalInterval = new Interval(arr.get(0), arr.get(1));
            }

            List<Double> svc = (List<Double>) q.get("serviceInterval");
            Interval serviceInterval = new Interval(svc.get(0), svc.get(1));

            double[] routing = routingMap.get(name);
            queues.add(new Queue(servers, capacity, arrivalInterval, serviceInterval, routing));
        }
    }

    public int getCount() {
        return count;
    }

    public double getFirstArrival() {
        return firstArrival;
    }

    public List<Queue> getQueues() {
        return queues;
    }

    public Simulator buildSimulator(Scheduler scheduler) {
        return new Simulator(queues, scheduler);
    }
}