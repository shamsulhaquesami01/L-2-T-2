public class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite, "knight");
    }

    @Override
    public boolean isValidMove(int srcRow, int srcCol, int destRow, int destCol, Piece[][] board) {
        int dr = Math.abs(destRow - srcRow);
        int dc = Math.abs(destCol - srcCol);

        if ((dr == 2 && dc == 1) || (dr == 1 && dc == 2)) {
            return board[destRow][destCol] == null || board[destRow][destCol].isWhite() != isWhite();
        }

        return false;
    }
}
