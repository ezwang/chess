import java.io.IOException;

/**
 * A packet used to negotiate and perform draw operations.
 */
public class PacketDraw implements Packet {
    @Override
    public void processClient(ClientConnection c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processServer(ServerConnection c) throws IOException {
        boolean opponentDraw = c.getOpponent().getDraw();
        if (opponentDraw) {
            Packet notify = new PacketChat("Draw accepted!");
            c.sendPacket(notify);
            c.getOpponent().sendPacket(notify);

            Packet p = new PacketEnd(PacketEnd.EndResult.DRAW);
            c.sendPacket(p);
            c.getOpponent().sendPacket(p);
        }
        else {
            c.setDraw(!c.getDraw());

            Packet notify = new PacketChat(c.getNickname() + " has " + (c.getDraw() ? "offered" : "rescinded") + " a draw!");
            c.getOpponent().sendPacket(notify);
        }
    }
}
