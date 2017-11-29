import java.io.IOException;

/**
 * A packet that represents a player moving a piece
 * on the chessboard.
 */
public class PacketMove implements Packet {
    private Location old, now;
    private String transform;

    /**
     * Create a packet to move a piece.
     * @param old The old position of the piece.
     * @param now The new position of the piece.
     */
    public PacketMove(Location old, Location now) {
        this.old = old;
        this.now = now;
        this.transform = null;
    }

    /**
     * This constructor is used for pawn promotions, which
     * have an extra piece of information (the new piece
     * the pawn will turn into).
     */
    public PacketMove(Location old, Location now, String p) {
        this.old = old;
        this.now = now;
        this.transform = p;
    }

    @Override
    public void processClient(ClientConnection c) throws UnsupportedOperationException {
        GameState state = c.getGameState();
        state.move(old, now, transform);
        state.togglePlayerTurn();

        c.getGUI().addMove(now);
        c.update();
    }

    @Override
    public void processServer(ServerConnection c) throws IOException {
        c.sendPacket(this);
        c.getOpponent().sendPacket(this);
    }
}
