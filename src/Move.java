/**
 * Represents a move. Contains information about the piece that was
 * originally where the piece will move to in case of captures. Contains
 * information about what piece was moved in the case of pawn promotion.
 */
public class Move implements Comparable<Move> {
    private final Location from, to;
    private final Piece originalPiece, newPiece;

    public Move(Location from, Location to) {
        this.from = from;
        this.to = to;
        this.originalPiece = null;
        this.newPiece = null;
    }

    public Move(Location from, Location to, Piece fromPiece, Piece toPiece) {
        this.from = from;
        this.to = to;
        this.originalPiece = fromPiece;
        this.newPiece = toPiece;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public Piece getOriginalPiece() {
        return originalPiece;
    }

    public Piece getNewPiece() {
        return newPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        Move m = (Move)o;
        return from.equals(m.from) && to.equals(m.to);
    }

    @Override
    public int compareTo(Move move) {
        int a = from.compareTo(move.from);
        if (a == 0) {
            return to.compareTo(move.to);
        }
        return a;
    }

    @Override
    public String toString() {
        return from + " " + to;
    }
}
