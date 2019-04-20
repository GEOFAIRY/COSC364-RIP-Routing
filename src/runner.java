import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for checking code
 */
public class runner {

    public static void main(String[] args) {
        //get the file from the launch arguments
        String file = null;
        ArrayList<ServerSocket> inputSockets = new ArrayList<>();
        if (args.length > 0 && (args[0].equals("--file") || args[0].equals("-f"))) {
            file = args[1];
        } else {
            System.out.println("File is needed to configure RIP");
            System.exit(0);
        }

        //get the router config from file and output
        List routerConfig = null;
        try {
            routerConfig = ConfigIO.readConfig(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int port: (ArrayList<Integer>)routerConfig.get(1)) {
            try {
                ServerSocket inputSocket = new ServerSocket(port);
                inputSockets.add(inputSocket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
