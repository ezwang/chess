import java.util.*;

/**
 * Represents a piece on the chessboard.
 */
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

    /**
     * Get the color of this piece.
     * @return True if this piece is white, false if this piece is black.
     */
    public boolean getIsWhite() {
        return isWhite;
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }

    /**
     * Used by the queen, rook, and bishop classes to calculate movable
     * locations according to a set of directions that the piece can move.
     * @param directions A set of locations representing directions that the
     *                   piece can move in (ex: (-1, 0)).
     * @return A set of locations that the piece can move to.
     */
    protected Set<Location> getLocationsByDirection(Location[] directions) {
        Set<Location> moves = new TreeSet<Location>();
        for (Location dir : directions) {
            Location n = loc.translate(dir);
            while (gameState.onBoard(n) && !gameState.isSameColor(n, isWhite)) {
                moves.add(n);
                if (gameState.isOccupied(n)) {
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

    /**
     * Given a set of directions, see which direction has a path from the
     * current piece to the target location.
     * @param dir A set of directions to try.
     * @param dest The target location.
     * @return A path from the current piece to the target location,
     * excluding the current piece and the target location.
     */
    protected Set<Location> tryDirectionsPathToPiece(Location[] dir, Location
            dest) {
        for (Location l : dir) {
            if (isDirectionCorrect(l, dest)) {
                TreeSet<Location> out = new TreeSet<Location>();
                Location curr = this.loc.translate(l);
                while (!curr.equals(dest)) {
                    out.add(curr);
                    curr = curr.translate(l);
                }
                return out;
            }
        }
        return new TreeSet<Location>();
    }

    /**
     * Check if a location lies in a particular direction relative to the
     * current piece.
     * @param dir The direction to test.
     * @param dest The location that we are looking for.
     * @return Whether the location lies in the direction.
     */
    private boolean isDirectionCorrect(Location dir, Location dest) {
        Location curr = this.loc;
        while (gameState.onBoard(curr)) {
            if (dest.equals(curr)) {
                return true;
            }
            curr = curr.translate(dir);
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
