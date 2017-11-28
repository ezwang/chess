import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Knight extends Piece {
    public Knight(boolean isWhite, GameState state, Location loc) {
        super(isWhite, state, loc);
    }

    @Override
    public Set<Location> getMovableLocations() {
        Set<Location> moves = new TreeSet<Location>();
        moves.add(loc.translate(1, 2));
        moves.add(loc.translate(-1, 2));
        moves.add(loc.translate(1, -2));
        moves.add(loc.translate(-1, -2));
        moves.add(loc.translate(2, 1));
        moves.add(loc.translate(-2, 1));
        moves.add(loc.translate(2, -1));
        moves.add(loc.translate(-2, -1));
        Iterator<Location> i = moves.iterator();
        while (i.hasNext()) {
            Location l = i.next();
            if (!gameState.onBoard(l.getX(), l.getY()) || gameState.isSameColor(l.getX(), l.getY(), getIsWhite())) {
                i.remove();
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
        return "n";
    }
}
