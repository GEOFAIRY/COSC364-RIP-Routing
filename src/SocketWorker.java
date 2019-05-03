import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class SocketWorker implements Runnable {

    protected DatagramPacket packet = null;
    protected DatagramSocket socket = null;

    public SocketWorker(DatagramPacket packet, DatagramSocket socket) {
        this.packet = packet;
        this.socket = socket;
    }

    public void run() {
        LocalTime now = LocalTime.now();
        System.out.println("REQUEST \ttime: " + now.toString() + " \tport: " + socket.getLocalPort());
        byte[] buffer = new byte[600];
        InputStream stream;
        try {
            ByteArrayInputStream byteArray = new ByteArrayInputStream(buffer);
            //byte[] header = Arrays.copyOfRange(buffer, 0, 4);
            
            ObjectInputStream inputStream = new ObjectInputStream(byteArray);
            try {
                EntryTable table = (EntryTable) inputStream.readObject();
                System.out.println(table.toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("got");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}