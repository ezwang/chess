import java.io.IOException;

public class PacketEnd implements Packet {
    public enum EndResult {
        WHITE,
        BLACK,
        STALEMATE,
        DRAW
    }

    private EndResult state;

    public PacketEnd(EndResult state) {
        this.state = state;
    }

    @Override
    public void processClient(ClientConnection c) {
        switch (state) {
            case WHITE:
                c.endGame("White wins!");
                break;
            case BLACK:
                c.endGame("Black wins!");
                break;
            case STALEMATE:
                c.endGame("Stalemate!");
                break;
            case DRAW:
                c.endGame("Draw!");
        }
    }

    @Override
    public void processServer(ServerConnection c) throws IOException {
        c.endGame();
        c.getOpponent().endGame();
        c.getOpponent().sendPacket(this);
        c.sendPacket(this);
    }
}
