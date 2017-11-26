import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GamePanel extends JPanel {
    BoardPanel board;

    public GamePanel(GameState state) {
        this.setLayout(new BorderLayout());

        JPanel info = new JPanel();
        info.setLayout(new BorderLayout());
        info.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel names = new JLabel(state.getPlayerNickname() + " vs. " + state.getOpponentNickname(), SwingConstants.CENTER);
        names.setForeground(Color.GRAY);
        info.add(names, BorderLayout.NORTH);

        JLabel turn = new JLabel(state.isPlayerTurn() ?"Your Turn" : "Opponent's Turn", SwingConstants.CENTER);
        turn.setForeground(Color.BLACK);
        info.add(turn, BorderLayout.CENTER);

        this.add(info, BorderLayout.WEST);

        board = new BoardPanel(state);
        this.add(board, BorderLayout.CENTER);
    }
}
