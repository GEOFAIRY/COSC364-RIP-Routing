import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to parse the config files
 */
public class ConfigIO {

    /**
     * Method to read config files and return them as a list
     * @param file String the config file to read
     * @return List a list containing the Integer routerId an array of inputPorts and an array of outputs
     * @throws IOException for when reading a cfg file has an error
     */
    public static List readConfig(String file) throws IOException {
        //Initialize variables
        Integer routerId = null;
        ArrayList<Integer> inputPorts = new ArrayList<>();
        ArrayList<String> outputs = new ArrayList<>();
        //Open the file
        File f = new File(file);
        BufferedReader buffer = new BufferedReader(new FileReader(f));
        String readLine;

        //Iterate over every line
        while ((readLine = buffer.readLine()) != null) {
            //When router-id is found, save it
            if (readLine.equals("router-id:")) {
                if((readLine = buffer.readLine()) != null) {
                    routerId = Integer.parseInt(readLine);
                }
            }

            //When input-ports are found, save them
            assert readLine != null;
            if (readLine.equals("input-ports:")) {
                while((readLine = buffer.readLine()) != null && !readLine.equals("")) {
                    inputPorts.add(Integer.parseInt(readLine));
                }
            }

            //When output information is found, save it
            assert readLine != null;
            if (readLine.equals("outputs:")){
                while((readLine = buffer.readLine()) != null && !readLine.equals("")) {
                    outputs.add(readLine);
                }
            }
        }

        return Arrays.asList(routerId, inputPorts, outputs);
    }
}