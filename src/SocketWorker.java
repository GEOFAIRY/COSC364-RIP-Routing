import java.net.Socket;
import java.time.LocalTime;
import java.io.IOException;
import java.io.InputStream;

public class SocketWorker implements Runnable {

    protected Socket clientSocket = null;

    public SocketWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        LocalTime now = LocalTime.now();
        System.out.println("REQUEST \ttime: " + now.toString() + " \tport: " + clientSocket.getLocalPort());
        InputStream stream;
        try {
            stream = clientSocket.getInputStream();
            byte[] data = new byte[100];
            int count = stream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}