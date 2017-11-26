/**
 * Represents a packet used in communication between
 * the client and the server.
 */
public interface Packet {
    /**
     * The code executed by the client to handle this event.
     */
    public abstract void processClient(ClientConnection c) throws UnsupportedOperationException;

    /**
     * The code executed by the server to handle this event.
     */
    public abstract void processServer(ServerConnection c) throws UnsupportedOperationException;
}