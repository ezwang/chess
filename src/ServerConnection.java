import java.net.*;
import java.io.*;

public class ServerConnection implements Runnable {
    private Server server;
    private Socket socket;

    public ServerConnection(Server server, Socket sock) {
        this.server = server;
        this.socket = sock;
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            while (true) {
                String line = in.readUTF();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
