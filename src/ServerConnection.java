import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.*;
import java.io.*;

/**
 * Handles a game on the server side.
 */
public class ServerConnection implements Runnable {
    private Server server;
    private Socket socket;

    private ServerConnection opponent;
    private DataOutputStream out;

    private String nickname;
    private Gson gson;

    public ServerConnection(Server server, Socket sock) throws IOException {
        this.server = server;
        this.socket = sock;

        gson = new GsonBuilder().create();

        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            String type = in.readUTF();
            String packet = in.readUTF();
            try {
                Packet p = (Packet)gson.fromJson(packet, Class.forName(type));
                p.processServer(this);
            }
            catch (ClassNotFoundException ex) {
                // TODO; handle this
                ex.printStackTrace();
            }
        }
        catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }

    public void sendPacket(Packet p) throws IOException {
        out.writeUTF(p.getClass().getName());
        out.writeUTF(gson.toJson(p));
        out.flush();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Server getServer() {
        return server;
    }

    public void setOpponent(ServerConnection opponent) {
        this.opponent = opponent;
    }

    public String getNickname() {
        return nickname;
    }
}
