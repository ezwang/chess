import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GamePanel extends JPanel {
    private BoardPanel board;
    private JLabel turn;
    private GameState state;

    public GamePanel(GameState state) {
        this.setLayout(new BorderLayout());
        this.state = state;

        JPanel info = new JPanel();
        info.setLayout(new BorderLayout());
        info.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel names = new JLabel(state.getPlayerNickname() + " vs. " + state.getOpponentNickname(), SwingConstants.CENTER);
        names.setForeground(Color.GRAY);
        info.add(names, BorderLayout.NORTH);

        turn = new JLabel(state.isPlayerTurn() ?"Your Turn" : "Opponent's Turn", SwingConstants.CENTER);
        turn.setForeground(Color.BLACK);
        info.add(turn, BorderLayout.CENTER);

        this.add(info, BorderLayout.WEST);

        board = new BoardPanel(state);
        this.add(board, BorderLayout.CENTER);
    }

    public void update() {
        turn.setText(state.isPlayerTurn() ?"Your Turn" : "Opponent's Turn");
        board.repaint();
    }
}
