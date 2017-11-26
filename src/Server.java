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

    public Server() {
         queue = new LinkedList<ServerConnection>();
    }

    public ServerConnection getMatch(ServerConnection other) {
        if (queue.isEmpty()) {
            queue.add(other);
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
                // TODO
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
    
    public void start() throws IOException {
        server = new ServerSocket(1337);
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        try {
            server.close();
            thread = null;
        }
        catch (IOException e) {
            // ignore
        }
    }

    public void removeFromQueue(ServerConnection sc) {
        queue.remove(sc);
    }
}
