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

    protected EntryTable newTable = null;
    protected DatagramSocket socket = null;

    public SocketWorker(EntryTable newTable, DatagramSocket socket) {
        this.newTable = newTable;
        this.socket = socket;
    }

    public void run() {
        LocalTime now = LocalTime.now();
        System.out.println("REQUEST \ttime: " + now.toString() + " \tport: " + socket.getLocalPort());
        System.out.println(newTable.toString());
    }
}