/**
 * Represents a location on the chessboard. Does not have to be a real
 * location (ex: could have negative values).
 */
public class Location implements Comparable<Location> {
    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Gets the chess notation symbol (a to h) for a certain number.
     * @param num The value of the vertical position, starting at 0.
     * @return The chess notation for that value. Returns "?" for an invalid
     * value.
     */
    private static String getLetterFromNumber(int num) {
        switch (num) {
            case 0:
                return "a";
            case 1:
                return "b";
            case 2:
                return "c";
            case 3:
                return "d";
            case 4:
                return "e";
            case 5:
                return "f";
            case 6:
                return "g";
            case 7:
                return "h";
            default:
                return "?";
        }
    }

    /**
     * Create a new location by shifting the current location.
     * @param x How much to shift to the right.
     * @param y How much to shift to the top.
     * @return A new location shifted by the specified amount.
     */
    public Location translate(int x, int y) {
        return new Location(this.x + x, this.y + y);
    }

    /**
     * Create a new location by shifting the current location.
     * @param l The location to shift by.
     * @return A new location shifted by the specified amount.
     */
    public Location translate(Location l) {
        return new Location(this.x + l.getX(), this.y + l.getY());
    }

    @Override
    public String toString() {
        return getLetterFromNumber(x) + (y + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Location l = (Location) o;
        return l.x == x && l.y == y;
    }

    @Override
    public int compareTo(Location l) {
        if (l.y == y) {
            return x - l.x;
        }
        return y - l.y;
    }
}
