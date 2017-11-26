public class PacketStart implements Packet {
    private String whiteNick, blackNick;
    private boolean isWhite;

    public PacketStart(String whiteNick, String blackNick, boolean isWhite) {
        this.whiteNick = whiteNick;
        this.blackNick = blackNick;
        this.isWhite = isWhite;
    }

    @Override
    public void processClient(ClientConnection c) throws UnsupportedOperationException {
        // TODO: implement game start
        c.startGame();
    }

    @Override
    public void processServer(ServerConnection c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
