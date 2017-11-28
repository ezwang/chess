import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoPanel extends JPanel {
    private JLabel turn;
    private DefaultTableModel moves;
    private JScrollPane movesContainer;

    private GameState state;
    private ClientConnection client;

    private boolean offeringDraw;

    private JButton drawButton, endButton, backButton;

    public InfoPanel(GameState state, ClientConnection c) {
        this.state = state;
        this.client = c;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel names = new JLabel(state.getPlayerNickname() + " vs. " + state.getOpponentNickname());
        names.setForeground(Color.GRAY);
        names.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(names);

        turn = new JLabel(state.isPlayerTurn() ?"Your Turn" : "Opponent's Turn");
        turn.setAlignmentX(Component.CENTER_ALIGNMENT);
        turn.setForeground(state.isPlayerTurn() ? Color.BLACK : Color.GRAY);
        turn.setFont(new Font("Lucidia", Font.BOLD, 20));
        turn.setForeground(Color.BLACK);
        this.add(turn);

        moves = new DefaultTableModel();
        JTable movesTable = new JTable(moves);
        movesContainer = new JScrollPane(movesTable);
        movesContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
        movesTable.setEnabled(false);
        moves.addColumn("White");
        moves.addColumn("Black");
        this.add(movesContainer);

        JPanel controls = new JPanel();
        drawButton = new JButton("Offer Draw");
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Packet p = new PacketDraw();
                c.sendPacket(p);
                offeringDraw = !offeringDraw;
                drawButton.setText(offeringDraw ? "Cancel Offer" : "Offer Draw");
            }
        });
        controls.add(drawButton);

        endButton = new JButton("Resign");
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Packet p = new PacketEnd(!state.playerIsWhite());
                c.sendPacket(p);
            }
        });
        controls.add(endButton);

        backButton = new JButton("Play Again");
        backButton.setVisible(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                client.goBackToIntro();
            }
        });
        controls.add(backButton);
        this.add(controls);
    }

    /**
     * Add a move to the table of moves.
     * @param loc The new location of the moved piece.
     */
    public void addMove(Location loc) {
        int lastRow = moves.getRowCount() - 1;
        if (lastRow >= 0 && moves.getValueAt(lastRow,1).equals("")) {
            Object first = moves.getValueAt(lastRow, 0);
            moves.removeRow(lastRow);
            moves.addRow(new Object[] { first, loc.toString() });
        }
        else {
            moves.addRow(new Object[]{loc.toString(), ""});
        }
        // scroll to the bottom of the moves table
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JScrollBar bar = movesContainer.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            }
        });
    }

    public void update() {
        turn.setText(state.isPlayerTurn() ? "Your Turn" : "Opponent's Turn");
        turn.setForeground(state.isPlayerTurn() ? Color.BLACK : Color.GRAY);
    }

    public void endGame(String message) {
        turn.setText(message);
        turn.setForeground(Color.BLUE);
        drawButton.setVisible(false);
        endButton.setVisible(false);
        backButton.setVisible(true);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, super.getPreferredSize().height);
    }
}
