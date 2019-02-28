import java.io.IOException;
import java.util.List;

/**
 * Test class for checking code
 */
public class runner {

    public static void main(String[] args) {
        //get the file from the launch arguments
        String file = null;
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
        System.out.println(routerConfig.get(0));
        System.out.println(routerConfig.get(1));
        System.out.println(routerConfig.get(2));
    }
}
