public class GameState {
    private Piece[][] board;

    public GameState() {
        board = new Piece[8][8];

        setupBoard();
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
        // TODO: switch position of queen/king
        board[0][0] = new Rook(true, this, new Location(0, 0));
        board[0][7] = new Rook(true, this, new Location(7, 0));
        board[0][1] = new Knight(true, this, new Location(1, 0));
        board[0][6] = new Knight(true, this, new Location(6, 0));
        board[0][2] = new Bishop(true, this, new Location(2, 0));
        board[0][5] = new Bishop(true, this, new Location(5, 0));
        board[0][3] = new King(true, this, new Location(3, 0));
        board[0][4] = new Queen(true, this, new Location(4, 0));

        board[7][0] = new Rook(true, this, new Location(0, 7));
        board[7][7] = new Rook(true, this, new Location(7, 7));
        board[7][1] = new Knight(true, this, new Location(1, 7));
        board[7][6] = new Knight(true, this, new Location(6, 7));
        board[7][2] = new Bishop(true, this, new Location(2, 7));
        board[7][5] = new Bishop(true, this, new Location(5, 7));
        board[7][3] = new King(true, this, new Location(3, 7));
        board[7][4] = new Queen(true, this, new Location(4, 7));
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
        return board[y][x] != null;
    }

    public boolean isSameColor(int x, int y, boolean isWhite) {
        return isOccupied(x, y) && board[y][x].isWhite == isWhite;
    }

    public boolean isDifferentColor(int x, int y, boolean isWhite) {
        return isOccupied(x, y) && board[y][x].isWhite != isWhite;
    }
}