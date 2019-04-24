import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for checking code
 */
public class Runner {
    private List<Object> routerConfig = null;

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
            runner.routerConfig = ConfigIO.readConfig(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Runner runner = new Runner();

        // get the file from the launch arguments
        runner.processConfig(runner, args);

        ArrayList<SocketRunner> sockets = new ArrayList<>();
        ArrayList<Integer> inputPorts = (ArrayList<Integer>) runner.routerConfig.get(1);
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
