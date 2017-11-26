import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    BoardPanel board;

    public GamePanel(GameState state) {
        this.setLayout(new BorderLayout());

        board = new BoardPanel(state);
        this.add(board, BorderLayout.CENTER);
    }
}
