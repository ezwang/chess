import java.util.*;

public interface Piece {
    public Set<Location> getMovableLocations();
    public String getNotationSymbol();
}
