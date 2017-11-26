import java.io.*;
import java.net.*;
import java.util.Enumeration;

public class Server implements Runnable {
    Thread thread;
    ServerSocket server;

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
            e.printStackTrace();
        }
    }
}
