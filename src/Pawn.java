import java.util.Set;
import java.util.TreeSet;

public class Pawn extends Piece {
    public Pawn(boolean isWhite, GameState state, Location loc) {
        super(isWhite, state, loc);
    }

    @Override
    public Set<Location> getMovableLocations() {
        Set<Location> moves = new TreeSet<Location>();
        boolean isInitial = false;
        // check en passant
        Move m = gameState.getLastMove();
        boolean canEnPassant = movedTwoSteps(m) && nextTo(m.getTo(), loc);
        if (getIsWhite()) {
            if (loc.getY() == 1) {
                isInitial = true;
            }
            if (gameState.onBoard(loc.getX(), loc.getY() + 1)) {
                if (!gameState.isOccupied(loc.getX(), loc.getY() + 1)) {
                    moves.add(loc.translate(0, 1));
                }
                if (isInitial) {
                    if (!gameState.isOccupied(loc.getX(), loc.getY() + 2)) {
                        moves.add(loc.translate(0, 2));
                    }
                }
                if (gameState.isDifferentColor(loc.getX() + 1, loc.getY() + 1, getIsWhite())) {
                    moves.add(loc.translate(1, 1));
                }
                if (gameState.isDifferentColor(loc.getX() - 1, loc.getY() + 1, getIsWhite())) {
                    moves.add(loc.translate(-1, 1));
                }
            }
            if (canEnPassant) {
                moves.add(loc.translate(m.getTo().getX() - loc.getX(), 1));
            }
        }
        else {
            if (loc.getY() == 6) {
                isInitial = true;
            }
            if (gameState.onBoard(loc.getX(), loc.getY() - 1)) {
                if (!gameState.isOccupied(loc.getX(), loc.getY() - 1)) {
                    moves.add(loc.translate(0, -1));
                }
                if (isInitial) {
                    if (!gameState.isOccupied(loc.getX(), loc.getY() - 2)) {
                        moves.add(loc.translate(0, -2));
                    }
                }
                if (gameState.isDifferentColor(loc.getX() + 1, loc.getY() - 1, getIsWhite())) {
                    moves.add(loc.translate(1, -1));
                }
                if (gameState.isDifferentColor(loc.getX() - 1, loc.getY() - 1, getIsWhite())) {
                    moves.add(loc.translate(-1, -1));
                }
            }
            if (canEnPassant) {
                moves.add(loc.translate(m.getTo().getX() - loc.getX(), -1));
            }
        }
        return moves;
    }

    /**
     * Check if a move is a pawn moving forward 2 steps.
     * @param m The move to check.
     * @return Whether or not the move is a pawn moving forward 2 steps.
     */
    private boolean movedTwoSteps(Move m) {
        if (m == null) {
            return false;
        }
        Piece p = m.getNewPiece();
        if (p instanceof Pawn) {
            return Math.abs(m.getFrom().getY() - m.getTo().getY()) == 2;
        }
        return false;
    }

    /**
     * Check to see if two locations have the same Y value and have X values
     * that are next to each other.
     * @param p1 The first location.
     * @param p2 The second location.
     * @return If the locations are next to each other.
     */
    private boolean nextTo(Location p1, Location p2) {
        return p1.getY() == p2.getY() && Math.abs(p2.getX() - p1.getX()) == 1;
    }

    @Override
    public Set<Location> getPathToPiece(Location dest) {
        return new TreeSet<Location>();
    }

    @Override
    public String getNotationSymbol() {
        return "p";
    }
}
