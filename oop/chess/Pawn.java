public class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite, "pawn");
    }

    @Override
    public boolean isValidMove(int srcRow, int srcCol, int destRow, int destCol, Piece[][] board) {
        int direction = isWhite() ? -1 : 1;
        int startRow = isWhite() ? 6 : 1;
        if (srcCol == destCol && destRow == srcRow + direction) {
            return board[destRow][destCol] == null;
        }
        if (srcCol == destCol && srcRow == startRow && destRow == srcRow + 2 * direction) {
            return board[destRow][destCol] == null && board[srcRow + direction][destCol] == null;
        }

        if (Math.abs(srcCol - destCol) == 1 && destRow == srcRow + direction) {
            Piece targetPiece = board[destRow][destCol];
            return targetPiece != null && targetPiece.getColor() != this.getColor();
        }

        return false;
    }
}
