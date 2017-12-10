import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class BoardPanel extends JPanel {
    public static final int SQUARE_SIZE = 64;
    public static final int BORDER_WIDTH = 30;

    private GameState state;
    private ClientConnection client;
    private Location selected;
    private Set<Location> allowed;

    private final Color BOARD_LIGHT = Color.decode("#FED496");
    private final Color BOARD_DARK = Color.decode("#D09137");

    private final Font BOARD_FONT = new Font("Lucidia", Font.BOLD, 18);

    private Map<String, Image> images;

    /**
     * Instantiates a new game board component.
     * @param state The state of the game.
     * @param client The client used to communicate with the opponent.
     */
    public BoardPanel(GameState state, ClientConnection client) {
        this.state = state;
        this.client = client;

        blackInCheck = false;
        whiteInCheck = false;

        images = new TreeMap<String, Image>();
        loadImages();

        BoardPanel t = this;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (!state.isPlayerTurn() || !client.isConnected()) {
                    return;
                }

                int x = (mouseEvent.getX() - BORDER_WIDTH) / SQUARE_SIZE;
                int y = (mouseEvent.getY() - BORDER_WIDTH) / SQUARE_SIZE;

                Piece p = state.getPiece(x, state.playerIsWhite() ? 7-y : y);
                Location mv = new Location(x, state.playerIsWhite() ? 7-y : y);

                if (selected != null) {
                    performMove(mv);
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
     * Performs a piece move and updates the GUI. Relies on the select variable.
     * @param mv The location to move to.
     */
    private void performMove(Location mv) {
        // deselect pieces if they are clicked when selected
        if (selected.equals(mv)) {
            selected = null;
            allowed = null;
        }
        else if (allowed != null && allowed.contains(mv)) {
            if (playerInCheck) {
                Move m = new Move(selected, mv);
                if (!movesUnderCheck.contains(m)) {
                    client.addChat("You cannot make that move because you are in check!");
                    client.addChat("Valid Moves: " + movesUnderCheck.toString());
                    return;
                }
            }

            // check if moving would put player in check
            state.move(selected, mv);
            boolean nowInCheck = state.checkInCheck(state.playerIsWhite());
            state.undo();

            if (nowInCheck) {
                client.addChat("Performing that move would put you in check!");
                return;
            }

            Packet movePacket;
            Piece originalPiece = state.getPiece(selected);
            // this case handles pawn promotion
            if (originalPiece instanceof Pawn && (mv.getY() == 0 || mv.getY() == 7)) {
                String[] values = new String[] { "Queen", "Rook", "Bishop", "Knight" };
                Object result = JOptionPane.showInputDialog(null, "What should this pawn be promoted into?","Piece Promotion", JOptionPane.DEFAULT_OPTION, null, values, 0);
                if (result == null) {
                    // user canceled the selection, cancel the move
                    return;
                }
                String res = result.toString();
                movePacket = new PacketMove(selected, mv, res);
            }
            else {
                movePacket = new PacketMove(selected, mv);
            }
            client.sendPacket(movePacket);
            selected = null;
            allowed = null;
        }
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
                    Image img = getPlaceholderImage(c, p);
                    images.put(p + c, img);
                }
            }
        }
    }

    /**
     * Generate a placeholder image for missing images.
     * Consists of a colored letter representing the piece.
     * @param color The color of the piece.
     * @param piece The piece.
     * @return The placeholder image.
     */
    private Image getPlaceholderImage(String color, String piece) {
        BufferedImage img = new BufferedImage(SQUARE_SIZE, SQUARE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setColor(color.equals("l") ? Color.WHITE : Color.BLACK);
        graphics.setFont(new Font("Lucidia", Font.BOLD, 30));
        graphics.drawString(piece.toUpperCase(), 15, 40);
        return img;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.translate(BORDER_WIDTH, BORDER_WIDTH);
        g.setFont(BOARD_FONT);
        g.setColor(Color.GRAY.brighter());
        // draw the numbers/letters on the side of the board
        for (int i = 0; i < 8; i++) {
            g.drawString(""+(state.playerIsWhite() ? 8-i : i + 1), -18, i*SQUARE_SIZE + SQUARE_SIZE/2 + 9);
            g.drawString(""+(state.playerIsWhite() ? 8-i : i + 1), SQUARE_SIZE*8 + 6, i*SQUARE_SIZE + SQUARE_SIZE/2 + 9);
            g.drawString(Character.toString((char)(i + 65)), i*SQUARE_SIZE + SQUARE_SIZE/2 - 9, -6);
            g.drawString(Character.toString((char)(i + 65)), i*SQUARE_SIZE + SQUARE_SIZE/2 - 9, SQUARE_SIZE*8 + 18);
        }
        // draw the board
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
                    Move m = new Move(selected, curr);
                    if (!playerInCheck || movesUnderCheck.contains(m)) {
                        if (whiteSquare) {
                            g.setColor(Color.BLUE);
                        } else {
                            g.setColor(Color.BLUE.darker().darker());
                        }
                    }
                }
                g.fillRect(SQUARE_SIZE * i, SQUARE_SIZE * j, SQUARE_SIZE, SQUARE_SIZE);
                if (p != null) {
                    String color = p.getIsWhite() ? "l" : "d";
                    g.drawImage(images.get(p.getNotationSymbol() + color),SQUARE_SIZE * i, SQUARE_SIZE * j, null);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SQUARE_SIZE * 8 + BORDER_WIDTH * 2, SQUARE_SIZE * 8 + BORDER_WIDTH * 2);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    private boolean blackInCheck;
    private boolean whiteInCheck;
    private boolean playerInCheck;

    private Set<Move> movesUnderCheck;

    /**
     * Recomputes information about the current game, such as who is in check
     * and the valid moves that can be made under check. Should be called
     * every turn.
     */
    public void update() {
        blackInCheck = state.checkInCheck(false);
        whiteInCheck = state.checkInCheck(true);
        playerInCheck = state.playerIsWhite() ? whiteInCheck : blackInCheck;
        if (state.playerIsWhite() && whiteInCheck) {
            movesUnderCheck = state.getPossibleMovesUnderCheck(true);
        }
        else if (!state.playerIsWhite() && blackInCheck) {
            movesUnderCheck = state.getPossibleMovesUnderCheck(false);
        }
        else {
            movesUnderCheck = null;
        }
        // checkmate
        if (playerInCheck && movesUnderCheck.size() == 0) {
            Packet lose = new PacketEnd(state.playerIsWhite() ? PacketEnd.EndResult.BLACK : PacketEnd.EndResult.WHITE);
            client.sendPacket(lose);
        }
        // stalemate
        if (state.isDraw(state.playerIsWhite())) {
            Packet tie = new PacketEnd(PacketEnd.EndResult.STALEMATE);
            client.sendPacket(tie);
        }
        this.repaint();
    }
}
