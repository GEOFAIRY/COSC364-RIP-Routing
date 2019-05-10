import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.Random;
import java.util.Timer;

/**
 * class to handle the single socket that sends out response packets
 */
public class InputSocketRunner extends SocketRunner {

    /**
     * constructor to create a socketRunner
     * @param port int the port to bind a socket to
     */
    InputSocketRunner(int port) {
        super(port);
    }


    /**
     * runnable method to use its own thread.
     * this method is used to block its socket to listen for incoming data
     * and also to send data when a timer runs out
     */
    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        super.openServerSocket();

        //create the timed task (with some randomness to avoid missing a transmission)
        Random rand = new Random();
        Timer UpdateTimer = new Timer();
        SendEntryTable sendEntryTable = new SendEntryTable();
        UpdateTimer.schedule(sendEntryTable, 10000, 10000);

        while (!isStopped()) {
            byte[] buffer = new byte[1000000];
            DatagramPacket packetReceived = new DatagramPacket(buffer, buffer.length);
            try {
                //blocking the socket to get data
                this.serverSocket.receive(packetReceived);
                try {
                    //convert recived data to entry table object and pass onto worker thread
                    ByteArrayInputStream byteArray = new ByteArrayInputStream(buffer);
                    ObjectInputStream inputStream = new ObjectInputStream(byteArray);
                    try {
                        EntryTable table = (EntryTable) inputStream.readObject();
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