
// TicTacToeModel.java
import java.util.ArrayList;
import java.util.List;

/**
 * The Model in the MVC pattern for the Tic-Tac-Toe game.
 * Manages the game state, board, current player, and game logic.
 */
public class TicTacToeModel {
    private char[][] board;
    private char currentPlayer;
    private GameStatus gameStatus;
    private GameHistory gameHistory; // Reference to the game history

    public enum GameStatus {
        PLAYING,
        X_WINS,
        O_WINS,
        DRAW
    }

    /**
     * Constructor to initialize the game board and state.
     */
    public TicTacToeModel() {
        initializeGame();
    }

    /**
     * Initializes a new game.
     */
    public void initializeGame() {
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' '; // Empty cell
            }
        }
        currentPlayer = 'X';
        gameStatus = GameStatus.PLAYING;
        gameHistory = new GameHistory(); // Initialize new history for each game
    }

    /**
     * Places a move on the board if valid.
     * @param row The row for the move.
     * @param col The column for the move.
     * @return True if the move was successfully placed, false otherwise (e.g., cell already taken).
     */
    public boolean makeMove(int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != ' ' || gameStatus != GameStatus.PLAYING) {
            return false; // Invalid move
        }

        board[row][col] = currentPlayer;
        gameHistory.addMove(new Move(row, col, currentPlayer)); // Record the move
        checkGameStatus();
        if (gameStatus == GameStatus.PLAYING) {
            switchPlayer();
        }
        return true;
    }

    /**
     * Switches the current player from 'X' to 'O' or vice versa.
     */
    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    /**
     * Checks the current state of the game to determine if there's a winner or a draw.
     */
    private void checkGameStatus() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                setWinner(board[i][0]);
                return;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                setWinner(board[0][j]);
                return;
            }
        }

        // Check diagonals
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            setWinner(board[0][0]);
            return;
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            setWinner(board[0][2]);
            return;
        }

        // Check for draw
        boolean isBoardFull = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    isBoardFull = false;
                    break;
                }
            }
            if (!isBoardFull) break;
        }

        if (isBoardFull) {
            gameStatus = GameStatus.DRAW;
        }
    }

    /**
     * Sets the game status to indicate a winner.
     * @param winner The character of the winning player ('X' or 'O').
     */
    private void setWinner(char winner) {
        if (winner == 'X') {
            gameStatus = GameStatus.X_WINS;
        } else if (winner == 'O') {
            gameStatus = GameStatus.O_WINS;
        }
    }

    /**
     * Gets the character at a specific cell on the board.
     * @param row The row index.
     * @param col The column index.
     * @return The character ('X', 'O', or ' ') at the specified cell.
     */
    public char getCell(int row, int col) {
        return board[row][col];
    }

    /**
     * Gets the current player.
     * @return The current player ('X' or 'O').
     */
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the current game status.
     * @return The current game status (PLAYING, X_WINS, O_WINS, DRAW).
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Gets the game history object.
     * @return The GameHistory instance.
     */
    public GameHistory getGameHistory() {
        return gameHistory;
    }
}
