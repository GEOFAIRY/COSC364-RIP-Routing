import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;

public class InputSocketRunner extends SocketRunner {

    public byte[] createPacket() {
        int command = 2; // command
        int version = 2; // version (always 2)
        int zero = 0; // zero bytes
        int addressFamily = 2; //2 meaning ipv4
        byte[] ip = Runner.LOCALHOST.getBytes();

        byte[] packetHeader = { (byte) command, (byte) version, (byte) zero, (byte) zero };
        byte[] packet = null;
        System.arraycopy(packetHeader, 0, packet, 0, packetHeader.length);
        for (List<String> outputs : Runner.routerConfig.outputs) {
            byte[] tempPayload = {};
        }

        return packet;
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
            this.serverSocket.setSoTimeout(10000);
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                new Thread(new SocketWorker(clientSocket)).start();
            } catch (SocketTimeoutException e) {
                createPacket();
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