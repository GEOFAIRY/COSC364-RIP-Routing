import java.util.TimerTask;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalTime;
import java.util.List;

/**
 * class to send the entry table to other directly connected routers
 */
public class SendEntryTable extends TimerTask {

    /**
     * method to create a packet containing the current entry table
     * @param output the current output socket details
     * @return DatagramPacket a packet to be sent to a specific output socket
     */
    private DatagramPacket createPacket(List<String> output) {
        try {
            //remove all garbage entries
            for (Entry entry : Runner.entryTable.getEntries()) {
                if ((entry.timer() > 60) && (entry.getDest() != Runner.routerConfig.routerId)) {
                    Runner.entryTable.removeEntry(entry.getDest());
                }
            }
            //duplicate the table so infinity can be inserted without effecting the real table
            EntryTable tableToSend = Runner.entryTable.duplicateTable();
            for (Entry entry : tableToSend.getEntries()) {
                entry.setMetric(entry.getMetric() + Integer.parseInt(output.get(1)));
                if (entry.getNextHop() == Integer.parseInt(output.get(2))) {
                    entry.setMetric(Runner.INFINITY);
                }
            }

            //create the packet from the entry table object
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(tableToSend);
            objectOutputStream.flush();
            byte[] buffer = arrayOutputStream.toByteArray();

            return new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(),
                    Integer.parseInt(output.get(0)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * method to make this class runnable in its own thread.
     * when the thread is started the class will send packets to all output ports
     */
    public void run() {
        for (List<String> output : Runner.routerConfig.outputs) {
            DatagramPacket packet = createPacket(output);
            try {
                System.out.println("RESPONSE\tRouter id:" + output.get(2) + "\t" + LocalTime.now());
                DatagramSocket outputSocket = new DatagramSocket();
                assert packet != null;
                outputSocket.send(packet);
                outputSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}