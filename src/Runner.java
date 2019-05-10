import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Main entry class for program
 */
public class Runner {
    final static int INFINITY = 16;
    static EntryTable entryTable;
    static ConfigIO routerConfig = null;

    /**
     * method to call and organise router configuration before starting server
     * @param args String[] the arguments provided by the user
     */
    private void processConfig(String[] args) {
        String file = null;
        if (args.length > 0 && (args[0].equals("--file") || args[0].equals("-f"))) {
            file = args[1];
        } else {
            System.out.println("File is needed to configure RIP");
            System.exit(0);
        }

        // get the router config from file
        try {
            routerConfig = new ConfigIO(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        entryTable = new EntryTable();
        Entry self = new Entry(routerConfig.routerId, routerConfig.routerId, 0, LocalTime.now());
        entryTable.update(self);
        for (List<String> output : routerConfig.outputs) {
            Entry entry = new Entry(Integer.parseInt(output.get(2)), Integer.parseInt(output.get(2)), Integer.parseInt(output.get(1)), LocalTime.now());
            entryTable.update(entry);
        }
        System.out.println(entryTable.toString());
    }

    /**
     * main method to start program creates multiple threads for each input socket
     * @param args String[] user provided options for execution
     */
    public static void main(String[] args) {
        Runner runner = new Runner();

        // get the file from the launch arguments
        runner.processConfig(args);

        //set variables for later use
        ArrayList<SocketRunner> sockets = new ArrayList<>();
        ArrayList<Integer> inputPorts = routerConfig.inputPorts;
        SocketRunner socket;

        //create sockets and new threads for each socket
        for (int port : inputPorts) {
            if (port == inputPorts.get(0)) {
                socket = new InputSocketRunner(port);
                System.out.println("Port used for data transmission: " + port);
            } else {
                socket = new SocketRunner(port);
            }
            new Thread(socket).start();
            sockets.add(socket);
        }


    }
}
