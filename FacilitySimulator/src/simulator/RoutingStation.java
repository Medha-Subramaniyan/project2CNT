// RoutingStation.java

/*
 Name: <Your Name>
 Course: CNT 4714 Summer 2025
 Assignment title: Project 2 – Multi-threaded programming in Java
 Date:   June 15, 2025
 Class:  <Your Class Name>
*/

package simulator;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A routing station thread that moves 'workload' groups of packages
 * by locking its input and output conveyors in a fixed order
 * to prevent deadlock :contentReference[oaicite:1]{index=1}.
 */
public class RoutingStation implements Runnable {
    private final int stationId;
    private int remainingWork;
    private final Conveyor input, output;
    private final int otherStation;  // for logging who holds the conveyor

    public RoutingStation(int stationId, int workload,
                          Conveyor input, Conveyor output,
                          int otherStation) {
        this.stationId = stationId;
        this.remainingWork = workload;
        this.input = input;
        this.output = output;
        this.otherStation = otherStation;
    }

    @Override
    public void run() {
        while (remainingWork > 0) {
            // Attempt to acquire both locks
            boolean acquired = false;
            while (!acquired) {
                if (input.tryLock()) {
                    if (output.tryLock()) {
                        // got both
                        acquired = true;
                        logInputLock();
                        logOutputLock();
                    } else {
                        // failed to get output → release input and retry
                        logUnableToLockOutput();
                        input.unlock();
                        sleepRandomIdle();
                    }
                } else {
                    sleepRandomIdle();
                }
            }

            // Simulate moving packages
            logWorking();
            sleepRandomWork();
            remainingWork--;
            logWorkCompleted();

            // Release locks
            output.unlock();
            logUnlockOutput();
            input.unlock();
            logUnlockInput();

            // Idle before next package group
            sleepRandomIdle();
        }

        logGoingOffline();
    }

    private void logInputLock() {
        System.out.printf("Routing Station S%d: Currently holds lock on input conveyor C%d.%n",
                stationId, input.getId());
    }

    private void logOutputLock() {
        System.out.printf("Routing Station S%d: Currently holds lock on output conveyor C%d.%n",
                stationId, output.getId());
    }

    private void logUnlockInput() {
        System.out.printf("Routing Station S%d: Unlocks/releases input conveyor C%d.%n",
                stationId, input.getId());
    }

    private void logUnlockOutput() {
        System.out.printf("Routing Station S%d: Unlocks/releases output conveyor C%d.%n",
                stationId, output.getId());
    }

    private void logUnableToLockOutput() {
        System.out.printf(
                "Routing Station S%d: UNABLE TO LOCK OUTPUT CONVEYOR C%d.   SYNCHRONIZATION ISSUE: "
                        + "Station S%d currently holds the lock on output conveyor C%d – Station S%d releasing lock on input conveyor C%d.%n",
                stationId, output.getId(),
                otherStation, output.getId(),
                stationId, input.getId()
        );
    }

    private void logWorking() {
        System.out.printf("* * Routing Station S%d: * * CURRENTLY HARD AT WORK MOVING PACKAGES. * *%n",
                stationId);
    }

    private void logWorkCompleted() {
        System.out.printf("Routing Station S%d: Package group completed - %d package groups remaining to move.%n",
                stationId, remainingWork);
    }

    private void logGoingOffline() {
        System.out.printf("# # Routing Station S%d: going offline – work completed!   BYE!  # #%n",
                stationId);
    }

    private void sleepRandomIdle() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(50, 101));
        } catch (InterruptedException ignored) { }
    }

    private void sleepRandomWork() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 201));
        } catch (InterruptedException ignored) { }
    }
}
