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
}
