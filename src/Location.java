public class Location {
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

    @Override
    public String toString() {
        return getLetterFromNumber(x) + (y + 1);
    }
}
