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
7. The simulation ends when the 100.000th random number is consumed;
8. Results are collected per queue as accumulated times, state probabilities and loss counts, output after simulation completes.

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

Queue queue = new Queue(1, 5, new Interval(3, 5), new Interval(4, 5));
Scheduler scheduler = new Scheduler();

Simulator simulator = new Simulator(List.of(queue), scheduler);
simulator.simulate(count, 3.0);
```

### Tandem queues

```java
int count = 100000;

Queue queue1 = new Queue(2, 3, new Interval(1, 5), new Interval(4, 5));
Queue queue2 = new Queue(1, 5, null, new Interval(1, 3));
Scheduler scheduler = new Scheduler();

Simulator simulator = new Simulator(List.of(queue1, queue2), scheduler);
simulator.simulate(count, 2.5);
```

**Parameters:**
- **`count`**: Total number of random numbers to consume before stopping the simulation;
- **`timeFirstEvent`**: Time of the first customer arrival (e.g. `2.5`);
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

Accumulated time and state probabilities per queue:

```
--- Simulation Results ---
Total simulated time: 100674.71

Queue 1 | Losses: 386
Customers |       Time | Time (%)
        0 |    1127.03 |   1.12%
        1 |   49676.88 |  49.38%
        2 |   43456.21 |  43.19%
        3 |    6341.59 |   6.30%

Queue 2 | Losses: 0
Customers |       Time | Time (%)
        0 |   34085.96 |  33.89%
        1 |   60279.40 |  59.93%
        2 |    6207.64 |   6.17%
        3 |      10.06 |   0.01%
        4 |       0.00 |   0.00%
        5 |       0.00 |   0.00%
```

## Course context

**Course:** Simulation and Analytical Methods  
**Program:** Software Engineering  
**Institution:** PUCRS (Pontifícia Universidade Católica do Rio Grande do Sul)

This project implements a discrete-event simulation to model and analyze single and tandem queueing systems, comparing simulated results with analytical solutions from queueing theory.