public class Move implements Comparable<Move> {
    private final Location from, to;

    public Move(Location from, Location to) {
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
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
        return from + " -> " + to;
    }
}
