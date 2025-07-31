
public class ChessGame {
    private Piece[][] board;
    private PieceColor currentTurn;
    private GameMode gameMode;
    private GameStatus status;
    public ChessGame(GameMode mode) {
        this.gameMode = mode;
        board = new Piece[8][8];
        currentTurn = PieceColor.WHITE;
        initializeBoard();
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setCurrentTurn(PieceColor color) {
        this.currentTurn = color;
    }

    public PieceColor getCurrentTurn() {
        return currentTurn;
    }

    private void initializeBoard() {
        for (int col = 0; col < 8; col++) {
            board[1][col] = new Pawn(false);
            board[6][col] = new Pawn(true);
        }

        // Initialize rooks
       board[0][0] = new Rook(false);
       board[0][7] = new Rook(false);
        board[7][0] = new Rook(true);
        board[7][7] = new Rook(true);

        // Initialize knights
        board[0][1] = new Knight(false);
        board[0][6] = new Knight(false);
        board[7][1] = new Knight(true);
        board[7][6] = new Knight(true);

        // Initialize bishops
        board[0][2] = new Bishop(false);
        board[0][5] = new Bishop(false);
        board[7][2] = new Bishop(true);
        board[7][5] = new Bishop(true);

        // Initialize queen and king
        board[0][3] = new Queen(false); // Black Queen
        board[0][4] = new King(false);  // Black King
        board[7][3] = new Queen(true);  // White Queen
        board[7][4] = new King(true);   // White King
    }
    public boolean movePiece(int srcRow, int srcCol, int destRow, int destCol, int i) {
        if (!isValidPosition(srcRow, srcCol) || !isValidPosition(destRow, destCol)) {
            return false;
        }

        Piece movingPiece = board[srcRow][srcCol];
        if (movingPiece == null || movingPiece.getColor() != currentTurn) {
            return false;
        }

        if (!movingPiece.isValidMove(srcRow, srcCol, destRow, destCol, board)) {
            return false;
        }

        Piece destinationPiece = board[destRow][destCol];
        if (destinationPiece != null && destinationPiece.getColor() == movingPiece.getColor()) {
            return false;
        }
        if (movingPiece instanceof King && Math.abs(destCol - srcCol) == 2) {
            if (!canCastleSafely(srcRow, srcCol, destRow, destCol)) {
                return false;
            }
        }
        if (wouldMoveResultInCheck(srcRow, srcCol, destRow, destCol, currentTurn)) {
            return false;
        }

        // Handle pawn promotion
        if (i != 0) {
            if (i == 1) movingPiece = new Queen(currentTurn == PieceColor.WHITE);
            if (i == 2) movingPiece = new Rook(currentTurn == PieceColor.WHITE);
            if (i == 3) movingPiece = new Bishop(currentTurn == PieceColor.WHITE);
            if (i == 4) movingPiece = new Knight(currentTurn == PieceColor.WHITE);
        }

        // Handle castling move
        if (movingPiece instanceof King && Math.abs(destCol - srcCol) == 2) {
            performCastling(srcRow, srcCol, destRow, destCol);
        } else {
            board[destRow][destCol] = movingPiece;
            board[srcRow][srcCol] = null;
        }

        movingPiece.setMoved(true);

        currentTurn = (currentTurn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

        if (gameMode == GameMode.QUICK_CHESS) {
        }

        return true;
    }
    private boolean canCastleSafely(int srcRow, int srcCol, int destRow, int destCol) {
        if (isKingInCheck(currentTurn)) {
            return false;
        }
        int step = (destCol > srcCol) ? 1 : -1;

        for (int col = srcCol; col != destCol + step; col += step) {
            Piece[][] tempBoard = copyBoard();
            tempBoard[destRow][col] = tempBoard[srcRow][srcCol];
            tempBoard[srcRow][srcCol] = null;
            if (wouldKingBeInCheckOnBoard(tempBoard, currentTurn, destRow, col)) {
                return false;
            }
        }

        return true;
    }

    private void performCastling(int srcRow, int srcCol, int destRow, int destCol) {
        King king = (King) board[srcRow][srcCol];

        // Move the king
        board[destRow][destCol] = king;
        board[srcRow][srcCol] = null;

        // Move the rook
        int rookSrcCol = king.getRookColumnForCastling(srcCol, destCol);
        int rookDestCol = king.getRookDestinationForCastling(srcCol, destCol);

        Piece rook = board[srcRow][rookSrcCol];
        board[srcRow][rookDestCol] = rook;
        board[srcRow][rookSrcCol] = null;
        rook.setMoved(true);
    }

    private Piece[][] copyBoard() {
        Piece[][] tempBoard = new Piece[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                tempBoard[r][c] = board[r][c];
            }
        }
        return tempBoard;
    }

    private boolean wouldKingBeInCheckOnBoard(Piece[][] boardState, PieceColor kingColor, int kingRow, int kingCol) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = boardState[r][c];
                if (piece != null && piece.getColor() != kingColor) {
                    if (piece.isValidMove(r, c, kingRow, kingCol, boardState)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isKingInCheck(PieceColor kingColor) {
        int kingRow = -1;
        int kingCol = -1;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece instanceof King && piece.getColor() == kingColor) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
            if (kingRow != -1) break;
        }

        if (kingRow == -1) return false;

        return wouldKingBeInCheckOnBoard(board, kingColor, kingRow, kingCol);
    }

    public boolean wouldMoveResultInCheck(int srcRow, int srcCol, int destRow, int destCol, PieceColor kingColor) {
        Piece[][] tempBoard = copyBoard();
        Piece movingPiece = tempBoard[srcRow][srcCol];
        tempBoard[destRow][destCol] = movingPiece;
        tempBoard[srcRow][srcCol] = null;
        int kingRow = -1;
        int kingCol = -1;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = tempBoard[r][c];
                if (piece instanceof King && piece.getColor() == kingColor) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
            if (kingRow != -1) break;
        }

        if (kingRow == -1) return false;

        return wouldKingBeInCheckOnBoard(tempBoard, kingColor, kingRow, kingCol);
    }

