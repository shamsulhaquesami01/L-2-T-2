public class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(isWhite, "queen");
    }

    @Override
    public boolean isValidMove(int srcRow, int srcCol, int destRow, int destCol, Piece[][] board) {
        Rook tempRook = new Rook(isWhite());
        Bishop tempBishop = new Bishop(isWhite());
        return tempRook.isValidMove(srcRow, srcCol, destRow, destCol, board) ||
                tempBishop.isValidMove(srcRow, srcCol, destRow, destCol, board);
    }
}
