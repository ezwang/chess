import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game implements Runnable {
    private JFrame frame;

    public void run() {
        frame = new JFrame("Multiplayer Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(new IntroPanel(this));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setContent(JPanel panel) {
        frame.setContentPane(panel);
        this.resizeWindow();
    }

    public void resizeWindow() {
        frame.pack();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
