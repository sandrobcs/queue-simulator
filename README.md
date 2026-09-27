# Queue Simulator

Discrete-event queue simulator built for the **Simulation and Analytical Methods** course in the Software Engineering program at PUCRS.

The simulator models queueing networks with probabilistic routing between queues. Each queue can have its own number of servers, capacity, arrival interval, and service interval. A queue without a configured capacity has infinite capacity.

## Components

- `Main`: application entry point; receives the YAML configuration file as an argument.
- `Config`: reads the YAML file and builds the simulation parameters, queues, and routing table.
- `Simulator`: processes events, advances simulation time, and collects queue statistics.
- `Scheduler`: stores events in chronological order and provides pseudo-random values.
- `Queue`: tracks customers, capacity, losses, and the time spent in each state.
- `Event` and `EventType`: represent events and their types (`ARRIVAL`, `PASSAGE`, and `DEPARTURE`).
- `Interval`: represents the lower and upper bounds of an arrival or service interval.
- `RandomNumberGenerator`: deterministic linear congruential generator used by the simulation.

## Simulation flow

1. `Main` loads the YAML file through `Config` and creates the simulator.
2. The first `ARRIVAL` event is scheduled for the configured `firstArrival` time.
3. Events are retrieved chronologically from the scheduler's priority queue.
4. On `ARRIVAL`, the customer enters the external-arrival queue if there is capacity. A service event is scheduled when a server is available, and the next external arrival is added.
5. On `PASSAGE`, the customer leaves an intermediate queue and is routed probabilistically to another queue. If the destination is full, the loss is counted there.
6. On `DEPARTURE`, the customer leaves the final queue. Remaining customers can start another service, and the completed customer can be routed according to the queue's probabilities.
7. The simulation stops when the configured number of random values has been consumed (`count`), and prints accumulated state times and losses for every queue.

Probabilities in a queue's routing table do not need to sum to `1.0`. Any remaining probability represents a customer leaving the system.

## Project structure

```text
queue-simulator/
├── model.yml                         # Example YAML configuration
├── pom.xml                           # Maven build and dependency configuration
├── src/
│   └── main/                         # Source root configured in pom.xml
│       ├── Main.java                 # Application entry point
│       ├── core/
│       │   ├── Queue.java            # Queue state and loss tracking
│       │   ├── Scheduler.java        # Event priority queue and random values
│       │   └── Simulator.java        # Main simulation engine
│       ├── model/
│       │   ├── Event.java            # Event time, type, and queue index
│       │   ├── EventType.java        # ARRIVAL, PASSAGE, DEPARTURE
│       │   └── Interval.java         # Interval representation
│       └── util/
│           ├── Config.java           # YAML configuration loader
│           └── RandomNumberGenerator.java # Deterministic random number generator
└── README.md
```

The compiled classes and shaded JAR are generated in `target/` by Maven. The `bin/` directory is no longer used.

## Configuration

The simulator accepts a YAML file with the `simulation`, `queues`, and `routing` sections. The example below is the content of `model.yml`:

```yaml
simulation:
  count: 100000
  firstArrival: 2.0

queues:
  - name: Q1
    servers: 1
    arrivalInterval: [2.0, 4.0]
    serviceInterval: [1.0, 2.0]

  - name: Q2
    servers: 2
    capacity: 5
    serviceInterval: [4.0, 6.0]

  - name: Q3
    servers: 2
    capacity: 10
    serviceInterval: [5.0, 15.0]

routing:
  - from: Q1
    to: Q2
    probability: 0.2
  - from: Q1
    to: Q3
    probability: 0.8
  - from: Q2
    to: Q1
    probability: 0.3
  - from: Q2
    to: Q3
    probability: 0.5
  - from: Q3
    to: Q2
    probability: 0.7
```

Configuration fields:

- `simulation.count`: number of random values to consume before stopping.
- `simulation.firstArrival`: time of the first external arrival.
- `queues[].name`: queue identifier used by the routing entries.
- `queues[].servers`: number of parallel servers.
- `queues[].capacity`: maximum number of customers; omit it for infinite capacity.
- `queues[].arrivalInterval`: lower and upper bounds for external arrivals; omit it for internal queues.
- `queues[].serviceInterval`: lower and upper bounds for service times.
- `routing[].from`: source queue name.
- `routing[].to`: destination queue name.
- `routing[].probability`: routing probability from the source to the destination.

## Build and run

Build the executable JAR with Maven:

```bash
mvn package
```

Run the simulator with the example configuration:

```bash
java -jar target/queue-simulator-1.0.0.jar model.yml
```

## Example output

The following is the output produced with the three-queue configuration above:

```text
--- Simulation Results ---
Total simulated time: 50660.27

Queue 1 | Losses: 0
Customers |       Time | Time (%)
        0 |   20222.99 |  39.92%
        1 |   26713.78 |  52.73%
        2 |    3604.09 |   7.11%
        3 |     119.41 |   0.24%

Queue 2 | Losses: 10
Customers |       Time | Time (%)
        0 |   12372.21 |  24.42%
        1 |   20818.38 |  41.09%
        2 |   12966.66 |  25.60%
        3 |    3762.17 |   7.43%
        4 |     653.00 |   1.29%
        5 |      87.85 |   0.17%

Queue 3 | Losses: 11570
Customers |       Time | Time (%)
        0 |       5.34 |   0.01%
        1 |       2.64 |   0.01%
        2 |       2.86 |   0.01%
        3 |       6.52 |   0.01%
        4 |       2.77 |   0.01%
        5 |       3.34 |   0.01%
        6 |       8.14 |   0.02%
        7 |      52.27 |   0.10%
        8 |    2973.38 |   5.87%
        9 |   15940.02 |  31.46%
       10 |   31663.00 |  62.50%
```

## Course context

**Course:** Simulation and Analytical Methods  
**Program:** Software Engineering  
**Institution:** PUCRS (Pontifícia Universidade Católica do Rio Grande do Sul)