import java.io.*;
import java.util.Base64;

/**
 * Used to convert Java classes to strings and vice versa.
 */
public class PacketUtils {
    public static String encode(Packet p) throws IOException {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bs);
        os.writeObject(p);
        os.close();
        return Base64.getEncoder().encodeToString(bs.toByteArray());
    }

    public static Packet decode(String s) throws IOException, ClassNotFoundException {
        byte[] raw = Base64.getDecoder().decode(s);
        ObjectInputStream os = new ObjectInputStream(new ByteArrayInputStream(raw));
        Object obj = os.readObject();
        os.close();
        return (Packet) obj;
    }
}
