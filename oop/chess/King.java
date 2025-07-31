public class King extends Piece {
    public King(boolean isWhite) {
        super(isWhite, "king");
    }

    @Override
    public boolean isValidMove(int srcRow, int srcCol, int destRow, int destCol, Piece[][] board) {
        int rowDiff = Math.abs(destRow - srcRow);
        int colDiff = Math.abs(destCol - srcCol);
        if (rowDiff <= 1 && colDiff <= 1 && !(rowDiff == 0 && colDiff == 0)) {
            return true;
        }
        if (canCastle(srcRow, srcCol, destRow, destCol, board)) {
            return true;
        }

        return false;
    }


    private boolean canCastle(int srcRow, int srcCol, int destRow, int destCol, Piece[][] board) {
        if (srcRow != destRow) return false;
        if (hasMoved()) return false;
        int colDiff = destCol - srcCol;
        if (Math.abs(colDiff) != 2) return false;
        boolean isKingside = colDiff > 0;
        int rookCol = isKingside ? 7 : 0;
        Piece rook = board[srcRow][rookCol];
        if (rook == null || !(rook instanceof Rook) || rook.hasMoved()) {
            return false;
        }
        if (rook.getColor() != this.getColor()) {
            return false;
        }
        int startCol = Math.min(srcCol, rookCol);
        int endCol = Math.max(srcCol, rookCol);

        for (int col = startCol + 1; col < endCol; col++) {
            if (board[srcRow][col] != null) {
                return false;
            }
        }

        return true;
    }


    public int getRookColumnForCastling(int srcCol, int destCol) {
        int colDiff = destCol - srcCol;
        boolean isKingside = colDiff > 0;
        return isKingside ? 7 : 0;
    }

    public int getRookDestinationForCastling(int srcCol, int destCol) {
        int colDiff = destCol - srcCol;
        boolean isKingside = colDiff > 0;
        return isKingside ? destCol - 1 : destCol + 1;
    }


}
