import java.util.*;

public class ChessAI {

    public static int MAX_TIME_MS; // 22 seconds
    private static final int CHECKMATE_SCORE = 100000;
    private static final int STALEMATE_SCORE = 0;
    private static final int QUIESCENCE_DEPTH = 6;
    private static final int  MATE_THRESHOLD=CHECKMATE_SCORE-1000;

    // Transposition table
    private static final int TT_SIZE = 1 << 20; // 1M entries
    private static TranspositionTable tt = new TranspositionTable(TT_SIZE);

    // Zobrist hashing
    private static ZobristHash zobrist = new ZobristHash();

    // Move ordering
    private static KillerMoves killerMoves = new KillerMoves();
    private static HistoryHeuristic historyHeuristic = new HistoryHeuristic();

    private static long searchStartTime;
    private static boolean timeUp = false;

    // Complete Piece-Square Tables
    private static final int[][] PAWN_TABLE = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {5, -5, -5, 5, 5, -5, -5, 5},
            {10, 10, 10, 10, 10, 20, 10, 10},
            {5, 5, 10, 25, 25, 10, 5, 5},
            {0, 0, 0, 20, 20, 0, 0, 0},
            {5, 5, 10, 0, 0, 10, 5, 5},
            {5, 10, 10, 20, 20, 10, 10, 5},
            {0, 0, 0, 0, 0, 0, 0, 0}
    };

    private static final int[][] KNIGHT_TABLE = {
            {-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20, 0, 0, 0, 0, -20, -40},
            {-30, 0, 10, 15, 15, 10, 0, -30},
            {-30, 5, 15, 20, 20, 15, 5, -30},
            {-30, 0, 15, 20, 20, 15, 0, -30},
            {-30, 5, 10, 15, 15, 10, 5, -30},
            {-40, -20, 0, 5, 5, 0, -20, -40},
            {-50, -40, -30, -30, -30, -30, -40, -50}
    };

    private static final int[][] BISHOP_TABLE = {
            {-20, -10, -10, -10, -10, -10, -10, -20},
            {-10, 0, 0, 0, 0, 0, 0, -10},
            {-10, 0, 5, 10, 10, 50, 0, -10},
            {-10, 5, 5, 10, 10, 50, 5, -10},
            {-10, 0, 10, 10, 10, 10, 0, -10},
            {-10, 10, 10, 10, 10, 10, 10, -10},
            {-10, 5, 0, 0, 0, 0, 5, -10},
            {-20, -10, -10, -10, -10, -10, -10, -20}
    };

    private static final int[][] ROOK_TABLE = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {5, 10, 10, 10, 10, 10, 10, 5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {0, 0, 0, 5, 5, 0, 0, 0}
    };

    private static final int[][] QUEEN_TABLE = {
            {-20, -10, -10, -5, -5, -10, -10, -20},
            {-10, 0, 0, 0, 0, 0, 0, -10},
            {-10, 0, 5, 5, 5, 5, 0, -10},
            {-5, 0, 5, 5, 5, 5, 0, -5},
            {0, 0, 5, 5, 5, 5, 0, -5},
            {-10, 5, 5, 5, 5, 5, 0, -10},
            {-10, 0, 5, 0, 0, 0, 0, -10},
            {-20, -10, -10, -5, -5, -10, -10, -20}
    };

    private static final int[][] KING_MIDDLE_GAME_TABLE = {
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-20, -30, -30, -40, -40, -30, -30, -20},
            {-10, -20, -20, -20, -20, -20, -20, -10},
            {20, 20, 0, 0, 0, 0, 20, 20},
            {20, 30, 10, 0, 0, 10, 30, 20}
    };

    private static final int[][] KING_END_GAME_TABLE = {
            {-50, -40, -30, -20, -20, -30, -40, -50},
            {-30, -20, -10, 0, 0, -10, -20, -30},
            {-30, -10, 20, 30, 30, 20, -10, -30},
            {-30, -10, 30, 40, 40, 30, -10, -30},
            {-30, -10, 30, 40, 40, 30, -10, -30},
            {-30, -10, 20, 30, 30, 20, -10, -30},
            {-30, -30, 0, 0, 0, 0, -30, -30},
            {-50, -30, -30, -30, -30, -30, -30, -50}
    };

    private static final int[][] PAWN_MG = {
            {   0,   0,   0,   0,   0,   0,   0,   0},
            {  50,  50,  50,  50,  50,  50,  50,  50},
            {  10,  10,  20,  30,  30,  20,  10,  10},
            {   5,   5,  10,  25,  25,  10,   5,   5},
            {   0,   0,   0,  20,  20,   0,   0,   0},
            {   5,  -5, -10,   0,   0, -10,  -5,   5},
            {   5,  10,  10, -20, -20,  10,  10,   5},
            {   0,   0,   0,   0,   0,   0,   0,   0}
    };

    private static final int[][] KNIGHT_MG = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}
    };

    private static final int[][] BISHOP_MG = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}
    };

    private static final int[][] ROOK_MG = {
            {  0,   0,   0,   0,   0,   0,   0,   0},
            {  5,  10,  10,  10,  10,  10,  10,   5},
            { -5,   0,   0,   0,   0,   0,   0,  -5},
            { -5,   0,   0,   0,   0,   0,   0,  -5},
            { -5,   0,   0,   0,   0,   0,   0,  -5},
            { -5,   0,   0,   0,   0,   0,   0,  -5},
            { -5,   0,   0,   0,   0,   0,   0,  -5},
            {  0,   0,   0,   5,   5,   0,   0,   0}
    };

    private static final int[][] QUEEN_MG = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}
    };


    public static Move findBestMove(ChessGame game, int maxDepth) {
        searchStartTime = System.currentTimeMillis();
        timeUp = false;
        tt.clear();

        PieceColor aiColor = game.getCurrentTurn();
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        List<Move> allMoves = generateLegalMoves(game.getBoard(), aiColor, game);
        for (Move move : allMoves) {
            if (isCheckmateMove(game, move, aiColor)) {
                System.out.println("FOUND IMMEDIATE CHECKMATE: " + moveToString(move));
                return move;
            }
        }

        int aspirationWindow = 50;
        int previousScore = 0;

        for (int depth = 1; depth <= maxDepth && !timeUp; depth++) {
            try {
                SearchResult result;

                if (depth <= 2) {
                    result = negamaxRoot(game, depth, aiColor, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
                } else {
                    int alpha = previousScore - aspirationWindow;
                    int beta = previousScore + aspirationWindow;

                    result = negamaxRoot(game, depth, aiColor, alpha, beta);
                    if (result.score <= alpha || result.score >= beta) {
                        result = negamaxRoot(game, depth, aiColor, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
                    }
                }

                if (!timeUp && result.bestMove != null) {
                    bestMove = result.bestMove;
                    bestScore = result.score;
                    previousScore = result.score;

                    System.out.println("Depth " + depth + ": score=" + result.score +
                            " move=" + moveToString(result.bestMove) +
                            " time=" + (System.currentTimeMillis() - searchStartTime) + "ms");
                    if (isMateScore(result.score)) {
                        int mateInMoves = getMateDistance(result.score);
                        System.out.println("EXECUTING FORCED MATE IN " + mateInMoves + ": " + moveToString(result.bestMove));
                        break;
                    }
                }
            } catch (TimeUpException e) {
                System.out.println("Time up at depth " + depth);
                break;
            }
        }

        killerMoves.clear();
        historyHeuristic.age();

        return bestMove != null ? bestMove : getRandomLegalMove(game, aiColor);
    }

    private static SearchResult negamaxRoot(ChessGame game, int depth, PieceColor color, int alpha, int beta)
            throws TimeUpException {
        checkTime();

        List<Move> moves = generateLegalMoves(game.getBoard(), color, game);
        if (moves.isEmpty()) {
            return new SearchResult(null, isInCheck(game.getBoard(), color) ?
                    -CHECKMATE_SCORE : STALEMATE_SCORE);
        }
        orderMovesAtRoot(moves, game, color, depth);

        int bestScore = Integer.MIN_VALUE;
        Move bestMove = moves.get(0);

        for (Move move : moves) {
            ChessGame simulated = simulateGame(game, move);
            long zobristKey = zobrist.calculateKey(simulated.getBoard(), getOppositeColor(color));

            int score = -negamax(simulated, depth - 1, -beta, -alpha,
                    getOppositeColor(color), zobristKey, true);

            if (simulated.getGameStatus() == GameStatus.CHECKMATE) {
                System.out.println("DELIVERING CHECKMATE with move: " + moveToString(move));
                return new SearchResult(move, CHECKMATE_SCORE + depth);
            }

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                alpha = Math.max(alpha, score);
                if (isMateScore(score)) {
                    System.out.println("Found mate pattern with move: " + moveToString(move) +
                            " mate in: " + getMateDistance(score));
                }
            }
        }

        return new SearchResult(bestMove, bestScore);
    }

    private static boolean isCheckmateMove(ChessGame game, Move move, PieceColor color) {
        ChessGame simulated = simulateGame(game, move);
        PieceColor opponentColor = getOppositeColor(color);
        List<Move> opponentMoves = generateLegalMoves(simulated.getBoard(), opponentColor, simulated);
        return opponentMoves.isEmpty() && isInCheck(simulated.getBoard(), opponentColor);
    }

    private static int negamax(ChessGame game, int depth, int alpha, int beta,
                               PieceColor color, long zobristKey, boolean allowNullMove)
            throws TimeUpException {

        checkTime();
        GameStatus status = game.getGameStatus();
        if (status == GameStatus.CHECKMATE) {
            return -CHECKMATE_SCORE + depth;
        }
        if (status == GameStatus.STALEMATE) {
            return STALEMATE_SCORE;
        }

        int mateAlpha = -CHECKMATE_SCORE + depth;
        int mateBeta = CHECKMATE_SCORE - depth - 1;

        if (mateAlpha >= beta) return mateAlpha;
        if (mateBeta <= alpha) return mateBeta;

        alpha = Math.max(alpha, mateAlpha);
        beta = Math.min(beta, mateBeta);

        TTEntry ttEntry = tt.probe(zobristKey);
        Move ttMove = null;
        if (ttEntry != null && ttEntry.depth >= depth) {
            int ttScore = adjustMateScore(ttEntry.score, depth, false);

            if (ttEntry.flag == TTEntry.EXACT) return ttScore;
            if (ttEntry.flag == TTEntry.LOWER_BOUND) alpha = Math.max(alpha, ttScore);
            if (ttEntry.flag == TTEntry.UPPER_BOUND) beta = Math.min(beta, ttScore);
            if (alpha >= beta) return ttScore;

            ttMove = ttEntry.bestMove;
        }

        if (depth <= 0) {
            return quiescenceSearch(game, alpha, beta, color, QUIESCENCE_DEPTH);
        }

        boolean canDoNullMove = allowNullMove && depth >= 3 && !isInCheck(game.getBoard(), color) &&
                hasNonPawnPieces(game.getBoard(), color) &&
                beta < MATE_THRESHOLD && alpha > -MATE_THRESHOLD;

        if (canDoNullMove) {
            int R = 2 + (depth > 6 ? 1 : 0);
            ChessGame nullMoveGame = copyGameForNullMove(game);
            long nullZobrist = zobrist.calculateKey(nullMoveGame.getBoard(), getOppositeColor(color));

            int nullScore = -negamax(nullMoveGame, depth - 1 - R, -beta, -beta + 1,
                    getOppositeColor(color), nullZobrist, false);

            if (nullScore >= beta) {
                return nullScore;
            }
        }

        List<Move> moves = generateLegalMoves(game.getBoard(), color, game);
        if (moves.isEmpty()) {
            return isInCheck(game.getBoard(), color) ? -CHECKMATE_SCORE + depth : STALEMATE_SCORE;
        }

        orderMoves(moves, game.getBoard(), color, ttMove, depth);

        int originalAlpha = alpha;
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        boolean foundPV = false;

        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            ChessGame simulated = simulateGame(game, move);
            long newZobristKey = zobrist.calculateKey(simulated.getBoard(), getOppositeColor(color));

            int score;
            boolean doLMR = i >= 4 && depth >= 3 && !isCapture(move, game.getBoard()) &&
                    !isInCheck(game.getBoard(), color) &&
                    !isInCheck(simulated.getBoard(), getOppositeColor(color)) &&
                    !killerMoves.isKiller(move, depth) &&
                    alpha < MATE_THRESHOLD && beta > -MATE_THRESHOLD;

            if (doLMR) {
                int reduction = 1 + Math.min(2, (i - 4) / 4);
                score = -negamax(simulated, depth - 1 - reduction, -alpha - 1, -alpha,
                        getOppositeColor(color), newZobristKey, true);

                if (score > alpha) {
                    score = -negamax(simulated, depth - 1, -beta, -alpha,
                            getOppositeColor(color), newZobristKey, true);
                }
            } else {
                if (!foundPV) {
                    score = -negamax(simulated, depth - 1, -beta, -alpha,
                            getOppositeColor(color), newZobristKey, true);
                    foundPV = true;
                } else {
                    score = -negamax(simulated, depth - 1, -alpha - 1, -alpha,
                            getOppositeColor(color), newZobristKey, true);
                    if (score > alpha && score < beta) {
                        score = -negamax(simulated, depth - 1, -beta, -alpha,
                                getOppositeColor(color), newZobristKey, true);
                    }
                }
            }

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }

            alpha = Math.max(alpha, score);
            if (alpha >= beta) {
                if (!isCapture(move, game.getBoard())) {
                    killerMoves.store(move, depth);
                    historyHeuristic.update(move, depth);
                }
                break;
            }
        }

        int storeScore = adjustMateScore(bestScore, depth, true);
        int flag = bestScore <= originalAlpha ? TTEntry.UPPER_BOUND :
                bestScore >= beta ? TTEntry.LOWER_BOUND : TTEntry.EXACT;
        tt.store(zobristKey, storeScore, depth, flag, bestMove);

        return bestScore;
    }


    private static boolean isMateScore(int score) {
        return Math.abs(score) >= MATE_THRESHOLD;
    }

    private static int getMateDistance(int score) {
        if (score > MATE_THRESHOLD) {
            return (CHECKMATE_SCORE - score + 1) / 2;
        } else if (score < -MATE_THRESHOLD) {
            return (CHECKMATE_SCORE + score + 1) / 2;
        }
        return 0;
    }

    private static int adjustMateScore(int score, int depth, boolean storing) {
        if (score > MATE_THRESHOLD) {
            return storing ? score + depth : score - depth;
        } else if (score < -MATE_THRESHOLD) {
            return storing ? score - depth : score + depth;
        }
        return score;
    }



    private static int quiescenceSearch(ChessGame game, int alpha, int beta, PieceColor color, int depth)
            throws TimeUpException {

        if (depth <= 0 || timeUp) {
            return evaluateBoard(game.getBoard(), color);
        }

        checkTime();

        int standPat = evaluateBoard(game.getBoard(), color);

        if (standPat >= beta) {
            return beta;
        }
        if (standPat + 1000 < alpha) {
            return alpha;
        }

        if (alpha < standPat) {
            alpha = standPat;
        }

        List<Move> captures = generateCaptures(game.getBoard(), color, game);
        orderCaptures(captures, game.getBoard());

        for (Move capture : captures) {

            ChessGame simulated = simulateGame(game, capture);
            int score = -quiescenceSearch(simulated, -beta, -alpha, getOppositeColor(color), depth - 1);

            if (score >= beta) {
                return beta;
            }
            if (score > alpha) {
                alpha = score;
            }
        }

        return alpha;
    }

    private static void orderMovesAtRoot(List<Move> moves, ChessGame game, PieceColor color, int depth) {
        moves.sort((m1, m2) -> {
            int score1 = getRootMoveOrderScore(m1, game, color);
            int score2 = getRootMoveOrderScore(m2, game, color);
            return Integer.compare(score2, score1);
        });
    }


    private static int getRootMoveOrderScore(Move move, ChessGame game, PieceColor color) {
        int score = 0;
        Piece[][] board = game.getBoard();
        if (isCheckmateMove(game, move, color)) {
            return 2000000;
        }

        ChessGame simulated = simulateGame(game, move);
        if (isInCheck(simulated.getBoard(), getOppositeColor(color))) {
            score += 500000;
        }

        score += getMoveOrderScore(move, board, color, null, 0);

        return score;
    }

    private static void orderMoves(List<Move> moves, Piece[][] board, PieceColor color,
                                   Move ttMove, int depth) {
        moves.sort((m1, m2) -> {
            int score1 = getMoveOrderScore(m1, board, color, ttMove, depth);
            int score2 = getMoveOrderScore(m2, board, color, ttMove, depth);
            return Integer.compare(score2, score1);
        });
    }

    private static int getMoveOrderScore(Move move, Piece[][] board, PieceColor color,
                                         Move ttMove, int depth) {
        int score = 0;

        if (ttMove != null && move.equals(ttMove)) {
            return 1000000;
        }

        // Captures with MVV-LVA (Most Valuable Victim - Least Valuable Attacker)
        Piece target = board[move.destRow][move.destCol];
        if (target != null && target.getColor() != color) {
            Piece attacker = board[move.srcRow][move.srcCol];
            score += getPieceValue(target) * 100 - getPieceValue(attacker);
            score += 100000; // Base capture bonus
        }
        if (killerMoves.isKiller(move, depth)) {
            score += 50000;
        }
        score += historyHeuristic.getScore(move);
        Piece moving = board[move.srcRow][move.srcCol];
        if (moving != null) {
            score += getPositionalValue(moving, move.destRow, move.destCol, board) -
                    getPositionalValue(moving, move.srcRow, move.srcCol, board);
        }

        return score;
    }

    private static void orderCaptures(List<Move> captures, Piece[][] board) {
        captures.sort((m1, m2) -> {
            Piece target1 = board[m1.destRow][m1.destCol];
            Piece target2 = board[m2.destRow][m2.destCol];
            Piece attacker1 = board[m1.srcRow][m1.srcCol];
            Piece attacker2 = board[m2.srcRow][m2.srcCol];

            int value1 = (target1 != null ? getPieceValue(target1) : 0) * 100 -
                    (attacker1 != null ? getPieceValue(attacker1) : 0);
            int value2 = (target2 != null ? getPieceValue(target2) : 0) * 100 -
                    (attacker2 != null ? getPieceValue(attacker2) : 0);

            return Integer.compare(value2, value1);
        });
    }

    private static int evaluateBoard(Piece[][] board, PieceColor color) {
        int materialBalance = 0;
        int positionalBalance = 0;
        int mobilityBalance = 0;
        int kingSafetyBalance = 0;
        int totalMaterial = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    int pieceValue = getPieceValue(piece);
                    int positionalValue = getPositionalValue(piece, row, col, board);

                    totalMaterial += Math.abs(pieceValue);

                    if (piece.getColor() == color) {
                        materialBalance += pieceValue;
                        positionalBalance += positionalValue;
                    } else {
                        materialBalance -= pieceValue;
                        positionalBalance -= positionalValue;
                    }
                }
            }
        }

        mobilityBalance = evaluateMobility(board, color);
        kingSafetyBalance = evaluateKingSafety(board, color);

        int pawnBonus=passedPawnBonus(board,color);

        return materialBalance + positionalBalance + mobilityBalance / 10 + kingSafetyBalance+pawnBonus;
    }

    private static int passedPawnBonus(Piece[][] board, PieceColor color) {
        int bonus = 0;
        int dir = (color == PieceColor.WHITE) ? -1 : 1;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p instanceof Pawn && p.getColor() == color) {
                    boolean passed = true;
                    for (int rr = r + dir; rr >= 0 && rr < 8; rr += dir) {
                        for (int dc = -1; dc <= 1; dc++) {
                            int cc = c + dc;
                            if (cc < 0 || cc > 7) continue;
                            Piece q = board[rr][cc];
                            if (q instanceof Pawn && q.getColor() != color) {
                                passed = false;
                                break;
                            }
                        }
                        if (!passed) break;
                    }
                    if (passed) {
                        int rank = (color == PieceColor.WHITE) ? 7 - r : r;
                        bonus += 50 + 20 * rank;
                    }
                }
            }
        }
        return bonus;
    }


    private static int evaluateMobility(Piece[][] board, PieceColor color) {
        int mobility = 0;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece != null) {
                    int moves = countPieceMoves(board, r, c, piece);
                    if (piece.getColor() == color) {
                        mobility += moves;
                    } else {
                        mobility -= moves;
                    }
                }
            }
        }

        return mobility;
    }

    private static int countPieceMoves(Piece[][] board, int row, int col, Piece piece) {
        int count = 0;
        if (piece instanceof Pawn) {
            int direction = piece.getColor() == PieceColor.WHITE ? -1 : 1;
            int newRow = row + direction;
            if (newRow >= 0 && newRow < 8 && board[newRow][col] == null) {
                count++;
                if ((piece.getColor() == PieceColor.WHITE && row == 6) ||
                        (piece.getColor() == PieceColor.BLACK && row == 1)) {
                    if (board[row + 2 * direction][col] == null) count++;
                }
            }
            for (int dc = -1; dc <= 1; dc += 2) {
                if (col + dc >= 0 && col + dc < 8 && newRow >= 0 && newRow < 8) {
                    Piece target = board[newRow][col + dc];
                    if (target != null && target.getColor() != piece.getColor()) {
                        count++;
                    }
                }
            }
        } else {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;

                    for (int dist = 1; dist < 8; dist++) {
                        int newRow = row + dr * dist;
                        int newCol = col + dc * dist;

                        if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) break;

                        if (piece.isValidMove(row, col, newRow, newCol, board)) {
                            Piece target = board[newRow][newCol];
                            if (target == null) {
                                count++;
                            } else if (target.getColor() != piece.getColor()) {
                                count++;
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        return Math.min(count, 30);
    }

    private static int evaluateKingSafety(Piece[][] board, PieceColor color) {
        int[] kingPos = findKing(board, color);
        if (kingPos == null) return -50000;

        int safety = 0;
        int kingRow = kingPos[0];
        int kingCol = kingPos[1];
        if (color == PieceColor.WHITE) {
            for (int dc = -1; dc <= 1; dc++) {
                int col = kingCol + dc;
                if (col >= 0 && col < 8) {
                    for (int dr = 1; dr <= 2; dr++) {
                        int row = kingRow - dr;
                        if (row >= 0) {
                            Piece piece = board[row][col];
                            if (piece instanceof Pawn && piece.getColor() == color) {
                                safety += 10;
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            for (int dc = -1; dc <= 1; dc++) {
                int col = kingCol + dc;
                if (col >= 0 && col < 8) {
                    for (int dr = 1; dr <= 2; dr++) {
                        int row = kingRow + dr;
                        if (row < 8) {
                            Piece piece = board[row][col];
                            if (piece instanceof Pawn && piece.getColor() == color) {
                                safety += 10;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return safety;
    }


    public static Piece consultAboutNewPiece(ChessGame game, int row, int col) {
        boolean aiColor=false;
        Piece[] candidates = {
                new Queen(aiColor),
                new Rook(aiColor),
                new Bishop(aiColor),
                new Knight(aiColor)
        };

        Piece bestPiece   = candidates[0];
        int   bestScore   = Integer.MIN_VALUE;
        Piece[][] board   = game.getBoard();

        System.out.println("Starting to update the pawn");

        for (Piece candidate : candidates) {
            ChessGame sim = new ChessGame(GameMode.VS_COMPUTER);
            copyBoard(board, sim.getBoard());
            sim.setCurrentTurn(PieceColor.BLACK);
            sim.getBoard()[row][col] = candidate;
            int score = evaluateBoard(sim.getBoard(), PieceColor.BLACK);
            if (score > bestScore) {
                bestScore = score;
                bestPiece = candidate;
            }
        }

        System.out.println("Found best piece "+bestPiece);
        return bestPiece;
    }

    private static int getPositionalValue(Piece piece, int row, int col, Piece[][] board) {
        int tableRow = piece.getColor() == PieceColor.WHITE ? 7 - row : row;
        boolean isEndgame = countTotalMaterial(board) < 2000;
        if (piece instanceof Pawn) {
            return isEndgame ?PAWN_MG[tableRow][col]:PAWN_TABLE[tableRow][col];
        } else if (piece instanceof Knight) {
            return isEndgame ?KNIGHT_MG[tableRow][col]:KNIGHT_TABLE[tableRow][col];
        } else if (piece instanceof Bishop) {
            return isEndgame ?BISHOP_MG[tableRow][col]:BISHOP_TABLE[tableRow][col];
        } else if (piece instanceof Rook) {
            return isEndgame ?ROOK_MG[tableRow][col]:ROOK_TABLE[tableRow][col];
        } else if (piece instanceof Queen) {
            return isEndgame ?QUEEN_MG[tableRow][col]:QUEEN_TABLE[tableRow][col];
        } else if (piece instanceof King) {

            return isEndgame ? KING_END_GAME_TABLE[tableRow][col] : KING_MIDDLE_GAME_TABLE[tableRow][col];
        }

        return 0;
    }

    private static int countTotalMaterial(Piece[][] board) {
        int total = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece != null && !(piece instanceof King)) {
                    total += getPieceValue(piece);
                }
            }
        }
        return total;
    }

    private static int getPieceValue(Piece piece) {
        if (piece instanceof Queen) return 900;
        if (piece instanceof Rook) return 500;
        if (piece instanceof Bishop) return 330;
        if (piece instanceof Knight) return 320;
        if (piece instanceof Pawn) return 100;
        if (piece instanceof King) return 20000;
        return 0;
    }

    private static boolean hasNonPawnPieces(Piece[][] board, PieceColor color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.getColor() == color &&
                        !(piece instanceof Pawn) && !(piece instanceof King)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static ChessGame copyGameForNullMove(ChessGame game) {
        ChessGame copy = new ChessGame(GameMode.VS_COMPUTER);
        copyBoard(game.getBoard(), copy.getBoard());
        copy.setCurrentTurn(getOppositeColor(game.getCurrentTurn()));
        return copy;
    }

    private static void checkTime() throws TimeUpException {
        if (System.currentTimeMillis() - searchStartTime > MAX_TIME_MS) {
            timeUp = true;
            throw new TimeUpException();
        }
    }

    private static boolean isCapture(Move move, Piece[][] board) {
        return board[move.destRow][move.destCol] != null;
    }

    private static boolean isInCheck(Piece[][] board, PieceColor color) {
        int[] kingPos = findKing(board, color);
        if (kingPos == null) return false;

        PieceColor oppositeColor = getOppositeColor(color);
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.getColor() == oppositeColor) {
                    if (piece.isValidMove(r, c, kingPos[0], kingPos[1], board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static PieceColor getOppositeColor(PieceColor color) {
        return color == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
    }

    private static List<Move> generateLegalMoves(Piece[][] board, PieceColor color, ChessGame game) {
        List<Move> moves = new ArrayList<>();
        for (int srcRow = 0; srcRow < 8; srcRow++) {
            for (int srcCol = 0; srcCol < 8; srcCol++) {
                Piece piece = board[srcRow][srcCol];
                if (piece != null && piece.getColor() == color) {
                    List<Move> pseudoLegalMoves = generatePseudoLegalMoves(board, srcRow, srcCol, game);
                    for (Move move : pseudoLegalMoves) {
                        if (!game.wouldMoveResultInCheck(move.srcRow, move.srcCol, move.destRow, move.destCol, color)) {
                            moves.add(move);
                        }
                    }
                }
            }
        }
        return moves;
    }


    private static List<Move> generateCaptures(Piece[][] board, PieceColor color, ChessGame game) {
        List<Move> captures = new ArrayList<>();
        for (int srcRow = 0; srcRow < 8; srcRow++) {
            for (int srcCol = 0; srcCol < 8; srcCol++) {
                Piece piece = board[srcRow][srcCol];
                if (piece != null && piece.getColor() == color) {
                    List<Move> pseudoLegalMoves = generatePseudoLegalMoves(board, srcRow, srcCol, game);
                    for (Move move : pseudoLegalMoves) {
                        Piece target = board[move.destRow][move.destCol];
                        if (target != null && target.getColor() != color) {
                            if (!game.wouldMoveResultInCheck(move.srcRow, move.srcCol, move.destRow, move.destCol, color)) {
                                captures.add(move);
                            }
                        }
                    }
                }
            }
        }
        return captures;
    }


    private static List<Move> generatePseudoLegalMoves(Piece[][] board, int srcRow, int srcCol, ChessGame game) {
        List<Move> moves = new ArrayList<>();
        Piece piece = board[srcRow][srcCol];

        if (piece == null) {
            return moves;
        }

        PieceColor color = piece.getColor();


        if (piece instanceof Pawn) {
            int direction = (color == PieceColor.WHITE) ? -1 : 1;
            int startRow = (color == PieceColor.WHITE) ? 6 : 1;

            int newRow = srcRow + direction;
            if (newRow >= 0 && newRow < 8 && board[newRow][srcCol] == null) {
                moves.add(new Move(srcRow, srcCol, newRow, srcCol));

                if (srcRow == startRow) {
                    int twoSquaresRow = srcRow + 2 * direction;
                    if (twoSquaresRow >= 0 && twoSquaresRow < 8 && board[twoSquaresRow][srcCol] == null) {
                        moves.add(new Move(srcRow, srcCol, twoSquaresRow, srcCol));
                    }
                }
            }


            if (newRow >= 0 && newRow < 8) {
                for (int dc : new int[]{-1, 1}) {
                    int captureCol = srcCol + dc;
                    int captureRow = newRow;


                    if (captureRow >= 0 && captureRow < 8 && captureCol >= 0 && captureCol < 8) {
                        Piece target = board[captureRow][captureCol];

                        if (target != null && target.getColor() != color) {
                            moves.add(new Move(srcRow, srcCol, captureRow, captureCol));
                        }

                    }
                }
            }

        } else if (piece instanceof Knight) {

            for (int dr : new int[]{-2, -1, 1, 2}) {
                for (int dc : new int[]{-2, -1, 1, 2}) {

                    if (Math.abs(dr) + Math.abs(dc) == 3) {
                        int newRow = srcRow + dr;
                        int newCol = srcCol + dc;
                        if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                            Piece target = board[newRow][newCol];
                            if (target == null || target.getColor() != color) {
                                moves.add(new Move(srcRow, srcCol, newRow, newCol));
                            }
                        }
                    }
                }
            }

        } else if (piece instanceof King) {

            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    int newRow = srcRow + dr;
                    int newCol = srcCol + dc;
                    if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                        Piece target = board[newRow][newCol];
                        if (target == null || target.getColor() != color) {
                            moves.add(new Move(srcRow, srcCol, newRow, newCol));
                        }
                    }
                }
            }




            if (!isInCheck(board, color)) {
                int kingsideDestCol = 6;
                if (piece.isValidMove(srcRow, srcCol, srcRow, kingsideDestCol, board)) {

                    moves.add(new Move(srcRow, srcCol, srcRow, kingsideDestCol));
                }

                int queensideDestCol = 2;
                if (piece.isValidMove(srcRow, srcCol, srcRow, queensideDestCol, board)) {
                    moves.add(new Move(srcRow, srcCol, srcRow, queensideDestCol));
                }
            }

        } else {

            int[][] directions;
            if (piece instanceof Bishop) {
                directions = new int[][]{{-1, -1}, {-1, 1}, {1, -1}, {1, 1}}; // Diagonals
            } else if (piece instanceof Rook) {
                directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Ranks and Files
            } else if (piece instanceof Queen) {
                directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}}; // All 8 directions
            } else {

                directions = new int[0][0];
            }


            for (int[] dir : directions) {
                int dr = dir[0];
                int dc = dir[1];


                for (int i = 1; i < 8; i++) {
                    int newRow = srcRow + i * dr;
                    int newCol = srcCol + i * dc;


                    if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
                        break;
                    }

                    Piece target = board[newRow][newCol];

                    if (target == null) {

                        moves.add(new Move(srcRow, srcCol, newRow, newCol));
                    } else {

                        if (target.getColor() != color) {

                            moves.add(new Move(srcRow, srcCol, newRow, newCol));
                        }
                        break;
                    }
                }
            }
        }

        return moves;
    }
    private static int[] findKing(Piece[][] board, PieceColor color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece instanceof King && piece.getColor() == color) {
                    return new int[]{r, c};
                }
            }
        }
        return null;
    }

    private static ChessGame simulateGame(ChessGame game, Move move) {
        ChessGame simulated = new ChessGame(GameMode.VS_COMPUTER);
        copyBoard(game.getBoard(), simulated.getBoard());
        simulated.setCurrentTurn(game.getCurrentTurn());
        Piece piece = simulated.getBoard()[move.srcRow][move.srcCol];
        simulated.getBoard()[move.destRow][move.destCol] = piece;
        simulated.getBoard()[move.srcRow][move.srcCol] = null;
        simulated.setCurrentTurn(getOppositeColor(simulated.getCurrentTurn()));
        simulated.updateGameStatus();

        return simulated;
    }
    private static void copyBoard(Piece[][] src, Piece[][] dest) {
        for (int i = 0; i < 8; i++) {
            System.arraycopy(src[i], 0, dest[i], 0, 8);
        }
    }

    private static Move getRandomLegalMove(ChessGame game, PieceColor color) {
        List<Move> moves = generateLegalMoves(game.getBoard(), color, game);
        if (moves.isEmpty()) return null;
        return moves.get((int) (Math.random() * moves.size()));
    }

    private static String moveToString(Move move) {
        if (move == null) return "null";
        char fromFile = (char) ('a' + move.srcCol);
        char toFile = (char) ('a' + move.destCol);
        return "" + fromFile + (8 - move.srcRow) + toFile + (8 - move.destRow);
    }

    static class SearchResult {
        Move bestMove;
        int score;

        SearchResult(Move bestMove, int score) {
            this.bestMove = bestMove;
            this.score = score;
        }
    }

    static class TranspositionTable {
        private TTEntry[] table;
        private int size;

        TranspositionTable(int size) {
            this.size = size;
            this.table = new TTEntry[size];
        }

        void store(long key, int score, int depth, int flag, Move bestMove) {
            int index = (int) (key & (size - 1));
            TTEntry existingEntry = table[index];
            if (existingEntry == null || depth >= existingEntry.depth) {
                table[index] = new TTEntry(key, score, depth, flag, bestMove);
            }
        }
        TTEntry probe(long key) {
            int index = (int) (key & (size - 1));
            TTEntry entry = table[index];
            return (entry != null && entry.key == key) ? entry : null;
        }

        void clear() {
            Arrays.fill(table, null);
        }
    }

    static class TTEntry {
        static final int EXACT = 0;
        static final int LOWER_BOUND = 1;
        static final int UPPER_BOUND = 2;

        long key;
        int score;
        int depth;
        int flag;
        Move bestMove;

        TTEntry(long key, int score, int depth, int flag, Move bestMove) {
            this.key = key;
            this.score = score;
            this.depth = depth;
            this.flag = flag;
            this.bestMove = bestMove;
        }
    }

    static class ZobristHash {
        private Random random = new Random(12345);
        private long[][][] pieceKeys = new long[8][8][12];
        private long blackToMove;

        ZobristHash() {
            initializeKeys();
        }

        private void initializeKeys() {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 12; k++) {
                        pieceKeys[i][j][k] = random.nextLong();
                    }
                }
            }
            blackToMove = random.nextLong();
        }

        long calculateKey(Piece[][] board, PieceColor currentTurn) {
            long key = 0;

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece piece = board[r][c];
                    if (piece != null) {
                        int pieceIndex = getPieceIndex(piece);
                        key ^= pieceKeys[r][c][pieceIndex];
                    }
                }
            }

            if (currentTurn == PieceColor.BLACK) {
                key ^= blackToMove;
            }

            return key;
        }

        private int getPieceIndex(Piece piece) {
            int base = piece.getColor() == PieceColor.WHITE ? 0 : 6;
            if (piece instanceof Pawn) return base + 0;
            if (piece instanceof Knight) return base + 1;
            if (piece instanceof Bishop) return base + 2;
            if (piece instanceof Rook) return base + 3;
            if (piece instanceof Queen) return base + 4;
            if (piece instanceof King) return base + 5;
            return 0;
        }
    }

    static class KillerMoves {
        private static final int MAX_DEPTH = 64;
        private Move[][] killers = new Move[MAX_DEPTH][2];

        void store(Move move, int depth) {
            if (depth >= 0 && depth < MAX_DEPTH) {
                if (!move.equals(killers[depth][0])) {
                    killers[depth][1] = killers[depth][0];
                    killers[depth][0] = move;
                }
            }
        }

        boolean isKiller(Move move, int depth) {
            if (depth >= 0 && depth < MAX_DEPTH) {
                return move.equals(killers[depth][0]) || move.equals(killers[depth][1]);
            }
            return false;
        }

        void clear() {
            for (int i = 0; i < MAX_DEPTH; i++) {
                killers[i][0] = null;
                killers[i][1] = null;
            }
        }
    }

    static class HistoryHeuristic {
        private int[][][][] history = new int[8][8][8][8]; // from->to squares

        void update(Move move, int depth) {
            history[move.srcRow][move.srcCol][move.destRow][move.destCol] += depth * depth;
        }

        int getScore(Move move) {
            return history[move.srcRow][move.srcCol][move.destRow][move.destCol];
        }

        void age() {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 8; k++) {
                        for (int l = 0; l < 8; l++) {
                            history[i][j][k][l] /= 8;
                        }
                    }
                }
            }
        }
    }

    static class TimeUpException extends Exception {
    }
}