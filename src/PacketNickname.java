import java.io.IOException;

public class PacketNickname implements Packet {
    String nick;

    public PacketNickname(String nick) {
        this.nick = nick;
    }

    @Override
    public void processClient(ClientConnection c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processServer(ServerConnection c) throws UnsupportedOperationException {
        c.setNickname(nick);

        ServerConnection match = c.getServer().getMatch(c);
        if (match == null) {
            return;
        }

        // start game with other player

        c.setOpponent(match);
        match.setOpponent(c);

        Packet p = new PacketStart(c.getNickname(), match.getNickname(), true);
        Packet p2 = new PacketStart(match.getNickname(), c.getNickname(), false);

        try {
            c.sendPacket(p);
            match.sendPacket(p2);
        }
        catch (IOException ex) {
            // TODO: handle this
            ex.printStackTrace();
        }
    }
}
