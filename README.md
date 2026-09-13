# Queue Simulator

Discrete-event queue simulator built for the **Simulation and Analytical Methods** course, Software Engineering @ PUCRS.

Simulates single and tandem queueing systems in Kendall's notation **A/B/c/K** (arrival distribution / service distribution / number of servers / system capacity).

## How it works

**Core components:**
- `Simulator`: main simulation engine that orchestrates the event-driven simulation process;
- `Scheduler`: manages event scheduling and retrieval from the priority queue;
- `Queue`: handles customer queue management, state tracking and loss counting;
- `Event` & `EventType`: represent discrete events (`ARRIVAL`, `PASSAGE`, `DEPARTURE`);
- `RandomNumberGenerator`: Linear Congruential Generator (`Xₙ₊₁ = (a·Xₙ + c) mod M`) for reproducible random sequences;
- `Interval`: data structure for representing arrival/service intervals.

**Simulation flow:**
1. `Main.java` initializes the `Simulator` with a list of queues and a `Scheduler`;
2. Events (`ARRIVAL`, `PASSAGE`, `DEPARTURE`) are processed in chronological order via the `Scheduler`'s priority queue;
3. Time spent in each system state (0..K customers) is accumulated per queue, weighted by elapsed time;
4. **Arrival**: if `queue < K`, the customer is accepted; if a server is available, the next event is scheduled (`PASSAGE` if there are more queues ahead, `DEPARTURE` if it's the last); next arrival is always scheduled; otherwise the customer is lost;
5. **Passage**: customer leaves an intermediate queue and attempts to enter the next one; if the next queue is full, the customer is lost and counted in that queue's loss;
6. **Departure**: customer leaves the last queue; if enough customers remain, another `DEPARTURE` is scheduled;
7. Results are collected per queue as state probabilities and loss counts, output after simulation completes.

## Project structure

```
queue-simulator/
├── bin/                             # compiled class files
├── src/
│   ├── Main.java                    # entry point, simulation parameters
│   ├── core/
│   │   ├── Queue.java               # queue management, state tracking, loss counting
│   │   ├── Scheduler.java           # event scheduling (priority queue)
│   │   └── Simulator.java           # main simulation engine
│   ├── model/
│   │   ├── Event.java               # event class (time + type + queueIndex), Comparable
│   │   ├── EventType.java           # enum: ARRIVAL, PASSAGE, DEPARTURE
│   │   └── Interval.java            # interval distribution representation
│   └── util/
│       └── RandomNumberGenerator.java # LCG-based RNG
└── README.md
```

## Configuration

### Single queue

```java
int count = 100000;
Interval arrival = new Interval(3, 5);
Interval departure = new Interval(4, 5);
Queue queue = new Queue(1, 5, arrival, departure);
Scheduler scheduler = new Scheduler();

Simulator simulator = new Simulator(List.of(queue), scheduler);
simulator.simulate(count);
```

### Tandem queues

```java
int count = 100000;
Queue queue1 = new Queue(1, 5, new Interval(3, 5), new Interval(4, 5));
Queue queue2 = new Queue(2, 3, null, new Interval(3, 4));
Scheduler scheduler = new Scheduler();

Simulator simulator = new Simulator(List.of(queue1, queue2), scheduler);
simulator.simulate(count);
```

- **`count`**: Total number of events to process;
- **`arrival`**: Uniform random interval for inter-arrival times (`null` for queues with no external arrivals);
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

State probabilities and loss count per queue:

```
--- Simulation Results ---
Total simulated time: 211760.08

Queue 1 | Losses: 12
Customers | Time (%)
        0 |   0.00%
        1 |   0.00%
        2 |   0.01%
        3 |   0.33%
        4 |  47.93%
        5 |  51.72%

Queue 2 | Losses: 5
Customers | Time (%)
        0 |  12.45%
        1 |  35.21%
        2 |  38.10%
        3 |  14.24%
```

## Course context

**Course:** Simulation and Analytical Methods  
**Program:** Software Engineering  
**Institution:** PUCRS (Pontifícia Universidade Católica do Rio Grande do Sul)

This project implements a discrete-event simulation to model and analyze single and tandem queueing systems, comparing simulated results with analytical solutions from queueing theory.