    public boolean isCheckmate(PieceColor playerColor) {
        if (!isKingInCheck(playerColor)) {
            return false;
        }
        for (int srcRow = 0; srcRow < 8; srcRow++) {
            for (int srcCol = 0; srcCol < 8; srcCol++) {
                Piece piece = board[srcRow][srcCol];
                if (piece != null && piece.getColor() == playerColor) {
                    for (int destRow = 0; destRow < 8; destRow++) {
                        for (int destCol = 0; destCol < 8; destCol++) {
                            if (piece.isValidMove(srcRow, srcCol, destRow, destCol, board)) {
                                Piece destinationPiece = board[destRow][destCol];
                                if (destinationPiece == null || destinationPiece.getColor() != playerColor) {
                                    // Special handling for castling in checkmate detection
                                    if (piece instanceof King && Math.abs(destCol - srcCol) == 2) {
                                        if (canCastleSafely(srcRow, srcCol, destRow, destCol)) {
                                            return false;
                                        }
                                    } else if (!wouldMoveResultInCheck(srcRow, srcCol, destRow, destCol, playerColor)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean isStalemate(PieceColor playerColor) {
        if (isKingInCheck(playerColor)) {
            return false;
        }
        for (int srcRow = 0; srcRow < 8; srcRow++) {
            for (int srcCol = 0; srcCol < 8; srcCol++) {
                Piece piece = board[srcRow][srcCol];
                if (piece != null && piece.getColor() == playerColor) {
                    for (int destRow = 0; destRow < 8; destRow++) {
                        for (int destCol = 0; destCol < 8; destCol++) {
                            if (piece.isValidMove(srcRow, srcCol, destRow, destCol, board)) {
                                Piece destinationPiece = board[destRow][destCol];
                                if (destinationPiece == null || destinationPiece.getColor() != playerColor) {
                                    if (piece instanceof King && Math.abs(destCol - srcCol) == 2) {
                                        if (canCastleSafely(srcRow, srcCol, destRow, destCol)) {
                                            return false;
                                        }
                                    } else if (!wouldMoveResultInCheck(srcRow, srcCol, destRow, destCol, playerColor)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }


    public void updateGameStatus() {
        if (isCheckmate(getCurrentTurn())) {
            setGameStatus(GameStatus.CHECKMATE);
            return;
        }

        if (isStalemate(getCurrentTurn())) {
            setGameStatus(GameStatus.STALEMATE);
            return;
        }

        if (isKingInCheck( getCurrentTurn())) {
            setGameStatus(GameStatus.CHECK);
            return;
        }

        else setGameStatus(GameStatus.NORMAL);

    }

    private void setGameStatus(GameStatus status)
    {
        this.status=status;
    }

    public GameStatus getGameStatus() {
        if (isCheckmate(currentTurn)) {
            return GameStatus.CHECKMATE;
        } else if (isStalemate(currentTurn)) {
            return GameStatus.STALEMATE;
        } else if (isKingInCheck(currentTurn)) {
            return GameStatus.CHECK;
        } else {
            return GameStatus.NORMAL;
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

}