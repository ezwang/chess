import java.io.IOException;

/**
 * A packet used to negotiate and perform undo operations.
 */
public class PacketUndo implements Packet {
    @Override
    public void processClient(ClientConnection c) {
        c.undo();
    }

    @Override
    public void processServer(ServerConnection c) throws IOException {
        if (c.getOpponent().getUndo()) {
            // undo move
            c.sendPacket(this);
            c.getOpponent().sendPacket(this);

            // notify players about undo
            Packet notify = new PacketChat("The most recent move has been undone.");
            c.sendPacket(notify);
            c.getOpponent().sendPacket(notify);

            // reset undo accept flags
            c.setUndo(false);
            c.getOpponent().setUndo(false);
        }
        else {
            c.setUndo(!c.getUndo());
            Packet notify = new PacketChat(c.getNickname() + (c.getUndo() ? " is requesting an undo." : " rescinded an undo request."));
            c.sendPacket(notify);
            c.getOpponent().sendPacket(notify);
        }
    }
}
