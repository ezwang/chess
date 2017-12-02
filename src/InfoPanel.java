import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The panel to the right of the game screen that shows game information to
 * the player.
 */
public class InfoPanel extends JPanel {
    private JLabel turn;
    private DefaultTableModel moves;
    private JScrollPane movesContainer;

    private GameState state;
    private ClientConnection client;

    private boolean offeringDraw;

    private JButton drawButton, endButton, backButton, undoButton;
    private JTextArea chatOutput;
    private JTextField chatInput;

    public InfoPanel(GameState state, ClientConnection c) {
        this.state = state;
        this.client = c;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));

        String vs = state.getPlayerNickname() + " vs. " + state.getOpponentNickname();
        JLabel names = new JLabel(vs);
        names.setForeground(Color.GRAY);
        names.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(names);

        turn = new JLabel(state.isPlayerTurn() ? "Your Turn" : "Opponent's Turn");
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

        chatOutput = new JTextArea();
        chatOutput.setEnabled(false);
        chatOutput.setDisabledTextColor(Color.BLACK);
        chatOutput.setRows(6);
        DefaultCaret caret = (DefaultCaret) chatOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.add(new JScrollPane(chatOutput));

        chatInput = new JTextField();
        chatInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String msg = "<" + client.getNickname() + "> " + chatInput
                        .getText();
                Packet p = new PacketChat(msg);
                c.sendPacket(p);
                chatInput.setText("");
            }
        });
        this.add(chatInput);

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

        undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Packet p = new PacketUndo();
                c.sendPacket(p);
            }
        });
        controls.add(undoButton);

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
     * @param move The new location of the moved piece.
     */
    public void addMove(Move move) {
        int lastRow = moves.getRowCount() - 1;
        if (lastRow >= 0 && moves.getValueAt(lastRow,1).equals("")) {
            Object first = moves.getValueAt(lastRow, 0);
            moves.removeRow(lastRow);
            moves.addRow(new Object[] { first, move.toString() });
        }
        else {
            moves.addRow(new Object[]{move.toString(), ""});
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

    /**
     * Update the current turn. Should be called after every move.
     */
    public void update() {
        turn.setText(state.isPlayerTurn() ? "Your Turn" : "Opponent's Turn");
        turn.setForeground(state.isPlayerTurn() ? Color.BLACK : Color.GRAY);
    }

    /**
     * Disable all of the components related to the game, show a play again
     * button, and show the reason for the game ending.
     * @param message
     */
    public void endGame(String message) {
        turn.setText(message);
        turn.setForeground(Color.BLUE);
        drawButton.setVisible(false);
        endButton.setVisible(false);
        undoButton.setVisible(false);
        backButton.setVisible(true);
        chatInput.setEnabled(false);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, super.getPreferredSize().height);
    }

    /**
     * Display a message to the user.
     * @param message The message to display.
     */
    public void addChat(String message) {
        chatOutput.append(message + "\n");
    }

    /**
     * Undo the most recent move from the list of moves. Does nothing if
     * there are no moves.
     */
    public void removeMove() {
        int lastRow = moves.getRowCount() - 1;
        if (lastRow < 0) {
            return;
        }
        if (!moves.getValueAt(lastRow, 1).equals("")) {
            Object keep = moves.getValueAt(lastRow, 0);
            moves.removeRow(lastRow);
            moves.addRow(new Object[] { keep, "" });
        }
        else {
            moves.removeRow(lastRow);
        }
    }
}
