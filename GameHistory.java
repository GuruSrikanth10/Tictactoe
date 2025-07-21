// GameHistory.java
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the history of moves for replay functionality.
 */
class GameHistory {
    private List<Move> moves;
    private int replayIndex;

    public GameHistory() {
        this.moves = new ArrayList<>();
        this.replayIndex = -1; // -1 indicates not in replay mode or at start
    }

    /**
     * Adds a move to the history.
     * @param move The move to add.
     */
    public void addMove(Move move) {
        moves.add(move);
    }

    /**
     * Clears the game history.
     */
    public void clearHistory() {
        moves.clear();
        replayIndex = -1;
    }

    /**
     * Starts replay mode.
     */
    public void startReplay() {
        replayIndex = 0;
    }

    /**
     * Checks if there's a next move in replay.
     * @return true if there's a next move, false otherwise.
     */
    public boolean hasNextMove() {
        return replayIndex >= 0 && replayIndex < moves.size();
    }

    /**
     * Gets the next move in replay and advances the index.
     * @return The next move, or null if no more moves.
     */
    public Move getNextMove() {
        if (hasNextMove()) {
            Move nextMove = moves.get(replayIndex);
            replayIndex++;
            return nextMove;
        }
        return null;
    }

    /**
     * Checks if there's a previous move in replay.
     * @return true if there's a previous move, false otherwise.
     */
    public boolean hasPreviousMove() {
        return replayIndex > 0;
    }

    /**
     * Gets the previous move in replay and moves the index back.
     * @return The previous move, or null if no previous moves.
     */
    public Move getPreviousMove() {
        if (hasPreviousMove()) {
            replayIndex--;
            return moves.get(replayIndex); // Return the move at the new (decremented) index
        }
        return null;
    }

    /**
     * Gets the total number of moves recorded.
     * @return The number of moves.
     */
    public int getTotalMoves() {
        return moves.size();
    }

    /**
     * Gets the current replay index.
     * @return The current index in replay mode.
     */
    public int getReplayIndex() {
        return replayIndex;
    }
}
