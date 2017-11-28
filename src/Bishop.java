import java.util.Set;

public class Bishop extends Piece {
    private static final Location[] DIRECTIONS = new Location[] { new Location(1, 1), new Location(-1, 1), new Location(-1, -1), new Location(1, -1) };

    public Bishop(boolean isWhite, GameState state, Location loc) {
        super(isWhite, state, loc);
    }

    @Override
    public Set<Location> getMovableLocations() {
        return getLocationsByDirection(DIRECTIONS);
    }

    @Override
    public Set<Location> getPathToPiece(Location dest) {
        return tryDirectionsPathToPiece(DIRECTIONS, dest);
    }

    @Override
    public String getNotationSymbol() {
        return "b";
    }
}
