import java.io.IOException;

public class PacketDraw implements Packet {
    @Override
    public void processClient(ClientConnection c) throws UnsupportedOperationException, IOException {

    }

    @Override
    public void processServer(ServerConnection c) throws IOException {
        boolean opponentDraw = c.getOpponent().getDraw();
        if (opponentDraw) {
            Packet p = new PacketEnd("Tie!");
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
