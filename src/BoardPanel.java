import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class BoardPanel extends JPanel {
    public static final int SQUARE_SIZE = 64;
    private GameState state;

    private Map<String, Image> images = new TreeMap<String, Image>();

    public BoardPanel(GameState state) {
        this.state = state;

        loadImages();
    }

    /**
     * Preload the images so we don't have to load them on every draw call.
     */
    private void loadImages() {
        for (String c : new String[] { "l", "d" }) {
            for (String p : new String[] { "k", "q", "n", "b", "r", "p" }) {
                try {
                    Image img = ImageIO.read(new File("files/" + p + c + ".png"));
                    images.put(p + c, img.getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_DEFAULT));
                }
                catch (IOException ex) {
                    // this shouldn't happen unless the images are missing
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((j + i) % 2 == 0) {
                    g.setColor(Color.WHITE);
                }
                else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(SQUARE_SIZE * i, SQUARE_SIZE * j, SQUARE_SIZE * (i + 1), SQUARE_SIZE * (j + 1));
                Piece p = state.getPiece(i, 7-j);
                if (p != null) {
                    String color = p.getIsWhite() ? "l" : "d";
                    g.drawImage(images.get(p.getNotationSymbol() + color),SQUARE_SIZE * i, SQUARE_SIZE * j, null);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SQUARE_SIZE * 8, SQUARE_SIZE * 8);
    }
}
