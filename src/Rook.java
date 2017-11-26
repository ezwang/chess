import java.util.Set;
import java.util.TreeSet;

public class Rook extends Piece {
    public Rook(boolean isWhite, GameState state, Location loc) {
        super(isWhite, state, loc);
    }

    @Override
    public Set<Location> getMovableLocations() {
        Location[] directions = new Location[] { new Location(1, 0), new Location(0, 1), new Location(-1, 0), new Location(0, -1) };
        return getLocationsByDirection(directions);
    }

    @Override
    public String getNotationSymbol() {
        return "r";
    }
}
