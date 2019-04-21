import java.net.Socket;
import java.time.LocalTime;

public class SocketWorker implements Runnable {

    protected Socket clientSocket = null;

    public SocketWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        LocalTime now = LocalTime.now();
        System.out.println("REQUEST \ttime: " + now.toString() + " \tport: " + clientSocket.getLocalPort());
        
    }
}