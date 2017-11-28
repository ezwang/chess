import java.util.Set;
import java.util.TreeSet;

public class GameState {
    private Piece[][] board;
    private String player, opponent;
    private boolean isWhite;
    private boolean isWhiteTurn;

    public GameState(String player, String opponent, boolean isWhite) {
        this.player = player;
        this.opponent = opponent;
        this.isWhite = isWhite;
        this.isWhiteTurn = true;

        board = new Piece[8][8];

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

    public boolean checkCheckmate(boolean isWhite) {
        // TODO: check if piece can block/piece can get captured
        King king = getKing(isWhite);
        Set<Location> routes = king.getMovableLocations();
        routes.add(king.getLocation());
        Set<Piece> pieces = getPiecesByColor(!isWhite);
        for (Piece p : pieces) {
            Set<Location> locs = p.getMovableLocations();
            for (Location bad : locs) {
                routes.remove(bad);
            }
        }
        return routes.size() == 0;
    }

    public void togglePlayerTurn() {
        isWhiteTurn = !isWhiteTurn;
    }
}
