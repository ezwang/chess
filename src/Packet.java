import java.io.IOException;
import java.io.Serializable;

/**
 * Represents a packet used in communication between the client and the server.
 */
public interface Packet extends Serializable {
    /**
     * The code executed by the client to handle this event.
     * @throws UnsupportedOperationException This happens if this packet does
     * not have any client side processing.
     */
    public abstract void processClient(ClientConnection c) throws
            UnsupportedOperationException, IOException;

    /**
     * The code executed by the server to handle this event.
     * @throws UnsupportedOperationException This happens i this packet does
     * not have any server side processing.
     */
    public abstract void processServer(ServerConnection c) throws
            UnsupportedOperationException, IOException;
}