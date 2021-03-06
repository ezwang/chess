import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * A panel that contains the game board and information about the current game.
 */
public class GamePanel extends JPanel {
    private BoardPanel board;
    private InfoPanel info;
    private GameState state;
    private ClientConnection client;

    public GamePanel(GameState state, ClientConnection c) {
        this.setLayout(new BorderLayout());
        this.state = state;
        this.client = c;

        info = new InfoPanel(state, c);
        this.add(info, BorderLayout.WEST);

        board = new BoardPanel(state, c);
        this.add(board, BorderLayout.CENTER);
    }

    public void addMove(Move move) {
        info.addMove(move);
    }

    public void update() {
        info.update();
        board.update();
    }

    public void endGame(String message) {
        info.endGame(message);
    }

    public void addChat(String message) {
        info.addChat(message);
    }

    public void undo() {
        state.undo();
        info.removeMove();
        update();
    }
}
