/**
 * Represents a move. Contains information about the piece that was
 * originally where the piece will move to in case of captures. Contains
 * information about what piece was moved in the case of pawn promotion.
 */
public class Move implements Comparable<Move> {
    private final Location from, to;
    private final Piece originalPiece, newPiece;
    private final boolean pawnPromotion;

    /**
     * Represents a move from one location to another.
     * @param from The original location.
     * @param to The new location.
     */
    public Move(Location from, Location to) {
        this.from = from;
        this.to = to;
        this.originalPiece = null;
        this.newPiece = null;
        this.pawnPromotion = false;
    }

    /**
     * Represents a move with additional information.
     * @param from The original location.
     * @param to The new location.
     * @param fromPiece The piece that was originally in the the new location.
     * @param toPiece The piece that is now in the new location.
     * @param isPawnPromotion Whether or not this move is a pawn promotion.
     */
    public Move(Location from, Location to, Piece fromPiece, Piece toPiece, boolean isPawnPromotion) {
        this.from = from;
        this.to = to;
        this.originalPiece = fromPiece;
        this.newPiece = toPiece;
        this.pawnPromotion = isPawnPromotion;
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

    public boolean isPawnPromotion() {
        return pawnPromotion;
    }
}
