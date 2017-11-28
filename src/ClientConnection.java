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
    private GameState state;

    private DataOutputStream out;

    private Socket socket;

    public ClientConnection(String addr, String nick, Game game) {
        this.addr = addr;
        this.nick = nick;
        this.game = game;

        gson = new GsonBuilder().create();
    }

    /**
     * Sends a packet to the server.
     * @param p The packet to send to the server.
     * @throws IOException
     */
    public void sendPacket(Packet p) throws IOException {
        out.writeUTF(p.getClass().getName());
        out.writeUTF(gson.toJson(p));
        out.flush();
    }

    public GameState getGameState() {
        return state;
    }

    /**
     * Move away from the introduction screen and start the game.
     * @param oppNick The nickname of the opponent.
     * @param isWhite Whether or not the current player is white.
     */
    public void startGame(String oppNick, boolean isWhite) {
        state = new GameState(nick, oppNick, isWhite);
        gui = new GamePanel(state, this);
        game.setContent(gui);
    }

    /**
     * Send player nickname to the server.
     */
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
                    // TODO: handle this
                    ex.printStackTrace();
                }
            }
        }
        catch (IOException ex) {
            // TODO: handle exception
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

    public void update() {
        gui.update();
    }

    public void endGame() {
        gui.endGame();
    }

    public GamePanel getGUI() {
        return gui;
    }
}
