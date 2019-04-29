import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class InputSocketRunner extends SocketRunner {

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