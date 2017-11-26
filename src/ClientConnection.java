import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;

/**
 * Handles a game on the client side.
 */
public class ClientConnection implements Runnable {
    private String addr, nick;
    private Thread thread;
    private Gson gson;

    private Game game;
    private GamePanel gui;

    private DataOutputStream out;

    private Socket socket;

    public ClientConnection(String addr, String nick, Game game) {
        this.addr = addr;
        this.nick = nick;
        this.game = game;

        gson = new GsonBuilder().create();
    }

    public void sendPacket(Packet p) throws IOException {
        out.writeUTF(p.getClass().getName());
        out.writeUTF(gson.toJson(p));
        out.flush();
    }

    public void startGame() {
        gui = new GamePanel();
        game.setContent(gui);
    }

    public void sendNickname() {
        try {
            Packet p = new PacketNickname(nick);
            sendPacket(p);
        }
        catch (IOException ex) {
            // TODO: handle this
            ex.printStackTrace();
        }
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            while (true) {
                String type = in.readUTF();
                String packet = in.readUTF();
                try {
                    Packet p = (Packet)gson.fromJson(packet, Class.forName(type));
                    p.processClient(this);
                }
                catch (ClassNotFoundException ex) {
                    // TODO; handle this
                    ex.printStackTrace();
                }
            }
        }
        catch (IOException ex) {
            // TODO
            ex.printStackTrace();
        }
    }

    /**
     * Check whether an address is valid or not.
     * @return If this address is valid or not.
     */
    public boolean checkValidHost() {
        try {
            InetAddress.getByName(addr);
            return true;
        }
        catch (UnknownHostException ex) {
            return false;
        }
    }

    /**
     * Connect to the server.
     * @return Returns true if connection was successful, false otherwise.
     */
    public boolean connect() {
        try {
            socket = new Socket(this.addr, 1337);
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            thread = new Thread(this);
            thread.start();
            return true;
        }
        catch (UnknownHostException ex) {
            ex.printStackTrace();
            return false;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
