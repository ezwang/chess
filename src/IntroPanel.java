import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * The panel that is shown when the application first starts.
 */
public class IntroPanel extends JPanel {
    private Game game;

    private JLabel clientStatusLabel;
    private JLabel serverStatusLabel;

    private JTextField ipField, nicknameField;
    private JButton startGameButton, serverButton;

    public IntroPanel(Game game) {
        this.game = game;
        this.setLayout(new BorderLayout());

        JLabel title = new JLabel("Multiplayer Chess", SwingConstants.CENTER);
        title.setFont(new Font("Lucidia", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.add(title, BorderLayout.NORTH);

        JLabel instructions = new JLabel("<html><style>p { margin-bottom: 15px }</style><body style='width:400px'><p>This game is a multiplayer version of the popular board game chess. To learn more about how to play chess, you can go to <a href='https://en.wikipedia.org/wiki/Chess'>https://en.wikipedia.org/wiki/Chess</a>.</p> <p>To begin playing, first start a server instance of the application using the 'Start Server' button below. If there is already a server, then enter the IP address of the server and a nickname in the fields below and press 'Start Game'. Once you have been matched with another player, the game will automatically start.</p> <p>You will be able to move pieces by clicking the piece you want to move, and then clicking the location where you want to move the piece.</p></body></html>");
        instructions.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.add(instructions, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setLayout(new BorderLayout());
        controls.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel clientSettings = new JPanel();
        clientSettings.setBorder(new EmptyBorder(0, 0, 0, 10));
        clientSettings.setLayout(new GridLayout(0, 2));
        clientSettings.add(new JLabel("Server IP: "));
        // set to local computer as a reasonable default
        ipField = new JTextField("127.0.0.1");
        clientSettings.add(ipField);
        clientSettings.add(new JLabel("Nickname: "));
        nicknameField = new JTextField(getRandomNickname());
        clientSettings.add(nicknameField);
        clientStatusLabel = new JLabel();
        clientStatusLabel.setForeground(Color.BLUE);
        clientSettings.add(clientStatusLabel);
        startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String ip = ipField.getText();
                String nick = nicknameField.getText();

                if (ip.isEmpty() || nick.isEmpty()) {
                    clientStatusLabel.setText("Fill out all fields!");
                }
                else {
                    toggleControls(false);
                    clientStatusLabel.setText("Connecting...");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            ClientConnection client = new ClientConnection(ip, nick, game);
                            if (!client.checkValidHost()) {
                                clientStatusLabel.setText("Invalid IP!");
                                toggleControls(true);
                            }
                            else if (!client.connect()) {
                                clientStatusLabel.setText("Failed to connect!");
                                toggleControls(true);
                            }
                            else {
                                clientStatusLabel.setText("Matchmaking...");
                                client.sendNickname();
                            }
                        }
                    });
                }
            }
        });
        clientSettings.add(startGameButton);

        controls.add(clientSettings, BorderLayout.CENTER);

        serverButton = new JButton("Start Server");
        serverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Server s = new Server();
                try {
                    s.start();

                    serverStatusLabel.setText("<html><body style='width: 400px; text-align:center'>Server started! Connect to the server at: <b>" + Server.getServerIP() + "</b><br />" + "Closing this window will close the server. Keep this window open until you are done playing the game!</body></html>");
                    toggleControls(false);

                    // resize the window to accommodate the additional text
                    // that appears at the bottom of the screen
                    game.resizeWindow();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    serverStatusLabel.setText("Failed to start server! Check the command line logs for more details.");
                }
            }
        });
        controls.add(serverButton, BorderLayout.EAST);

        serverStatusLabel = new JLabel("Created by Eric Wang - CIS 120 Final Project", SwingConstants.CENTER);
        serverStatusLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        serverStatusLabel.setForeground(Color.BLUE);
        controls.add(serverStatusLabel, BorderLayout.SOUTH);

        this.add(controls, BorderLayout.SOUTH);
    }

    /**
     * Create a random nickname from a verb and a noun.
     * @return A randomly generated nickname.
     */
    private String getRandomNickname() {
        String[] verbs = new String[] { "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Amazing", "Sad", "Happy" };
        String[] nouns = new String[] { "Laser", "Potato", "Tomato", "Apple", "Pineapple", "Blueberry", "Strawberry", "Pear" };
        return verbs[(int)(Math.random() * verbs.length)] + nouns[(int)(Math.random() * nouns.length)];
    }

    private void toggleControls(boolean enabled) {
        serverButton.setEnabled(enabled);
        startGameButton.setEnabled(enabled);
        nicknameField.setEnabled(enabled);
        ipField.setEnabled(enabled);
    }
}
