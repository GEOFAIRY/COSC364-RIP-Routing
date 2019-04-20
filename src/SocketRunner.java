import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketRunner implements Runnable {

    private Socket clientSocket = null;

    public SocketRunner(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                InputStream input  = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();
                long time = System.currentTimeMillis();

                //todo >>do something on socket input <<

                output.close();
                input.close();
                System.out.println("Request processed: " + time);
            } catch (IOException e) {
                //report exception somewhere.
                e.printStackTrace();
            }
        }
    }
