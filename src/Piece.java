import java.util.*;

public abstract class Piece {
    protected Location loc;

    protected boolean isWhite;
    protected GameState gameState;

    public Piece(boolean isWhite, GameState state, Location loc) {
        this.isWhite = isWhite;
        this.gameState = state;
        this.loc = loc;
    }

    public abstract Set<Location> getMovableLocations();
    public abstract String getNotationSymbol();

    public boolean getIsWhite() {
        return isWhite;
    }

    public void setNewLocation(Location loc) {
        this.loc = loc;
    }

    protected Set<Location> getLocationsByDirection(Location[] directions) {
        Set<Location> moves = new TreeSet<Location>();
        for (Location dir : directions) {
            Location n = loc.translate(dir);
            while (gameState.onBoard(n.getX(), n.getY()) && !gameState.isSameColor(n.getX(), n.getY(), isWhite)) {
                moves.add(n);
                if (gameState.isOccupied(n.getX(), n.getY())) {
                    break;
                }
                n = n.translate(dir);
            }
        }
        return moves;
    }
}
