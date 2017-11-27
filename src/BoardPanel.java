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

    private final Color BOARD_LIGHT = Color.decode("#FED496");
    private final Color BOARD_DARK = Color.decode("#D09137");

    private Map<String, Image> images;

    public BoardPanel(GameState state, ClientConnection client) {
        this.state = state;

        blackInCheck = false;
        whiteInCheck = false;

        images = new TreeMap<String, Image>();
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

                Piece op = null;
                if (selected != null) {
                    op = state.getPiece(selected.getX(), selected.getY());
                }
                Piece p = state.getPiece(x, state.playerIsWhite() ? 7-y : y);
                Location mv = new Location(x, state.playerIsWhite() ? 7-y : y);

                if (selected != null) {
                    if (selected.equals(mv)) {
                        selected = null;
                        allowed = null;
                    }
                    else if (allowed != null && allowed.contains(mv)) {
                        Packet movePacket;
                        // this case handles pawn promotion
                        if (op instanceof Pawn && (mv.getY() == 0 || mv.getY() == 7)) {
                            String[] values = new String[] { "Queen", "Rook", "Bishop", "Knight" };
                            Object result = JOptionPane.showInputDialog(null, "What should this pawn be promoted into?","Piece Promotion", JOptionPane.DEFAULT_OPTION, null, values, 0);
                            if (result == null) {
                                // user canceled the selection,
                                // cancel the move
                                return;
                            }
                            String res = result.toString();
                            movePacket = new PacketMove(selected, mv, res);
                        }
                        else {
                            movePacket = new PacketMove(selected, mv);
                        }
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
                    g.setColor(BOARD_LIGHT);
                    whiteSquare = true;
                }
                else {
                    g.setColor(BOARD_DARK);
                }
                Location curr = new Location(i, state.playerIsWhite() ? 7-j : j);
                Piece p = state.getPiece(i, state.playerIsWhite() ? 7-j : j);
                // if the king is under check, make the king's square red
                if (p instanceof King) {
                    if (blackInCheck && !p.getIsWhite()) {
                        g.setColor(Color.RED);
                    }
                    if (whiteInCheck && p.getIsWhite()) {
                        g.setColor(Color.RED);
                    }
                }
                // if the piece is currently selected, make it green
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

    private boolean blackInCheck;
    private boolean whiteInCheck;

    public void update() {
        blackInCheck = state.checkInCheck(false);
        whiteInCheck = state.checkInCheck(true);
        this.repaint();
    }
}
