import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.net.InetAddress;

public class InputSocketRunner extends SocketRunner {

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

    public InputSocketRunner(int port) {
        super(port);
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        super.openServerSocket();
        try {
            Random rand = new Random();
            this.serverSocket.setSoTimeout(10000 + rand.nextInt(50000));
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        while (!isStopped()) {
            byte[] buffer = new byte[600];
            DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length);
            try {
                this.serverSocket.receive(packetReceived);
                //new Thread(new SocketWorker(packetReceived, serverSocket)).start();
            } catch (SocketTimeoutException e) {
                // send response packets
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