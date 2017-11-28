import java.util.Set;
import java.util.TreeSet;

public class King extends Piece {
    public King(boolean isWhite, GameState state, Location loc) {
        super(isWhite, state, loc);
    }

    @Override
    public Set<Location> getMovableLocations() {
        // TODO: implement castling
        Set<Location> moves = new TreeSet<Location>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                Location n = loc.translate(i, j);
                if (gameState.onBoard(n.getX(), n.getY()) && !gameState.isSameColor(n.getX(), n.getY(), getIsWhite())) {
                    moves.add(n);
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
        return "k";
    }
}
