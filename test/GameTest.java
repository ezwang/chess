import static org.junit.Assert.*;

import org.junit.*;

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
    public void testPawnFirstMoveBlocked() {
        state.setupEmptyBoard();

        Pawn pawn = new Pawn(true, state, new Location(0, 1));
        state.setPiece(pawn.getLocation(), pawn);
        Rook rook = new Rook(true, state, new Location(0, 2));
        state.setPiece(rook.getLocation(), rook);

        Set<Location> expected = new TreeSet<Location>();
        assertEquals(expected, pawn.getMovableLocations());
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
    public void testRookMove() {
        state.setupEmptyBoard();
        Rook rook = new Rook(true, state, new Location(0, 0));
        state.setPiece(rook.getLocation(), rook);

        Knight knight = new Knight(false, state, new Location(0, 3));
        state.setPiece(knight.getLocation(), knight);

        Knight knight2 = new Knight(true, state, new Location(3, 0));
        state.setPiece(knight2.getLocation(), knight2);

        Set<Location> expected = new TreeSet<Location>();
        expected.add(new Location(0, 1));
        expected.add(new Location(0, 2));
        expected.add(new Location(0, 3));
        expected.add(new Location(1, 0));
        expected.add(new Location(2, 0));
        assertEquals(expected, rook.getMovableLocations());
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
    public void testCheckingBlock() {
        state.setupEmptyBoard();
        King king = new King(false, state, new Location(0, 0));
        state.setPiece(king.getLocation(), king);
        Rook rook1 = new Rook(true, state, new Location(7, 0));
        state.setPiece(rook1.getLocation(), rook1);
        Rook rook2 = new Rook(true, state, new Location(7, 1));
        state.setPiece(rook2.getLocation(), rook2);

        Rook defender = new Rook(false, state, new Location(5, 7));
        state.setPiece(defender.getLocation(), defender);

        assertTrue(state.checkInCheck(false));
        Set<Move> expected = new TreeSet<Move>();
        expected.add(new Move(defender.getLocation(), new Location(5, 0)));
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

        assertEquals(new Location(3, 0), rook.getLocation());
        assertEquals(c, king.getLocation());
    }

    @Test
    public void testCastlingRight() {
        state.setupEmptyBoard();
        Location kingLoc = new Location(4, 0);
        King king = new King(true, state, kingLoc);
        state.setPiece(kingLoc, king);
        Location rookLoc = new Location(7, 0);
        Rook rook = new Rook(true, state, rookLoc);
        state.setPiece(rookLoc, rook);

        Location c = new Location(6, 0);
        assertTrue(state.getPiece(kingLoc).getMovableLocations().contains(c));

        state.move(kingLoc, c);

        assertEquals(king, state.getPiece(c));
        assertEquals(rook, state.getPiece(c.translate(-1, 0)));

        assertEquals(new Location(5, 0), rook.getLocation());
        assertEquals(c, king.getLocation());
    }

    @Test
    public void testRookMoveAfterCastling() {
        state.setupEmptyBoard();
        Location kingLoc = new Location(4, 0);
        state.setPiece(kingLoc, new King(true, state, kingLoc));
        Location rookLoc = new Location(7, 0);
        Rook rook = new Rook(true, state, rookLoc);
        state.setPiece(rookLoc, rook);

        Pawn p = new Pawn(true, state, new Location(5, 1));
        state.setPiece(p.getLocation(), p);

        Queen q = new Queen(true, state, new Location(3, 0));
        state.setPiece(q.getLocation(), q);

        Location c = new Location(6, 0);
        state.move(kingLoc, c);

        TreeSet<Location> expected = new TreeSet<Location>();
        expected.add(new Location(4, 0));
        assertEquals(expected, rook.getMovableLocations());
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

    @Test
    public void testCastlingLineOfFire() {
        state.setupEmptyBoard();
        Location kingLoc = new Location(4, 0);
        state.setPiece(kingLoc, new King(true, state, kingLoc));
        Location rookLoc = new Location(0, 0);
        state.setPiece(rookLoc, new Rook(true, state, rookLoc));

        Location enemyRookLoc = new Location(1, 5);
        state.setPiece(enemyRookLoc, new Rook(false, state, enemyRookLoc));

        Location c = new Location(2, 0);
        assertTrue(state.isProtected(new Location(1, 0), false));
        assertFalse(state.getPiece(kingLoc).getMovableLocations().contains(c));
    }

    @Test
    public void testCastlingUnderCheck() {
        state.setupEmptyBoard();
        Location kingLoc = new Location(4, 0);
        state.setPiece(kingLoc, new King(true, state, kingLoc));
        Location rookLoc = new Location(0, 0);
        state.setPiece(rookLoc, new Rook(true, state, rookLoc));

        Location enemyRookLoc = new Location(4, 5);
        state.setPiece(enemyRookLoc, new Rook(false, state, enemyRookLoc));

        Location c = new Location(2, 0);
        assertTrue(state.isProtected(kingLoc, false));
        assertFalse(state.getPiece(kingLoc).getMovableLocations().contains(c));
    }

    @Test
    public void testFourMoveCheckmate() {
        state.move(new Location(5, 1), new Location(5, 2));
        state.move(new Location(4, 6), new Location(4, 4));
        state.move(new Location(6, 1), new Location(6, 3));
        state.move(new Location(3, 7), new Location(7, 3));

        assertTrue(state.checkInCheck(true));
        assertEquals(0, state.getPossibleMovesUnderCheck(true).size());
    }

    @Test
    public void testKingMovableLocations() {
        state.move(new Location(4, 1), new Location(4, 3));
        state.move(new Location(4, 6), new Location(4, 4));
        state.move(new Location(5, 0), new Location(2, 3));
        state.move(new Location(5, 7), new Location(2, 4));

        Set<Location> expected = new TreeSet<Location>();
        expected.add(new Location(4, 6));
        expected.add(new Location(5, 7));
        Set<Location> expected2 = new TreeSet<Location>();
        expected2.add(new Location(5, 0));
        expected2.add(new Location(4, 1));
        assertEquals(expected, state.getKing(false).getMovableLocations());
        assertEquals(expected2, state.getKing(true).getMovableLocations());
    }
}