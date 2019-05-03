import java.util.TimerTask;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class SendEntryTable extends TimerTask {

    public DatagramPacket createPacket(List<String> output) {
        try {
            int command = 2; // command
            int version = 2; // version (always 2)
            int zero = 0; // zero bytes
            EntryTable tableToSend = Runner.entryTable.duplicateTable();
            int routerId = Runner.routerConfig.routerId;

            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(tableToSend);
            objectOutputStream.flush();
            byte[] buffer = arrayOutputStream.toByteArray();

            //todo add header to packet
            //byte[] header = { (byte) command, (byte) version, (byte) routerId, (byte) zero };
            //System.arraycopy(header, 0, buffer, 0, header.length);

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(),
                    Integer.parseInt(output.get(0)));
      
            return packet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	// Add your task here
	public void run() {
		for (List<String> output : Runner.routerConfig.outputs) {
            DatagramPacket packet = createPacket(output);
            try {
                System.out.println("\nSending a timed response\n");
                DatagramSocket outputSocket = new DatagramSocket();
                outputSocket.send(packet);
                outputSocket.close();
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
	}
}