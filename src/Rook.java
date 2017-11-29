import java.util.Set;

public class Rook extends PieceFirstMove {
    private static final Location[] DIRECTIONS = new Location[] { new Location(1, 0), new Location(0, 1), new Location(-1, 0), new Location(0, -1) };

    public Rook(boolean isWhite, GameState state, Location loc) {
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
        return "r";
    }
}
