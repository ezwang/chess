import java.util.*;

public abstract class Piece implements Comparable<Piece> {
    protected Location loc;

    private boolean isWhite;
    protected transient GameState gameState;

    public Piece(boolean isWhite, GameState state, Location loc) {
        this.isWhite = isWhite;
        this.gameState = state;
        this.loc = loc;
    }

    /**
     * Get all the locations this piece can move to.
     * @return A set of locations where this piece can move.
     */
    public abstract Set<Location> getMovableLocations();

    /**
     * Get all of the locations between this piece and the target piece.
     * Returns an empty set if the piece is not reachable, or if the piece
     * is not blockable. This method is used for check/checkmate checks.
     * @param dest The target piece.
     * @return The locations between this piece and target.
     */
    public abstract Set<Location> getPathToPiece(Location dest);

    /**
     * Get the character symbol for this piece.
     * @return The character symbol for this piece.
     */
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

    public Location getLocation() {
        return loc;
    }

    protected Set<Location> tryDirectionsPathToPiece(Location[] loc, Location dest) {
        for (Location l : loc) {
            if (isDirectionCorrect(l, dest)) {
                TreeSet<Location> out = new TreeSet<Location>();
                Location curr = this.loc.translate(l.getX(), l.getY());
                while (!curr.equals(dest)) {
                    out.add(curr);
                    curr = curr.translate(l.getX(), l.getY());
                }
                return out;
            }
        }
        return new TreeSet<Location>();
    }

    private boolean isDirectionCorrect(Location dir, Location dest) {
        Location curr = this.loc;
        while (gameState.onBoard(curr.getX(), curr.getY())) {
            if (dest.equals(curr)) {
                return true;
            }
            curr = curr.translate(dir.getX(), dir.getY());
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Piece p = (Piece)o;
        return loc.equals(p.loc);
    }

    @Override
    public int compareTo(Piece p) {
        return loc.compareTo(p.loc);
    }
}
