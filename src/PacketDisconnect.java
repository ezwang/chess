import java.io.IOException;

public class PacketDisconnect implements Packet {
    @Override
    public void processClient(ClientConnection c) throws UnsupportedOperationException, IOException {
        c.endGame();
    }

    @Override
    public void processServer(ServerConnection c) throws UnsupportedOperationException, IOException {
        throw new UnsupportedOperationException();
    }
}
