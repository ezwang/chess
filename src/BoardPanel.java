import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class BoardPanel extends JPanel {
    public static final int SQUARE_SIZE = 64;
    private GameState state;
    private Location selected;
    private Set<Location> allowed;

    private Map<String, Image> images = new TreeMap<String, Image>();

    public BoardPanel(GameState state, ClientConnection client) {
        this.state = state;

        loadImages();

        BoardPanel t = this;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (!state.isPlayerTurn()) {
                    return;
                }

                int x = mouseEvent.getX() / SQUARE_SIZE;
                int y = mouseEvent.getY() / SQUARE_SIZE;

                Piece p = state.getPiece(x, state.playerIsWhite() ? 7-y : y);
                Location mv = new Location(x, state.playerIsWhite() ? 7-y : y);

                if (selected != null) {
                    if (selected.equals(mv)) {
                        selected = null;
                        allowed = null;
                    }
                    else if (allowed != null && allowed.contains(mv)) {
                        Packet movePacket = new PacketMove(selected, mv);
                        try {
                            client.sendPacket(movePacket);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        selected = null;
                        allowed = null;
                    }
                }
                else if (p != null) {
                    // if the piece is not the same color as the player
                    // then the player cannot move the piece
                    if (p.getIsWhite() != state.isWhiteTurn()) {
                        return;
                    }

                    selected = mv;
                    allowed = p.getMovableLocations();
                }
                t.repaint();
            }
        });
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
                boolean whiteSquare = false;
                if ((j + i) % 2 == (state.playerIsWhite() ? 0 : 1)) {
                    g.setColor(Color.WHITE);
                    whiteSquare = true;
                }
                else {
                    g.setColor(Color.BLACK);
                }
                Location curr = new Location(i, state.playerIsWhite() ? 7-j : j);
                if (curr.equals(selected)) {
                    if (whiteSquare) {
                        g.setColor(Color.GREEN);
                    }
                    else {
                        g.setColor(Color.GREEN.darker().darker());
                    }
                }
                else if (allowed != null && allowed.contains(curr)) {
                    if (whiteSquare) {
                        g.setColor(Color.BLUE);
                    }
                    else {
                        g.setColor(Color.BLUE.darker().darker());
                    }
                }
                g.fillRect(SQUARE_SIZE * i, SQUARE_SIZE * j, SQUARE_SIZE * (i + 1), SQUARE_SIZE * (j + 1));
                Piece p = state.getPiece(i, state.playerIsWhite() ? 7-j : j);
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
