import java.util.*;

/**
 * Stores information about a chess game.
 */
public class GameState {
    private Piece[][] board;
    private String player, opponent;
    private boolean isWhite;
    private boolean isWhiteTurn;

    private LinkedList<Move> history;

    public GameState(String player, String opponent, boolean isWhite) {
        this.player = player;
        this.opponent = opponent;
        this.isWhite = isWhite;
        this.isWhiteTurn = true;

        board = new Piece[8][8];
        history = new LinkedList<Move>();

        setupBoard();
    }

    public String getPlayerNickname() {
        return player;
    }

    public String getOpponentNickname() {
        return opponent;
    }

    public boolean playerIsWhite() {
        return isWhite;
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public boolean isPlayerTurn() {
        return isWhiteTurn == isWhite;
    }

    /**
     * Setup a board with no pieces. Used for testing.
     */
    public void setupEmptyBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
    }

    /**
     * Setup a board with all of the pieces in the standard starting position.
     */
    public void setupBoard() {
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
        for (int j = 0; j < 8; j++) {
            board[1][j] = new Pawn(true, this, new Location(j, 1));
            board[6][j] = new Pawn(false, this, new Location(j, 6));
        }
        board[0][0] = new Rook(true, this, new Location(0, 0));
        board[0][7] = new Rook(true, this, new Location(7, 0));
        board[0][1] = new Knight(true, this, new Location(1, 0));
        board[0][6] = new Knight(true, this, new Location(6, 0));
        board[0][2] = new Bishop(true, this, new Location(2, 0));
        board[0][5] = new Bishop(true, this, new Location(5, 0));
        board[0][3] = new Queen(true, this, new Location(3, 0));
        board[0][4] = new King(true, this, new Location(4, 0));

        board[7][0] = new Rook(false, this, new Location(0, 7));
        board[7][7] = new Rook(false, this, new Location(7, 7));
        board[7][1] = new Knight(false, this, new Location(1, 7));
        board[7][6] = new Knight(false, this, new Location(6, 7));
        board[7][2] = new Bishop(false, this, new Location(2, 7));
        board[7][5] = new Bishop(false, this, new Location(5, 7));
        board[7][3] = new Queen(false, this, new Location(3, 7));
        board[7][4] = new King(false, this, new Location(4, 7));
    }

    public Piece getPiece(int x, int y) {
        if (x < 0 || y < 0 || x > 7 || y > 7) {
            return null;
        }
        return board[y][x];
    }

    public Piece getPiece(Location loc) {
        return getPiece(loc.getX(), loc.getY());
    }

    public boolean onBoard(int x, int y) {
        return x >= 0 && y >= 0 && x < 8 && y < 8;
    }

    public boolean onBoard(Location loc) {
        return onBoard(loc.getX(), loc.getY());
    }

    public boolean isOccupied(int x, int y) {
        return onBoard(x, y) && board[y][x] != null;
    }

    public boolean isOccupied(Location loc) {
        return isOccupied(loc.getX(), loc.getY());
    }

    public boolean isSameColor(int x, int y, boolean isWhite) {
        return isOccupied(x, y) && board[y][x].getIsWhite() == isWhite;
    }

    public boolean isSameColor(Location loc, boolean isWhite) {
        return isSameColor(loc.getX(), loc.getY(), isWhite);
    }

    public boolean isDifferentColor(int x, int y, boolean isWhite) {
        return isOccupied(x, y) && board[y][x].getIsWhite() != isWhite;
    }

    public boolean isDifferentColor(Location loc, boolean isWhite) {
        return isDifferentColor(loc.getX(), loc.getY(), isWhite);
    }

    public void setPiece(int x, int y, Piece p) {
        if (x >= 0 && y >= 0 && x < 8 && y < 8) {
            board[y][x] = p;
        }
        else {
            throw new IllegalArgumentException(String.format("Cannot place piece at (%d, %d)!", x, y));
        }
    }

