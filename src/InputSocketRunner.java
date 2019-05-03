import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.Random;
import java.util.Timer;

public class InputSocketRunner extends SocketRunner {

    public InputSocketRunner(int port) {
        super(port);
    }


    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        super.openServerSocket();
        while (!isStopped()) {
            byte[] buffer = new byte[600];
            DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length);

            Random rand = new Random();
            Timer UpdateTimer = new Timer();
            SendEntryTable sendEntryTable = new SendEntryTable();
            UpdateTimer.schedule(sendEntryTable, 10000 + rand.nextInt(50000), 10000 + rand.nextInt(50000));

            try {
                this.serverSocket.receive(packetReceived);
                byte[] tempBuffer = new byte[600];
                InputStream stream;
                try {
                    ByteArrayInputStream byteArray = new ByteArrayInputStream(buffer);
                    
                    ObjectInputStream inputStream = new ObjectInputStream(byteArray);
                    try {
                        EntryTable table = (EntryTable) inputStream.readObject();
                        System.out.println(table.toString());
                        new Thread(new SocketWorker(table, serverSocket)).start();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
        }
        System.out.println("Server Stopped.");
    }
}