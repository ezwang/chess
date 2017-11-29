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

    public boolean onBoard(int x, int y) {
        return x >= 0 && y >= 0 && x < 8 && y < 8;
    }

    public boolean isOccupied(int x, int y) {
        return onBoard(x, y) && board[y][x] != null;
    }

    public boolean isSameColor(int x, int y, boolean isWhite) {
        return isOccupied(x, y) && board[y][x].getIsWhite() == isWhite;
    }

    public boolean isDifferentColor(int x, int y, boolean isWhite) {
        return isOccupied(x, y) && board[y][x].getIsWhite() != isWhite;
    }

    public void setPiece(int x, int y, Piece p) {
        if (x >= 0 && y >= 0 && x < 8 && y < 8) {
            board[y][x] = p;
        }
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

    private King getKing(boolean isWhite) {
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
     * @param old The old location of the piece.
     * @param now The new location of the piece.
     * @param transform If this is a pawn promotion, the piece to promote
     *                  the pawn into (Queen, Rook, Bishop, Knight). Null
     *                  otherwise.
     */
    public void move(Location old, Location now, String transform) {
        Piece p = this.getPiece(old.getX(), old.getY());
        Piece orig = this.getPiece(now.getX(), now.getY());
        this.setPiece(old.getX(), old.getY(), null);
        if (transform == null) {
            this.setPiece(now.getX(), now.getY(), p);
            p.setLocation(now);
        }
        else {
            switch (transform) {
                case "Rook":
                    p = new Rook(p.getIsWhite(), this, now);
                    break;
                case "Bishop":
                    p = new Bishop(p.getIsWhite(), this, now);
                    break;
                case "Knight":
                    p = new Knight(p.getIsWhite(), this, now);
                    break;
                default:
                    p = new Queen(p.getIsWhite(), this, now);
            }
            this.setPiece(now.getX(), now.getY(), p);
        }
        history.add(new Move(old, now, orig, p));
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
        p.setLocation(from);
        this.setPiece(from.getX(), from.getY(), p);
        this.setPiece(to.getX(), to.getY(), m.getOriginalPiece());
        this.togglePlayerTurn();
    }
}
