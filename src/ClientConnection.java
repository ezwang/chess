import java.io.*;
import java.net.*;

/**
 * Handles a game on the client side.
 */
public class ClientConnection implements Runnable {
    private String addr, nick;
    private Thread thread;

    private Game game;
    private GamePanel gui;
    private GameState state;

    private DataOutputStream out;

    private Socket socket;

    public ClientConnection(String addr, String nick, Game game) {
        this.addr = addr;
        this.nick = nick;
        this.game = game;
    }

    /**
     * Sends a packet to the server.
     * @param p The packet to send to the server.
     * @throws IOException
     */
    public void sendPacket(Packet p) {
        if (socket.isConnected()) {
            try {
                out.writeUTF(PacketUtils.encode(p));
                out.flush();
            }
            catch (IOException ex) {
                // print out the error to the console
                // this is probably a network error, ignore the error and hope
                // that it doesn't break the game
                ex.printStackTrace();
            }
        }
    }

    /**
     * Check whether or not we are still connected to the server.
     * @return Whether or not client is connected to server.
     */
    public boolean isConnected() {
        return socket != null && socket.isConnected();
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
        Packet p = new PacketNickname(nick);
        sendPacket(p);
    }

    /**
     * Listen for packets from the server and process them.
     */
    public void run() {
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            while (true) {
                String packet = in.readUTF();
                try {
                    Packet p = PacketUtils.decode(packet);
                    p.processClient(this);
                }
                catch (ClassNotFoundException | ClassCastException ex) {
                    // this should never happen
                    ex.printStackTrace();
                }
            }
        }
        catch (IOException ex) {
            // ignore the error that comes from reading a closed socket
            if (!socket.isConnected()) {
                // print out an error message to the console
                // probably a network error, ignore the invalid packet and hope
                // that it doesn't break the game
                ex.printStackTrace();
            }
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
            socket = new Socket(this.addr, Server.PORT);
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

    public void endGame(String message) {
        try {
            socket.close();
        }
        catch (IOException ex) {
            // ignore if socket fails to close
        }
        gui.endGame(message);
    }

    public GamePanel getGUI() {
        return gui;
    }

    public void goBackToIntro() {
        game.setContent(new IntroPanel(game));
    }

    public void addChat(String message) {
        gui.addChat(message);
    }

    public String getNickname() {
        return nick;
    }

    public void undo() {
        gui.undo();
    }
}
