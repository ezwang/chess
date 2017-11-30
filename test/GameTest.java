import static org.junit.Assert.*;

import org.junit.*;
import sun.reflect.generics.tree.Tree;

import java.util.*;

/**
 * Test cases for the game.
 */
public class GameTest {
    GameState state;

    @Before
    public void setUp() {
        this.state = new GameState("Test Player", "Test Opponent", true);
    }

    @Test
    public void testPawnFirstMove() {
        Set<Location> expected = new TreeSet<Location>();
        expected.add(new Location(0, 2));
        expected.add(new Location(0, 3));
        assertEquals(expected, state.getPiece(0, 1).getMovableLocations());
    }

    @Test
    public void testPawnAfterFirstMove() {
        Set<Location> expected = new TreeSet<Location>();
        expected.add(new Location(0, 3));
        state.move(new Location(0, 1), new Location(0, 2));
        assertEquals(expected, state.getPiece(0, 2).getMovableLocations());
    }

    @Test
    public void testEnPassant() {
        state.setupEmptyBoard();
        Pawn enemyPawn = new Pawn(false, state, new Location(0, 6));
        state.setPiece(enemyPawn.getLocation(), enemyPawn);
        Pawn friendlyPawn = new Pawn(true, state, new Location(1,4));
        state.setPiece(friendlyPawn.getLocation(), friendlyPawn);

        state.move(enemyPawn.getLocation(), new Location(0, 4));

        assertTrue(friendlyPawn.getMovableLocations().contains(new Location(0, 5)));

        state.move(friendlyPawn.getLocation(), new Location(0, 5));

        assertEquals(null, state.getPiece(0, 4));
        assertEquals(friendlyPawn, state.getPiece(0, 5));

        state.undo();

        assertEquals(enemyPawn, state.getPiece(0, 4));
        assertEquals(friendlyPawn, state.getPiece(1, 4));
    }

    @Test
    public void testKnightMove() {
        Set<Location> expected = new TreeSet<Location>();
        expected.add(new Location(0, 2));
        expected.add(new Location(2, 2));
        assertEquals(expected, state.getPiece(1, 0).getMovableLocations());
    }

    @Test
    public void testProtectedLocation() {
        state.setupEmptyBoard();
        Bishop bishop = new Bishop(true, state, new Location(7, 7));
        state.setPiece(bishop.getLocation(), bishop);

        assertTrue(state.isProtected(new Location(0, 0), true));
        assertTrue(state.isProtected(new Location(1, 1), true));
    }

    @Test
    public void testProtectedOccupiedLocation() {
        Queen queen = new Queen(true, state, new Location(1, 1));
        state.setPiece(queen.getLocation(), queen);
        Bishop bishop = new Bishop(true, state, new Location(7, 7));
        state.setPiece(bishop.getLocation(), bishop);

        assertTrue(state.isProtected(new Location(1, 1), true));
    }

    @Test
    public void testCheckmate() {
        state.setupEmptyBoard();
        King king = new King(false, state, new Location(0, 0));
        state.setPiece(king.getLocation(), king);
        Queen queen = new Queen(true, state, new Location(1, 1));
        state.setPiece(queen.getLocation(), queen);
        Bishop bishop = new Bishop(true, state, new Location(7, 7));
        state.setPiece(bishop.getLocation(), bishop);

        assertTrue(state.checkInCheck(false));
        assertEquals(new TreeSet<Move>(), state.getPossibleMovesUnderCheck(false));
    }

    @Test
    public void testCheckKingCapture() {
        state.setupEmptyBoard();
        King king = new King(false, state, new Location(0, 0));
        state.setPiece(king.getLocation(), king);
        Queen queen = new Queen(true, state, new Location(1, 1));
        state.setPiece(queen.getLocation(), queen);

        assertTrue(state.checkInCheck(false));
        Set<Move> expected = new TreeSet<Move>();
        expected.add(new Move(king.getLocation(), queen.getLocation()));
        assertEquals(expected, state.getPossibleMovesUnderCheck(false));
    }

    @Test
    public void testCastlingLeft() {
        state.setupEmptyBoard();
        Location kingLoc = new Location(4, 0);
        King king = new King(true, state, kingLoc);
        state.setPiece(kingLoc, king);
        Location rookLoc = new Location(0, 0);
        Rook rook = new Rook(true, state, rookLoc);
        state.setPiece(rookLoc, rook);

        Location c = new Location(2, 0);
        assertTrue(state.getPiece(kingLoc).getMovableLocations().contains(c));

        state.move(kingLoc, c);
        assertEquals(king, state.getPiece(c));
        assertEquals(rook, state.getPiece(c.translate(1, 0)));
    }

    @Test
    public void testCastlingRight() {
        state.setupEmptyBoard();
        Location kingLoc = new Location(4, 0);
        state.setPiece(kingLoc, new King(true, state, kingLoc));
        Location rookLoc = new Location(7, 0);
        state.setPiece(rookLoc, new Rook(true, state, rookLoc));

        Location c = new Location(6, 0);
        assertTrue(state.getPiece(kingLoc).getMovableLocations().contains(c));
    }

    @Test
    public void testCastlingAfterMove() {
        state.setupEmptyBoard();
        Location kingLoc = new Location(4, 0);
        state.setPiece(kingLoc, new King(true, state, kingLoc));
        Location rookLoc = new Location(0, 0);
        state.setPiece(rookLoc, new Rook(true, state, rookLoc));

        state.move(rookLoc, new Location(0, 3));
        state.move(new Location(0, 3), rookLoc);

        Location c = new Location(2, 0);
        assertFalse(state.getPiece(kingLoc).getMovableLocations().contains(c));
    }

    @Test
    public void testCastlingPiecesBetween() {
        state.setupEmptyBoard();
        Location kingLoc = new Location(4, 0);
        state.setPiece(kingLoc, new King(true, state, kingLoc));
        Location rookLoc = new Location(0, 0);
        state.setPiece(rookLoc, new Rook(true, state, rookLoc));

        Location knightLoc = new Location(1, 0);
        state.setPiece(knightLoc, new Knight(true, state, knightLoc));

        Location c = new Location(2, 0);
        assertFalse(state.getPiece(kingLoc).getMovableLocations().contains(c));
    }
}