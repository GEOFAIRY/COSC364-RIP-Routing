import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class SocketRunner implements Runnable {

    protected int serverPort;
    protected DatagramSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public SocketRunner(int port) {
        this.serverPort = port;
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isStopped()) {
            byte[] buffer = new byte[600];
            DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length);
            try {
                this.serverSocket.receive(packetReceived);
                ByteArrayInputStream byteArray = new ByteArrayInputStream(buffer);
                //byte[] header = Arrays.copyOfRange(buffer, 0, 4);
                
                ObjectInputStream inputStream = new ObjectInputStream(byteArray);
                try {
                    EntryTable table = (EntryTable) inputStream.readObject();
                    System.out.println(table.toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            //new Thread(new SocketWorker(packetReceived, serverSocket)).start();
        }
        System.out.println("Server Stopped.");
    }

    protected synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        this.serverSocket.close();
    }

    protected void openServerSocket() {
        try {
            this.serverSocket = new DatagramSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }

}