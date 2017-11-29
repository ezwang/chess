/**
 * A piece that keeps track of how many times it was moved. Necessary for
 * implementing castling, since if the King/Rook moves then castling is no
 * longer an option. It is necessary to keep a count of how many moves were
 * done in order to implement undo correctly.
 */
public abstract class PieceFirstMove extends Piece {
    private int moveCounter;

    public PieceFirstMove(boolean isWhite, GameState state, Location loc) {
        super(isWhite, state, loc);
        moveCounter = 0;
    }

    public boolean movedBefore() {
        return moveCounter != 0;
    }

    public void move() {
        moveCounter += 1;
    }

    public void unmove() {
        moveCounter -= 1;
    }
}
