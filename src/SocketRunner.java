import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * class to handle sockets that receive data only
 */
public class SocketRunner implements Runnable {

    private int serverPort;
    DatagramSocket serverSocket = null;
    private boolean isStopped = false;
    Thread runningThread = null;

    /**
     * constructor to create a socketRunner
     * @param port int the port to bind a socket to
     */
    SocketRunner(int port) {
        this.serverPort = port;
    }

    /**
     * runnable method to use its own thread.
     * this method is used to block its socket to listen for incoming data
     */
    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
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

    synchronized boolean isStopped() {
        return this.isStopped;
    }

    /**
     * method to open the socket for this thread to handle
     */
    void openServerSocket() {
        try {
            this.serverSocket = new DatagramSocket(this.serverPort);
        } catch (java.net.BindException e) {
            throw new RuntimeException("Input ports must be unique and not already bound port: " + serverPort, e);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }

}