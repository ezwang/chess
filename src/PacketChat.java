import java.io.IOException;

public class PacketChat implements Packet {
    private String message;

    public PacketChat(String message) {
        this.message = message;
    }

    @Override
    public void processClient(ClientConnection c) throws IOException {
        c.addChat(message);
    }

    @Override
    public void processServer(ServerConnection c) throws IOException {
        c.sendPacket(this);
        c.getOpponent().sendPacket(this);
    }
}
