public class Rook extends Piece {
    public Rook(boolean isWhite) {
        super(isWhite, "rook");
    }

    @Override
    public boolean isValidMove(int srcRow, int srcCol, int destRow, int destCol, Piece[][] board) {
        if (srcRow != destRow && srcCol != destCol) return false;

        int rowStep = Integer.compare(destRow, srcRow);
        int colStep = Integer.compare(destCol, srcCol);

        int r = srcRow + rowStep;
        int c = srcCol + colStep;

        while (r != destRow || c != destCol) {
            if (board[r][c] != null) return false;
            r += rowStep;
            c += colStep;
        }

        return board[destRow][destCol] == null || board[destRow][destCol].isWhite() != isWhite();
    }
}
