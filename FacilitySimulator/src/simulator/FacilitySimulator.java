// FacilitySimulator.java

/*
 Name: <Your Name>
 Course: CNT 4714 Summer 2025
 Assignment title: Project 2 – Multi-threaded programming in Java
 Date:   June 15, 2025
 Class:  <Your Class Name>
*/

package simulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main driver: reads the config file, creates conveyors and stations,
 * then uses an ExecutorService FixedThreadPool(10) to run them :contentReference[oaicite:2]{index=2}.
 */
public class FacilitySimulator {
    public static void main(String[] args) throws Exception {
        // 1) Read config file
        if (args.length < 1) {
            System.err.println("Usage: java FacilitySimulator <config-file>");
            System.exit(1);
        }
        List<Integer> nums = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String line;
            while ((line = br.readLine()) != null) {
                nums.add(Integer.parseInt(line.trim()));
            }
        }
        int numStations = nums.get(0);
        int[] workloads = new int[numStations];
        for (int i = 0; i < numStations; i++) {
            workloads[i] = nums.get(i + 1);
        }

        // 2) Create conveyors
        List<Conveyor> conveyors = new ArrayList<>();
        for (int i = 0; i < numStations; i++) {
            conveyors.add(new Conveyor(i));
        }

        // 3) Create thread pool
        ExecutorService exec = Executors.newFixedThreadPool(10);

        // 4) Assign conveyors & submit stations
        for (int i = 0; i < numStations; i++) {
            int inId  = i;
            int outId = (i + numStations - 1) % numStations;
            Conveyor inC  = conveyors.get(inId);
            Conveyor outC = conveyors.get(outId);

            // Log assignments :contentReference[oaicite:3]{index=3}
            System.out.printf("Routing Station S%d: Input conveyor assigned to conveyor number C%d.%n", i, inId);
            System.out.printf("Routing Station S%d: Output conveyor assigned to conveyor number C%d.%n", i, outId);
            System.out.printf("Routing Station S%d Has Total Workload of %d Package Groups.%n", i, workloads[i]);

            // otherStation for synchronization logging is the station sharing 'output' as input
            int otherStation = outId;
            exec.submit(new RoutingStation(i, workloads[i], inC, outC, otherStation));
        }

        // 5) Shutdown
        exec.shutdown();
    }
}
