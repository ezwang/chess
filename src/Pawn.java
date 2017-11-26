import java.util.Set;
import java.util.TreeSet;

public class Pawn extends Piece {
    public Pawn(boolean isWhite, GameState state, Location loc) {
        super(isWhite, state, loc);
    }

    @Override
    public Set<Location> getMovableLocations() {
        // TODO: implement en passant
        Set<Location> moves = new TreeSet<Location>();
        boolean isInitial = false;
        if (isWhite) {
            if (loc.getY() == 1) {
                isInitial = true;
            }
            if (gameState.onBoard(loc.getX(), loc.getY() + 1)) {
                if (!gameState.isSameColor(loc.getX(), loc.getY() + 1, isWhite)) {
                    moves.add(loc.translate(0, 1));
                }
                if (isInitial) {
                    if (!gameState.isOccupied(loc.getX(), loc.getY() + 1) && !gameState.isSameColor(loc.getX(), loc.getY() + 2, isWhite)) {
                        moves.add(loc.translate(0, 2));
                    }
                }
            }
        }
        else {
            if (loc.getY() == 6) {
                isInitial = true;
            }
            if (gameState.onBoard(loc.getX(), loc.getY() - 1)) {
                if (!gameState.isSameColor(loc.getX(), loc.getY() - 1, isWhite)) {
                    moves.add(loc.translate(0, -1));
                }
                if (isInitial) {
                    if (!gameState.isOccupied(loc.getX(), loc.getY() - 1) && !gameState.isSameColor(loc.getX(), loc.getY() - 2, isWhite)) {
                        moves.add(loc.translate(0, -2));
                    }
                }
            }
        }
        return moves;
    }

    @Override
    public String getNotationSymbol() {
        return "p";
    }
}
