public class Location implements Comparable<Location> {
    private int x;
    private int y;

    public Location(int x, int y) {
        if (x < 0 || y < 0 || x > 7 || y > 7) {
            throw new IllegalArgumentException();
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static String getLetterFromNumber(int num) {
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

    public Location translate(int x, int y) {
        return new Location(this.x + x, this.y + y);
    }

    public Location translate(Location l) {
        return new Location(this.x + l.getX(), this.y + l.getY());
    }

    @Override
    public String toString() {
        return getLetterFromNumber(x) + (y + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != Location.class) {
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
