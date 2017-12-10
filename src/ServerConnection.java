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

    private boolean draw;
    private boolean undo;
    private boolean gameEnded;

    public ServerConnection(Server server, Socket sock) throws IOException {
        this.server = server;
        this.socket = sock;

        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    /**
     * Listen for incoming packets and process them as they arrive.
     */
    public void run() {
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            while (true) {
                String packet;
                try {
                    packet = in.readUTF();
                }
                catch (EOFException | SocketException ex) {
                    // client has disconnected
                    if (opponent != null && !gameEnded) {
                        opponent.sendPacket(new PacketDisconnect());
                    }
                    else {
                        server.removeFromQueue(this);
                    }
                    break;
                }
                try {
                    Packet p = PacketUtils.decode(packet);
                    p.processServer(this);
                } catch (ClassNotFoundException | ClassCastException ex) {
                    // this should never happen
                    ex.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            // print the exception to the console
            // this is probably a network error, ignore the invalid packet and
            // hope that it doesn't break the game
            e.printStackTrace();
        }
    }

    /**
     * Send a packet to the client.
     * @param p The packet that should be sent.
     * @throws IOException
     */
    public void sendPacket(Packet p) throws IOException {
        out.writeUTF(PacketUtils.encode(p));
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

    public ServerConnection getOpponent() {
        return opponent;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != ServerConnection.class) {
            return false;
        }
        ServerConnection c = (ServerConnection)o;
        return c.socket == socket;
    }

    public boolean getDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public void endGame() {
        gameEnded = true;
    }

    public boolean getUndo() {
        return undo;
    }

    public void setUndo(boolean undo) {
        this.undo = undo;
    }
}
