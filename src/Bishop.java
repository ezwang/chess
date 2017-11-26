import java.util.Set;
import java.util.TreeSet;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, GameState state, Location loc) {
        super(isWhite, state, loc);
    }

    @Override
    public Set<Location> getMovableLocations() {
        Location[] directions = new Location[] { new Location(1, 1), new Location(-1, 1), new Location(-1, -1), new Location(1, -1) };
        return getLocationsByDirection(directions);
    }

    @Override
    public String getNotationSymbol() {
        return "b";
    }
}
