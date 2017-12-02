import java.io.IOException;

/**
 * A packet that notifies the server about client nicknames.
 */
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
    public void processServer(ServerConnection c) throws IOException {
        c.setNickname(nick);

        ServerConnection match = c.getServer().getMatch(c);
        if (match == null) {
            return;
        }

        // start game with other player

        c.setOpponent(match);
        match.setOpponent(c);

        boolean random = Math.random() > 0.5;

        Packet p = new PacketStart(c.getNickname(), match.getNickname(), random);
        Packet p2 = new PacketStart(match.getNickname(), c.getNickname(), !random);

        c.sendPacket(p);
        match.sendPacket(p2);

        Packet intro = new PacketChat("Welcome to multiplayer chess!\n" +
                                      "Use the textbox below to chat.\n" +
                                      "Press enter to send your message.");

        c.sendPacket(intro);
        match.sendPacket(intro);
    }
}
