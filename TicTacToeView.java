// TicTacToeView.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The View in the MVC pattern for the Tic-Tac-Toe game.
 * Displays the game board and status messages.
 */
public class TicTacToeView extends JFrame {
    private JButton[][] buttons;
    private JLabel statusLabel;
    private JButton newGameButton;
    private JButton replayButton;
    private JButton prevMoveButton;
    private JButton nextMoveButton;

    /**
     * Constructor to set up the GUI.
     */
    public TicTacToeView() {
        setTitle("Tic Tac Toe MVC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());
        setResizable(false); // Make the window not resizable for simplicity

        // Panel for the game board
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        buttons = new JButton[3][3];
        Font buttonFont = new Font("Arial", Font.BOLD, 60);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(buttonFont);
                buttons[i][j].setFocusPainted(false); // Remove focus border
                buttons[i][j].setBackground(Color.LIGHT_GRAY); // Default background
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Status label
        statusLabel = new JLabel("Player X's Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(statusLabel, BorderLayout.NORTH);

        // Control panel for buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Add spacing
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        newGameButton = new JButton("New Game");
        replayButton = new JButton("Replay");
        prevMoveButton = new JButton("Previous");
        nextMoveButton = new JButton("Next");

        // Style buttons
        Font controlButtonFont = new Font("Arial", Font.PLAIN, 16);
        newGameButton.setFont(controlButtonFont);
        replayButton.setFont(controlButtonFont);
        prevMoveButton.setFont(controlButtonFont);
        nextMoveButton.setFont(controlButtonFont);

        newGameButton.setBackground(new Color(144, 238, 144)); // Light green
        replayButton.setBackground(new Color(173, 216, 230)); // Light blue
        prevMoveButton.setBackground(new Color(255, 223, 186)); // Light orange
        nextMoveButton.setBackground(new Color(255, 223, 186)); // Light orange

        newGameButton.setFocusPainted(false);
        replayButton.setFocusPainted(false);
        prevMoveButton.setFocusPainted(false);
        nextMoveButton.setFocusPainted(false);

        // Initially disable replay navigation buttons
        setReplayNavigationEnabled(false);

        controlPanel.add(newGameButton);
        controlPanel.add(replayButton);
        controlPanel.add(prevMoveButton);
        controlPanel.add(nextMoveButton);
        add(controlPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    /**
     * Updates the text of a specific button on the board.
     * @param row The row of the button.
     * @param col The column of the button.
     * @param text The text to set ('X', 'O', or '').
     */
    public void setButtonText(int row, int col, char text) {
        buttons[row][col].setText(String.valueOf(text));
        // Set color for X and O
        if (text == 'X') {
            buttons[row][col].setForeground(Color.BLUE);
        } else if (text == 'O') {
            buttons[row][col].setForeground(Color.RED);
        } else {
            buttons[row][col].setForeground(Color.BLACK); // Default for empty
        }
    }

    /**
     * Updates the status message displayed to the user.
     * @param message The message to display.
     */
    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    /**
     * Clears the text on all board buttons.
     */
    public void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setForeground(Color.BLACK); // Reset color
                buttons[i][j].setBackground(Color.LIGHT_GRAY); // Reset background
            }
        }
    }

    /**
     * Enables or disables all game board buttons.
     * @param enabled True to enable, false to disable.
     */
    public void setBoardEnabled(boolean enabled) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(enabled);
            }
        }
    }

    /**
     * Enables or disables the replay navigation buttons (Previous, Next).
     * @param enabled True to enable, false to disable.
     */
    public void setReplayNavigationEnabled(boolean enabled) {
        prevMoveButton.setEnabled(enabled);
        nextMoveButton.setEnabled(enabled);
    }

    /**
     * Highlights the winning line on the board.
     * @param r1 Row of the first cell.
     * @param c1 Column of the first cell.
     * @param r2 Row of the second cell.
     * @param c2 Column of the second cell.
     * @param r3 Row of the third cell.
     * @param c3 Column of the third cell.
     */
    public void highlightWinningLine(int r1, int c1, int r2, int c2, int r3, int c3) {
        buttons[r1][c1].setBackground(Color.YELLOW);
        buttons[r2][c2].setBackground(Color.YELLOW);
        buttons[r3][c3].setBackground(Color.YELLOW);
    }

    /**
     * Adds an ActionListener to each board button.
     * @param listener The ActionListener to add.
     */
    public void addBoardButtonListener(ActionListener listener) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].addActionListener(listener);
                // Set action command to identify the button later
                buttons[i][j].setActionCommand(i + "," + j);
            }
        }
    }

    /**
     * Adds an ActionListener to the "New Game" button.
     * @param listener The ActionListener to add.
     */
    public void addNewGameButtonListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }

    /**
     * Adds an ActionListener to the "Replay" button.
     * @param listener The ActionListener to add.
     */
    public void addReplayButtonListener(ActionListener listener) {
        replayButton.addActionListener(listener);
    }

    /**
     * Adds an ActionListener to the "Previous Move" button.
     * @param listener The ActionListener to add.
     */
    public void addPrevMoveButtonListener(ActionListener listener) {
        prevMoveButton.addActionListener(listener);
    }

    /**
     * Adds an ActionListener to the "Next Move" button.
     * @param listener The ActionListener to add.
     */
    public void addNextMoveButtonListener(ActionListener listener) {
        nextMoveButton.addActionListener(listener);
    }
}
