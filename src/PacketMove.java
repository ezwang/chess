import java.io.IOException;

public class PacketMove implements Packet {
    private Location old, now;

    public PacketMove(Location old, Location now) {
        this.old = old;
        this.now = now;
    }

    @Override
    public void processClient(ClientConnection c) throws UnsupportedOperationException {
        GameState state = c.getGameState();
        state.togglePlayerTurn();
        Piece p = state.getPiece(old.getX(), old.getY());
        state.setPiece(old.getX(), old.getY(), null);
        state.setPiece(now.getX(), now.getY(), p);
        p.setNewLocation(now);
        c.update();
    }

    @Override
    public void processServer(ServerConnection c) throws IOException {
        c.sendPacket(this);
        c.getOpponent().sendPacket(this);
    }
}
