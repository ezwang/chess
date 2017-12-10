import java.util.Set;

/**
 * Used by kings and pawns, since they have special capture conditions.
 * Kings cannot capture when it would put them in danger.
 * Pawns can only capture diagonally, not by moving straight forward.
 */
public interface PieceCaptureCondition {
    public Set<Location> getPotentialCapturableLocations();
}
