# Package Management Facility Simulator

A Java-based concurrent simulation system that models a package management facility with multiple routing stations and conveyors. This project demonstrates multi-threading concepts, synchronization, and deadlock prevention in a realistic industrial simulation.

## Overview

This simulator models a circular package management facility where:
- Multiple routing stations process packages concurrently
- Each station has input and output conveyors
- Stations must acquire locks on both conveyors to move packages
- The system prevents deadlocks using ordered locking

## Project Structure

```
FacilitySimulator/
├── src/simulator/
│   ├── FacilitySimulator.java    # Main simulation controller
│   ├── RoutingStation.java       # Worker thread implementation
│   └── Conveyor.java             # Shared resource with locking
├── configs/
│   ├── config.txt                # Sample configuration file
│   ├── config1.txt               # Alternative configuration
│   └── configSU25.txt            # Additional configuration
└── simulation_output.txt         # Output from simulation runs
```

## Features

- **Concurrent Processing**: Multiple routing stations work simultaneously
- **Deadlock Prevention**: Uses ordered locking to prevent deadlocks
- **Configurable Workloads**: Each station can have different package counts
- **Realistic Simulation**: Includes random delays and detailed logging
- **Thread Safety**: Proper synchronization using ReentrantLock

## Configuration

Configuration files specify:
1. Number of routing stations
2. Workload (package groups) for each station

Example `config.txt`:
```
3    # Number of stations
2    # Workload for station 0
3    # Workload for station 1
4    # Workload for station 2
```

## How to Run

1. Compile the Java files:
   ```bash
   javac FacilitySimulator/src/simulator/*.java
   ```

2. Run the simulation:
   ```bash
   java -cp FacilitySimulator/src simulator.FacilitySimulator FacilitySimulator/configs/config.txt
   ```

## Key Components

### FacilitySimulator
- Main class that initializes the simulation
- Reads configuration files
- Creates conveyors and routing stations
- Manages the thread pool

### RoutingStation
- Implements `Runnable` for concurrent execution
- Handles package movement between conveyors
- Uses ordered locking to prevent deadlocks
- Provides detailed logging of operations

### Conveyor
- Shared resource with thread-safe locking
- Uses `ReentrantLock` for synchronization
- Provides `tryLock()` for non-blocking lock attempts

## Synchronization Strategy

The simulator prevents deadlocks by:
1. **Ordered Locking**: Always acquiring locks in ascending ID order
2. **Non-blocking Attempts**: Using `tryLock()` instead of `lock()`
3. **Backoff Strategy**: Releasing locks and retrying when conflicts occur
4. **Random Delays**: Adding realistic timing between operations

## Course Information

- **Course**: CNT 4714 Summer 2025
- **Assignment**: Project 2
- **Student**: Medha Subramaniyan
- **Date**: June 15, 2025

## Output

The simulation provides detailed output including:
- Station initialization and configuration
- Lock acquisition and release events
- Package movement operations
- Workload completion status
- Synchronization conflict handling

## Requirements

- Java 8 or higher
- No external dependencies required