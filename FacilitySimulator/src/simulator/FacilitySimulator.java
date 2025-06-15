/*
 Name: Medha Subramaniyan
 Course: CNT 4714 Summer 2025
 Assignment title: Project 2
 Date:   June 15, 2025
 Class:  FacilitySimulator
*/

package simulator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FacilitySimulator
{
    public static void main(String[] args) throws Exception
    {
        if (args.length < 1)
        {
            System.err.println("Usage: java FacilitySimulator <config-file>");
            System.exit(1);
        }

        // read config file
        List<Integer> nums = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(args[0])))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                nums.add(Integer.parseInt(line.trim()));
            }
        }
        int numStations = nums.get(0);
        int[] workloads = new int[numStations];
        for (int i = 0; i < numStations; i++)
        {
            workloads[i] = nums.get(i + 1);
        }

        // sim header
        System.out.println("Summer 2025 – Project 2 – Package Management Facility Simulator");
        System.out.println();
        System.out.println("********** PACKAGE MANAGEMENT FACILITY SIMULATION BEGINS **********");
        System.out.println();
        System.out.println("The parameters for this simulation run are:");
        for (int i = 0; i < numStations; i++)
        {
            // worklaod set
            System.out.printf("Routing Station S%d Has Total Workload Of %d Package Groups.%n",
                    i, workloads[i]);
        }
        System.out.println();

        // converyors and thread pool
        List<Conveyor> conveyors = new ArrayList<>();
        for (int i = 0; i < numStations; i++)
        {
            conveyors.add(new Conveyor(i));
        }
        ExecutorService exec = Executors.newFixedThreadPool(10);

        // launch threads
        for (int i = 0; i < numStations; i++)
        {
            int inId  = i;
            int outId = (i + numStations - 1) % numStations;

            // online and asisgnments
            System.out.printf("%% %% ROUTING STATION %d: Coming Online – Initializing Conveyors %% %% %n%n", i);
            System.out.printf("Routing Station S%d: Input conveyor assigned to conveyor number C%d.%n", i, inId);
            System.out.printf("Routing Station S%d: Output conveyor assigned to conveyor number C%d.%n", i, outId);
            System.out.printf("Routing Station S%d: Workload set. Station S%d has a total of %d package groups to move.%n%n",
                    i, i, workloads[i]);
            System.out.printf("Routing Station S%d: Now Online And Ready To Move Packages%n%n", i);

            exec.submit(new RoutingStation(i,
                    workloads[i],
                    conveyors.get(inId),
                    conveyors.get(outId),
                    outId));
        }

        // completion and banner
        exec.shutdown();
        exec.awaitTermination(5, TimeUnit.MINUTES);
        System.out.println();
        System.out.println("********** ALL WORKLOADS COMPLETE * * * PACKAGE MANAGEMENT FACILITY SIMULATION TERMINATES **********");
        System.out.println();
        System.out.println("* * * * * SIMULATION ENDS * * * * *");
    }
}
