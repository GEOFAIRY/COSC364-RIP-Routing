import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to parse the config files
 */
public class ConfigIO {

    public Integer routerId = null;
    public ArrayList<Integer> inputPorts = new ArrayList<>();
    public ArrayList<List<String>> outputs = new ArrayList<>();

    
    public ConfigIO(String file) throws IOException {
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
                    }
                }

                // When input-ports are found, save them
                assert readLine != null;
                if (readLine.equals("input-ports:")) {
                    while ((readLine = buffer.readLine()) != null && !readLine.equals("")) {
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
            
            
            for (String str: listOutputs) {
                outputs.add(Arrays.asList(str.split("-")));
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                buffer.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}