    public void setPiece(Location loc, Piece p) {
        setPiece(loc.getX(), loc.getY(), p);
    }

    private Set<Piece> getPiecesByColor(boolean isWhite) {
        Set<Piece> pieces = new TreeSet<Piece>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = board[j][i];
                if (p != null && p.getIsWhite() == isWhite) {
                    pieces.add(p);
                }
            }
        }
        return pieces;
    }

    /**
     * Get the King from the chessboard.
     * @param isWhite Which player's king to get.
     * @return The King.
     */
    public King getKing(boolean isWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[j][i] instanceof King) {
                    if (board[j][i].getIsWhite() == isWhite) {
                        return (King)board[j][i];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get the rooks from the chessboard.
     * @param isWhite Which player's king to get.
     * @return A set of rooks.
     */
    public List<Rook> getRooks(boolean isWhite) {
        List<Rook> rooks = new LinkedList<Rook>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[j][i] instanceof Rook) {
                    if (board[j][i].getIsWhite() == isWhite) {
                        rooks.add((Rook)board[j][i]);
                    }
                }
            }
        }
        return rooks;
    }

    /**
     * Checks if a player is in check.
     * @param isWhite Which player to check.
     * @return Whether or not the player is in check.
     */
    public boolean checkInCheck(boolean isWhite) {
        Set<Piece> pieces = getPiecesByColor(!isWhite);
        Location king = getKing(isWhite).getLocation();
        for (Piece p : pieces) {
            Set<Location> locs = p.getMovableLocations();
            if (locs.contains(king)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check to see if a location can be captured in one turn.
     * @param loc The location to check.
     * @param isWhite If protected by white or black.
     * @return Whether this location is protected.
     */
    public boolean isProtected(Location loc, boolean isWhite) {
        Piece temp = getPiece(loc);
        setPiece(loc, null);
        for (Piece p : getPiecesByColor(isWhite)) {
            Set<Location> locs = p.getCapturableLocations();
            if (locs.contains(loc)) {
                setPiece(loc, temp);
                return true;
            }
        }
        setPiece(loc, temp);
        return false;
    }

    /**
     * Return all possible moves that can escape check. If the set is empty,
     * then the game is over (checkmate).
     * @param isWhite Which player to use.
     * @return A set of all allowed moves to escape check.
     */
    public Set<Move> getPossibleMovesUnderCheck(boolean isWhite) {
        Set<Move> out = new TreeSet<Move>();
        King king = getKing(isWhite);
        Set<Location> routes = king.getMovableLocations();
        Location curr = king.getLocation();
        Set<Piece> opponentPieces = getPiecesByColor(!isWhite);
        Set<Piece> friendlyPieces = getPiecesByColor(isWhite);
        friendlyPieces.remove(king);
        Set<Piece> mustCapture = new TreeSet<Piece>();
        for (Piece p : opponentPieces) {
            Set<Location> locs = p.getMovableLocations();
            for (Location bad : locs) {
                routes.remove(bad);
                if (bad.equals(curr)) {
                    mustCapture.add(p);
                }
            }
        }
        // case where you can run away
        for (Location route : routes) {
            if (isDifferentColor(route, isWhite) && isProtected(route, !isWhite)) {
                continue;
            }
            out.add(new Move(curr, route));
        }
        // case where you can capture opponent
        // case where you can block opponent
        if (mustCapture.size() == 1) {
            Piece attackPiece = mustCapture.iterator().next();
            Set<Location> blocks = attackPiece.getPathToPiece(curr);
            Location gl = attackPiece.getLocation();
            for (Piece fp : friendlyPieces) {
                Set<Location> locs = fp.getMovableLocations();
                for (Location good : locs) {
                    if (gl.equals(good) || blocks.contains(good)) {
                        out.add(new Move(fp.getLocation(), good));
                    }
                }
            }
        }
        return out;
    }

    public void togglePlayerTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    /**
     * Move a piece.
     * @param from The old location of the piece.
     * @param to The new location of the piece.
     */
    public void move(Location from, Location to) {
        move(from, to, null);
    }

    /**
     * Move a piece.
     * @param from The old location of the piece.
     * @param to The new location of the piece.
     * @param transform If this is a pawn promotion, the piece to promote
     *                  the pawn into (Queen, Rook, Bishop, Knight). Null
     *                  otherwise.
     */
    public void move(Location from, Location to, String transform) {
        Piece p = this.getPiece(from);
        Piece orig = this.getPiece(to);
        this.setPiece(from, null);
        if (transform == null) {
            // en passant
            if (p instanceof Pawn && orig == null && from.getX() != to.getX()) {
                orig = this.getPiece(to.getX(), from.getY());
                this.setPiece(to.getX(), from.getY(), null);
            }
            // castling
            if (p instanceof King && Math.abs(from.getX() - to.getX()) == 2) {
                // right side rook
                if (from.getX() < to.getX()) {
                    Piece rook = this.getPiece(7, to.getY());
                    this.setPiece(7, to.getY(), null);
                    this.setPiece(to.translate(-1, 0), rook);
                    rook.setLocation(to.translate(-1, 0));
                }
                else {
                    Piece rook = this.getPiece(0, to.getY());
                    this.setPiece(0, to.getY(), null);
                    this.setPiece(to.translate(1, 0), rook);
                    rook.setLocation(to.translate(1, 0));
                }
            }
            this.setPiece(to, p);
            p.setLocation(to);
        }
        else {
            switch (transform) {
                case "Rook":
                    p = new Rook(p.getIsWhite(), this, to);
                    break;
                case "Bishop":
                    p = new Bishop(p.getIsWhite(), this, to);
                    break;
                case "Knight":
                    p = new Knight(p.getIsWhite(), this, to);
                    break;
                default:
                    p = new Queen(p.getIsWhite(), this, to);
            }
            this.setPiece(to, p);
        }
        if (p instanceof PieceFirstMove) {
            ((PieceFirstMove)p).move();
        }
        history.add(new Move(from, to, orig, p));
        this.togglePlayerTurn();
    }

    /**
     * Undo a move. Does nothing if there are no moves to undo.
     */
    public void undo() {
        if (history.size() == 0) {
            return;
        }
        Move m = history.removeLast();
        Location from = m.getFrom();
        Location to = m.getTo();
        Piece p = m.getNewPiece();
        Piece op = m.getOriginalPiece();
        p.setLocation(from);
        this.setPiece(from, p);
        // en passant
        if (op instanceof Pawn && !op.getLocation().equals(to)) {
            this.setPiece(to, null);
            this.setPiece(op.getLocation(), op);
        }
        // castling
        else if (p instanceof King && Math.abs(from.getX() - to.getX()) == 2) {
            if (from.getX() < to.getX()) {
                Piece rook = this.getPiece(to.getX() - 1, to.getY());
                this.setPiece(7, to.getY(), rook);
                this.setPiece(to.getX() - 1, to.getY(), null);
            }
            else {
                Piece rook = this.getPiece(to.getX() + 1, to.getY());
                this.setPiece(0, to.getY(), rook);
                this.setPiece(to.getX() + 1, to.getY(), null);
            }
            this.setPiece(to, null);
        }
        else {
            this.setPiece(to, m.getOriginalPiece());
        }
        if (p instanceof PieceFirstMove) {
            ((PieceFirstMove)p).unmove();
        }
        this.togglePlayerTurn();
    }

    /**
     * Returns a copy of the last move.
     * @return A copy of the last move. Null if no last move.
     */
    public Move getLastMove() {
        if (history.size() == 0) {
            return null;
        }
        Move m = history.getLast();
        return new Move(m.getFrom(), m.getTo(), m.getOriginalPiece(), m.getNewPiece());
    }
}
