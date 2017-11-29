import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles new connections and delegates them to a ServerConnection instance.
 */
public class Server implements Runnable {
    Thread thread;
    ServerSocket server;

    List<ServerConnection> queue;

    /**
     * The port that the server should listen on.
     */
    public static final int PORT = 1337;

    public Server() {
         queue = new LinkedList<ServerConnection>();
    }

    /**
     * Return an opponent for the player, or null if there are no opponents
     * available.
     * @param player The current player.
     * @return The player's opponent, or null if there are no opponents
     * available.
     */
    public ServerConnection getMatch(ServerConnection player) {
        if (queue.isEmpty()) {
            queue.add(player);
            return null;
        }
        else {
            return queue.remove(0);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = server.accept();
                new Thread(new ServerConnection(this, socket)).start();
            }
            catch (IOException e) {
                if (thread == null) {
                    // server has been stopped
                    return;
                }
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the IP address of the machine that the server is running on.
     * There can be multiple valid IP addresses for a single machine, so just
     * return the first one that isn't the loopback address. If this fails,
     * then just return the word "Unknown".
     * @return The IP address of the server.
     */
    public static String getServerIP() {
        String backupAddr = "Unknown";
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface inter = (NetworkInterface) e.nextElement();
                if (inter.isLoopback() || !inter.isUp()) {
                    continue;
                }
                Enumeration ee = inter.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress addr = (InetAddress) ee.nextElement();
                    String a = addr.getHostAddress();
                    // prefer IPv4 addresses, easier to type
                    if (addr instanceof Inet4Address) {
                        return a;
                    }
                    else {
                        backupAddr = a;
                    }
                }
            }
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        return backupAddr;
    }

    /**
     * Starts the server.
     * @throws IOException
     */
    public void start() throws IOException {
        server = new ServerSocket(Server.PORT);
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stops the server and ignores any errors that occur.
     */
    public void stop() {
        try {
            server.close();
            thread = null;
        }
        catch (IOException e) {
            // ignore
        }
    }

    /**
     * Remove a player from the queue.
     * @param sc The connection to remove from the queue.
     */
    public void removeFromQueue(ServerConnection sc) {
        queue.remove(sc);
    }
}
