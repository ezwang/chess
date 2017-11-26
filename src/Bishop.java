import java.util.Set;
import java.util.TreeSet;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, GameState state, Location loc) {
        super(isWhite, state, loc);
    }

    @Override
    public Set<Location> getMovableLocations() {
        Location[] directions = new Location[] { new Location(1, 1), new Location(-1, 1), new Location(-1, -1), new Location(1, -1) };
        Set<Location> moves = new TreeSet<Location>();
        for (Location dir : directions) {
            Location n = loc.translate(dir);
            while (gameState.onBoard(n.getX(), n.getY()) && !gameState.isSameColor(n.getX(), n.getY(), isWhite)) {
                moves.add(n);
                n = loc.translate(dir);
            }
        }
        return moves;
    }

    @Override
    public String getNotationSymbol() {
        return "b";
    }
}
