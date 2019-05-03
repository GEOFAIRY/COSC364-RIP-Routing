import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to parse the config files
 */
public class ConfigIO {

    Integer routerId = null;
    ArrayList<Integer> inputPorts = new ArrayList<>();
    public ArrayList<List<String>> outputs = new ArrayList<>();

    /**
     * method to get and check all the config files attributes. saves them to lists in the runner class
     * @param file String the file to access
     * @throws IOException exception to handle file opening errors
     */
    ConfigIO(String file) throws IOException {
        // Initialize variables
        ArrayList<String> listOutputs = new ArrayList<>();
        // Open the file
        File f = new File(file);
        BufferedReader buffer = new BufferedReader(new FileReader(f));
        String readLine;
        try {
            // Iterate over every line
            while ((readLine = buffer.readLine()) != null) {
                // When router-id is found, save it
                if (readLine.equals("router-id:")) {
                    if ((readLine = buffer.readLine()) != null) {
                        routerId = Integer.parseInt(readLine);
                        if (!(routerId >= 1 && routerId <= 64000)){
                            throw new RuntimeException("Router id must be between 1 and 64000 inclusive id: "+routerId);
                        }
                    }
                }

                // When input-ports are found, save them
                assert readLine != null;
                if (readLine.equals("input-ports:")) {
                    while ((readLine = buffer.readLine()) != null && !readLine.equals("")) {
                        if (!(Integer.parseInt(readLine) >= 1024 && Integer.parseInt(readLine) <= 64000)){
                            throw new RuntimeException("Input ports must be between 1024 and 64000 inclusive port: " + Integer.parseInt(readLine));
                        }
                        inputPorts.add(Integer.parseInt(readLine));
                    }
                }

                // When output information is found, save it
                assert readLine != null;
                if (readLine.equals("outputs:")) {
                    while ((readLine = buffer.readLine()) != null && !readLine.equals("")) {
                        listOutputs.add(readLine);
                    }
                }
            }

            //split the outputs lines up into a list
            for (String str : listOutputs) {
                outputs.add(Arrays.asList(str.split("-")));
            }

            //boundary checking
            for (List<String> output: outputs){
                for (Integer i: inputPorts) {
                    if (Integer.parseInt(output.get(0)) == i){
                        throw new RuntimeException("Input and Output ports must be different port: " + i);
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}