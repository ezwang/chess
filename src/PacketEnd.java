import java.io.IOException;

public class PacketEnd implements Packet {
    private boolean winner;

    public PacketEnd(boolean winner) {
        this.winner = winner;
    }

    @Override
    public void processClient(ClientConnection c) {
        c.endGame();
    }

    @Override
    public void processServer(ServerConnection c) throws IOException {
        c.sendPacket(this);
        c.getOpponent().sendPacket(this);
    }
}
