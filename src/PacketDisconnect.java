import java.io.IOException;

/**
 * A packet that represents the opponent disconnecting from the server.
 */
public class PacketDisconnect implements Packet {
    @Override
    public void processClient(ClientConnection c) {
        c.endGame("Opponent Disconnected");
    }

    @Override
    public void processServer(ServerConnection c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
