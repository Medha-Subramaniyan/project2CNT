/*
 Name:  Medha Subramanyain
 Course: CNT 4714 Summer 2025
 Assignment title: Project 2
 Date:   June 15, 2025
 Class:  RoutingStation
*/

package simulator;

import java.util.concurrent.ThreadLocalRandom;

public class RoutingStation implements Runnable
{
    private final int stationId, otherStation;
    private final Conveyor input, output;
    private int remainingWork;

    public RoutingStation(int stationId, int workload,
                          Conveyor input, Conveyor output,
                          int otherStation)
    {
        this.stationId     = stationId;
        this.remainingWork = workload;
        this.input         = input;
        this.output        = output;
        this.otherStation  = otherStation;
    }

    @Override
    public void run()
    {
        while (remainingWork > 0)
        {
            System.out.printf("Routing Station S%d: Entering Lock Acquisition Phase.%n",
                    stationId);

            boolean locked = false;

            //increasing order
            while (!locked)
            {
                // decide lock order
                Conveyor first  = (input.getId() < output.getId()) ? input  : output;
                Conveyor second = (first == input)                  ? output : input;

                if (first.tryLock())
                {
                    // report which conveyor we just locked
                    if (first == input)
                        System.out.printf("Routing Station S%d: Currently holds lock on input conveyor C%d.%n",
                                stationId, first.getId());
                    else
                        System.out.printf("Routing Station S%d: Currently holds lock on output conveyor C%d.%n",
                                stationId, first.getId());

                    if (second.tryLock())
                    {
                        if (second == input)
                            System.out.printf("Routing Station S%d: Currently holds lock on input conveyor C%d.%n",
                                    stationId, second.getId());
                        else
                            System.out.printf("Routing Station S%d: Currently holds lock on output conveyor C%d.%n",
                                    stationId, second.getId());

                        System.out.printf("* * Routing Station S%d: * * CURRENTLY HARD AT WORK MOVING PACKAGES. * *%n",
                                stationId);
                        locked = true;          // both locks acquired
                    }
                    else
                    {
                        // could not get second lock – back off
                        System.out.printf(
                                "Routing Station S%d: UNABLE TO LOCK %s CONVEYOR C%d.   SYNCHRONIZATION ISSUE: "
                                        + "Station S%d currently holds the lock on that conveyor – "
                                        + "Station S%d releasing lock on conveyor C%d.%n",
                                stationId,
                                (second == output ? "OUTPUT" : "INPUT"),
                                second.getId(),
                                otherStation,
                                stationId,
                                first.getId());

                        first.unlock();         // release and retry
                        sleepRandom(50, 100);
                    }
                }
                else
                {
                    sleepRandom(50, 100);      // couldn’t lock first wait and retry
                }
            }

            //move
            System.out.printf("Routing Station S%d: Currently moving packages into station on input conveyor C%d.%n",
                    stationId, input.getId());
            System.out.printf("Routing Station S%d: Currently moving packages out of station on output conveyor C%d.%n",
                    stationId, output.getId());

            sleepRandom(100, 200);

            remainingWork--;
            System.out.printf("Routing Station S%d: Package group completed - %d package groups remaining to move.%n",
                    stationId, remainingWork);

            //release
            System.out.printf("Routing Station S%d: Unlocks/releases input conveyor C%d.%n",
                    stationId, input.getId());
            input.unlock();

            System.out.printf("Routing Station S%d: Unlocks/releases output conveyor C%d.%n",
                    stationId, output.getId());
            output.unlock();

            sleepRandom(50, 100);              // idle before next group
        }

        System.out.printf("# # Routing Station S%d: going offline – work completed!   BYE!  # #%n",
                stationId);
    }

    private void sleepRandom(int lowMs, int highMs)
    {
        try
        {
            Thread.sleep(ThreadLocalRandom.current().nextInt(lowMs, highMs + 1));
        }
        catch (InterruptedException ignored) { }
    }
}
