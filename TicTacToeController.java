// TicTacToeController.java
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.SwingUtilities; // Required for SwingUtilities.invokeLater

/**
 * The Controller in the MVC pattern for the Tic-Tac-Toe game.
 * Handles user interactions, updates the model, and updates the view.
 */
public class TicTacToeController {
    private TicTacToeModel model;
    private TicTacToeView view;
    private boolean isReplaying;

    /**
     * Constructor to link the model and view.
     * @param model The TicTacToeModel instance.
     * @param view The TicTacToeView instance.
     */
    public TicTacToeController(TicTacToeModel model, TicTacToeView view) {
        this.model = model;
        this.view = view;
        this.isReplaying = false;

        // Add listeners to view components
        this.view.addBoardButtonListener(new BoardButtonListener());
        this.view.addNewGameButtonListener(new NewGameButtonListener());
        this.view.addReplayButtonListener(new ReplayButtonListener());
        this.view.addPrevMoveButtonListener(new PrevMoveButtonListener());
        this.view.addNextMoveButtonListener(new NextMoveButtonListener());

        // Initial update of the view
        updateView();
    }

    /**
     * Updates the view based on the current state of the model.
     */
    private void updateView() {
        // Update board buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                view.setButtonText(i, j, model.getCell(i, j));
            }
        }

        // Update status label
        switch (model.getGameStatus()) {
            case PLAYING:
                view.setStatusMessage("Player " + model.getCurrentPlayer() + "'s Turn");
                view.setBoardEnabled(true); // Enable board for playing
                // Replay navigation should be disabled if not in replay mode
                if (!isReplaying) {
                    view.setReplayNavigationEnabled(false);
                }
                break;
            case X_WINS:
                view.setStatusMessage("Player X Wins!");
                view.setBoardEnabled(false); // Disable board after game ends
                highlightWinningLine('X');
                break;
            case O_WINS:
                view.setStatusMessage("Player O Wins!");
                view.setBoardEnabled(false); // Disable board after game ends
                highlightWinningLine('O');
                break;
            case DRAW:
                view.setStatusMessage("It's a Draw!");
                view.setBoardEnabled(false); // Disable board after game ends
                break;
        }
    }

    /**
     * Highlights the winning line on the board if a player has won.
     * This logic is duplicated from the model's checkGameStatus for view purposes.
     * In a more complex game, the model might provide the winning line coordinates.
     * @param winner The winning player ('X' or 'O').
     */
    private void highlightWinningLine(char winner) {
        char[][] board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = model.getCell(i, j); // Get current board state from model
            }
        }

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == winner && board[i][1] == winner && board[i][2] == winner) {
                view.highlightWinningLine(i, 0, i, 1, i, 2);
                return;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == winner && board[1][j] == winner && board[2][j] == winner) {
                view.highlightWinningLine(0, j, 1, j, 2, j);
                return;
            }
        }

        // Check diagonals
        if (board[0][0] == winner && board[1][1] == winner && board[2][2] == winner) {
            view.highlightWinningLine(0, 0, 1, 1, 2, 2);
            return;
        }
        if (board[0][2] == winner && board[1][1] == winner && board[2][0] == winner) {
            view.highlightWinningLine(0, 2, 1, 1, 2, 0);
            return;
        }
    }

    /**
     * ActionListener for the board buttons.
     */
    private class BoardButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isReplaying) {
                JOptionPane.showMessageDialog(view, "Cannot make moves during replay mode. Click 'New Game' to play.", "Replay Mode Active", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JButton clickedButton = (JButton) e.getSource();
            String[] parts = clickedButton.getActionCommand().split(",");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);

            if (model.getGameStatus() == TicTacToeModel.GameStatus.PLAYING) {
                if (model.makeMove(row, col)) {
                    updateView();
                } else {
                    // Optionally provide feedback for invalid move (e.g., cell already taken)
                    // JOptionPane.showMessageDialog(view, "Invalid move. Cell already taken or game ended.", "Invalid Move", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    /**
     * ActionListener for the "New Game" button.
     */
    private class NewGameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.initializeGame(); // Reset model state
            view.clearBoard(); // Clear view board
            view.setBoardEnabled(true); // Enable board for new game
            view.setReplayNavigationEnabled(false); // Disable replay navigation
            isReplaying = false; // Exit replay mode
            updateView(); // Update status message
        }
    }

    /**
     * ActionListener for the "Replay" button.
     */
    private class ReplayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getGameHistory().getTotalMoves() == 0) {
                JOptionPane.showMessageDialog(view, "No moves to replay. Play a game first!", "No History", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            isReplaying = true;
            view.setBoardEnabled(false); // Disable board during replay
            view.clearBoard(); // Clear board to start replay from scratch
            model.getGameHistory().startReplay(); // Reset replay index
            view.setReplayNavigationEnabled(true); // Enable replay navigation
            view.setStatusMessage("Replay Mode: Use Previous/Next buttons");
            // Automatically show the first move if available
            showNextReplayMove();
        }
    }

    /**
     * ActionListener for the "Previous Move" button.
     */
    private class PrevMoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isReplaying && model.getGameHistory().hasPreviousMove()) {
                // To show previous move, we need to "undo" the last move shown.
                // A simpler approach for replay is to clear and re-apply moves up to the new index.
                // For this simple game, we'll just clear and re-apply all moves up to the current index.
                // This is less efficient but simpler to implement for a small board.

                // The getPreviousMove() already decrements the index, so we need to get the move
                // at the new (decremented) index.
                Move prevMove = model.getGameHistory().getPreviousMove(); // This moves the index back
                if (prevMove != null) {
                    // Clear the board and re-apply moves up to the new replay index
                    view.clearBoard();
                    // Temporarily reset replay index to 0 to re-apply from start
                    int tempReplayIndex = model.getGameHistory().getReplayIndex();
                    model.getGameHistory().startReplay(); // Reset to 0
                    for (int i = 0; i < tempReplayIndex; i++) {
                        Move move = model.getGameHistory().getNextMove();
                        if (move != null) {
                            view.setButtonText(move.getRow(), move.getCol(), move.getPlayer());
                        }
                    }
                    view.setStatusMessage("Replay: Move " + (model.getGameHistory().getReplayIndex()) + "/" + model.getGameHistory().getTotalMoves());
                } else {
                    view.setStatusMessage("Replay: No previous moves.");
                }
            } else {
                view.setStatusMessage("Replay: No previous moves.");
            }
        }
    }

    /**
     * ActionListener for the "Next Move" button.
     */
    private class NextMoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showNextReplayMove();
        }
    }

    /**
     * Helper method to show the next move during replay.
     */
    private void showNextReplayMove() {
        if (isReplaying && model.getGameHistory().hasNextMove()) {
            Move nextMove = model.getGameHistory().getNextMove();
            view.setButtonText(nextMove.getRow(), nextMove.getCol(), nextMove.getPlayer());
            view.setStatusMessage("Replay: Move " + (model.getGameHistory().getReplayIndex()) + "/" + model.getGameHistory().getTotalMoves());
        } else if (isReplaying) {
            view.setStatusMessage("Replay Finished!");
            // Optionally disable next button when replay finishes
            view.setReplayNavigationEnabled(false);
        }
    }

    /**
     * Main method to run the application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Ensure GUI updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            TicTacToeModel model = new TicTacToeModel();
            TicTacToeView view = new TicTacToeView();
            new TicTacToeController(model, view);
        });
    }
}
