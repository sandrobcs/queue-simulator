# Queue Simulator

Discrete-event queue simulator built for the **Simulation and Analytical Methods** course, Software Engineering @ PUCRS.

Simulates queueing systems in Kendall's notation **A/B/c/K** (arrival distribution / service distribution / number of servers / system capacity), and compares simulated results with analytical queueing theory.

## How it works

**Core components:**
- `Simulator`: main simulation engine that orchestrates the event-driven simulation process;
- `Scheduler`: manages event scheduling and retrieval from the priority queue;
- `Queue`: handles customer queue management and state tracking;
- `Event` & `EventType`: represent discrete events (`ARRIVAL`, `DEPARTURE`);
- `RandomNumberGenerator`: Linear Congruential Generator (`Xₙ₊₁ = (a·Xₙ + c) mod M`) for reproducible random sequences;
- `Interval`: data structure for representing arrival/service intervals.

**Simulation flow:**
1. `Main.java` initializes the `Simulator` with system parameters (arrival/service distributions, number of servers, capacity);
2. Events (`ARRIVAL`, `DEPARTURE`) are processed in chronological order via the `Scheduler`'s priority queue;
3. Time spent in each system state (0..K customers) is accumulated, weighted by elapsed time;
4. **Arrival**: if `queue < K`, the customer is accepted; if a server is available, a `DEPARTURE` is scheduled; next arrival is always scheduled;
5. **Departure**: customer leaves; if enough customers remain, another `DEPARTURE` is scheduled;
6. Results are collected as state probabilities and output after simulation completes.

## Project structure

```
queue-simulator/
├── bin/                             # compiled class files
├── src/
│   ├── Main.java                    # entry point, simulation parameters
│   ├── core/
│   │   ├── Queue.java               # queue management, state tracking
│   │   ├── Scheduler.java           # event scheduling (priority queue)
│   │   └── Simulator.java           # main simulation engine
│   ├── model/
│   │   ├── Event.java               # event class (time + type), Comparable
│   │   ├── EventType.java           # enum: ARRIVAL, DEPARTURE
│   │   └── Interval.java            # interval distribution representation
│   └── util/
│       └── RandomNumberGenerator.java # LCG-based RNG
└── README.md
```

## Configuration

Edit `Main.java` to adjust simulation parameters:

```java
int count = 100000;                           // number of events to simulate
Interval arrival = new Interval(3, 5);        // arrival time range [min, max]
Interval departure = new Interval(4, 5);      // service time range [min, max]
Queue queue = new Queue(1, 5, arrival, departure);  // (servers, capacity, arrival, service)
```

- **`count`**: Total number of events to process;
- **`arrival`**: Uniform random interval for inter-arrival times;
- **`departure`**: Uniform random interval for service times;
- **`servers`** (1st param of Queue): Number of parallel servers (c in Kendall's A/B/c/K);
- **`capacity`** (2nd param of Queue): System capacity (K in Kendall's A/B/c/K).

## Build and Run

**Compile:**
```bash
javac -d bin src/Main.java src/core/*.java src/model/*.java src/util/*.java
```

**Run:**
```bash
java -cp bin Main
```

**Note:** Classes in `core/`, `model/`, and `util/` packages must have proper `package` declarations (e.g., `package core;`, `package model;`, `package util;`). `Main.java` is in the default package.

## Output

Percentage of time spent in each state (0..K customers), i.e. simulated state probabilities:

```
--- Simulation Results ---
Total simulated time: 211760.08
Customers | Time (%)
        0 |   0.00%
        1 |   0.00%
        2 |   0.01%
        3 |   0.33%
        4 |  47.93%
        5 |  51.72%
```

The output shows the steady-state probability distribution across system states. With the default configuration (1 server, capacity 5, arrival/service intervals [3,5]), the system converges to mostly full states (4-5 customers).

## Course context

**Course:** Simulation and Analytical Methods  
**Program:** Software Engineering  
**Institution:** PUCRS (Pontifícia Universidade Católica do Rio Grande do Sul)

This project implements a discrete-event simulation to model and analyze queueing systems, comparing simulated results with analytical solutions from queueing theory.