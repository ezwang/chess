import java.io.IOException;

public class PacketEnd implements Packet {
    private boolean winner;
    private String message;

    public PacketEnd(boolean winner) {
        this.winner = winner;
    }

    public PacketEnd(String message) {
        this.message = message;
    }

    @Override
    public void processClient(ClientConnection c) {
        if (message != null) {
            c.endGame(message);
        }
        else {
            c.endGame((winner ? "White" : "Black") + " Wins!");
        }
    }

    @Override
    public void processServer(ServerConnection c) throws IOException {
        c.sendPacket(this);
        c.getOpponent().sendPacket(this);
    }
}
