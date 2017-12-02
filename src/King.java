import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class King extends PieceFirstMove {
    public King(boolean isWhite, GameState state, Location loc) {
        super(isWhite, state, loc);
    }

    @Override
    public Set<Location> getCapturableLocations() {
        return getLocations(false);
    }

    @Override
    public Set<Location> getMovableLocations() {
        return getLocations(true);
    }

    private Set<Location> getLocations(boolean includeCastling) {
        Set<Location> moves = new TreeSet<Location>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                Location n = getLocation().translate(i, j);
                if (gameState.onBoard(n) && !gameState.isSameColor(n, getIsWhite())) {
                    if (!gameState.isProtectedThroughPiece(n, !getIsWhite(), this)) {
                        moves.add(n);
                    }
                }
            }
        }
        // castling
        if (!movedBefore() && includeCastling) {
            List<Rook> rooks = gameState.getRooks(getIsWhite());
            for (Rook r : rooks) {
                if (r.movedBefore()) {
                    continue;
                }
                boolean pieceBetween = false;
                Location tmp = loc;
                // if under check, can't castle
                if (gameState.isProtected(tmp, !getIsWhite())) {
                    pieceBetween = true;
                }
                if (r.getLocation().getX() > getLocation().getX()) {
                    tmp = tmp.translate(1, 0);
                    while (!tmp.equals(r.getLocation())) {
                        // can't castle with pieces blocking
                        if (gameState.isOccupied(tmp)) {
                            pieceBetween = true;
                            break;
                        }
                        // can't castle in line of check
                        if (gameState.isProtected(tmp, !getIsWhite())) {
                            pieceBetween = true;
                            break;
                        }
                        tmp = tmp.translate(1, 0);
                    }
                    if (!pieceBetween) {
                        moves.add(loc.translate(2, 0));
                    }
                }
                else {
                    tmp = tmp.translate(-1, 0);
                    while (!tmp.equals(r.getLocation())) {
                        // can't castle with pieces blocking
                        if (gameState.isOccupied(tmp)) {
                            pieceBetween = true;
                            break;
                        }
                        // can't castle in line of check
                        if (gameState.isProtected(tmp, !getIsWhite())) {
                            pieceBetween = true;
                            break;
                        }
                        tmp = tmp.translate(-1, 0);
                    }

                    if (!pieceBetween) {
                        moves.add(loc.translate(-2, 0));
                    }
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
