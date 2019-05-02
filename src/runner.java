import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for checking code
 */
public class Runner {
    public final static String LOCALHOST = "127.0.0.1";
	public final static int BUFSIZE = 1023;
	public final static int TIMER = 6;
	public final static int TIMEROUT = TIMER / 6;
	public final static int ENTRY_TIMEOUT = TIMER * 6;
	public final static int GARBAGE = TIMER * 6;
    public final static int INFINITY = 30;
    public EntryTable entryTable = new EntryTable();
    public static ConfigIO routerConfig = null;

    public void processConfig(Runner runner, String[] args) {
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
    }

    public static void main(String[] args) {
        Runner runner = new Runner();

        // get the file from the launch arguments
        runner.processConfig(runner, args);

        ArrayList<SocketRunner> sockets = new ArrayList<>();
        ArrayList<Integer> inputPorts = routerConfig.inputPorts;
        SocketRunner socket;

        for (int port : inputPorts) {
            if (port == inputPorts.get(0)) {
                socket = new InputSocketRunner(port);
            } else {
                socket = new SocketRunner(port);
            }
            new Thread(socket).start();
            sockets.add(socket);
        }

    }
}
