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
        }
        return moves;
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
