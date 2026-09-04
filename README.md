# Queue Simulator

Discrete-event queue simulator built for the **Simulation and Analytical Methods** course, Software Engineering @ PUCRS.

Simulates queueing systems in Kendall's notation **A/B/c/K** (arrival distribution / service distribution / number of servers / system capacity), and compares simulated results with analytical queueing theory.

## How it works

- Events (`ARRIVAL`, `DEPARTURE`) are processed from a `PriorityQueue<Event>`, always picking the earliest one next.
- Time spent in each system state (0..K customers) is accumulated in a `HashMap<Integer, Double>`, weighted by elapsed time — used to compute state probabilities.
- **Arrival**: if `queue < K`, the customer is accepted; if a server was free (`queue <= c` after incrementing), a `DEPARTURE` is scheduled. A new arrival is always scheduled next.
- **Departure**: customer leaves; if enough customers remain to keep all `c` servers busy (`queue >= c` after decrementing), another `DEPARTURE` is scheduled.
- Random values come from `RandomNumberGenerator`, a Linear Congruential Generator (`Xₙ₊₁ = (a·Xₙ + c) mod M`), normalized to `[0, 1)` and mapped to `[a, b]` via `rnd(a, b)`.

## Project structure

```
queue-simulator/
├── src/
│   ├── Main.java                    # entry point, event loop, stats
│   ├── Event.java                   # time + type, Comparable
│   ├── EventType.java               # ARRIVAL, DEPARTURE
│   └── RandomNumberGenerator.java   # LCG
└── README.md
```

No packages/build tool yet — flat files, compiled directly.

## Run

```bash
javac -d bin src/*.java
java -cp bin Main
```

## Output

Percentage of time spent in each state (0..K customers), i.e. simulated state probabilities:

```
--- Simulation Results ---
Total simulated time: 245123.87
Customers | Time (%)
        0 |  12.34%
        1 |  28.91%
        2 |  35.20%
        3 |  15.67%
        4 |   6.12%
        5 |   1.76%

Sum check: 100.00% (should be ~100%)
```

## Course context

Simulation and Analytical Methods — Software Engineering — PUCRS